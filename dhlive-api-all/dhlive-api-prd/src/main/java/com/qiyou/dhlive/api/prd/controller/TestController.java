package com.qiyou.dhlive.api.prd.controller;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiyou.dhlive.api.base.outward.util.TimeConverterUtil;
import com.qiyou.dhlive.api.prd.mvc.HttpSessionTool;
import com.qiyou.dhlive.api.prd.mvc.UserSession;
import com.qiyou.dhlive.api.prd.util.AddressUtils;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.UnSession;


/**
 * ${DESCRIPTION}
 *
 * @author fish
 * @create 2018-01-21 15:51
 **/
@Controller
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    private IUserManageInfoService userManageInfoService;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @UnSession
    @RequestMapping(value = "login")
    @ResponseBody
    public DataResponse userLogin(UserManageInfo user, HttpSession session, HttpServletRequest request) {
        String loginName = user.getUserTel();
        user = this.userManageInfoService.getManageUserByLoginName(loginName);
        if (EmptyUtil.isNotEmpty(user)) {//管理员登录(老师, 管理员等)
            String loginIp = AddressUtils.getIpAddrFromRequest(request);
            user.setLastLoginIp(loginIp);
            this.userManageInfoService.modifyEntity(user);
            UserSession userSession = UserSession.getUserSession();
            if (EmptyUtil.isNotEmpty(userSession)) {
                HttpSessionTool.doUserLoginOut(session);
            }
            HttpSessionTool.doLoginUser(session, user);
        } else {//会员登录
            UserVipInfo vip = this.userVipInfoService.getVipUserByLoginName(loginName);
            if (EmptyUtil.isNotEmpty(vip)) {
                String loginIp = AddressUtils.getIpAddrFromRequest(request);
                vip.setLastLoginIp(loginIp);
                this.userVipInfoService.modifyEntity(vip);
                UserSession userSession = UserSession.getUserSession();
                if (EmptyUtil.isNotEmpty(userSession)) {
                    HttpSessionTool.doUserLoginOut(session);
                }
                HttpSessionTool.doLoginUser(session, vip);
            }
        }
        return new DataResponse(1000, user);
    }


