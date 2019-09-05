package com.xb.flakeview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

/**
 * author True Lies
 * 星星动画View
 * 三阶公式 B(T) = p0((1-t)(1-t)(1-t))+3P1t((1-t)(1-t))+3p2(t*t)(1-t)+p3(t*t*t)
 * 二阶公式 B(T) = (1-t)*(1-t)*P0+2t(1-t)P1+t*t*P2
 * 一阶公式 B(T) = P0+(P1-P0)*t=(1-t)P0+t*P1
 * 全屏View
 * Created by Coding on 2017/9/13.
 * 天女散花O(∩_∩)O
 */

public class FlakeStarAnimView extends FrameLayout {

    private Context mContext = null;
    private int mMaxWidth = 0;
    private int mMaxHeight = 0;
    private Interpolator mLinearInterpolator = new LinearInterpolator();//线性
    private Interpolator mAccelerateInterpolator = new AccelerateInterpolator();//加速
    private Interpolator mDecelerateInterpolator = new DecelerateInterpolator();//减速
    private Interpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();//先加速后减速
    private Interpolator[] mInterpolatorArray = null;
    private Handler mHandler = new Handler();
    private int viewCount = 0;

    public FlakeStarAnimView(@NonNull Context context) {
        this(context, null);
        this.mContext = context;
    }

