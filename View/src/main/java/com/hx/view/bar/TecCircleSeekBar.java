package com.hx.view.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.hx_view.R;


/**
 * 科技主题圆形Seekbar之内外环
 * Created by Ray on 2017/8/24.
 * Website: http://www.geek-era.com
 * Email:nuoone@163.com
 */

public class TecCircleSeekBar extends View {
    private float progressWidth = 2; //progress width, default 2
    private float innerProgressWidth = 20; //内圆宽度
    private float innerPadding = 2; //外部进度条与内部进度条的边距
    private Drawable thumb;
    private boolean clockWise = true; //true顺时针，false逆时针

    private int rotation = 0; //旋转了多少的值，不是度数
    private int sumRotation = 360; //该控件所能表示的最大值

    private int sweepAngle = 360; //画一个多少度的控件，默认是个360度的圆
    private int progressSweepAngle = 0; //progress滑动角度，需要根据rotation和sumRotationo进行计算
    private int startAngle = 0; //起始角度
    private double currentAngle = 0; //在9点钟方向是270,12点钟方向是360.一圈从0-360的圆中当前的角度

    private int correctAngle = 0; //纠正角度，默认从9点钟方向开始的，顺时针调整correctAngle度

    private Paint outerPaint, innerBgPaint, innerPaint; //分别：外圆画笔，内圆背景画笔，内圆画笔
    private RectF outerRectF = new RectF(), innerRectF = new RectF();

    private Paint vTextPaint;  //画文本内容
    private Paint hintPaint; //画单位文本内容
    private Paint curSpeedPaint;//标题
    //文本内容大小颜色
    private float textSize = dipToPx(60);//文本大小
    private float hintSize = dipToPx(15);//单位值大小
    private float curSpeedSize = dipToPx(13);//提示值大小
    private int hintColor = 0xff676767; //文字颜色
    private int textColor = 0xff676767; //文字颜色
    private int curSpeedColor = 0xff676767; //文字颜色


    //文本内容
    private String curSpeedString = "title";
    private String hintString = "单位";
    private String textValues = "0";
    //位置参数
    private int translateX = 0, translateY = 0;
    private int thumbX = 0, thumbY = 0;
    private float circleRadius; //外圆半径
    private static int INVALID_ROTATION = -1;
    private float forbiddenRadius = 0; //禁止点击的范围


    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setCurSpeedColor(int curSpeedColor) {
        this.curSpeedColor = curSpeedColor;
    }

    public void setProgressText(String  curValues) {
        this.textValues = curValues + "";
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setHintSize(float hintSize) {
        this.hintSize = hintSize;
    }

    public void setCurSpeedSize(float curSpeedSize) {
        this.curSpeedSize = curSpeedSize;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
    }

    public void setCurSpeedString(String curSpeedString) {
        this.curSpeedString = curSpeedString;
    }

    public void setHintText(String hintString) {
        this.hintString = hintString;
    }

    private GeekSeekBarOnChangeListener changeListener;


    public TecCircleSeekBar(Context context) {
        super(context);
    }

    public TecCircleSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViewAttr(context, attrs, R.attr.tecCircleSeekBarStyle);
    }

