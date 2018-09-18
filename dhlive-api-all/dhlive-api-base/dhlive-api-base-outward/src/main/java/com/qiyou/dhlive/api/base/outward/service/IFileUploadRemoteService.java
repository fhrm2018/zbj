package com.qiyou.dhlive.api.base.outward.service;

import java.awt.Color;
import java.io.InputStream;

public interface IFileUploadRemoteService {

    /**
     * 保存文件(包括图片和文件)
     *
     * @param inputStream
     * @param extName
     * @return 图片ID路径
     * @throws Exception
     */
    String storeFile(InputStream inputStream, String extName) throws Exception;

    /**
     * 保存文件(包括图片和文件)
     *
     * @param fileByte
     * @param extName
     * @return 图片ID路径
     * @throws Exception
     */
    public String storeFile(byte[] fileByte, String extName) throws Exception;

    /**
     * 读取图片
     *
     * @param fileId
     * @param type
     * @return 图片文件
     * @throws Exception
     */
    byte[] getImageFile(String fileId, String type) throws Exception;

    /**
     * 保存原始文件
     *
     * @param fileByte
     * @param extName
     * @return 图片ID路径
     * @throws Exception
     */
    public String storeOriFile(byte[] fileByte, String extName) throws Exception;

    public String chatImgFile(byte[] fileByte, String extName) throws Exception;

//	public String download(String urlString);

    public String markHead(String iconImg, String outImgPath, Color markContentColor);

    public String markQrcode(String srcImgPath, String iconImg, String outImgPath, Color markContentColor);
}
