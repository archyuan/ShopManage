package com.jyy.util;

import com.jyy.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.nio.ch.FileKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr){
            String realFileName = getRandomFileName();
            String extension = getFileExtension(thumbnail.getImageName());
            makeDirPath(targetAddr);
            String relativeAddr = targetAddr + realFileName + extension;
            File dest = new File(PathUtil.getImgBasePath()+relativeAddr);

            try{
                Thumbnails.of(thumbnail.getImage()).size(200,200)
                        .outputQuality(0.8)
                        .toFile(dest);
            }catch (IOException e){

                e.printStackTrace();
            }
        return relativeAddr;
    }

    /**
     * 创建目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realPath = PathUtil.getImgBasePath() + targetAddr;
        File realFile = new File(realPath);
        if(!realFile.exists()){
            realFile.mkdirs();
        }
    }

    /**
     * 获取文件扩展名
     * @param
     * @return
     */
    private static String getFileExtension(String fileName) {

        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
         *生成随机文件名
         * */
    public static String getRandomFileName() {
        int randomNumber = new Random().nextInt(89999) + 10000;
        String nowTime = dateFormat.format(new Date());
        return nowTime + randomNumber;
        }

        public static void deleteFileOrPath(String storePath){
            File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
            if(fileOrPath.exists()){
                if(fileOrPath.isDirectory()){
                    File files[] = fileOrPath.listFiles();
                    for(int i = 0;i<files.length;i++){
                        files[i].delete();
                    }
                }
            }
            fileOrPath.delete();
        }


    public static String  generateNormalImg(ImageHolder thumbnail,
                                            String targetAddr){
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath()+relativeAddr);

        try{
            Thumbnails.of(thumbnail.getImage()).size(337,640)
                    .outputQuality(0.9f)
                    .toFile(dest);
        }catch (IOException e){

            e.printStackTrace();
        }
        return relativeAddr;
    }
}