    public TecCircleSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewAttr(context, attrs, defStyleAttr);
    }

    /**
     * 初始化参数
     *
     * @param context      Context
     * @param attrs        AttributeSet
     * @param defStyleAttr int
     */
    private void initViewAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        float density = context.getResources().getDisplayMetrics().density; //转为像素
        //默认的外圆宽度和颜色
        progressWidth = progressWidth * density;
        int progressColor = ContextCompat.getColor(context, R.color.colorTecCircleSeekBarProgress); //进度条的默认颜色
        //默认的内圆宽度和颜色
        innerProgressWidth = innerProgressWidth * density;
        int innerProgressBgColor = ContextCompat.getColor(context, R.color.colorTecCircleSeekBarProgressInnerBg); //进度条内圆的默认背景颜色
        int innerProgressColor = ContextCompat.getColor(context, R.color.colorTecCircleSeekBarProgressInner); //进度条内圆的默认颜色
        int defColor = ContextCompat.getColor(context, R.color.colorTecCircleSeekBarProgressInnerText); //文本的默认颜色
        //内边距，避免thumb遮挡内圆
        innerPadding = innerPadding * density;

        thumb = ContextCompat.getDrawable(context, R.drawable.teccircleseekbar_thumb); //默认的thumb资源

        //在开发者设置了自己的属性的时候，使用开发者定义的属性
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TecCircleSeekBarStyle, defStyleAttr, 0);
            //开发者设置的外圆宽度和颜色
            progressWidth = typedArray.getDimension(R.styleable.TecCircleSeekBarStyle_outerWidth, progressWidth);
            progressColor = typedArray.getColor(R.styleable.TecCircleSeekBarStyle_outerColor, progressColor);

            //开发者设置的内圆宽度和颜色
            innerProgressWidth = typedArray.getDimension(R.styleable.TecCircleSeekBarStyle_innerWidth, innerProgressWidth);
            innerProgressBgColor = typedArray.getColor(R.styleable.TecCircleSeekBarStyle_innerBgColor, innerProgressBgColor);
            innerProgressColor = typedArray.getColor(R.styleable.TecCircleSeekBarStyle_innerColor, innerProgressColor);

            //内边距
            innerPadding = typedArray.getDimension(R.styleable.TecCircleSeekBarStyle_innerPadding, innerPadding);
            //旋转的值
            rotation = typedArray.getInt(R.styleable.TecCircleSeekBarStyle_rotation, rotation);
            //总值
            sumRotation = typedArray.getInt(R.styleable.TecCircleSeekBarStyle_sumRotation, sumRotation);

            //顺时针还是逆时针
            clockWise = typedArray.getBoolean(R.styleable.TecCircleSeekBarStyle_clockWise, clockWise);

            //控件度数
            sweepAngle = typedArray.getInt(R.styleable.TecCircleSeekBarStyle_sweepAngle, sweepAngle);

            /**文本内容设置*/
            //大小
            textSize = typedArray.getDimension(R.styleable.TecCircleSeekBarStyle_tec_textSize, dipToPx(60));
            hintSize = typedArray.getDimension(R.styleable.TecCircleSeekBarStyle_tec_hintSize, dipToPx(15));
            curSpeedSize = typedArray.getDimension(R.styleable.TecCircleSeekBarStyle_tec_curSpeedSize, dipToPx(13));
            //颜色
            hintColor = typedArray.getColor(R.styleable.TecCircleSeekBarStyle_tec_hintColor, defColor);
            textColor = typedArray.getColor(R.styleable.TecCircleSeekBarStyle_tec_textColor, defColor);
            curSpeedColor = typedArray.getColor(R.styleable.TecCircleSeekBarStyle_tec_curSpeedColor, defColor);
            //内容
            curSpeedString = typedArray.getString(R.styleable.TecCircleSeekBarStyle_tec_curSpeedText);
            if (curSpeedString == null)
                curSpeedString = "";
            hintString = typedArray.getString(R.styleable.TecCircleSeekBarStyle_tec_hintText);
            if (hintString == null)
                hintString = "";
            textValues = typedArray.getString(R.styleable.TecCircleSeekBarStyle_tec_textValues);
            if (textValues == null)
                textValues = "0";
            /*********************************/

            //只能放在rotation、sumRotation、sweepAngle设置值之后
            progressSweepAngle = (int) Math.round((double) rotation / sumRotation * sweepAngle);
            //只有360度的圆圈可以有矫正度数
            if (sweepAngle == 360) {
                //纠正角度
                correctAngle = typedArray.getInt(R.styleable.TecCircleSeekBarStyle_correctAngle, correctAngle);
                //防止纠正角度超限
                if (correctAngle < 0) {
                    correctAngle = 0;
                } else if (correctAngle > sweepAngle) {
                    correctAngle = sweepAngle;
                }
            }

            //thumb
            Drawable customThumb = typedArray.getDrawable(R.styleable.TecCircleSeekBarStyle_tecThumb);
            if (customThumb != null) {
                thumb = customThumb;
            }
            int thumbHalfWidth = thumb.getIntrinsicWidth() / 2;
            int thumbHalfHeight = thumb.getIntrinsicHeight() / 2;
            thumb.setBounds(-thumbHalfWidth, -thumbHalfHeight, thumbHalfWidth, thumbHalfHeight);

            typedArray.recycle();

        }


        //绘制外圆
        PathEffect progressEffect = new PathEffect();
        outerPaint = new Paint();
        outerPaint.setColor(progressColor);
        outerPaint.setAntiAlias(true);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(progressWidth);
        outerPaint.setPathEffect(progressEffect);

        PathEffect innerBgEffect = new DashPathEffect(new float[]{8, 2, 8, 2}, 1);
        innerBgPaint = new Paint();
        innerBgPaint.setColor(innerProgressBgColor);
        innerBgPaint.setAntiAlias(true);
        innerBgPaint.setStyle(Paint.Style.STROKE);
        innerBgPaint.setStrokeWidth(innerProgressWidth);
        innerBgPaint.setPathEffect(innerBgEffect);

        PathEffect innerProgressEffect = new PathEffect();
        innerPaint = new Paint();
        innerPaint.setColor(innerProgressColor);
        innerPaint.setAntiAlias(true);
        innerPaint.setStyle(Paint.Style.STROKE);
        innerPaint.setStrokeWidth(innerProgressWidth);
        innerPaint.setPathEffect(innerProgressEffect);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(textColor);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
