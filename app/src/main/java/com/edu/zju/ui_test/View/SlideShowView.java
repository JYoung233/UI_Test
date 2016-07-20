package com.edu.zju.ui_test.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.edu.zju.ui_test.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yy on 16/7/18.
 * 实现轮播图的View,目前ViewPager没办法循环
 */
public class SlideShowView extends FrameLayout {
    private final static int IMAGE_COUNT=5;
    private final static int TIME_INTERVAL=5;
    private final static boolean isAutoPlay=true;
    private int[] imagesResIds;
    private List<ImageView> imageViewList;
    private List<View> dotList;
    private ViewPager viewPager;
    private int currentItem=0;
    private ScheduledExecutorService scheduledExecutorService;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);//在指定事件切换图片
        }
    };

    public SlideShowView(Context context, AttributeSet attrs) {//这个构造方法必须有
        super(context, attrs);
        initData();
        initUI(context);
        if(isAutoPlay){
            startPlay();
        }
    }

    private void initUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.slideshow,this,true);

        for(int imageID:imagesResIds){
            ImageView view=new ImageView(context);
            view.setImageResource(imageID);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //记住设置长宽比
            imageViewList.add(view);
        }

        viewPager= (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter(context,imageViewList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean isAutoPlay=false;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                currentItem=position;
//                for(int i=0;i<dotList.size();i++){
//                    if(i==position){
//                        dotList.get(position).setBackgroundResource();
//                    }else{
//                       dotList.get(i).setBackgroundResource();
//                    }
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case 1:
                        //手势滑动,空闲中
                        isAutoPlay=false;
                        currentItem=viewPager.getCurrentItem();
                        break;
                    //界面切换中
                    case 2:
                        isAutoPlay=true;
                        break;
                    case 0:
                        //滑动结束,即切换完毕或者加载完毕
                        if(viewPager.getCurrentItem()==viewPager.getAdapter().getCount()-1&&!isAutoPlay){
                            viewPager.setCurrentItem(0);
                        }else if(viewPager.getCurrentItem()==0&&!isAutoPlay){
                            viewPager.setCurrentItem(viewPager.getAdapter().getCount()-1);
                        }
                        break;


                }

            }
        });

    }

    private void initData() {
        imagesResIds=new int[]{
                R.mipmap.m,
                R.mipmap.n,
                R.mipmap.o,
                R.mipmap.p,
                R.mipmap.q,

        };
        imageViewList=new ArrayList<>();

    }

    //开始轮播图切换
    private void startPlay() {
        Log.v("tag","现在的页面是:"+currentItem);

        scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(),1,4, TimeUnit.SECONDS);

    }
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }
    public SlideShowView(Context context) {
        super(context);
    }
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewList.size();
                handler.obtainMessage().sendToTarget();
            }

        }
    }
    private void destroyBitmaps(){
        for(int i=0;i<IMAGE_COUNT;i++){
            ImageView im=imageViewList.get(i);
            Drawable drawable=im.getDrawable();
            if(drawable!=null){
                drawable.setCallback(null);//接触drawable对view的引用
            }
        }
    }
}
