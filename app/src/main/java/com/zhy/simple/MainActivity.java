package com.zhy.simple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TabLayout myTabLayout;
    ViewPager2 viewPager2;
    String[] tabs = new String[]{"小说","相声小品","书评","名著","夜听","戏曲","情感","音乐","财经","党员学习","历史"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

    }
    private void initView() {
        myTabLayout = findViewById(R.id.myTabLayout);
        viewPager2 = findViewById(R.id.viewpager);
    }
    private void initData(){
        myTabLayout.addTab(tabs);
        myTabLayout.setWithViewPager2(viewPager2);
        viewPager2.setAdapter(new TestFragmentAdapter(this));
    }

    class TestFragmentAdapter extends FragmentStateAdapter {
        List<Fragment> fragments = new ArrayList<>();
        public TestFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            for(String s:tabs){
                fragments.add(new BlankFragment());
            }
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}