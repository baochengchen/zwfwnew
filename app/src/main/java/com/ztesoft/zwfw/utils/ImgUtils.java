package com.ztesoft.zwfw.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.service.dreams.DreamService;

public class ImgUtils {

	
	  public static Bitmap toRoundBitmap(Bitmap bitmap){
          int width=bitmap.getWidth();
          int height=bitmap.getHeight();
          int r=0;
          if(width<height){
                  r=width;
          }else{
                  r=height;
          }
          Bitmap backgroundBm=Bitmap.createBitmap(width,height,Config.ARGB_8888);
          Canvas canvas=new Canvas(backgroundBm);
          Paint p=new Paint();
          p.setAntiAlias(true);
          RectF rect=new RectF(0, 0, r, r);
          canvas.drawRoundRect(rect, r/2, r/2, p);
          p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
          canvas.drawBitmap(bitmap, 0, 0, p);
          return backgroundBm;
  }
	  
	  public static Bitmap drawableToBitmap(Drawable drawable) {  

	        int w = drawable.getIntrinsicWidth();  
	        int h = drawable.getIntrinsicHeight();  

	        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888  : Config.RGB_565;

	        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  

	        Canvas canvas = new Canvas(bitmap);  
	        drawable.setBounds(0, 0, w, h);
	        drawable.draw(canvas);  
	        return bitmap;  
	    } 
	  
	  
	  
	  public static byte[] Img2byte(String path){
		  byte[] bytes=null;
		  File file=new File(path);
		try {
			FileInputStream fin=new FileInputStream(file);
            bytes=new byte[(int) file.length()];
			fin.read(bytes);
			fin.close();
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		  
		  
		return bytes;
		  
	  }
	  
	  
	  
	  
	  public static void Drawble2File(String path,Drawable drawable){
		  
		  Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
		  ByteArrayOutputStream bos=new ByteArrayOutputStream();
		  bitmap.compress(CompressFormat.JPEG, 20, bos);
		  byte[] bytes=bos.toByteArray();
		  File file=new File(path);
		  FileOutputStream fos = null;
		  try {
			 fos=new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		  
	  }
	  
	  
	  
	  public static void Bitmap2File(String path,Bitmap bitmap){
		  
		  ByteArrayOutputStream bos=new ByteArrayOutputStream();
		  bitmap.compress(CompressFormat.JPEG, 100, bos);
		  byte[] bytes=bos.toByteArray();
		  File file=new File(path);
		  FileOutputStream fos = null;
		  try {
			 fos=new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		  
	  }
	  
	  
	  
	  public static Bitmap getThumbnails(String path){

			Options options=new Options();

				options.inJustDecodeBounds=false;
				options.inSampleSize=4;
		   Bitmap bitmap=BitmapFactory.decodeFile(path, options);

			return bitmap;
			
		} 
}
