package com.qiyou.dhlive.prd.room.controller;

import com.qiyou.dhlive.api.base.outward.service.IFileUploadRemoteService;
import com.qiyou.dhlive.api.base.outward.service.ILiveRoomApiService;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.room.outward.model.RoomArticle;
import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.model.RoomVideo;
import com.qiyou.dhlive.core.room.outward.service.IRoomArticleService;
import com.qiyou.dhlive.core.room.outward.service.IRoomVideoService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.qiyou.dhlive.prd.room.vo.FileVO;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;
import com.yaozhong.framework.web.annotation.session.UnSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * Created by fish on 2018/9/11.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("goods")
public class GoodsController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    private IFileUploadRemoteService fileUploadRemoteService;

    @Autowired
    private ILiveRoomApiService liveRoomApiService;

    @Autowired
    private IRoomVideoService roomVideoService;

    @Autowired
    private IRoomArticleService roomArticleService;

    @RequestMapping("")
    public String index() {
        return "good/article";
    }

    @RequestMapping("file")
    public String file() {
        return "good/file";
    }

    @RequestMapping("video")
    public String video() {
        return "good/video";
    }

    @RequestMapping("article")
    public String article() {
        return "good/article";
    }


    //----------------------------文档---------------------------------

    /**
     * 保存文档
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveRoomFile")
    @ResponseBody
    public DataResponse saveRoomFile(RoomFile params, MultipartFile file) {
        String fileUrl = "";
        try {
            if (EmptyUtil.isNotEmpty(file)) {
                baseLog.info(LogFormatUtil.getActionFormat("开始上传文件"));
                InputStream input = file.getInputStream();
                int count = input.available();
                byte[] fileByte = new byte[count];
                input.read(fileByte);
                String fileName = file.getOriginalFilename();
                baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
                input.read(fileByte);
                fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
                fileUrl = fileId + "." + fileName;
                baseLog.info(LogFormatUtil.getActionFormat("结束上传文件"));
                baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + fileUrl));
            }
            params.setFileUrl(fileUrl);
            params.setRoomId(4);
            params.setCreateTime(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.liveRoomApiService.saveRoomFile(params);
    }


    /**
     * 文档列表
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @UnSecurity
    @UnSession
    @RequestMapping("getFileList")
    @ResponseBody
    public DataResponse getFileList(PageSearch pageSearch, RoomFile params) {
        params.setStatus(0);
        return this.liveRoomApiService.getLiveRoomFileList(pageSearch, params);
    }

    /**
     * 删除文档
     *
     * @param params
     * @return
     */
    @UnSecurity
    @NeedSession("goods/deleteFile")
    @RequestMapping("deleteFile")
    @ResponseBody
    public DataResponse deleteFile(RoomFile params) {
        return this.liveRoomApiService.deleteFile(params);
    }


    //----------------------------文章---------------------------------

    /**
     * kindEditor文件上传
     *
     * @param model
     * @param imgFile
     * @return
     */
    @UnSession
    @RequestMapping("upload")
    @ResponseBody
    public FileVO upload(MultipartFile imgFile) {
        String url = "";
        try {
            if (EmptyUtil.isNotEmpty(imgFile)) {
                baseLog.info(LogFormatUtil.getActionFormat("开始上传图片"));
                InputStream input = imgFile.getInputStream();
                int count = input.available();
                byte[] fileByte = new byte[count];
                input.read(fileByte);
                String fileName = imgFile.getOriginalFilename();
                baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
                if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
                    return new FileVO(1, "文件格式有误");
                } else {
                    input.read(fileByte);
                    fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                    String fileId = fileUploadRemoteService.storeFile(fileByte, fileName);
                    url = fileId + "." + fileName;
                    baseLog.info(LogFormatUtil.getActionFormat("结束上传 banner"));
                    baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + url));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new FileVO(1, "文件上传失败");
        }
        baseLog.info(LogFormatUtil.getActionFormat("文件路径：" + this.baseSysParamService.getValueByKey("host.images") + "ori/" + url));
        return new FileVO(0, this.baseSysParamService.getValueByKey("host.images") + "ori/" + url);
    }


    /**
     * 文章列表
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @UnSecurity
    @UnSession
    @RequestMapping("getArticleList")
    @ResponseBody
    public DataResponse getArticleList(PageSearch pageSearch, RoomArticle params) {
        params.setStatus(0);
        SearchCondition<RoomArticle> condition = new SearchCondition<RoomArticle>(params, pageSearch);
        PageResult<RoomArticle> result = this.roomArticleService.findByPage(condition);
        return new DataResponse(1000, result);
    }


    /**
     * 保存文章
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveArticle")
    @ResponseBody
    public DataResponse saveArticle(RoomArticle params) {
        try {
            EmployeeSession eSession = EmployeeSession.getEmployeeSession();
            params.setCreateUserId(eSession.getEmployeeId());
            params.setRoomId(4);
            params.setArticleContent(HtmlUtils.htmlUnescape(params.getArticleContent()));
            params.setCreateTime(new Date());
            this.roomArticleService.save(params);
            return new DataResponse(1000, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new DataResponse(1001, e.getMessage());
        }
    }


    /**
     * 删除文章
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "delArticle")
    @ResponseBody
    public DataResponse delArticle(RoomArticle params) {
        try {
            params.setStatus(1);
            this.roomArticleService.modifyEntity(params);
            return new DataResponse(1000, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new DataResponse(1001, e.getMessage());
        }
    }


    //----------------------------视频---------------------------------

    /**
     * 保存视频
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveVideo")
    @ResponseBody
    public DataResponse saveRoomVideo(RoomVideo params, MultipartFile sp, MultipartFile fm) {
        String videoUrl = "";
        String fmUrl = "";
        try {
            if (EmptyUtil.isNotEmpty(sp)) {
                baseLog.info(LogFormatUtil.getActionFormat("开始上传文件"));
                InputStream input = sp.getInputStream();
                int count = input.available();
                byte[] fileByte = new byte[count];
                input.read(fileByte);
                String fileName = sp.getOriginalFilename();
                baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
                input.read(fileByte);
                fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
                videoUrl = fileId + "." + fileName;
                baseLog.info(LogFormatUtil.getActionFormat("结束上传文件"));
                baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + videoUrl));
                params.setVideoUrl(videoUrl);
            }

            if (EmptyUtil.isNotEmpty(fm)) {
                baseLog.info(LogFormatUtil.getActionFormat("开始上传文件"));
                InputStream input = fm.getInputStream();
                int count = input.available();
                byte[] fileByte = new byte[count];
                input.read(fileByte);
                String fileName = fm.getOriginalFilename();
                baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
                input.read(fileByte);
                fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
                fmUrl = fileId + "." + fileName;
                baseLog.info(LogFormatUtil.getActionFormat("结束上传文件"));
                baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + fmUrl));
                params.setVideoImgUrl(fmUrl);
            }

            EmployeeSession eSession = EmployeeSession.getEmployeeSession();
            params.setRoomId(4);
            params.setVideoContent(HtmlUtils.htmlUnescape(params.getVideoContent()));
            params.setCreateUserId(eSession.getEmployeeId());
            params.setCreateTime(new Date());
            this.roomVideoService.save(params);
            return new DataResponse(1000, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new DataResponse(1001, e.getMessage());
        }
    }


    /**
     * 视频列表
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @UnSecurity
    @UnSession
    @RequestMapping("getVideoList")
    @ResponseBody
    public DataResponse getVideoList(PageSearch pageSearch, RoomVideo params) {
        params.setStatus(0);
        SearchCondition<RoomVideo> condition = new SearchCondition<RoomVideo>(params, pageSearch);
        PageResult<RoomVideo> result = this.roomVideoService.findByPage(condition);
        return new DataResponse(1000, result);
    }

    /**
     * 删除视频
     *
     * @param params
     * @return
     */
    @UnSecurity
    @NeedSession("goods/delVideo")
    @RequestMapping("delVideo")
    @ResponseBody
    public DataResponse deleteVideo(RoomVideo params) {
        try {
            params.setStatus(1);
            this.roomVideoService.modifyEntity(params);
            return new DataResponse(1000, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new DataResponse(1001, e.getMessage());
        }
    }


}
