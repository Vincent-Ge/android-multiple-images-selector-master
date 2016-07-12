package com.zfdang.demo.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;


public class ImageFactory {  
  
    /** 
     * 直接加载成流资源，调jni,执行底层语言
     *  
     * @param imgPath 图片路径
     * @return 
     */  
    public Bitmap getBitmap(String imgPath) {  
        // Get bitmap through image path  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = false;  

	    /* BitmapFactory.Options.inPurgeable; 
	     * 如果 inPurgeable 设为True的话表示使用BitmapFactory创建的Bitmap 
	     * 用于存储Pixel的内存空间在系统内存不足时可以被回收， 
	     * 在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel）， 
	     * 系统会再次调用BitmapFactory decoder重新生成Bitmap的Pixel数组。  
	     * 为了能够重新解码图像，bitmap要能够访问存储Bitmap的原始数据。 
	     *  
	     * 在inPurgeable为false时表示创建的Bitmap的Pixel内存空间不能被回收， 
	     * 这样BitmapFactory在不停decodeByteArray创建新的Bitmap对象， 
	     * 不同设备的内存不同，因此能够同时创建的Bitmap个数可能有所不同， 
	     * 200个bitmap足以使大部分的设备重新OutOfMemory错误。 
	     * 当isPurgable设为true时，系统中内存不足时， 
	     * 可以回收部分Bitmap占据的内存空间，这时一般不会出现OutOfMemory 错误。 */
        newOpts.inPurgeable = true;  
        newOpts.inInputShareable = true;  
        // Do not compress  
        newOpts.inSampleSize = 1;  
        newOpts.inPreferredConfig = Config.RGB_565;  
        return BitmapFactory.decodeFile(imgPath, newOpts);  
    }  
      
    /** 
     * 图像位图（bitmap）存储到指定的路径 
     *  
     * @param bitmap 位图
     * @param outPath 输出路径
     * @throws FileNotFoundException  
     */  
    public void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {  
        FileOutputStream os = new FileOutputStream(outPath);  
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);  
    }  
      
    /** 
     * 直接按像素缩小图片, 将影响图片.  
     * 用于缩略图 
     *  
     * @param imgPath 图片路径 
     * @param pixelW  
     * @param pixelH  
     * @return 
     */  
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {      //ratio 比率
        BitmapFactory.Options newOpts = new BitmapFactory.Options();    
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容  
        newOpts.inJustDecodeBounds = true;  
        newOpts.inPreferredConfig = Config.RGB_565;  
        // Get bitmap info, but notice that bitmap is null now    
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);  
            
        newOpts.inJustDecodeBounds = false;    
        int w = newOpts.outWidth;    
        int h = newOpts.outHeight;    
        // 想要缩放的目标尺寸  
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了  
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了  
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (newOpts.outWidth / ww);    
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (newOpts.outHeight / hh);    
        }    
        if (be <= 0) be = 1;    
        newOpts.inSampleSize = be;//设置缩放比例  
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);  
        // 压缩好比例大小后再进行质量压缩  
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除  
        return bitmap;  
    }  
      
    /** 
     * 按大小压缩图片, 将影响图片.   
     * 用于缩略图
     *  
     * @param image 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @return 
     */  
    public Bitmap ratio(Bitmap image, float pixelW, float pixelH) {  
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);  
        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出      
            os.reset();//重置baos即清空baos    
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中    
        }    
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());    
        BitmapFactory.Options newOpts = new BitmapFactory.Options();    
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了    
        newOpts.inJustDecodeBounds = true;  
        newOpts.inPreferredConfig = Config.RGB_565;  
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);    
        newOpts.inJustDecodeBounds = false;    
        int w = newOpts.outWidth;    
        int h = newOpts.outHeight;    
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了  
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (newOpts.outWidth / ww);    
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (newOpts.outHeight / hh);    
        }    
        if (be <= 0) be = 1;    
        newOpts.inSampleSize = be;//设置缩放比例    
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了    
        is = new ByteArrayInputStream(os.toByteArray());    
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);  
        //压缩好比例大小后再进行质量压缩  
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除  
        return bitmap;  
    }  
      
    /** 
     * 质量压缩,  指定的路径生成图像 
     *  
     * @param image 
     * @param outPath 
     * @param maxSize target will be compressed to be smaller than this size.(kb) 
     * @throws IOException  
     */  
    public void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {  
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        // scale  
        int options = 100;  
        // Store the bitmap into output stream(no compress)  
        image.compress(Bitmap.CompressFormat.JPEG, options, os);    
        // Compress by loop  
        while ( os.toByteArray().length / 1024 > maxSize) {  
            // Clean up os  
            os.reset();  
            // interval 10  
            options -= 10;  
            image.compress(Bitmap.CompressFormat.JPEG, options, os);  
        }  
          
        // Generate compressed image file  
        FileOutputStream fos = new FileOutputStream(outPath);    
        fos.write(os.toByteArray());    
        fos.flush();    
        fos.close();    
    }  
      
    /** 
     * 质量压缩,  生产图片到指定路径 
     *  
     * @param imgPath 
     * @param outPath 
     * @param maxSize target will be compressed to be smaller than this size.(kb) 
     * @param needsDelete 压缩后是否删除原始文件 
     * @throws IOException  
     */  
    public void compressAndGenImage(String imgPath, String outPath, int maxSize, boolean needsDelete) throws IOException {  
        compressAndGenImage(getBitmap(imgPath), outPath, maxSize);  
          
        // Delete original file  
        if (needsDelete) {  
            File file = new File (imgPath);  
            if (file.exists()) {  
                file.delete();  
            }  
        }  
    }  
      
    /** 
     * 判断大小压缩图片并生成指定路径文件 
     *  
     * @param image 
     * @param outPath 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @throws FileNotFoundException 
     */  
    public void ratioAndGenThumb(Bitmap image, String outPath, float pixelW, float pixelH) throws FileNotFoundException {  
        Bitmap bitmap = ratio(image, pixelW, pixelH);  
        storeImage( bitmap, outPath);  
    }  
      
    /** 
     * Ratio and generate thumb to the path specified 
     *  
     * @param image 
     * @param outPath 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @param needsDelete 是否删除原始文件 
     * @throws FileNotFoundException 
     */  
    public void ratioAndGenThumb(String imgPath, String outPath, float pixelW, float pixelH, boolean needsDelete) throws FileNotFoundException {  
        Bitmap bitmap = ratio(imgPath, pixelW, pixelH);  
        storeImage( bitmap, outPath);  
          
        // Delete original file  
                if (needsDelete) {  
                    File file = new File (imgPath);  
                    if (file.exists()) {  
                        file.delete();  
                    }  
                }  
    }  
      
}  