//        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setColor(hintColor);
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(curSpeedColor);
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

    }

    /**
     * 设置成多少值
     *
     * @param value 要设置显示的值
     */
    public void setRotation(int value) {
        this.rotation = value;
        update(rotation, false);
    }

    /**
     * 设置该控件所能便是的最大值
     *
     * @param sumRotation 表示的最大值
     */
    public void setSumRotation(int sumRotation) {
        this.sumRotation = sumRotation;
        update(rotation, false);
    }

    /**
     * 设置监听接口
     *
     * @param listener GeekSeekBarOnChangeListener
     */
    public void setChangeListener(GeekSeekBarOnChangeListener listener) {
        this.changeListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!clockWise) {
            // 开发者设置为逆时针
            canvas.scale(-1, 1, outerRectF.centerX(), outerRectF.centerY());
            canvas.scale(-1, 1, innerRectF.centerX(), innerRectF.centerY());
        }

        int circleStartAngle;
        if (clockWise) {
            circleStartAngle = (startAngle + correctAngle - 90) % 360;
            canvas.drawArc(outerRectF, circleStartAngle, sweepAngle, false, outerPaint);
            canvas.drawArc(innerRectF, circleStartAngle, sweepAngle, false, innerBgPaint);
            canvas.drawArc(innerRectF, circleStartAngle, progressSweepAngle, false, innerPaint);
        } else {
            circleStartAngle = startAngle - correctAngle - 90 + sweepAngle;
            canvas.drawArc(outerRectF, circleStartAngle, -sweepAngle, false, outerPaint);
            canvas.drawArc(innerRectF, circleStartAngle, -sweepAngle, false, innerBgPaint);
            canvas.drawArc(innerRectF, circleStartAngle, -progressSweepAngle, false, innerPaint);
        }

