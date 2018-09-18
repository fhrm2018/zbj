package com.qiyou.dhlive.prd.room.controller;

import com.qiyou.dhlive.core.room.outward.model.RoomArticle;
import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.model.RoomVideo;
import com.qiyou.dhlive.core.room.outward.service.IRoomArticleService;
import com.qiyou.dhlive.core.room.outward.service.IRoomFileService;
import com.qiyou.dhlive.core.room.outward.service.IRoomVideoService;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.MD5Util;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.RangeCondition;
import com.yaozhong.framework.base.database.domain.search.RangeConditionType;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.UnSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fish on 2018/9/12.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("remote")
public class GoodsRemoteController {

    @Autowired
    private IRoomVideoService roomVideoService;

    @Autowired
    private IRoomArticleService roomArticleService;

    @Autowired
    private IRoomFileService roomFileService;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    //----------------------------------登录----------------------------------
    @UnSession
    @UnSecurity
    @RequestMapping("login")
    @ResponseBody
    public DataResponse userLogin(UserVipInfo user, HttpServletRequest request) {
        UserVipInfo vip = this.userVipInfoService.getVipUserByLoginName(user.getUserTel());
        String pwd = MD5Util.MD5Encode(user.getUserPass(), "utf-8");
        if (EmptyUtil.isNotEmpty(vip)) {
            if (pwd.equals(vip.getUserPass())) {
                request.getSession().setAttribute("USER_VIP_INFO", vip);
                return new DataResponse(1000, "success");
            } else {
                return new DataResponse(1001, "用户名或密码不正确");
            }
        } else {
            return new DataResponse(1001, "用户名或密码不正确");
        }
    }


    //----------------------------------文档----------------------------------

    /**
     * 文件列表
     *
     * @param params
     * @return
     */
    @UnSession
    @UnSecurity
    @RequestMapping("getFileList")
    @ResponseBody
    public DataResponse getFileList(RoomFile params) {
        try {
            params.setStatus(0);
            SearchCondition<RoomFile> condition = new SearchCondition<RoomFile>(params);
            List<RoomFile> result = this.roomFileService.findByCondition(condition);
            return new DataResponse(1000, result);
        } catch (Exception e) {
            return new DataResponse(1001, e.getMessage());
        }
    }

    //----------------------------------文章----------------------------------

    /**
     * 文章列表
     *
     * @param params
     * @return
     */
    @UnSession
    @UnSecurity
    @RequestMapping("getArticleList")
    @ResponseBody
    public DataResponse getArticleList(RoomArticle params, HttpServletRequest request) {
        try {
            params.setStatus(0);
            UserVipInfo vip = (UserVipInfo) request.getSession().getAttribute("USER_VIP_INFO");
            List<RoomArticle> result = new ArrayList<RoomArticle>();
            //登录之后查看所有
            if (EmptyUtil.isNotEmpty(vip)) {
                SearchCondition<RoomArticle> condition = new SearchCondition<RoomArticle>(params);
                result = this.roomArticleService.findByCondition(condition);
            } else {
                SearchCondition<RoomArticle> condition = new SearchCondition<RoomArticle>(params);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                condition.buildRangeConditions(new RangeCondition("createTime", sdf.format(new Date()), RangeConditionType.LessThan));
                result = this.roomArticleService.findByCondition(condition);
            }
            return new DataResponse(1000, result);
        } catch (Exception e) {
            return new DataResponse(1001, e.getMessage());
        }
    }


    /**
     * 文章详情
     *
     * @param params
     * @return
     */
    @UnSession
    @UnSecurity
    @RequestMapping("getArticleDetail")
    @ResponseBody
    public DataResponse getFileDetail(RoomArticle params) {
        try {
            params = this.roomArticleService.findById(params.getArticleId());
            return new DataResponse(1000, params);
        } catch (Exception e) {
            return new DataResponse(1001, e.getMessage());
        }
    }


    //----------------------------------视频----------------------------------

    /**
     * 视频列表
     *
     * @param params
     * @return
     */
    @UnSession
    @UnSecurity
    @RequestMapping("getVideoList")
    @ResponseBody
    public DataResponse getVideoList(RoomVideo params) {
        try {
            params.setStatus(0);
            SearchCondition<RoomVideo> condition = new SearchCondition<RoomVideo>(params);
            List<RoomVideo> result = this.roomVideoService.findByCondition(condition);
            return new DataResponse(1000, result);
        } catch (Exception e) {
            return new DataResponse(1001, e.getMessage());
        }
    }


    /**
     * 视频详情
     *
     * @param params
     * @return
     */
    @UnSession
    @UnSecurity
    @RequestMapping("getVideoDetail")
    @ResponseBody
    public DataResponse getVideoDetail(RoomVideo params) {
        try {
            params = this.roomVideoService.findById(params.getVideoId());
            return new DataResponse(1000, params);
        } catch (Exception e) {
            return new DataResponse(1001, e.getMessage());
        }
    }


}
