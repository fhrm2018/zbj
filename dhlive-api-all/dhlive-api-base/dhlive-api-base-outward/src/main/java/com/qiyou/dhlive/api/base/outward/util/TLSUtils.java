package com.qiyou.dhlive.api.base.outward.util;

import com.tls.tls_sigature.tls_sigature;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.tls.tls_sigature.tls_sigature.GenTLSSignatureEx;


/**
 * describe:
 *
 * @author fish
 * @date 2018/01/22
 */
public class TLSUtils {

    private static Logger baseLog = LoggerFactory.getLogger(TLSUtils.class);

    public static Integer getRandom() {
        StringBuilder str = new StringBuilder();
        //定义变长字符串
        Random r = new Random();
        //随机生成数字，并添加到字符串
        for (int i = 0; i < 8; i++) {
            str.append(r.nextInt(10));
        }
        //将字符串转换为数字并输出
        Integer random = Integer.parseInt(str.toString());
        return random;
    }


    /**
     * 获取用户对应的签名
     *
     * @param sdkAppId   appId
     * @param identifier 身份
     * @param privateKey 私钥
     * @return
     */
    public static String getUserSig(Integer sdkAppId, String identifier, String privateKey) {
        try {
            baseLog.info(LogFormatUtil.getActionFormat("获取用户对应的签名", 1));
            tls_sigature.GenTLSSignatureResult userSig = GenTLSSignatureEx(sdkAppId, identifier, privateKey);
            baseLog.info(LogFormatUtil.getActionFormat("获取用户对应的签名结果" + userSig.urlSig, 2));
            return userSig.urlSig;
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return null;
    }

}
