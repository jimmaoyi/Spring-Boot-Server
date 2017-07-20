package com.maryun.restful.system.usefulExpressions;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.maryun.utils.UuidUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @ClassName: UsefulExpressionsRestful
 * @Description: 常用语
 * @author SR
 * @date 2017年3月27日
 */

import com.github.pagehelper.PageHelper;
import com.maryun.mapper.system.usefulExpressions.UsefulExpressionsMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.WebResult;

/**
 *
 * @ClassName: UsefulExpressionsRestful
 * @Description: 常用语
 * @author SR
 * @date 2015年11月30日 下午1:45:00
 *
 */
@RestController
@RequestMapping("usefulExpressions")
public class UsefulExpressionsRestful extends BaseRestful {

    @Resource
    private UsefulExpressionsMapper usefulExpressionsMapper;

    /**
     * 列表显示和查找
     * @param pd
     * @return
     */
    @RequestMapping("pageSearch")
    public PageData getPageSearch(@RequestBody PageData pd) {
        List<PageData> userList = null;
        if (pd.getPageNumber() != 0) {
            PageHelper.startPage(pd.getPageNumber(), pd.getPageSize());
        }
        userList = usefulExpressionsMapper.selectAll(pd);
        // 分页
        pd = this.getPagingPd(userList);
        // 结果集封装
        return WebResult.requestSuccess(pd);
    }

    /**
     * 去编辑页面
     * @param pd
     * @return
     */
    @RequestMapping("toEdit")
    public PageData toEdit(@RequestBody PageData pd) {
        List<PageData> pageData = usefulExpressionsMapper.selectAll(pd);// 根据ID读取
        // 结果集封装
        return WebResult.requestSuccess(pageData);
    }

    /**
     *保存修改
     * @param pd
     * @return
     */
    @RequestMapping("saveEdit")
    public PageData saveEdit(@RequestBody PageData pd) {
        usefulExpressionsMapper.update(pd);
        return WebResult.requestSuccess();
    }

    @RequestMapping("saveAdd")
    public PageData saveAdd(@RequestBody PageData pd) {
        pd.put("ID", UuidUtil.get32UUID());
        if (usefulExpressionsMapper.selectAll(pd).size() == 0) {
            usefulExpressionsMapper.insert(pd);
            pd.put("msg", "success");
        } else {
            pd.put("msg", "failed");
        }
        return WebResult.requestSuccess(pd);
    }

    @RequestMapping("delete")
    private PageData delete(@RequestBody PageData pd){
        String IDS=pd.getString("IDS");
        if (IDS != null && !"".equals(IDS)) {
            String[] arrayIDS=IDS.split(",");
            usefulExpressionsMapper.delete(arrayIDS);
            pd.put("msg","ok");
        }else {
            pd.put("msg","no");
        }
        return WebResult.requestSuccess(pd);
    }
}