    public FlakeStarAnimView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public FlakeStarAnimView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initMeasure();
        initView();
        startAnim();
    }


    /**
     * 初始化测量
     */
    private void initMeasure() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mMaxWidth = displayMetrics.widthPixels;
        mMaxHeight = displayMetrics.heightPixels;
    }

    /**
     * 初始化
     */
    private void initView() {
        this.mInterpolatorArray = new Interpolator[]{mLinearInterpolator, mAccelerateInterpolator, mDecelerateInterpolator, mAccelerateDecelerateInterpolator};
    }


    /**
     * 设置为和屏幕一样大
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMaxWidth, mMaxHeight);
    }

    /**
     * 开始动画
     */
    public void startAnim() {
        mHandler.post(starStartRunnable);
    }

    /**
     * 开始动画任务
     */
    private Runnable starStartRunnable = new Runnable() {
        @Override
        public void run() {
//            AnomalyView anomalyView = new AnomalyView(mContext);
//            addView(anomalyView);
//            bezierAnim(anomalyView);
            if (viewCount > 9) {
                viewCount = 0;
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_flake, null);
            ImageView imageView = view.findViewById(R.id.imageView);
            switch (viewCount) {
                case 0:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_zeo);
                    break;
                case 1:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_one);
                    break;
                case 2:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_two);
                    break;
                case 3:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_three);
                    break;
                case 4:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_four);
                    break;
                case 5:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_five);
                    break;
                case 6:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_six);
                    break;
                case 7:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_seven);
                    break;
                case 8:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_eight);
                    break;
                case 9:
                    imageView.setBackgroundResource(R.mipmap.icon_prize_nine);
                    break;
            }
            addView(view);
            bezierAnim(view);
            viewCount++;
            mHandler.postDelayed(starStartRunnable, 100);
        }
    };

    /**
     * 动画实现
     */
    private void bezierAnim(final View view) {

        PointF startPointF = new PointF(new Random().nextInt(mMaxWidth), -50);//起始点
        PointF endPointF = new PointF(new Random().nextInt(mMaxWidth), mMaxHeight);//结束点

        ValueAnimator objectAnimator = ValueAnimator.ofObject(new BezierEvaluator(getScreenRandomPointF(1), getScreenRandomPointF(2)), startPointF, endPointF);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                view.setX(pointF.x);
                view.setY(pointF.y);
            }
        });
        objectAnimator.setInterpolator(mInterpolatorArray[new Random().nextInt(4)]);
        objectAnimator.setTarget(view);
        objectAnimator.setDuration(5000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(view);
            }
        });
        objectAnimator.start();
    }


    /**
     * 获取屏幕上的随机两个控制点
     *
     * @param number 第几个控制点
     * @return
     */
    private PointF getScreenRandomPointF(int number) {
        PointF pointF = new PointF();
        pointF.x = new Random().nextInt(mMaxWidth * 2) - mMaxWidth / 2; //尽量向左右两边扩散一点
        pointF.y = new Random().nextInt((mMaxHeight / 2) * number);
        return pointF;
    }

    /**
     * 贝塞尔路径实现
     * 暂时放置两个控制点 后面根据需求再加
     * 三阶公式 B(T) = p0((1-t)(1-t)(1-t))+3P1t((1-t)(1-t))+3p2(t*t)(1-t)+p3(t*t*t)
     * 二阶公式 B(T) = (1-t)*(1-t)*P0+2t(1-t)P1+t*t*P2
     * 一阶公式 B(T) = P0+(P1-P0)*t=(1-t)P0+t*P1
     */
    private class BezierEvaluator implements TypeEvaluator<PointF> {

        private PointF mControlPointFOne = null;
        private PointF mControlPointFTwo = null;

        public BezierEvaluator(PointF mControlPointFOne, PointF mControlPointFTwo) {
            this.mControlPointFOne = mControlPointFOne;
            this.mControlPointFTwo = mControlPointFTwo;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

            float time = fraction;
            PointF resultPointF = new PointF(); //结果值
            PointF startPointF = startValue; //开始控制点
            PointF endPointF = endValue;//结束控制点

            resultPointF.x = startPointF.x * ((1 - time) * (1 - time) * (1 - time))
                    + 3 * mControlPointFOne.x * time * ((1 - time) * (1 - time))
                    + 3 * mControlPointFTwo.x * (time * time) * (1 - time)
                    + endPointF.x * (time * time * time);
            resultPointF.y = startPointF.y * ((1 - time) * (1 - time) * (1 - time))
                    + 3 * mControlPointFOne.y * time * ((1 - time) * (1 - time))
                    + 3 * mControlPointFTwo.y * (time * time) * (1 - time)
                    + endPointF.y * (time * time * time);

            return resultPointF;
        }
    }

    /**
     * 不规则View
     */
    private class AnomalyView extends View {

        private Context mContext = null;
        private int mMaxWidth = 30;//最大宽度
        private int mMaxHeight = 30;//最大高度
        private int mPadding = 10;//内边距
        private Paint mPaint = null;
        private PointF[] mRandomPoint = null;//随机点  4个  顺序为  左上右下
        private String[] mLumpColorArray = null;

        public AnomalyView(Context context) {
            this(context, null);
            this.mContext = context;
        }

        public AnomalyView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
            this.mContext = context;
        }

        public AnomalyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.mContext = context;
            init();
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mMaxWidth, mMaxHeight);
        }

        /**
         * 初始化
         */
        private void init() {

            this.mLumpColorArray = new String[]{"#ef56c3", "#fffd6d", "#e7efff", "#8c9ae4", "#98e9f4", "#7697ff"};

            this.mRandomPoint = new PointF[4];
            this.mRandomPoint[0] = new PointF(new Random().nextInt(mPadding), new Random().nextInt(mMaxHeight - mPadding)); //左
            this.mRandomPoint[1] = new PointF(new Random().nextInt(mMaxWidth - mPadding) + mPadding, new Random().nextInt(mPadding)); //上
            this.mRandomPoint[2] = new PointF(new Random().nextInt(mPadding) + (mMaxWidth - mPadding), new Random().nextInt(mMaxHeight - mPadding) + mPadding); //右
            this.mRandomPoint[3] = new PointF(new Random().nextInt(mMaxWidth) - mPadding, new Random().nextInt(mPadding) + (mMaxHeight - mPadding));//下

            this.mPaint = new Paint();
            this.mPaint.setDither(true);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(Color.parseColor(this.mLumpColorArray[new Random().nextInt(this.mLumpColorArray.length)]));
            this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        /**
         * 绘制
         *
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Path path = new Path();
            path.moveTo(this.mRandomPoint[0].x, this.mRandomPoint[0].y);
            path.lineTo(this.mRandomPoint[1].x, this.mRandomPoint[1].y);
            path.lineTo(this.mRandomPoint[2].x, this.mRandomPoint[2].y);
            path.lineTo(this.mRandomPoint[3].x, this.mRandomPoint[3].y);
            path.close();
            canvas.drawPath(path, mPaint);
        }
    }

}
