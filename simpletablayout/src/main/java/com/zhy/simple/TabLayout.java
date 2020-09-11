package com.zhy.simple;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

public class TabLayout extends HorizontalScrollView {
    private ViewPager2 mViewPager2;
    private LinearLayout mTabContainer;
    private int mHeight;
    private Paint mPaint;
    private float positionOffset;
    private int currentPosition; //当前item
    private int iPosition; //上一个item
    private int nextPosition; //下一个item
    int mLastScrollX;
    private int defaultTextSize;
    private int defaultTextColor;
    private int selectedTextColor;
    private int indicatorColor;
    private int mInderLeft;

    private boolean aus = false;
    Rect mTabRect = new Rect();
    OnSelectedTabListener onSelectedTabListener;
    OnPageChangeListener onPageChangeListener;
    public TabLayout(Context context) {
        super(context);
        init(context);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
        defaultTextColor = array.getColor(R.styleable.TabLayout_default_text_color,0xff8A8A8A);
        defaultTextSize = array.getDimensionPixelSize(R.styleable.TabLayout_default_text_size,14);
        selectedTextColor = array.getColor(R.styleable.TabLayout_selected_text_color,0xff03a9f4);
        indicatorColor = array.getColor(R.styleable.TabLayout_indicator_color,0xffff33f5);
        init(context);
    }


