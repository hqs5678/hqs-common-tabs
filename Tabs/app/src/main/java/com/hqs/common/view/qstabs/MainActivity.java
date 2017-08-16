package com.hqs.common.view.qstabs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqs.common.utils.ActivityUtil;
import com.hqs.common.utils.DensityUtils;
import com.hqs.common.view.qtabs.QTabView;
import com.hqs.common.view.qtabs.QTabViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private QTabViewPager viewPager;
    private QTabView tabView;

    private ArrayList<MyFragment> fragments;
    private ArrayList<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActivityUtil.hideActionBar(this);

        viewPager = (QTabViewPager) findViewById(R.id.viewPager);
        tabView = (QTabView) findViewById(R.id.tabView);

        titles.add("热点");
        titles.add("新闻");
        titles.add("美文");
        titles.add("时尚潮流");
        titles.add("学生党");
        titles.add("美女");
        titles.add("美男");
        titles.add("夕阳红");
        titles.add("少年派");
        titles.add("大学生");
        titles.add("孩子");
        titles.add("两性");
        titles.add("新闻");
        titles.add("美文");
        titles.add("时尚潮流");
        titles.add("学生党");
        titles.add("美女");
        titles.add("美男");
        titles.add("夕阳红");
        titles.add("少年派");
        titles.add("大学生");
        titles.add("孩子");
        titles.add("两性");
        titles.add("新闻");
        titles.add("美文");
        titles.add("时尚潮流");
        titles.add("学生党");
        titles.add("美女");
        titles.add("美男");
        titles.add("夕阳红");
        titles.add("少年派");
        titles.add("大学生");
        titles.add("孩子");
        titles.add("两性");

        tabView.setTitles(titles);
        tabView.setSelectedTitleColor(Color.GRAY);
        tabView.setIndicatorColor(Color.LTGRAY);
        tabView.setTitleColor(Color.LTGRAY);


        fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++){
            MyFragment fragment = new MyFragment();
            fragment.title = titles.get(i) + "-" + i;
            if (i % 2 == 0){
                fragment.setBgColor(Color.LTGRAY);
            }
            else{
                fragment.setBgColor(Color.argb(23, 23, 23, 23));
            }
            fragments.add(fragment);
        }

        TestFragmentPagerAdapter adapter = new TestFragmentPagerAdapter(getSupportFragmentManager());
        adapter.fragments = fragments;
        viewPager.setAdapter(adapter);

        tabView.setOnClickTabListener(new QTabView.OnClickTabListener() {
            @Override
            public void onClickTabAt(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        tabView.setViewPager(viewPager);

    }


    public class TestFragmentPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<MyFragment> fragments;

        public TestFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    };


    public static class MyFragment extends Fragment {

        private String title = "";
        private int bgColor = 0;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // fragment 支持横竖屏切换
            setRetainInstance(true);
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            TextView tv = new TextView(getContext());
            tv.setText(this.title);
            tv.setTextSize(DensityUtils.dp2px(getContext(), 20));
            relativeLayout.addView(tv);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            params.addRule(relativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            tv.setLayoutParams(params);

            relativeLayout.setBackgroundColor(bgColor);
            return relativeLayout;
        }


        public void setBgColor(int color){
            this.bgColor = color;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}
