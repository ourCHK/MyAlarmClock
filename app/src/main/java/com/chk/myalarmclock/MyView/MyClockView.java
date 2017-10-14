package com.chk.myalarmclock.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chk on 17-10-13.
 * 自己定制的时钟View
 */

public class MyClockView extends View{

    public static final int UPDATE_HANDS = 1;
    public static int DEVICE_WIDTH;
    public static int DEVICE_HEIGHT;
    int viewWidth;
    int viewHeight;
    /**
     * viewWidth和viewHeight二者之间小者的那一个
     */
    int minViewWidthHeight;

    /**
     * viewWidth和viewHeight二者之间大者的那一个
     */
    int maxViewWidthHeight;

    int clockSize;

    int alarmHour;
    int alarmMinute;

    int clockHour;
    int clockMinute;
    int clockSecond;
    Timer timer = new Timer();
    TimerTask updateHandsTask;
    private Paint paintNums;
    private Paint paintHour;
    private Paint paintMinute;
    private Paint paintSecond;
    private Handler handler;
    public int[] NUMS = {12,1,2,3,4,5,6,7,8,9,10,11};

    public MyClockView(Context context) {
        super(context);
    }

    public MyClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHeight = getHeight();
        minViewWidthHeight = Math.min(viewWidth,viewHeight);
        maxViewWidthHeight = Math.max(viewWidth,viewHeight);
        init();
        Log.i("MyClockView+View","ViewHeight:"+viewHeight + " ViewWidth:"+viewWidth+" minViewWidthHeight"+minViewWidthHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawNum(canvas);
        drawHands(canvas);
    }

    public void init() {
        DisplayMetrics dm = getResources().getDisplayMetrics(); //获取屏幕尺寸大小
        DEVICE_WIDTH = dm.widthPixels;
        DEVICE_HEIGHT = dm.heightPixels;

        paintNums = new Paint();
        paintNums.setAntiAlias(true);
        paintNums.setColor(Color.BLACK);
        paintNums.setTextAlign(Paint.Align.CENTER);
        paintNums.setTextSize(minViewWidthHeight/10);

        paintHour = new Paint();
        paintHour.setColor(Color.argb(255,255,0,0));
        paintHour.setAntiAlias(true);
        paintHour.setStrokeWidth(minViewWidthHeight/100);

        paintMinute = new Paint();
        paintMinute.setColor(Color.argb(155,0,255,0));
        paintMinute.setAntiAlias(true);
        paintMinute.setStrokeWidth(minViewWidthHeight/100);

        paintSecond = new Paint();
        paintSecond.setColor(Color.argb(88,0,0,255));
        paintSecond.setAntiAlias(true);
        paintSecond.setStrokeWidth(minViewWidthHeight/100);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_HANDS:
                        invalidate();
                        break;
                    default:
                        break;
                }
            }
        };
        updateHandsTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(UPDATE_HANDS);
            }
        };

        timer.schedule(updateHandsTask,0,500);
    }


    /**
     * 测量view的宽度
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {  //父亲制定大小，对应match_parent
            result = specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(200,specSize);
            }
        }
        return result;
    }

    /**
     * 测量view的高度
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {  //父亲制定大小，对应match_parent
            result = specSize;
        } else {
            //这样，当时用wrap_content时，View就获得一个默认值200px，而不是填充整个父布局
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) {  //如果说父亲给的更小的话，那么就用更小的，默认是200
                result = Math.min(200,specSize);
            }
        }
        return result;
    }


    /**
     * 绘制闹钟提醒点
     * @param canvas
     */
    public void drawAlarmPoint(Canvas canvas) {

    }

    /**
     * 绘制时针，分针和秒针
     * @param canvas
     */
    public void drawHands(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        canvas.save();
        clockHour = calendar.get(Calendar.HOUR_OF_DAY);
        canvas.rotate(30 * clockHour,viewWidth/2,viewHeight/2);
        canvas.drawLine(viewWidth/2,viewHeight/2,viewWidth/2,viewHeight/2-minViewWidthHeight/2+minViewWidthHeight/3,paintHour);
        canvas.restore();

        canvas.save();
        clockMinute = calendar.get(Calendar.MINUTE);
        canvas.rotate(6 * clockMinute,viewWidth/2,viewHeight/2);
        canvas.drawLine(viewWidth/2,viewHeight/2,viewWidth/2,viewHeight/2-minViewWidthHeight/2+minViewWidthHeight/4,paintMinute);
        canvas.restore();

        canvas.save();
        clockSecond = calendar.get(Calendar.SECOND);
        canvas.rotate(6 * clockSecond,viewWidth/2,viewHeight/2);
        canvas.drawLine(viewWidth/2,viewHeight/2,viewWidth/2,viewHeight/2-minViewWidthHeight/2+minViewWidthHeight/5,paintSecond);
        canvas.restore();
    }

    /**
     * 绘制数字
     * @param canvas
     */
    public void drawNum(Canvas canvas) {
        for (int i=0; i<NUMS.length; i++) {
            double drawX = viewWidth/2 + minViewWidthHeight * 2/5 * Math.sin(Math.PI * i * 30 / 180);
            double drawY = viewHeight/2 - minViewWidthHeight* 2/5 * Math.cos(Math.PI * i * 30 / 180)+minViewWidthHeight/25;
            canvas.drawText(NUMS[i]+"",(float)drawX,(float)drawY,paintNums);
        }
    }
}