    private void init(Context context){
        mTabContainer = new LinearLayout(context);
        mTabContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setClipToPadding(false);
        setHorizontalScrollBarEnabled(false);
        mPaint = new Paint();
        mPaint.setColor(indicatorColor);
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left = 0;
        int right = 0;
        if(mTabContainer != null && mViewPager2 != null) {
            TextView current = (TextView) mTabContainer.getChildAt(currentPosition);
            TextView ipview = (TextView) mTabContainer.getChildAt(iPosition);
            if(iPosition > currentPosition) {
                if(positionOffset <= 0.5f) {
//                    left = ipview.getLeft();
//                    right = (int) (left + ipview.getWidth() * (positionOffset * 2));
                }else {

                }
            }else if(iPosition < currentPosition) {
                if(positionOffset <= 0.5f) {
                    left = ipview.getLeft();
                    right = (int) (current.getRight() - ipview.getWidth() * ((0.5f - positionOffset) * 2));
                }else {
                    left = (int) (current.getLeft() -  (ipview.getWidth()) * (1.0f - positionOffset) * 2);
                    right = current.getRight();
                }
                if(right <= ipview.getRight()) {
                    right = ipview.getRight();
                }
            }else if(iPosition == currentPosition && iPosition == 0){
                //positionOffset = 0.......1
                if(iPosition + 1 < mTabContainer.getChildCount()) {
                    if(positionOffset <= 0.5f) {
                        left = current.getLeft();
                        right = (int) (current.getWidth() + mTabContainer.getChildAt(iPosition + 1).getWidth() * (positionOffset * 2));
                    }else {
                        left = current.getLeft() + (int) (mTabContainer.getChildAt(iPosition + 1).getLeft() * ((positionOffset - 0.5) * 2));
                        right = current.getWidth() + mTabContainer.getChildAt(iPosition + 1).getWidth();
                    }
                }else {
                    left = current.getLeft();
                    right = current.getRight();
                }

            }else {
                //iPosition == currentPosition
                if(iPosition + 1 < mTabContainer.getChildCount()) {
                    if(positionOffset <= 0.5f) {
                        left = current.getLeft();
                        right = (int) (current.getRight() + mTabContainer.getChildAt(iPosition + 1).getWidth() * (positionOffset * 2));

                    }else {
                        left = current.getLeft() + (int) (current.getWidth() * ((positionOffset - 0.5) * 2));
                        right = current.getRight() + mTabContainer.getChildAt(iPosition + 1).getWidth();

                    }
                    if(left >=  (mTabContainer.getChildAt(iPosition + 1).getLeft())) {
                        left = mTabContainer.getChildAt(iPosition + 1).getLeft();
                    }

                }else {
                    left = current.getLeft();
                    right = current.getRight();
                }

            }
            left += current.getPaddingLeft();
            right -= current.getPaddingRight();

            RectF rectF = new RectF(left,mHeight - 7,right,mHeight);
            canvas.drawRoundRect(rectF,5,5,mPaint);


        }
    }
    public void setWithViewPager2(ViewPager2 viewPager2){
        this.mViewPager2 = viewPager2;
        this.mViewPager2.registerOnPageChangeCallback(new VpListener());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mInderLeft = l;
        invalidate();
    }
    //添加tab
    public void addTab(String[] tabs){
        if(tabs.length > 0) {
            if(mTabContainer != null) {
                mTabContainer.removeAllViews();
                for(int i = 0;i < tabs.length;i++) {
                    String str = tabs[i];
                    TextView textView = new TextView(getContext());
                    LinearLayout layout = new LinearLayout(getContext());
                    textView.setGravity(Gravity.CENTER);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    textView.setLayoutParams(params);
                    textView.setTextColor(defaultTextColor);
                    textView.setTag("" + i);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,defaultTextSize);
                    textView.setOnClickListener(new NextdClick());
                    textView.setPadding(35,15,35,5);
                    textView.setText(str);
                    mTabContainer.addView(textView);
                }
                addView(mTabContainer);
            }
            invalidate();

        }
    }

    private void calcIndicatorRect(float mCurrentPositionOffset,int position) {
        View currentTabView = mTabContainer.getChildAt(position);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();
        if (position < mTabContainer.getChildCount() - 1) {
            View nextTabView = mTabContainer.getChildAt(position + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();
        }
        mTabRect.left = (int) left;
        mTabRect.right = (int) right;


    }
    private void animateView(TextView view, float scale, int color) {
        if(view == null) {
            return;
        }
        view.animate().setDuration(300).scaleX(scale).scaleY(scale);
        view.setTextColor(color);
    }
    private void animateView(TextView view, float scale, int color, long duration) {
        if(view == null) {
            return;
        }
        view.animate().setDuration(duration).scaleX(scale).scaleY(scale);
        view.setTextColor(color);
    }

    class VpListener extends ViewPager2.OnPageChangeCallback {
        int state;
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            aus =false;
            currentPosition = position;
//            onSelectedTabListener.getPosition(position);
            for(int i = 0;i < mTabContainer.getChildCount();i++) {
                ((TextView)mTabContainer.getChildAt(i)).setTypeface(Typeface.DEFAULT);
            }
            TextView currentTv = (TextView)mTabContainer.getChildAt(position);
            currentTv.setTypeface(Typeface.DEFAULT_BOLD);
            if(onPageChangeListener != null) {
                onPageChangeListener.getPosition(position);
            }
            invalidate();

        }

        @Override
        public void onPageScrolled(int position, float pf, int positionOffsetPixels) {
//            super.onPageScrolled(position, pf, positionOffsetPixels);
            aus = true;
            iPosition = position;
            positionOffset = pf;
            if(iPosition < currentPosition) {
                if(currentPosition - iPosition >= 1 ) {
                    currentPosition -= 1;
                }
            }else if(iPosition > currentPosition) {
                if(iPosition - currentPosition >= 1 ) {
                    currentPosition += 1;
                }
            }
            ArgbEvaluator evaluator = new ArgbEvaluator();
            TextView currentTv = (TextView)mTabContainer.getChildAt(position);
            TextView previousTv = (TextView)mTabContainer.getChildAt(currentPosition);
            TextView nextTv = (TextView)mTabContainer.getChildAt(position + 1);
            int defaultColorToSelectedColorEvaluate = (Integer) evaluator.evaluate(pf,defaultTextColor,selectedTextColor);
            int selectedColorToDefaultColorEvaluate = (Integer) evaluator.evaluate(pf,selectedTextColor,defaultTextColor);
            if(currentPosition > position) {
                animateView(currentTv,1.0f + 0.2f * ( 1.0f - pf) ,selectedColorToDefaultColorEvaluate,0);
                animateView(previousTv,1.2f - 0.2f *  ( 1.0f - pf) ,defaultColorToSelectedColorEvaluate,0);
            }else {
                if(position + 1 <= mTabContainer.getChildCount() - 1) {
                    animateView(nextTv,1.0f + 0.2f * pf ,defaultColorToSelectedColorEvaluate,0);
                }
                animateView(currentTv,1.2f - 0.2f *  (pf) ,selectedColorToDefaultColorEvaluate,0);

            }
            int offset = (int)(mTabContainer.getChildAt(position).getWidth() * pf);
            int scrollX = (int) (mTabContainer.getChildAt(position).getLeft() + offset);

            if (position > 0 || offset > 0) {
                /**HorizontalScrollView移动到当前tab,并居中*/
                scrollX -= getWidth() / 2 - getPaddingLeft();
                calcIndicatorRect(pf,position);
                scrollX += ((mTabRect.right - mTabRect.left) / 2);
            }

            if (scrollX != mLastScrollX) {
                mLastScrollX = scrollX;
                smoothScrollTo(scrollX, 0);
            }
//            mInderLeft = scrollX;

            invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            this.state = state;
            if(state == 1) {
                currentPosition = mViewPager2.getCurrentItem();
            }
        }
    }
    //selectedTextColor
    private void selectTabItem(int p){
        if(currentPosition == p) {
            return;
        }
        animateView(((TextView)mTabContainer.getChildAt(p)),1.2f,selectedTextColor);
        animateView(((TextView)mTabContainer.getChildAt(currentPosition)),1f,defaultTextColor);
        currentPosition = p;
    }

    class NextdClick implements OnClickListener {
        @Override
        public void onClick(View view) {
            int p = Integer.valueOf((String) view.getTag()).intValue();
            if(mViewPager2 != null) {
                setCurrentItem(p);
                if(onSelectedTabListener != null) {
                    onSelectedTabListener.getPosition(p);
                }
            }
        }
    }
    public void setCurrentItem(int p){
        selectTabItem(p);
        mViewPager2.setCurrentItem(p,true);
    }
    public void setOnSelectedTabListener(OnSelectedTabListener onSelectedTabListener) {
        this.onSelectedTabListener = onSelectedTabListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnSelectedTabListener {
        void getPosition(int position);
    }
    public interface OnPageChangeListener{
        void getPosition(int position);
    }
}