//        canvas.drawText(curValues+"", translateX, translateY + textSize / 3, vTextPaint);
//        canvas.drawText(hintString, translateX, translateY + 2 * textSize / 3, hintPaint);
//        canvas.drawText(titleString, translateX, translateY - 2 * textSize / 3, curSpeedPaint);


        canvas.drawText(textValues, innerRectF.centerX(), innerRectF.centerY() + textSize / 3, vTextPaint);
        canvas.drawText(hintString, innerRectF.centerX(), innerRectF.centerY() + 2 * textSize / 3 + hintSize / 2, hintPaint);
        canvas.drawText(curSpeedString, innerRectF.centerX(), innerRectF.centerY() - 2 * textSize / 3, curSpeedPaint);

        canvas.translate(translateX - thumbX, translateY - thumbY); //移动画布原点
        thumb.draw(canvas);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(height, width); //选择设置的宽和高中最小的那一个，按照正方形绘制
        float top;
        float left;
        float arcDiameter; //外圆直径


        translateX = (int) (width * 0.5f);
        translateY = (int) (height * 0.5f);
        if (thumb != null) {
            arcDiameter = min - progressWidth - thumb.getIntrinsicWidth(); //、、这里做了修改
        } else {
            arcDiameter = min - progressWidth;
        }
        circleRadius = arcDiameter / 2; //外圆半径
        //将圆画在控件宽高的长方形内正中心，计算出top和left值
        top = (height - arcDiameter) / 2; //顶部到这里
        left = (width - arcDiameter) / 2; //左边到这里

        //内圆参数
        float innerDiameter = arcDiameter - 2 * innerPadding - 2 * innerProgressWidth; //内部空白直径
        float innerLeft;
        float innerTop;
        innerTop = (height - innerDiameter) / 2;
        innerLeft = (width - innerDiameter) / 2;

        outerRectF.set(left, top, left + arcDiameter, top + arcDiameter);
        innerRectF.set(innerLeft, innerTop, innerLeft + innerDiameter, innerTop + innerDiameter);

        forbiddenRadius = innerDiameter / 2 - 10; //该半径范围内禁止点击


        startAngle = (360 - sweepAngle / 2) % 360; //起始角度

        currentAngle = startAngle + progressSweepAngle;
        updateThumbPosition();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onStartTrackingTouch();
                updateOnTouch(event);
                break;
            case MotionEvent.ACTION_MOVE:
                updateOnTouch(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onStopTrackingTouch();
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    /**
     * 开始监听触摸事件
     */
    private void onStartTrackingTouch() {
        if (changeListener != null) {
            changeListener.onStartTrackingTouch(this);
        }
    }

    /**
     * 停止监听触摸事件
     */
    private void onStopTrackingTouch() {
        if (changeListener != null) {
            changeListener.onStopTrackingTouch(this);
        }
        setPressed(false);
    }

    /**
     * 更新控件上各组件的位置
     *
     * @param event 触摸事件
     */
    private void updateOnTouch(MotionEvent event) {
        //该区域禁止点击，为了优化用户体验，内部空白处不允许点击
        boolean forbidden = forbiddenTouch(event.getX(), event.getY());
        if (forbidden) {
            return;
        }
        setPressed(true);
        int currentSweepAngle = (int) releativeMoveAngle(event.getX(), event.getY());
        //防止进度条和进度条背景越界
        if (currentAngle >= 360 - sweepAngle / 2 || currentAngle <= sweepAngle / 2) {
            progressSweepAngle = currentSweepAngle; //触摸过程中走了多少度
            rotation = angle2Rotation(progressSweepAngle);
            update(rotation, true);
        }
    }

    /**
     * 设置变化值
     *
     * @param rotation
     * @param fromUser
     */
    private void update(int rotation, boolean fromUser) {
        if (rotation > sumRotation) {
            rotation = sumRotation;
        }
        if (!fromUser) {
            progressSweepAngle = (int) Math.round((double) rotation / sumRotation * sweepAngle);
        }
        onProgressRefresh(rotation, fromUser);

    }

    /**
     * 设定的范围内不允许触摸
     */
    private boolean forbiddenTouch(float eventX, float eventY) {
        boolean forbidden = false;
        float x = eventX - translateX;
        float y = eventY - translateY;

        float touchRadius = (float) Math.sqrt(((x * x) + (y * y)));
        if (touchRadius < forbiddenRadius) {
            forbidden = true;
        }
        return forbidden;
    }

    /**
     * 相对设置的初始点移动的角度
     *
     * @param eventX 点击事件的X值
     * @param eventY 点击事件的X值
     * @return 移动的距离 double
     */
    private double releativeMoveAngle(float eventX, float eventY) {
        float x = eventX - translateX;
        float y = eventY - translateY;
        x = (clockWise) ? x : -x;
        double angle = Math.toDegrees(Math.atan2(y, x) + (Math.PI / 2));
        if (angle < 0) {
            //修正度数，让9点钟方向是270,12点钟方向是360.一圈从0-360
            angle = 360 + angle;
        }
        currentAngle = angle - correctAngle;
        if (currentAngle > startAngle) {
            angle = currentAngle - startAngle;
        } else {
            angle = currentAngle + 360 - startAngle;
        }
        return angle;
    }

    /**
     * 将旋转角度转为旋转的值
     *
     * @param angle 旋转角度
     * @return 角度对应的值
     */
    private int angle2Rotation(double angle) {
        double rotation = (double) sumRotation / sweepAngle * angle;
        if (rotation < 0 || rotation > sumRotation) {
            rotation = INVALID_ROTATION;//不可能发生了，前面已经做了限制
        }
        return (int) Math.round(rotation);
    }

    private void onProgressRefresh(int rotation, boolean fromUser) {
        if (rotation == INVALID_ROTATION) {
            //无效的值
            return;
        }
        //设置文本值
        setProgressText(rotation+"");
        if (changeListener != null) {
            changeListener.onProgressChanged(this, rotation, fromUser);
        }

        updateThumbPosition();
        invalidate();
    }

    /**
     * 更新thunb的位置
     */
    private void updateThumbPosition() {
        double thumbAngle;
        if (clockWise) {
            thumbAngle = (currentAngle + 90 + correctAngle) % 360;
        } else {
            thumbAngle = (360 - (currentAngle - 90) - correctAngle) % 360;
        }
        thumbX = (int) (circleRadius * Math.cos(Math.toRadians(thumbAngle)));
        thumbY = (int) (circleRadius * Math.sin(Math.toRadians(thumbAngle)));
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (thumb != null && thumb.isStateful()) {
            int[] state = getDrawableState();
            thumb.setState(state);
        }
        invalidate();
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }


}
