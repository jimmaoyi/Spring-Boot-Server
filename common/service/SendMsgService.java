package com.maryun.common.service;

import com.alibaba.fastjson.JSON;
import com.maryun.mapper.system.smsLog.SmsLogMapper;
import com.maryun.model.PageData;
import com.maryun.utils.OrderNumService;
import com.maryun.utils.WebResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
    **
	* 类名称：SendMsg 创建人：MARYUN 创建时间：2016年2月13日
	*
	* @version
*/
@Component("sendMsgService")
public class SendMsgService {
    @Resource
    private SendMsgUtils sendMsgUtil;
    @Value("${msg.verificsyiom.code.smsTemplateCode}")
    public String smsTemplateCode;
    @Value("${msg.payIdentifyCode.code.smsTemplateCode}")
    public String payIdentifyCode;
    @Value("${msg.verificsyiom.code.smsFreeSingName}")
    public String smsFreeSignName;

    //登录验证码的key前缀
    @Value("${MsgKeyPre}")
    private static String MsgKeyPre;
    //付款识别码的key前缀
    @Value("${PayIdentifyCodeKeyPre}")
    private static String PayIdentifyCodeKeyPre;
    @Value("${redisTimeout}")
    private int redisTimeout;//超时时间
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SmsLogMapper smsLogMapper;

    /**
     * 发送短信验证码
     * recNum：发送号码
     * type:类型 1、登录 2、付款识别码
     *
     * @return
     */
    public PageData sendMsg(PageData pd) throws Exception {
        String msgType = pd.getString("type");
        if (StringUtils.isNotBlank(msgType) && "1".equals(msgType)) {
            return verificationCode(pd);
        } else if (StringUtils.isNotBlank(msgType) && "2".equals(msgType)) {
            return payIdentifyCode(pd);
        } else {
            return WebResult.requestFailed(400, "参数错误", null);
        }
    }

    /**
     * 验证码发送1、登录
     *
     * @param pd
     */
    public PageData verificationCode(PageData pd) {
        String recNum = pd.getString("recNum");
        //公用的发送短信的方法
        PageData commonSendMsgMethod = commonSendMsgMethod(pd);
        //如果发送成功的话
        if (commonSendMsgMethod.getString("sendMsg").equals("ok")) {
            //数据库保存日志
            save(UUID.randomUUID().toString().replaceAll("-", ""), recNum, smsTemplateCode, "登录", smsFreeSignName,
                    "1", new Date(), commonSendMsgMethod.getString("msgCode"), "laoban", (String) commonSendMsgMethod.get("sendState"));

            return WebResult.requestSuccess(pd);

        }
        ////发送失败
        else if (commonSendMsgMethod.getString("sendMsg").equals("sendMsgWrong")) {
            //数据库保存日志
            save(UUID.randomUUID().toString().replaceAll("-", ""), recNum, smsTemplateCode, "登录", smsFreeSignName,
                    "0", new Date(), commonSendMsgMethod.getString("msgCode"), "laoban", (String) commonSendMsgMethod.get("sendState"));

            return WebResult.requestFailed(401, "发送失败", null);

        }
        //参数错误
        else {
            return WebResult.requestFailed(400, "参数错误", null);
        }


    }

    /**
     * 付款识别码发送2、付款识别码
     *
     * @param pd
     * @return
     */
    public PageData payIdentifyCode(PageData pd) {
        String recNum = pd.getString("recNum");
        //公用的发送短信的方法
        PageData commonSendMsgMethod = commonSendMsgMethod(pd);
        //如果发送成功的话
        if (commonSendMsgMethod.getString("sendMsg").equals("ok")) {
            //数据库保存日志
            save(UUID.randomUUID().toString().replaceAll("-", ""), recNum, smsTemplateCode, "付款识别码", smsFreeSignName,
                    "1", new Date(), commonSendMsgMethod.getString("msgCode"), "laoban", (String) commonSendMsgMethod.get("sendState"));
            return WebResult.requestSuccess(pd);
        }
        //发送失败
        else if (commonSendMsgMethod.getString("sendMsg").equals("sendMsgWrong")) {
            //数据库保存日志
            save(UUID.randomUUID().toString().replaceAll("-", ""), recNum, smsTemplateCode, "付款识别码", smsFreeSignName,
                    "0", new Date(), commonSendMsgMethod.getString("msgCode"), "laoban", (String) commonSendMsgMethod.get("sendState"));
            return WebResult.requestFailed(401, "发送失败", null);
        }
        //参数错误
        else {
            return WebResult.requestFailed(400, "参数错误", null);
        }
    }

