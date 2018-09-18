package com.qiyou.dhlive.api.base.outward.util;

import com.yaozhong.framework.base.common.utils.EmptyUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AddressUtils {
    public final static String urlStr = "http://ip.taobao.com/service/getIpInfo.php";

    public final static String localIpStr = "0:0:0:0:0:0:0:1";

    public final static String toLocalIpStr = "192.168.0.1";

    public static String getAddresses(String ip, String encodingString)
            throws UnsupportedEncodingException {
        String content = "ip=" + ip;
        List<String> dataList = new ArrayList<String>();
        if (content != null && content.indexOf(localIpStr) > -1) {
            content = content.replace(localIpStr, toLocalIpStr);
        }
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = getResult(urlStr, content, encodingString);

        if (returnStr != null) {
            // 处理返回的省市区信息

            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return "未知地址"; //无效IP，局域网测试
            }

            String region = (temp[5].split(":"))[1].replaceAll("\"", "");
            region = decodeUnicode(region); // 省份

            String city = (temp[7].split(":"))[1].replaceAll("\"", "");
            city = decodeUnicode(city); // 省份

            String area = (temp[9].split(":"))[1].replaceAll("\"", "");
            area = decodeUnicode(area); // 省份

            dataList.add(region);
            dataList.add(city);
            dataList.add(area);

            if (EmptyUtil.isNotEmpty(region) && region.equals(city)) {
                return region + " " + area;
            }
            return region + " " + city + " " + area;
        }

        return "未知地址";
    }

    /**
     * 从 request 中获得 ip 地址 ( 考虑前端可能有一层反向代理服务器 )
     *
     * @param request
     * @return
     */
    public static String getIpAddrFromRequest(HttpServletRequest request) {
        String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");

        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * @param urlStr   请求的地址
     * @param content  请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    private static String getResult(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection(); // 新建连接实例
            connection.setConnectTimeout(2000); // 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2000); // 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true); // 是否打开输出流 true|false
            connection.setDoInput(true); // 是否打开输入流true|false
            connection.setRequestMethod("POST"); // 提交方法POST|GET
            connection.setUseCaches(false); // 是否缓存true|false
            connection.connect(); // 打开连接端口

            DataOutputStream out = new DataOutputStream(connection.getOutputStream()); // 打开输出流往对端服务器写数据
            out.writeBytes(content); // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush(); // 刷新
            out.close(); // 关闭输出流

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), encoding)); // 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();

            return buffer.toString();
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect(); // 关闭连接
            }

        }

        return null;
    }

    /**
     * unicode 转换成 中文
     *
     * @param theString
     * @return
     * @author fanhui 2007-3-15
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);

            if (aChar == '\\') {
                aChar = theString.charAt(x++);

                if (aChar == 'u') {
                    int value = 0;

                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);

                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = ((value << 4) + aChar) - '0';

                                break;

                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = ((value << 4) + 10 + aChar) - 'a';

                                break;

                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = ((value << 4) + 10 + aChar) - 'A';

                                break;

                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }

                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }

                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }

        return outBuffer.toString();
    }

    // 测试
    public static void main(String[] args) {
        AddressUtils addressUtils = new AddressUtils();

        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
        String ip = "0:0:0:0:1";
        String address = null;

        try {
            address = addressUtils.getAddresses(ip, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(address);

        // 输出结果为：广东省,广州市,越秀区
    }
}
