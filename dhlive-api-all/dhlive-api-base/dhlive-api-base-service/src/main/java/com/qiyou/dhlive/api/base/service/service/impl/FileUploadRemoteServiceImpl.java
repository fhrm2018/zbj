package com.qiyou.dhlive.api.base.service.service.impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.qiyou.dhlive.api.base.outward.service.IFileUploadRemoteService;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.FileUtil;
import com.yaozhong.framework.base.common.utils.ImageUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.common.ObjectId;
import net.coobird.thumbnailator.Thumbnails.Builder;
import org.springframework.stereotype.Service;

@Service
public class FileUploadRemoteServiceImpl implements IFileUploadRemoteService {

    @Autowired
    private IBaseSysParamService baseSysParamService;

    private String imageDir;

    private String fileDir;

    private Float minScale;

    private Float minQuality;

    private Integer smallWidth;

    private Integer smallHeight;

    private Integer photoWidth;

    private Integer photoHeight;


    public void init() {
        this.imageDir = this.baseSysParamService.getValueByKey("image.dir");
        this.fileDir = this.baseSysParamService.getValueByKey("file.dir");
        this.minScale = Float.parseFloat(this.baseSysParamService.getValueByKey("image.min.scale"));
        this.minQuality = Float.parseFloat(this.baseSysParamService.getValueByKey("image.min.quality"));
        this.smallWidth = Integer.parseInt(this.baseSysParamService.getValueByKey("image.small.width"));
        this.smallHeight = Integer.parseInt(this.baseSysParamService.getValueByKey("image.small.height"));
        this.photoWidth = Integer.parseInt(this.baseSysParamService.getValueByKey("image.photo.width"));
        this.photoHeight = Integer.parseInt(this.baseSysParamService.getValueByKey("image.photo.height"));
    }


    /**
     * 原图地址前缀
     */
    private static final String ORIGINAL_PATH = "ori";

    /**
     * 聊天发送图片地址前缀
     */
    private static final String SEND_IMAGE_PATH = "chat";

    /**
     * 压缩图地址前缀
     */
    private static final String MIN_PATH = "min";

    /**
     * 缩略图地址前缀
     */
    private static final String SMALL_PATH = "small";

    /**
     * 头像地址前缀
     */
    private static final String PHOTO_PATH = "photo";

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Override
    public String storeFile(InputStream inputStream, String extName) throws Exception {
        init();
        String picId = DateUtil.getDateString(new Date(), "yyyyMMddHHmmss") + ObjectId.get();
        String filePath = "";
        if (ImageUtil.isImage(extName)) {
            filePath = imageDir + "/" + ORIGINAL_PATH + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
            Thumbnails.of(inputStream).outputQuality(0.5).toFile(imageDir + "/" + MIN_PATH + "/" + picId + "." + extName);
            Thumbnails.of(inputStream).outputQuality(0.2).forceSize(40, 40).toFile(imageDir + "/" + SMALL_PATH + "/" + picId + "." + extName);
        } else {
            filePath = fileDir + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
        }
        return picId;
    }