//    @RequestMapping(value = "fish")
//    @UnSession
//    public String test(Model model) {
//        model.addAttribute("sdkAppID", "1400064635");
//        model.addAttribute("accountType", "21954");
//        model.addAttribute("avChatRoomId", "@TGS#aRFQHABFK");
//        model.addAttribute("identifier", "fish");
//        model.addAttribute("identifierNick", "fish");
//        model.addAttribute("userSig", "eJxFkFFvgjAUhf8Lz8tsoYW5xAcswtjwwSDRPZFqW7guQIVq5pb997EGs9fvy80553472yx-5FqDKLkpvV44zw5yHiyWnxp6WXJlZD9iTCl1Ebrbq*wH6NpRuAhT7HoI-UsQsjWgwB4qGOqJD1CNYL3asHSpivfZWy1r2tFzkqAg2BMck*iw3sHyhVzYuchTFsW7GQshJHMxZ0WWnhhqWLHJmnz7Wt94kBVhmsQrta-0F*Cn7BpVi8U9THyUdtpfeTKW84nv0UkaaKQdhX3q*sTDE*fHY3dpTWluWtpf-PwCIN5VLQ__");
//        return "index";
//    }


    @RequestMapping(value = "fish")
    @UnSession
    @ResponseBody
    public DataResponse test(HttpServletRequest request) throws Exception {
        String ip = AddressUtils.getIpAddrFromRequest(request);
        return new DataResponse(1000, getMACAddress(ip));
    }


    public static void main(String args[]) throws Exception {
//        String host = "39.108.88.133";
//        String port = "6379";
//        Jedis jedis = new Jedis(host, Integer.parseInt(port));
//        String auth = "qiangqiang";
//        if (EmptyUtil.isNotEmpty(auth)) {
//            jedis.auth(auth);
//        }
//        Set<String> chatMsgs = jedis.keys("dhlive-cachedata-chat-messageMap");
//        for (String key : chatMsgs) {
//            System.out.println(key);
//        }


        String str = "2018-06-11T08:10:12.167Z";

        System.out.print(TimeConverterUtil.utc2Local(str, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd HH:mm:ss"));


//        String UTC = "2018-06-11T08:10:12.167Z";
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        System.out.println(TimeZone.getTimeZone("UTC"));
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date UtcDate = null;
//        try {
//            UtcDate = sdf.parse(UTC);
//        } catch (Exception e) {
//            return;
//        }
//
//        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        localFormater.setTimeZone(TimeZone.getDefault());
//        String localTime = localFormater.format(UtcDate.getTime());





//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date now = new Date();
//        String date1 = "2018-04-15 00:00:00";
//        String date2 = "2018-04-21 23:59:59";
//
//        System.out.println(now.after(sdf.parse(date1)));
//        System.out.println(now.before(sdf.parse(date2)));


//        List<TestVo> list = new ArrayList<TestVo>();
//        String json = "[{\"money\":\"1.88\",\"probability\":\"50\"},{\"money\":\"2.88\",\"probability\":\"30\"},{\"money\":\"5.88\",\"probability\":\"15\"},{\"money\":\"8.88\",\"probability\":\"5\"}]";
//        JSONArray arr = JSONArray.parseArray(json);
//        for (int i = 0; i < arr.size(); i++) {
//            TestVo vo = JSONObject.parseObject(arr.get(i).toString(), TestVo.class);
//            list.add(vo);
//        }

//        for(int x = 0; x <= 100; x++){
//            Integer random = new Random().nextInt(100) + 1;
//            System.out.println(random);
//            BigDecimal money = new BigDecimal(0);
//            Integer item = list.size() - 1;
//
//            for (int i = 0; i < list.size(); i++) {
//                if (i == 0) {
//                    if (random > Integer.parseInt(list.get(i).getProbability())) {
//                        money = new BigDecimal(list.get(i).getMoney());
//                        break;
//                    }
//                    if (i + 1 <= item) {
//                        if (random < Integer.parseInt(list.get(i).getProbability()) && random > Integer.parseInt(list.get(i + 1).getProbability())) {
//                            money = new BigDecimal(list.get(i).getMoney());
//                            break;
//                        }
//                    }
//                }
//
//                if (i != 0 && i < item) {
//                    if (random < Integer.parseInt(list.get(i).getProbability()) && random > Integer.parseInt(list.get(i + 1).getProbability())) {
//                        money = new BigDecimal(list.get(i).getMoney());
//                        break;
//                    }
//                }
//
//                if (i == item) {
//                    if (random < Integer.parseInt(list.get(i).getProbability())) {
//                        money = new BigDecimal(list.get(i).getMoney());
//                        break;
//                    }
//                }
//            }
//
//            int result = money.compareTo(new BigDecimal(0));
//            if (result == 0) {
//                money = new BigDecimal(list.get(0).getMoney());
//            }
//            System.out.println(money);
//        }

//        String str = "骗子";
//        String input = "你们是骗子";
//
//        boolean b = input.contains(str);
//        System.out.println(b);

//        System.out.print(DateUtil.getDate(new Date()));


    }

    public static String test(List<TestVo> list) {
//        int n = 60;
        int n = (int) (Math.random() * 100 + 1);
        System.out.println(n);

        String money = "";
        for (int i = 0; i < list.size(); i++) {

        }
        return money;
    }


    //通过ip获取客户端mac地址
    public String getMACAddress(String ip) {
        String macAddr = "";
        try {
            Process p = Runtime.getRuntime().exec("nmblookup -A " + ip);
            InputStreamReader ir = new InputStreamReader(p.getInputStream(), "GBK");
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                String str = input.readLine();
                if (i == 14) {
                    macAddr = str.split("=")[1].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return macAddr;
    }
}
