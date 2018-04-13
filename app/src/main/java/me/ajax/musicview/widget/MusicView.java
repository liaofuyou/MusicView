package me.ajax.musicview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

import static me.ajax.musicview.utils.GeometryUtils.polarX;
import static me.ajax.musicview.utils.GeometryUtils.polarY;

/**
 * Created by aj on 2018/4/2
 */

public class MusicView extends View {

    Paint mPaint1 = new Paint();
    Paint mPaint2 = new Paint();
    Paint mPaint3 = new Paint();

    Paint circlePaint = new Paint();

    Path path1 = new Path();
    Path path2 = new Path();
    Path path3 = new Path();

    Random random = new Random();
    float animationValue1 = 1F;
    float animationValue2 = 1F;
    float animationValue3 = 1F;

    int[] range1 = new int[8];
    int[] range2 = new int[8];
    int[] range3 = new int[8];

    int degree = 0;
    int verticalTranslateValue;
    //屏幕宽度
    int screenWidth = getResources().getDisplayMetrics().widthPixels;


    public MusicView(Context context) {
        super(context);
        init();
    }

    public MusicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MusicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速

        //画笔
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(0x960000FF);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setPathEffect(new CornerPathEffect(dp2Dx(30)));
        mPaint2 = new Paint(mPaint1);
        mPaint2.setColor(0x9600FFE1);
        mPaint3 = new Paint(mPaint1);
        mPaint3.setColor(0x96FF00FF);


        circlePaint.setColor(Color.WHITE);
        //circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        refreshRange(range1);
        refreshRange(range2);
        refreshRange(range3);

        post(new Runnable() {
            @Override
            public void run() {

                //蠕动动画
                geneValueAnimator(40000, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        refreshRange(range1);
                    }
                }, new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        animationValue1 = (float) animation.getAnimatedValue();
                    }
                }).start();
                geneValueAnimator(4000 + random.nextInt(3000), new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        refreshRange(range2);
                    }
                }, new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        animationValue2 = (float) animation.getAnimatedValue();
                        invalidateView();
                    }
                }).start();
                geneValueAnimator(4000 + random.nextInt(3000), new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        refreshRange(range3);
                    }
                }, new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        animationValue3 = (float) animation.getAnimatedValue();
                    }
                }).start();


                //旋转动画
                ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
                animator.setDuration(500);
                animator.setRepeatCount(Integer.MAX_VALUE - 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        degree++;
                    }
                });
                animator.start();

                animator = ValueAnimator.ofInt(dp2Dx(10), 0, dp2Dx(10));
                animator.setDuration(2000);
                animator.setRepeatCount(Integer.MAX_VALUE - 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        verticalTranslateValue = (int) animation.getAnimatedValue();
                    }
                });
                animator.start();
            }
        });
    }

    void refreshRange(int[] range) {
        for (int i = 0; i < range.length; i++) {
            range[i] = dp2Dx(random.nextInt(25));
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mWidth = getWidth();
        int mHeight = getHeight();

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2 + verticalTranslateValue);
        //1
        computePath(path1, range1, animationValue1, 0);
        canvas.drawPath(path1, mPaint1);
        //2
        computePath(path2, range2, animationValue2, 30);
        canvas.drawPath(path2, mPaint2);
        //3
        computePath(path3, range3, animationValue3, 45);
        canvas.drawPath(path3, mPaint3);
        canvas.drawCircle(0, 0, dp2Dx(70), circlePaint);

        canvas.restore();
    }

    void computePath(Path path, int[] range, float animationValue, int degreeOffset) {
        path.reset();
        for (int i = 0; i < 8; i++) {
            float x;
            float y;

            if (range == range1) {
                x = polarX(dp2Dx(80) + range[i] * animationValue,
                        i * 45F + (degree + degreeOffset));
                y = polarY(dp2Dx(80) + range[i] * animationValue,
                        i * 45F + (degree + degreeOffset));
            } else {
                x = polarX(dp2Dx(80) + range[i] * animationValue,
                        i * 45F - (degree + degreeOffset));
                y = polarY(dp2Dx(80) + range[i] * animationValue,
                        i * 45F - (degree + degreeOffset));
            }

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
    }

    //生成动画
    private ValueAnimator geneValueAnimator(long duration, AnimatorListenerAdapter animatorListenerAdapter
            , ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {

        ValueAnimator animator = ValueAnimator.ofFloat(0.0F, 0.8F, 0.5F, 1F, 0.0F);
        animator.setDuration(duration);
        animator.setRepeatCount(Integer.MAX_VALUE - 1);
        animator.addListener(animatorListenerAdapter);
        animator.addUpdateListener(animatorUpdateListener);

        return animator;
    }


    int dp2Dx(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    void l(Object o) {
        Log.e("######", o.toString());
    }


    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //  当前线程是主UI线程，直接刷新。
            invalidate();
        } else {
            //  当前线程是非UI线程，post刷新。
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimAndRemoveCallbacks();
    }

    private void stopAnimAndRemoveCallbacks() {
        Handler handler = this.getHandler();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