    /**
     * 检查验证码
     *
     * @param pd 如果type是“2”，则校验付款识别码；其它情况校验登录验证码。
     * @return
     */
    public PageData checkVerificationCode(PageData pd) {
        String recNum = pd.getString("recNum");
        String msgCode = pd.getString("msgCode");
        String type = pd.getString("type");

        if (StringUtils.isNotBlank(recNum) && StringUtils.isNotBlank(msgCode)) {
            String msgToken = String.join("", MsgKeyPre, recNum);
            //如果是付款识别码，取识别码的value
            if (type.equals("2")) {
                msgToken = String.join("", payIdentifyCode, recNum);
            }
            // 获取redis值,判断token是否存在和用户传入一致
            if (redisTemplate.hasKey(msgToken)) {
                ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
                String code = redisOpt.get(msgToken).toString();
                if (code.equals(msgCode)) {
                    return WebResult.requestSuccess();
                } else {
                    return WebResult.requestFailed(505, "短信验证码验证失败", null);
                }
            } else {
                return WebResult.requestFailed(505, "短信验证码验证失败", null);
            }
        } else {
            return WebResult.requestFailed(500, "参数不正确", null);
        }
    }

    /**
     * 保存发送的短信到数据库
     *
     * @param ID
     * @param PHONE
     * @param TEMPLATE_CODE
     * @param CONTENT
     * @param SIGN_NAME
     * @param SEND_STATE
     * @param SEND_TIME
     * @param BUS_SYSTEM
     * @param MSG
     * @return
     */
    public void save(String ID, String PHONE, String TEMPLATE_CODE, String CONTENT, String SIGN_NAME,
                     String SEND_STATE, Date SEND_TIME, String BUS_ID, String BUS_SYSTEM, String MSG) {

        PageData smslogPd = new PageData();
        smslogPd.put("ID", ID);
        smslogPd.put("PHONE", PHONE);
        smslogPd.put("TEMPLATE_CODE", TEMPLATE_CODE);
        smslogPd.put("CONTENT", CONTENT);
        smslogPd.put("SIGN_NAME", SIGN_NAME);
        smslogPd.put("SEND_STATE", SEND_STATE);
        smslogPd.put("SEND_TIME", SEND_TIME);
        smslogPd.put("BUS_ID", BUS_ID);
        smslogPd.put("BUS_SYSTEM", BUS_SYSTEM);
        smslogPd.put("MSG", MSG);
        //数据库保存日志
        smsLogMapper.saveSmsLog(smslogPd);
    }

    /**
     * 公用发送信息方法
     *
     * @param pd
     * @return
     */
    public PageData commonSendMsgMethod(PageData pd) {
        String recNum = pd.getString("recNum");
        String type = pd.getString("type");
        String product = pd.getString("product");
        pd.put("smsFreeSignName", smsFreeSignName);
        if (StringUtils.isNotBlank(recNum)) {
            Map params = new HashMap();
            String code = "";
            String msgToken = "";
            if (type.equals("1")) {
                //四位随机码
                int codeInt = ((int) (Math.random() * 9999));
                code = String.format("%04d", codeInt);
                //登录短信模板
                pd.put("smsTemplateCode", smsTemplateCode);
                //登录存入redis的key
                msgToken = String.join("", MsgKeyPre, recNum);
            }
            //如果type是2为付款识别码
            if (type.equals("2")) {
                //六位数付款识别码
                code = OrderNumService.getOfflineNum();
                //付款识别码短信模板
                pd.put("smsTemplateCode", payIdentifyCode);
                //付款识别码存入redis的key
                msgToken = String.join("", payIdentifyCode, recNum);
            }
            params.put("code", code);
            params.put("product", product);
            if (type.equals("2")) {
                params.remove("params");
            }
            pd.put("msgCode", code);
            pd.put("smdParam", JSON.toJSONString(params));
            Map<String, String> sendState = sendMsgUtil.send(pd);
            pd.put("sendState", sendState.get("msg"));
            if (sendState.get("state").equals("1")) {
                ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
                redisOpt.set(msgToken, code, redisTimeout, TimeUnit.SECONDS);
            } else {
                pd.put("sendMsg", "sendMsgWrong");
                return pd;
            }
        } else {
            pd.put("sendMsg", "wrongArg");
            return pd;
        }
        pd.put("sendMsg", "ok");
        return pd;
    }

    /**
     * 检查验证码从数据库中
     *
     * @param pd
     * @return
     */
    public List<PageData> checkVerificationCodeFromDb(PageData pd) {
        return smsLogMapper.findByPhone(pd);
    }

}