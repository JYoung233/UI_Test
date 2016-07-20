package com.edu.zju.ui_test;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.edu.zju.ui_test.View.SlideShowView;

public class MainActivity extends AppCompatActivity {
    private SlideShowView slideShowView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideShowView= (SlideShowView) findViewById(R.id.slideView);
        //为什么不支持直接用WindowManager获取
        WindowManager wm=getWindowManager();
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int ScreenWidth=dm.widthPixels;

        ViewGroup.LayoutParams lp=slideShowView.getLayoutParams();
        lp.width=ScreenWidth;
        lp.height=ScreenWidth/3;
        slideShowView.setLayoutParams(lp);




    }
}
