package com.maryun.restful.system.fileuploads;

import com.maryun.common.service.FileUploadsService;
import com.maryun.mapper.system.fileuploads.FileUploadsMapper;
import com.maryun.model.PageData;
import com.maryun.restful.base.BaseRestful;
import com.maryun.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author SR
 */
@RestController
@RequestMapping(value = "filesUploads")
public class FilesUploadsRestful extends BaseRestful {
    @Resource
    private FileUploadsService fileUploadsService;
    @Resource
    private FileUploadsMapper fileUploadsMapper;
    @Value("${system.file.uploads.basePath}")
    private String BASE_PATH;
    @Value("${system.imageServer.uploads.basePath}")
    private String ImageServer;

    //缩略图片高宽
    @Value("${slt.height}")
    private int sltHeight;

    @Value("${slt.weight}")
    private int sltWeight;

    @Value("${appfriend.setLongImage}")
    private int setLongImage;

    @Value("${appfriend.setLongImage.waterImagePath}")
    private String waterImagePath;

    @Resource
    private ImgChange imgChange;

    @Resource
    private FilesSaveLocalUtils filesSaveLocalUtils;

    /**
     * @param file
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/fileUpds")
    public PageData fileUpd(MultipartFile[] file) throws Exception {
        List list = new ArrayList();
        try {
            //获得CREATE_UID
            PageData savePage = this.savePage(getPageData());
            String CREATE_UID = savePage.getString("CREATE_UID");

            String MASTER_ID = this.getPageData().get("MASTER_ID") + "";

            //String yyydd=DateUtil.getMonth();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String yyydd = simpleDateFormat.format(new Date());


            for (MultipartFile multipartFile : file) {
                //取得当前上传文件的文件名称
                String fileName = multipartFile.getOriginalFilename();
                String id = UuidUtil.get32UUID();
                //重命名上传后的文件名
                String fileType = fileName.substring(fileName.lastIndexOf("."));
                String newFileName = id + fileType;
                String path = File.separator + yyydd + File.separator + newFileName;
                //定义缩略图路径
                String newFileNameslt = id + "slt" + ".jpg";
                String pathslt = File.separator + yyydd + "slt" + File.separator + newFileNameslt;
                //硬盘路径是否存在，如果不存在则创建
                String dstPath = BASE_PATH + File.separator + yyydd;
                File dstFile = new File(dstPath);
                if (!dstFile.exists()) {
                    dstFile.mkdirs();
                }
                //缩略图硬盘路径是否存在，如果不存在则创建
                String dstPathslt = BASE_PATH + File.separator + yyydd + "slt";
                File dstFileslt = new File(dstPathslt);
                if (!dstFileslt.exists()) {
                    dstFileslt.mkdirs();
                }
                //定义上传路径
                String filePath = BASE_PATH + path;
                //储存图片
                if (!filesSaveLocalUtils.savePhotoToPath(multipartFile, filePath)) {
                    return WebResult.requestFailed(501, "存入文件失败", null);
                }
                //图片生成缩略图
                imgChange.setChangeHeightAndWeight(sltHeight, sltWeight);
                imgChange.setLongImage(setLongImage);
                imgChange.setWaterImagePath(waterImagePath);
                PageData generateThumImage = imgChange.generateThumImage(BASE_PATH + path, BASE_PATH + pathslt, BASE_PATH + pathslt);
                if (!generateThumImage.getString("state").equals("200")) {
                    return WebResult.requestFailed(501, "图片缩略图压缩失败", generateThumImage);
                }
                //"/"符号
                String replaceBasePath = BASE_PATH.replaceAll("\\\\", "/");
                String replaceSltPath = pathslt.replaceAll("\\\\", "/");
                String replacePath = path.replaceAll("\\\\", "/");
                //保存数据库
                PageData insertRes = fileUploadsService.insert(id, fileName, replaceBasePath,
                        replacePath, replaceSltPath,
                        String.valueOf(multipartFile.getSize()), String.valueOf(multipartFile.getContentType()),
                        MASTER_ID, "0", CREATE_UID, new Date(), null, null, "1");
//									
                if (!insertRes.getString("state").equals("200"))
                    return WebResult.requestFailed(501, "存入数据库失败", insertRes);
                Map map = new HashMap();
                map.put("id", id);
                map.put("path", ImageServer + replacePath);
                map.put("sltpath", ImageServer + replaceSltPath);
                list.add(map);
            }


        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e);
        }
        return WebResult.requestSuccess(list);
    }

    /**
     * Im头像上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("uploadImHeadPortrait")
    @ResponseBody
    public PageData uploadImHeadPortrait(MultipartFile[] file) throws Exception {
        PageData pd = fileUpd(file);
        Object content = pd.get("content");
        PageData returnPd = new PageData();
        if (pd.getString("state").equals("200")) {
            String path = (String) ((HashMap) ((List) content).get(0)).get("path");
            String sltpath = (String) ((HashMap) ((List) content).get(0)).get("sltpath");
            PageData returnPd1 = new PageData();
            returnPd1.put("src", path);
            returnPd1.put("srcSlt", sltpath);
            returnPd.put("code", "0");
            returnPd.put("msg", "success");
            returnPd.put("data", returnPd1);
            return WebResult.requestSuccess(returnPd);
        } else {
            return WebResult.requestFailed(500, "服务器错误", pd.get("content"));
        }
    }


    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/fileUpd")
    public Object fileUpd(@RequestBody PageData pd) throws Exception {
        pd.put("CREATE_TIME", new Date());
        fileUploadsMapper.insert(pd);
        return WebResult.requestSuccess();
    }


    /**
     * @param model
     * @return
     */
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "system/filesuploads/filesuploads_list";
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "listData")
    public Object listData(@RequestBody PageData pd) throws Exception {
        PageData parsePage2BootstrmpTable = BootstrapUtils.parsePage2BootstrmpTable(
                Long.valueOf((String) pd.get("pageSize")),
                fileUploadsMapper.findAllList(pd)
        );
        return WebResult.requestSuccess(parsePage2BootstrmpTable);
    }

    /**
     * @param model
     * @return
     */
    @RequestMapping(value = "toAdd")
    public String toAdd(Model model) {
        PageData pd = new PageData();
        model.addAttribute("pd", pd);
        return "system/filesuploads/filesuploads_add";
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "saveAdd")
    @ResponseBody
    public PageData saveAdd(@RequestBody PageData pd) throws Exception {
        pd.put("id", UuidUtil.get32UUID());
        fileUploadsMapper.save(pd);
        //return "system/filesuploads/filesuploads_list";
        return WebResult.requestSuccess();
    }


    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toEdit")
    public PageData toEdit(@RequestBody PageData pd) throws Exception {
        PageData filesUploadsData = fileUploadsMapper.get(pd);
        return WebResult.requestSuccess(filesUploadsData);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "updateMasterId")
    @ResponseBody
    public Object updateMasterId(@RequestBody PageData pd) throws Exception {

        String updId = pd.getString("IDS");
        if (null != updId && !"".equals(updId)) {
            String[] updIds = updId.split(",");
            pd.put("IDS", Arrays.asList(updIds));
            fileUploadsMapper.updateMasterId(pd);
            pd.put("msg", "ok");
        }
        pd.put("msg", "no");
        return WebResult.requestSuccess(pd);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "saveEdit")
    //@ResponseBody
    public PageData saveEdit(@RequestBody PageData pd) throws Exception {
        fileUploadsMapper.update(pd);
        return WebResult.requestSuccess();
        //return null;
    }


    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "deleteSolve")
    public Object deleteSolve(@RequestBody PageData pd) throws Exception {
        String delId = pd.getString("IDS");
        if (null != delId && !"".equals(delId)) {
            String[] delIds = delId.split(",");
            fileUploadsMapper.deleteSolve(delIds);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        return WebResult.requestSuccess(pd);
    }


    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "deleteByMaster")
    public Object deleteByMaster(@RequestBody PageData pd) throws Exception {
        String delId = pd.getString("IDS");
        if (null != delId && !"".equals(delId)) {
            String[] delIds = delId.split(",");
            fileUploadsMapper.deleteByMaster(delIds);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        return WebResult.requestSuccess(pd);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "delete")
    public Object delete(@RequestBody PageData pd) throws Exception {
        String delId = pd.getString("IDS");
        if (null != delId && !"".equals(delId)) {
            String[] delIds = delId.split(",");
            fileUploadsMapper.deleteById(delIds);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        return WebResult.requestSuccess(pd);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "download")
    public Object download(@RequestBody PageData pd) throws Exception {
        pd = fileUploadsMapper.get(pd);
        return WebResult.requestSuccess(pd);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping("findById")
    public Object backFile(@RequestBody PageData pd) throws Exception {
        PageData pageData = fileUploadsMapper.get(pd);
        return WebResult.requestSuccess(pageData);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping("findByMasterId")
    public PageData findByMasterId(@RequestBody PageData pd) throws Exception {
        List<PageData> findByMasterId = fileUploadsMapper.findByMasterId(pd);
        return WebResult.requestSuccess(findByMasterId);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping("findByHttpId")
    public PageData findByHttpId(@RequestBody PageData pd) throws Exception {
        PageData findByMasterId = fileUploadsMapper.findByHttpId(pd);
        return WebResult.requestSuccess(findByMasterId);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @RequestMapping("findByHttpIdOrMasterId")
    public PageData findByHttpIdOrMasterId(@RequestBody PageData pd) throws Exception {
        List<PageData> findByMasterId = fileUploadsMapper.findByHttpIdOrMasterId(pd);
        List<PageData> changePageDate = new ArrayList<PageData>();
        for (PageData changePd : findByMasterId) {
            String path = changePd.getString("PATH");
            changePd.put("PATH", ImageServer + path);
            changePageDate.add(changePd);
        }
        return WebResult.requestSuccess(changePageDate);
    }
}