<%@page import="com.itextpdf.text.log.SysoCounter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.io.*" %>
<%@page import="javax.swing.filechooser.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>

<%
    File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
    String desktopPath = desktopDir.getAbsolutePath();
    String templateContent = "[InternetShortcut]" + "\n" + "URL= http://live.61mdk.com/";
    String realfilename = "至尊阁财富讲堂" + ".url";
    String upurl = desktopPath;
    String filename = upurl + "/" + realfilename;
    File myfile = new File(filename);
    if (!myfile.exists()) {
        FileOutputStream fileoutputstream = new FileOutputStream(filename);//建立文件输出流
        byte tag_bytes[] = templateContent.getBytes();
        fileoutputstream.write(tag_bytes);
        fileoutputstream.close();
    }
    try {
        File file = new File(upurl, realfilename);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[111000];
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
            realfilename = new String(realfilename.getBytes("UTF-8"), "ISO8859-1");
        } else {
            realfilename = java.net.URLEncoder.encode(realfilename, "UTF-8");
        }
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");//不同类型的文件对应不同的MIME类型
        response.setHeader("Content-Disposition", "attachment; filename=" + realfilename);
        OutputStream os = response.getOutputStream();
        while (bis.read(buffer) > 0) {
            os.write(buffer);
        }
        bis.close();
        os.close();
        out.clear();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

</body>
</html>  