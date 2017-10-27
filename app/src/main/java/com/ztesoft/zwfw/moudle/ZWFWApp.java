package com.ztesoft.zwfw.moudle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.io.IOException;
import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by BaoChengchen on 2017/8/6.
 */

public class ZWFWApp extends Application{


    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        RequestManager.getInstance().init(this);
        initImageLoader();
        initImagePicker();
        //JPushInterface.setDebugMode(true);
        //JPushInterface.init(this);
    }




    private void initImageLoader(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pic_loading)    //设置下载过程中图片显示
                .showImageForEmptyUri(R.mipmap.pic_loading) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.pic_fail) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.imageDownloader(new MyImageDownLoader(getApplicationContext()))
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }



    public class PicassoImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

            Glide.with(activity).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }



        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }


    public class MyImageDownLoader extends BaseImageDownloader {

        public Context context;

        public MyImageDownLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public InputStream getStream(String imageUri, Object extra) throws IOException {
           /* if(DataReference.getInstance(context).getIMGIsWiFiDownload()){
                return null;
            }*/
            return super.getStream(imageUri, extra);
        }
    }
}
