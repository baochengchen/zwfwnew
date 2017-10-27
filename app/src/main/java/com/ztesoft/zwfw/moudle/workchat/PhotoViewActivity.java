package com.ztesoft.zwfw.moudle.workchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.moudle.Config;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends BaseActivity {

    ViewPager mViewPager;
    TextView imgPositionTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        final String[] imgIds = getIntent().getStringArrayExtra("imgs");
        int positon = getIntent().getIntExtra("position",0);
        List<String> imageUrls = new ArrayList<>();
        for(String imgId : imgIds){
            imageUrls.add(Config.BASE_URL + Config.URL_ATTACHMENT + "/"+imgId);
        }


        imgPositionTv = (TextView) findViewById(R.id.img_position_tv);
        imgPositionTv.setText(positon+1+"/"+imgIds.length);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ImageAdapter(imageUrls,this));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imgPositionTv.setText(position+1+"/"+imgIds.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(positon);
    }


    public class ImageAdapter extends PagerAdapter {

        private List<String> imageUrls;
        private Context context;

        public ImageAdapter(List<String> imageUrls, Context context) {
            this.imageUrls = imageUrls;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = imageUrls.get(position);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_view,null);

            final PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.loading_progress_bar);
            Glide.with(context).load(url).placeholder(R.mipmap.pic_loading).diskCacheStrategy(DiskCacheStrategy.ALL).into(new GlideDrawableImageViewTarget(photoView){
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    progressBar.setVisibility(View.GONE);
                    photoView.setImageResource(R.mipmap.pic_fail);
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    progressBar.setVisibility(View.GONE);
                }
            });
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return view;
        }

        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
