package com.maryun.common.restful;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maryun.common.service.SendMsgService;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 * @author SR
 */
@RestController
@RequestMapping("sendMsg")
public class SendMsgRestful extends BaseRestful {

    @Resource
    private SendMsgService sendMsgService;

    /**
     * @return {"state":200,"msg":"success","content":pd}
     */
    @RequestMapping("getRecentSmsCode")
    public PageData getRecentSmsCode() {
        PageData pd = this.getPageData();
        List<PageData> checkVerificationCodeFromDb = sendMsgService.checkVerificationCodeFromDb(pd);
        if (checkVerificationCodeFromDb.size() == 0) {
            return WebResult.requestFailed(600, "没有找到手机号对应的验证码,请检查传入的参数是否为大写的'PHONE'", pd);
        }
        //最近一次发送成功的验证码
        PageData pageData = checkVerificationCodeFromDb.get(0);
        String code = (String) pageData.get("BUS_ID");
        pd.put("CODE", code);
        return WebResult.requestSuccess(pd);
    }
}
