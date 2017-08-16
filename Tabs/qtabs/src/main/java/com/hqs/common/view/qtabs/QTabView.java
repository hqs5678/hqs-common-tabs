package com.hqs.common.view.qtabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by super on 2017/8/11.
 */

public class QTabView extends RelativeLayout {

    private ArrayList<String> titles;
    private int selectedTitleColor = Color.RED;
    private int indicatorColor = Color.RED;
    private int selectedIndex = -1;
    private int titleColor = Color.BLACK;
    private float titleFontSize = -1;
    private QRecyclerView recyclerView;
    private int indicatorHeight = 10;
    private int titlePadding = 50;
    private OnClickTabListener onClickTabListener;
    private int pageWidth = 100;
    private IndicatorView indicatorView;
    private RecyclerViewAdapter adapter;
    private Map<Integer, ViewSize> offsets = new HashMap<>();

    public QTabView(Context context) {
        super(context);
        init();
    }

    public QTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        pageWidth = getResources().getDisplayMetrics().widthPixels;
        d = (int) (pageWidth * 0.5);
        this.recyclerView = new QRecyclerView(getContext());
        this.addView(recyclerView);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);

        this.indicatorView = new IndicatorView(getContext());
        this.addView(indicatorView);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, indicatorHeight);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        indicatorView.setLayoutParams(layoutParams);


        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(layout);

        recyclerView.setOnScrolledListener(new OnScrolledListener() {
            @Override
            public void onScrolled(int sx) {
                updateIndicatorOffset(sx);
            }
        });

        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateIndicatorOffsetAndSize(0, false);
            }
        }, 20);
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public void setSelectedTitleColor(int selectedTitleColor) {
        this.selectedTitleColor = selectedTitleColor;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        indicatorView.paint.setColor(indicatorColor);
    }

    public void setTitlePadding(int titlePadding) {
        this.titlePadding = titlePadding;
    }

    public void setTitleFontSize(float titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public void setViewPager(QTabViewPager viewPager) {

        viewPager.setScrollListener(new QTabViewPager.ScrollListener() {
            @Override
            public void onScrollChanged(int left, boolean isOnTouching) {
                updateIndicatorOffsetAndSize(left, isOnTouching);
            }
        });
    }

    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }

    private void updateIndicatorOffset(int sx){
        indicatorView.offset = sx;
        indicatorView.invalidate();
    }

    private int l;
    private int r;
    private int offset;
    private int index;
    private int d;
    private int step;
    private int l0;
    private int l1;
    private int r0;
    private int r1;
    private int w;
    private int s;
    private int ss;
    private int ll;
    private int sx;
    private float t;
    private float s0;
    private float a0;
    private float s1;
    private float a1;
    private int preLeft = 0;
    private boolean clickActionCalled = false;
    private int startLeft = 0;

    public void updateIndicatorOffsetAndSize(int left, boolean isOnTouching){

        left += startLeft;
        offset = left % pageWidth;
        index = left / pageWidth;
        if (clickActionCalled) {
            if (index == selectedIndex && offset == 0){
                clickActionCalled = false;
                if (left < preLeft) {
                    return;
                }
            }
            else{
                adapter.updateSelectedItem(selectedIndex);
                preLeft = left;
                return;
            }
        }

        if (offset < d){
            adapter.selectItem(index);
        }
        else if (offset > pageWidth - d){
            adapter.selectItem(index + 1);
        }

        int time = 60;
        if (isOnTouching){
            time = 200;
        }

        t = offset * 1.0f / pageWidth;

        try {
            step = 0;
            if (left > preLeft){
                // 左滑
                if (index >= titles.size() - 1){
                    return;
                }

                l0 = offsets.get(index).left;
                l1 = offsets.get(index + 1).left;
                s0 = l1 - l0;
                a0 = s0 * 2;
                l = (int) (l0 + 0.5 * a0 * t * t);

                r0 = offsets.get(index).right;
                r1 = offsets.get(index + 1).right;
                s1 = r1 - r0;
                a1 = s1 * 2;
                r = (int) (r0 + a1 * t - 0.5 * a1 * t * t);

                if (l1 > d && offset != 0){

                    w = r1 - l1;
                    sx = recyclerView.sx;
                    ll = l1 - sx;
                    s = (int) (ll + w * 0.5 - d);
                    step = (int) (s / ((1 - t) * time));

                    ss = (int) (l1 + w * 0.5 - (sx + d));
                    if (step > ss && s > 0){
                        step = ss;
                    }
                }
            }
            else{
                // 右滑
                index += 1;
                if (index < 1){
                    return;
                }

                l0 = offsets.get(index).left;
                l1 = offsets.get(index - 1).left;
                s0 = l0 - l1;
                a0 = s0 * 2;
                float t1 = 1 - t;
                l = (int) (l0 - (a0 * t1 - 0.5 * a0 * t1 * t1));

                r0 = offsets.get(index).right;
                r1 = offsets.get(index - 1).right;
                s1 = r0 - r1;
                a1 = s1 * 2;
                r = (int) (r0 - 0.5 * a1 * t1 * t1);

                sx = recyclerView.sx;
                ll = l1 - sx;
                w = r1 - l1;
                s = (int) (ll + w * 0.5 - d);
                step = (int) (s / (t * time));

                ss = (int) (l1 + w * 0.5 - (sx + d));
                if (step < ss && s < 0){
                    step = ss;
                }
            }

            if (step != 0){
                recyclerView.scrollBy(step, 0);
            }
 
            indicatorView.left = l;
            indicatorView.right = r;
            indicatorView.invalidate();


        } catch (Exception e) {
        }

        preLeft = left;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("q_tab_view", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("q_tab_view_selected_index", selectedIndex);
        editor.putInt("q_tab_view_indicator_left", indicatorView.left);
        editor.putInt("q_tab_view_indicator_right", indicatorView.right);
        editor.putInt("q_tab_view_page_cur_tab_left", offsets.get(selectedIndex).left);
        editor.putInt("q_tab_view_page_cur_tab_width", offsets.get(selectedIndex).right - offsets.get(selectedIndex).left);
        editor.commit();
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("q_tab_view", Context.MODE_PRIVATE);
        int index = sharedPreferences.getInt("q_tab_view_selected_index", 0);
        if (index != selectedIndex){
            adapter.updateSelectedItem(index);
            adapter.updateDeselectedItem(selectedIndex);
            selectedIndex = index;
            startLeft = selectedIndex * pageWidth;
        }
        int left = sharedPreferences.getInt("q_tab_view_indicator_left", 0);
        int right = sharedPreferences.getInt("q_tab_view_indicator_right", 0);

        indicatorView.left = left;
        indicatorView.right = right;
        left = sharedPreferences.getInt("q_tab_view_page_cur_tab_left", 0);
        int tabWidth = sharedPreferences.getInt("q_tab_view_page_cur_tab_width", 0);
        sx = (int) (left - (pageWidth - tabWidth) * 0.5);
        updateRecyclerView(sx);

        super.onRestoreInstanceState(state);
    }

    private void updateRecyclerView(final int sx){
        if (sx > 0){
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollBy(sx, 0);
                }
            }, 10);
        }
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter<QViewHolder>  {

        private Map<Integer, QViewHolder> viewHolders = new HashMap<>();

        @Override
        public QViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View relativeLayout = inflater.inflate(R.layout.item_title, null);
            return new QViewHolder(relativeLayout);
        }

        @Override
        public void onBindViewHolder(final QViewHolder viewHolder, final int i) {

            viewHolders.put(i, viewHolder);
            viewHolder.tv.setText(titles.get(i));
            if (titleFontSize > 0){
                viewHolder.tv.setTextSize(titleFontSize);
            }
            viewHolder.i = i;

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewSize size = new ViewSize();
                    size.left = viewHolder.tv.getLeft() + viewHolder.rootView.getLeft() + recyclerView.sx;;
                    size.right = viewHolder.tv.getRight() + viewHolder.rootView.getLeft() + recyclerView.sx;;
                    size.sx = recyclerView.sx;
                    offsets.put(i, size);
                }
            }, 10);



            if (i == selectedIndex){
                selectItem(i);
            }
            else{
                updateDeselectedItem(i);
            }
            viewHolder.rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedIndex == i){
                        return;
                    }

                    scrollRecyclerViewToCenter(i);

                    selectItem(i);
                    if (onClickTabListener != null){
                        onClickTabListener.onClickTabAt(selectedIndex);
                        clickActionCalled = true;
                    }
                }
            });
        }

        void scrollRecyclerViewToCenter(final int index){

            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        int sx = recyclerView.sx;

                        int left = offsets.get(index).left;
                        int right = offsets.get(index).right;
                        int s = offsets.get(index).sx;
                        int center = (int) ((right + left) * 0.5);
                        center = center - s - (sx - s) + sx;
                        int newSx = (int) (center - pageWidth * 0.5);
                        int d = newSx - sx;

                        recyclerView.postOnAnimation(new AnimThread(d));
                    } catch (Exception e) {
                    }
                }
            }, 20);

        }

        class AnimThread extends Thread {

            private int d;
            private int time = 6;

            public AnimThread(int d){
                this.d = d;
            }

            @Override
            public void run() {
                int step = d / time;
                if (Math.abs(d) < time && step == 0){
                    if (d < 0){
                        step = -1;
                    }
                    else{
                        step = 1;
                    }
                }
                if (step != 0){
                    recyclerView.scrollBy(step, 0);
                    d = d - step;
                    if (d != 0){
                        recyclerView.postOnAnimationDelayed(new AnimThread(d), 10);
                        return;
                    }
                }
            }
        }

        public void selectItem(int i){
            updateSelectedItem(i);
            if (i != selectedIndex){
                updateDeselectedItem(selectedIndex);
            }
            selectedIndex = i;
        }

        public void updateSelectedItem(final int i){
            try {
                viewHolders.get(i).tv.setTextColor(selectedTitleColor);
            } catch (Exception e) {
            }
        }

        public void updateDeselectedItem(int i){
            try {
                viewHolders.get(i).tv.setTextColor(titleColor);
            } catch (Exception e) {
            }
        }

        @Override
        public int getItemCount() {
            if (titles == null){
                return 0;
            }
            return titles.size();
        }
    }

    private class QViewHolder extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView tv;
        private int i = 0;

        public QViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            tv = itemView.findViewById(R.id.title);
            rootView.setPadding(titlePadding, 0, titlePadding, 0);

            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    int left = tv.getLeft() + rootView.getLeft() + recyclerView.sx;
                    int right = tv.getRight() + rootView.getLeft() + recyclerView.sx;
                    if (i == selectedIndex){
                        indicatorView.left = left;
                        indicatorView.right = right;
                        indicatorView.invalidate();
                    }
                    ViewSize size = new ViewSize();
                    size.left = left;
                    size.right = right;
                    size.sx = recyclerView.sx;
                    offsets.put(i, size);
                }
            });
        }
    }

    private class IndicatorView extends View {

        private int left = 0;
        private int right = 100;
        private Paint paint;
        private int offset = 0;

        public IndicatorView(Context context) {
            super(context);
            this.paint = new Paint();
            paint.setColor(indicatorColor);
            paint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.drawRect(left - offset, 0, right - offset, indicatorHeight, paint);
        }
    }

    private class ViewSize {
        int left;
        int right;
        int sx;

        @Override
        public String toString() {
            return "left: " + left + "  right: " + right + "  sx: " + sx;
        }
    }

    private class QRecyclerView extends RecyclerView {

        private int sx = 0;
        private OnScrolledListener onScrolledListener;


        public QRecyclerView(Context context) {
            super(context);
        }

        @Override
        public void onScrolled(int dx, int dy) {
            super.onScrolled(dx, dy);

            sx += dx;
            if (onScrolledListener != null){
                onScrolledListener.onScrolled(sx);
            }
        }

        public void setOnScrolledListener(OnScrolledListener onScrolledListener) {
            this.onScrolledListener = onScrolledListener;
        }
    }

    private interface OnScrolledListener {
        void onScrolled(int sx);
    }

    public interface OnClickTabListener {
        void onClickTabAt(int index);
    }

}