    @Override
    public String storeFile(byte[] fileByte, String extName) throws Exception {
        init();
        String jobName = "文件上传";
        long start = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--开始"));
        InputStream inputStream = new ByteArrayInputStream(fileByte);
        String picId = DateUtil.getDateString(new Date(), "yyyyMMddHHmmss") + ObjectId.get();
        String filePath = "";
        if (ImageUtil.isImage(extName)) {
            filePath = imageDir + "/" + ORIGINAL_PATH + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
            try {
                Builder minBuiler = Thumbnails.of(filePath);
                minBuiler.scale(minScale).outputQuality(minQuality).toFile(imageDir + "/" + MIN_PATH + "/" + picId + "." + extName);
                Builder smallBuiler = Thumbnails.of(filePath);
                smallBuiler.size(smallWidth, smallHeight).outputQuality(1.0).toFile(imageDir + "/" + SMALL_PATH + "/" + picId + "." + extName);
                Builder photoBuiler = Thumbnails.of(filePath);
                photoBuiler.size(photoWidth, photoHeight).outputQuality(1.0).toFile(imageDir + "/" + PHOTO_PATH + "/" + picId + "." + extName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            filePath = fileDir + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
        }
        long end = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--结束----耗时:" + (end - start) + "ms"));
        return picId;
    }

    @Override
    public String chatImgFile(byte[] fileByte, String extName) throws Exception {
        init();
        String jobName = "文件上传";
        long start = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--开始"));
        InputStream inputStream = new ByteArrayInputStream(fileByte);
        String picId = DateUtil.getDateString(new Date(), "yyyyMMddHHmmss") + ObjectId.get();
        String filePath = "";
        if (ImageUtil.isImage(extName)) {
            filePath = imageDir + "/" + SEND_IMAGE_PATH + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
        } else {
            filePath = fileDir + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
        }
        long end = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--结束----耗时:" + (end - start) + "ms"));
        return picId;
    }

    @Override
    public byte[] getImageFile(String fileId, String type) throws Exception {
        init();
        File file = new File(imageDir + "/" + type + "/" + fileId);
        FileInputStream fis = new FileInputStream(file);
        byte[] fileByte = FileUtil.InputStreamToBytes(fis);
        return fileByte;
    }

    @Override
    public String storeOriFile(byte[] fileByte, String extName) throws Exception {
        init();
        String jobName = "文件上传(不压缩)";
        long start = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--开始"));
        InputStream inputStream = new ByteArrayInputStream(fileByte);
        String picId = DateUtil.getDateString(new Date(), "yyyyMMddHHmmss") + ObjectId.get();
        String filePath = "";
        if (ImageUtil.isImage(extName)) {
            filePath = imageDir + "/" + ORIGINAL_PATH + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
        } else {
            filePath = fileDir + "/" + picId + "." + extName;
            FileUtil.InputStreamTOFile(inputStream, filePath);
        }
        long end = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--结束----耗时:" + (end - start) + "ms"));
        return picId;
    }


    /**
     * 图片添加水印
     *
     * @param iconImg          需要添加水印的图片的路径
     * @param outImgPath       添加水印后图片输出路径
     * @param markContentColor 水印文字的颜色
     */
    public String markHead(String iconImg, String outImgPath, Color markContentColor) {
        init();
        try {
            String srcImgPath = imageDir + "/" + ORIGINAL_PATH + "/" + "ambassador.jpg";
            // 读取原图片信息  
            File srcImgFile = new File(srcImgPath);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印  
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //Font font = new Font("Courier New", Font.PLAIN, 12);  
//            Font font = new Font("宋体", Font.PLAIN, 50);    
//            g.setColor(markContentColor); //根据图片的背景设置水印颜色  
//              
//            g.setFont(font);  
//            int x = srcImgWidth - getWatermarkLength(waterMarkContent, g) - 3;  
//            int y = srcImgHeight - 3;  
//            g.drawString(waterMarkContent, x, y);  
            ImageIcon imgIcon = new ImageIcon(imageDir + "/" + ORIGINAL_PATH + "/" + iconImg);
            imgIcon.setImage((imgIcon.getImage().getScaledInstance(140, 140, Image.SCALE_DEFAULT)));
            // 得到Image对象。   
            Image img = imgIcon.getImage();
            float alpha = 1f; // 透明度   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(img, 115, 76, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            // 输出图片  
            FileOutputStream outImgStream = new FileOutputStream(imageDir + "/" + ORIGINAL_PATH + "/" + outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outImgPath;
    }


    /**
     * 图片添加水印
     *
     * @param srcImgPath       需要添加水印的图片的路径
     * @param outImgPath       添加水印后图片输出路径
     * @param markContentColor 水印文字的颜色
     * @param iconImg          水印的文字
     */
    public String markQrcode(String srcImgPath, String iconImg, String outImgPath, Color markContentColor) {
        init();
        try {
            // 读取原图片信息  
            File srcImgFile = new File(imageDir + "/" + ORIGINAL_PATH + "/" + srcImgPath);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印  
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //Font font = new Font("Courier New", Font.PLAIN, 12);  
//            Font font = new Font("宋体", Font.PLAIN, 50);    
//            g.setColor(markContentColor); //根据图片的背景设置水印颜色  
//              
//            g.setFont(font);  
//            int x = srcImgWidth - getWatermarkLength(waterMarkContent, g) - 3;  
//            int y = srcImgHeight - 3;  
//            g.drawString(waterMarkContent, x, y);  
            ImageIcon imgIcon = new ImageIcon(imageDir + "/" + ORIGINAL_PATH + "/" + iconImg);
            imgIcon.setImage((imgIcon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT)));
            // 得到Image对象。   
            Image img = imgIcon.getImage();

            float alpha = 1f; // 透明度   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(img, 370, 996, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            // 输出图片  
            FileOutputStream outImgStream = new FileOutputStream(imageDir + "/" + ORIGINAL_PATH + "/" + outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outImgPath;
    }
}
