package com.example.netty.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 袁
 * @date 2018/11/21 0021
 */
@Slf4j
public class FileUploadUtils {
    /**
     * nginx 图片服务器的存储路径
     */
    private static final String IMAGE_URL=FileProperties.address;
    /**
     * nginx 图片服务器的端口 {@value}
     */

    private static final String RETURN_IMAGE_URL=FileProperties.returnUrl+FileProperties.mapping;

    /**
     * 随机数位数
     */
    private static final int RANDOM_NUMBER=6;


    /**
     * LayUI 的图片上传的返回值
     *
     * @param file
     * @return
     */
    public static Map uploadLayUiImg(MultipartFile file, String customPath) {
        String imgURL=fileUpload(file, customPath);
        Map layUiImageResult=new HashMap<String, String>(10);
        Map imgSrc=new HashMap<String, String>(10);
        layUiImageResult.put("code", 0);
        layUiImageResult.put("msg", "成功");
        layUiImageResult.put("data", imgSrc);
        imgSrc.put("src", imgURL);
        return layUiImageResult;
    }

    /**
     * @param file       文件数组
     * @param customPath 自定义图片上传的路径
     * @return 返回字符拼接的图片路径
     */
    public static String filesUpload(MultipartFile[] file, String customPath) {
        final String saveFilePath=getFilePath(customPath);
        StringBuilder fileName=new StringBuilder();
        if (file != null && file.length > 0) {
            try {
                for (int i=0; i < file.length; i++) {
                    if (!file[i].isEmpty()) {
                        String filename=file[i].getOriginalFilename();
                        // 获取文件的扩展名如:mp4
                        String extensionName=filename.substring(filename.lastIndexOf(".")+1);
                        // 获取当前时间的毫秒数
                        String millisecond=String.valueOf(System.currentTimeMillis());
                        // 该文件在系统中的名字
                        String newFileName=millisecond+"."+extensionName;
                        // 文件存储的路径
                        String fileNames=RETURN_IMAGE_URL+saveFilePath+newFileName;
                        // 文件储存绝对路径
                        String path=IMAGE_URL+saveFilePath+newFileName;
                        File newFile=new File(path);
                        if (i == file.length-1) {
                            fileName.append(fileNames);
                        } else {
                            fileName.append(fileNames).append(",");
                        }
                        // 直接写文件
                        file[i].transferTo(newFile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.error(fileName.toString());
        return fileName.toString();
    }


    /**
     * @param file 文件
     * @return 返回图片的路径
     */
    public static String fileUpload(MultipartFile file, String customPath) {
        final String saveFilePath=getFilePath(customPath);
        if (file != null) {
            // 获取上传文件的全名
            String filename=file.getOriginalFilename();
            // 获取文件的扩展名如:mp4
            String extensionName=filename.substring(filename.lastIndexOf(".")+1);
            // 获取当前时间的毫秒数
            String millisecond=String.valueOf(System.currentTimeMillis());
            String milliseconds=millisecond+random();
            // 该文件在系统中的名字
            String newFileName=milliseconds+"."+extensionName;
            // 文件存储的路径
            String fileName=RETURN_IMAGE_URL+saveFilePath+newFileName;
            // 文件储存绝对路径
            String path=IMAGE_URL+saveFilePath+newFileName;
            System.out.println(path);
            //创建文件路径
            File newFile=new File(path);
            try {
                // 通过MultipartFile的方法直接写文件（注意这个时候）
                file.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.error(fileName);
            return fileName;
        }
        return null;
    }

    private static String getFilePath(String customPath) {
        /* 设置日期格式 */
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String format=df.format(new Date());
        String saveFilePath=customPath+format+"/";
        /* 构建文件目录 */
        File fileDir=new File(IMAGE_URL+saveFilePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return saveFilePath;
    }

    /**
     * 获取随机数
     *
     * @return 获取六位随机数
     */
    private static String random() {

        Random random=new Random();
        StringBuilder result=new StringBuilder();
        for (int i=0; i < RANDOM_NUMBER; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    /**
     * @param file       图片数组
     * @param customPath 自定义地址
     * @return wangEditor富文本编辑器返回值
     */
//    public static Map wangEditorImagesUpload(MultipartFile[] file, String customPath) {
//        List<String> url = new ArrayList<>();
//        String imgUrl = filesUpload(file, customPath);
//        String[] split = imgUrl.split(",");
//        for (int i = 0; i < split.length; i++) {
//            url.add(split[i]);
//        }
//        Map map = new HashMap(10);
//        map.put("errno", "0");
//        map.put("data", url);
//        return map;
//    }

}
