package com.openweather.sunviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andy6804tw on 2017/3/5.
 */

public class SunView extends View {

    private static final String DATE_FORMAT = "HH:mm";

    private Paint mPaint;
    private String mStartTime;
    private String mEndTime;
    private String mCurrentTime;

    private float mTimeTextSize;
    private float mArcDashWidth;
    private float mArcDashGapWidth;
    private float mArcDashHeight;
    private float mArcRadius;
    private float mDefaultWeatherIconSize;
    private float mTextPadding;


    private int mArcColor;
    private int mArcSolidColor;
    private int mBottomLineColor;
    private int mTimeTextColor;
    private int mSunColor;

    private float mBottomLineHeight;
    private float mArcOffsetAngle;

    private Drawable mWeatherDrawable;

    private SimpleDateFormat mDateFormat;

    private float offsetX;
    private float offsetY;


    public SunView(Context context) {
        super(context);
        init();
    }

    public SunView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttrs(context, attrs);
    }

    public SunView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
    }
    public  void setArcSolidColor(int color){//拱型內部顏色
        //mArcSolidColor = Color.parseColor(color);
        mArcSolidColor=color;
    }
    public void setArcColor(int color){//拱形虛線顏色
        mArcColor= color;
    }
    public void setBottomLineColor(int color){//拱形底線顏色
        mBottomLineColor= color;
    }
    public void setTimeTextColor(int color){//字體顏色
        mTimeTextColor= color;
    }
    public void setSunColor(int color){//太陽顏色
        mSunColor= color;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.WeatherViewStyle);
        mStartTime = attrArray.getString(R.styleable.WeatherViewStyle_startTime);
        mEndTime = attrArray.getString(R.styleable.WeatherViewStyle_endTime);
        mCurrentTime = attrArray.getString(R.styleable.WeatherViewStyle_currentTime);
        mTimeTextSize = attrArray.getDimension(R.styleable.WeatherViewStyle_timeTextSize, getResources().getDimension(R.dimen.default_text_size));
        mTimeTextColor = attrArray.getColor(R.styleable.WeatherViewStyle_timeTextColor, getResources().getColor(R.color.default_text_color));
        mWeatherDrawable = attrArray.getDrawable(R.styleable.WeatherViewStyle_weatherDrawable);
        mBottomLineHeight = attrArray.getDimension(R.styleable.WeatherViewStyle_bottomLineHeight, getResources().getDimension(R.dimen.default_bottom_line_height));
        mBottomLineColor = attrArray.getColor(R.styleable.WeatherViewStyle_bottomLineColor, getResources().getColor(R.color.default_bottom_line_color));
        mArcColor = attrArray.getColor(R.styleable.WeatherViewStyle_arcColor, getResources().getColor(R.color.default_arc_color));
        mArcSolidColor = attrArray.getColor(R.styleable.WeatherViewStyle_arcSolidColor, getResources().getColor(R.color.default_arc_solid_color));
        mArcDashWidth = attrArray.getDimension(R.styleable.WeatherViewStyle_arcDashWidth, getResources().getDimension(R.dimen.default_arc_dash_width));
        mArcDashGapWidth = attrArray.getDimension(R.styleable.WeatherViewStyle_arcDashGapWidth, getResources().getDimension(R.dimen.default_arc_dash_gap_width));
        mArcDashHeight = attrArray.getDimension(R.styleable.WeatherViewStyle_arcDashHeight, getResources().getDimension(R.dimen.default_arc_dash_height));
        mArcRadius = attrArray.getDimension(R.styleable.WeatherViewStyle_arcRadius, 0);
        mArcOffsetAngle = attrArray.getInteger(R.styleable.WeatherViewStyle_arcOffsetAngle, 0);
        mSunColor = attrArray.getColor(R.styleable.WeatherViewStyle_sunColor, getResources().getColor(R.color.default_sun_color));
        mTextPadding = attrArray.getDimension(R.styleable.WeatherViewStyle_textPadding, 0);
        attrArray.recycle();

        mDefaultWeatherIconSize = getResources().getDimension(R.dimen.default_weather_icon_size);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setDefaultTime();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);


        mTimeTextSize = getResources().getDimension(R.dimen.default_text_size);
        mTimeTextColor = getResources().getColor(R.color.default_text_color);

        mBottomLineHeight = getResources().getDimension(R.dimen.default_bottom_line_height);
        mBottomLineColor = getResources().getColor(R.color.default_bottom_line_color);
        mArcColor = getResources().getColor(R.color.default_arc_color);
        mArcSolidColor = getResources().getColor(R.color.default_arc_solid_color);
        mArcDashWidth = getResources().getDimension(R.dimen.default_arc_dash_width);
        mArcDashGapWidth = getResources().getDimension(R.dimen.default_arc_dash_gap_width);
        mArcDashHeight = getResources().getDimension(R.dimen.default_arc_dash_height);
        mDefaultWeatherIconSize = getResources().getDimension(R.dimen.default_weather_icon_size);
        mSunColor = getResources().getColor(R.color.default_sun_color);
        setDefaultTime();
    }
    public void setStartTime(String s){
        mStartTime =s;
    }
    public void setEndTime(String s){
        mEndTime =s;
    }
    public void setCurrentTime(String s){
        String str_Start[]=mStartTime.split(":");
        String str_Current[]=s.split(":");
        int current=Integer.parseInt(str_Current[0])*60+Integer.parseInt(str_Current[1]);
        int Start=Integer.parseInt(str_Start[0])*60+Integer.parseInt(str_Start[1]);
        if(current<Start){
            mArcSolidColor =getResources().getColor(R.color.Transparent);
            s=mStartTime;
        }
        mCurrentTime =s;
    }

    private void setDefaultTime() {

        mDateFormat = new SimpleDateFormat(DATE_FORMAT);

        if (TextUtils.isEmpty(mStartTime)) {
            mStartTime = getResources().getString(R.string.default_start_time);
        }

        if (TextUtils.isEmpty(mEndTime)) {
            mEndTime = getResources().getString(R.string.default_end_time);
        }

        if (TextUtils.isEmpty(mCurrentTime)) {

            mCurrentTime = mDateFormat.format(new Date());
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);


        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            if (mArcRadius == 0) {
                setMeasuredDimension(width, height);
            } else {
                width = (int) (mArcRadius * 2 + getWidthGap());
                height = (int) (mArcRadius + getHeightGap());
                setMeasuredDimension(width, height);
            }

        } else if (widthMode == MeasureSpec.AT_MOST) {
            if (mArcRadius == 0) {
                width = (height - getHeightGap()) * 2 + getWidthGap();
            } else {
                width = (int) (mArcRadius * 2 + getWidthGap());
            }
            setMeasuredDimension(width, height);

        } else if (heightMode == MeasureSpec.AT_MOST) {
            if (mArcRadius == 0) {
                height = (width - getWidthGap())/2 + getHeightGap();
            } else {
                height = (int) (mArcRadius + getHeightGap());
            }

            setMeasuredDimension(width, height);

        } else {
            setMeasuredDimension(width, height);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getBackground() != null){
            getBackground().draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }

        drawLine(canvas);
        drawArc(canvas);

    }

    /**
     * 畫圓弧
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        getRadius();


        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mArcColor);
        mPaint.setStrokeWidth(mArcDashHeight);




        float left = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight() - 2 * mArcRadius) / 2;
        float top = getHeight() - mArcRadius -getBottomHeightGap();
        float right = left + 2 * mArcRadius;
        float bottom = top + 2 * mArcRadius;
        RectF rectF = new RectF(left, top, right, bottom);

        PointF offsetPoint = calcArcEndPointXY(rectF.centerX(),rectF.centerY(),rectF.width()/2,180+mArcOffsetAngle);
        offsetX = offsetPoint.x - left;
        offsetY = rectF.centerY() - offsetPoint.y;

        rectF.top += offsetY;
        rectF.bottom += offsetY;

        DashPathEffect effect = new DashPathEffect(new float[]{mArcDashWidth, mArcDashGapWidth, mArcDashWidth, mArcDashGapWidth}, 0);
        mPaint.setPathEffect(effect);
        canvas.drawArc(rectF, 180 + mArcOffsetAngle, 180 - mArcOffsetAngle*2, false, mPaint);


        drawSolidArc(canvas, rectF);


    }

    private void getRadius() {
        if (mArcRadius == 0) {

            int width = getWidth() -getWidthGap();
            int height = getHeight() - getHeightGap();

            if(width / 2 > height){
                mArcRadius = height;
            }else{
                mArcRadius = width / 2 ;
            }
        }
    }

    /**
     * 繪製天氣圖標
     *
     * @param canvas
     * @param point
     */
    private void drawWeatherDrawable(Canvas canvas, PointF point) {

        if (mWeatherDrawable != null) {
            int dw = mWeatherDrawable.getIntrinsicWidth() == 0 ? (int) mDefaultWeatherIconSize : mWeatherDrawable.getIntrinsicWidth();
            int dh = mWeatherDrawable.getIntrinsicHeight() == 0 ? (int) mDefaultWeatherIconSize : mWeatherDrawable.getIntrinsicHeight();

            Rect rect = new Rect();
            rect.left = (int) (point.x - dw / 2);
            rect.top = (int) (point.y - dh / 2);
            rect.right = rect.left + dw;
            rect.bottom = rect.top + dh;

            mWeatherDrawable.setBounds(rect);
            mWeatherDrawable.draw(canvas);
        } else {
            drawSun(canvas, point);
        }

    }

    /**
     * 畫太陽
     *
     * @param canvas
     * @param point
     */
    private void drawSun(Canvas canvas, PointF point) {
        mPaint.setColor(mSunColor);

        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(point.x, point.y, mDefaultWeatherIconSize / 2, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDefaultWeatherIconSize / 3);


        float c = (float) (2 * mDefaultWeatherIconSize * Math.PI);

        int w = 3;
        float gapW = (c - 12 * 5) / 12;

        DashPathEffect effect = new DashPathEffect(new float[]{w, gapW, w, gapW}, 0);
        mPaint.setPathEffect(effect);

        canvas.drawCircle(point.x, point.y, mDefaultWeatherIconSize, mPaint);
    }

    /**
     * 畫實心圓弧
     *
     * @param canvas
     * @param rectF
     */
    private void drawSolidArc(Canvas canvas, RectF rectF) {

        int angle = 0;
        try {
            long start = mDateFormat.parse(mStartTime).getTime();
            long end = mDateFormat.parse(mEndTime).getTime();
            long current = mDateFormat.parse(mCurrentTime).getTime();
            float factor = 1.0f * (current - start) / (end - start);
            if(factor > 1){
                angle = (int) (180-mArcOffsetAngle*2);
            }else{
                angle = (int) (factor * (180-mArcOffsetAngle*2));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mArcSolidColor);
        canvas.drawArc(rectF, 180+mArcOffsetAngle, angle, false, mPaint);

        PointF point = calcArcEndPointXY(rectF.centerX(), rectF.centerY(), rectF.width()/2, 180 + mArcOffsetAngle + angle);

        drawTriangle(canvas, rectF, point);
        drawWeatherDrawable(canvas, point);

        drawText(canvas, rectF);
    }

    private void drawText(Canvas canvas, RectF rect) {

        mPaint.setColor(mTimeTextColor);
        mPaint.setTextSize(mTimeTextSize);

        int startTextWidth = getTextWidth(mPaint, mStartTime);
        int endTextWidth = getTextWidth(mPaint, mEndTime);

        int textHeight = getTextHeight()+15;

        mPaint.setPathEffect(null);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawText(mStartTime, rect.left - startTextWidth / 2 + offsetX, rect.centerY() - offsetY + textHeight + mTextPadding, mPaint);
        canvas.drawText(mEndTime, rect.right - endTextWidth / 2 - 2 - offsetX*2, rect.centerY() -offsetY + textHeight + mTextPadding, mPaint);
    }

    private int getTextHeight() {
        mPaint.setTextSize(mTimeTextSize);
        Paint.FontMetrics fm = mPaint.getFontMetrics();// 得到系統默認字體屬性
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 畫三角形
     *
     * @param canvas
     * @param rect
     * @param point
     */
    private void drawTriangle(Canvas canvas, RectF rect, PointF point) {

        //因為計算損失精度，所以這裡用1像素來微調
        Path path = new Path();
        path.moveTo(rect.left - 1 + offsetX, rect.centerY() - offsetY);// 此點為多邊形的起點
        path.lineTo(point.x - 1, point.y - 1);
        path.lineTo(point.x - 1, rect.centerY() - offsetY);
        path.close(); //使這些點構成封閉的多邊形
        canvas.drawPath(path, mPaint);
    }

    /**
     * 畫底部線條
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {

        mPaint.setColor(mBottomLineColor);
        mPaint.setStrokeWidth(mBottomLineHeight);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawLine(getPaddingLeft(), getHeight() - getBottomHeightGap(), getWidth() - getPaddingRight(), getHeight() -getBottomHeightGap(), mPaint);

    }


    /**
     * 寬度除了圓弧以外的空隙
     * @return
     */
    private int getWidthGap(){
        return getPaddingLeft() + getPaddingRight() + getTextWidth(mStartTime)/2+getTextWidth(mEndTime)/2;
    }

    /**
     * 高度除了圓弧以外的空隙
     * @return
     */
    private int getHeightGap(){
        return (int) (getPaddingTop() + getPaddingBottom() + mTextPadding + mBottomLineHeight + getWeatherHeight()/2) + getTextHeight();
    }

    /**
     * 圓弧底部空隙
     * @return
     */
    private int getBottomHeightGap(){
        return (int) (getPaddingBottom() + getTextHeight() + mTextPadding)+10;
    }

    /**
     * 天氣圖標高度
     * @return
     */
    private int getWeatherHeight(){
        if(mWeatherDrawable == null){
            return (int) mDefaultWeatherIconSize*2;
        }
        if(mWeatherDrawable.getIntrinsicHeight() == 0){
            return (int) mDefaultWeatherIconSize*2;
        }
        return mWeatherDrawable.getIntrinsicHeight();
    }

    /**
     * 天氣圖標寬度
     * @return
     */
    private int getWeatherWidth(){
        if(mWeatherDrawable == null){
            return (int) mDefaultWeatherIconSize*2;
        }
        if(mWeatherDrawable.getIntrinsicWidth() == 0){
            return (int) mDefaultWeatherIconSize*2;
        }
        return mWeatherDrawable.getIntrinsicWidth();
    }




    //依圓心坐標，半徑，扇形角度，計算出扇形終射線與圓弧交叉點的xy坐標
    public PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle) {

        PointF point = new PointF();

        //將角度轉換為弧度
        float arcAngle = (float) (Math.PI * cirAngle / 180.0);

        //當角度= 90°時，弧度=Π/ 2 =Π* 90°/ 180°=Π*角度/ 180°，
        //當角度= 180°時，弧度=Π=Π* 180°/ 180°=Π*角度/ 180°，
        //所以弧度（弧度）=Π* angle / 180（1弧度等於半徑的圓弧對應的圓心角，1度是1/360圓心角）

        if (cirAngle < 90)     //直角的三角形斜邊是半徑
        {
            point.x = cirX + (float) (Math.cos(arcAngle)) * radius;
            point.y = cirY + (float) (Math.sin(arcAngle)) * radius;
        } else if (cirAngle == 90) {
            point.x = cirX;
            point.y = cirY + radius;
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = (float) (Math.PI * (180 - cirAngle) / 180.0);
            point.x = cirX - (float) (Math.cos(arcAngle)) * radius;
            point.y = cirY + (float) (Math.sin(arcAngle)) * radius;
        } else if (cirAngle == 180) {
            point.x = cirX - radius;
            point.y = cirY;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = (float) (Math.PI * (cirAngle - 180) / 180.0);
            point.x = cirX - (float) (Math.cos(arcAngle)) * radius;
            point.y = cirY - (float) (Math.sin(arcAngle)) * radius;
        } else if (cirAngle == 270) {
            point.x = cirX;
            point.y = cirY - radius;
        } else {
            arcAngle = (float) (Math.PI * (360 - cirAngle) / 180.0);
            point.x = cirX + (float) (Math.cos(arcAngle)) * radius;
            point.y = cirY - (float) (Math.sin(arcAngle)) * radius;
        }

        return point;
    }


    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public int getTextWidth(String str) {
        mPaint.setTextSize(mTimeTextSize);
        return getTextWidth(mPaint, str);
    }

}