package com.example.Accounting_Application;
// 导入相关的包

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

// 定义一个自定义控件类，继承自View
public final class CircleButtonView extends View {
    // 定义一些常量和变量
    public static final int SECTOR_NUM = 4; // 扇形选区的数量
    public static final int SECTOR_ANGLE = 110 / SECTOR_NUM; // 扇形选区的角度
    public  static final int DEVIATION_ANGLE = 190;//自xy坐标轴顺时针偏差DEVIATION ANGLE°的角
    public static final int[] SECTOR_COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}; // 扇形选区的颜色
    public static final int[] SECTOR_ICONS = new int[]{ R.mipmap.ic_people,R.mipmap.ic_travel,R.mipmap.ic_foods,R.mipmap.ic_clothing}; // 扇形选区的图标

    private static final String[] ITEM_TYPE = new String[]{"饮食","衣物","家常","文旅"};

    private Paint paint; // 画笔
    private RectF rectF; // 扇形选区的外接矩形
    private float radius; // 扇形选区的半径
    private float centerX; // 扇形选区的中心点X坐标
    private float centerY; // 扇形选区的中心点Y坐标
    private boolean isPressed; // 按钮是否被按下
    private int selectedSector; // 被选中的扇形选区的索引

    private boolean isReady;//是否准备就绪
    private FloatingActionButton fab;
    private static final String TAG = "CircleButtonView";

    public static final int dt = 200;//时间间隔
    private double value=10;//通过按下的持续时间决定的商品价值
    private float width= 100f;//加减选区的宽度

    Timer timer = new Timer();//计时器
    private boolean taskIsRun=false;
    private boolean task1IsRun;
    private boolean task2IsRun;

    private TimerTask task1;

    private TimerTask task2;

    private int n1 = 0;
    private int n2 = 0;




    // 构造方法
    public CircleButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        // 初始化扇形选区的外接矩形
        rectF = new RectF();
        // 初始化按钮状态
        isPressed = false;
        selectedSector = -1;
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    // 重写onDraw方法，绘制扇形选区
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 如果按钮被按下，绘制扇形选区
        if (isPressed) {
            {
                paint.setColor(Color.LTGRAY);
                RectF bigrestf = new RectF(centerX - radius-width, centerY - radius-width, centerX + radius+width, centerY + radius+width);
                canvas.drawArc(bigrestf,-190,54,true,paint);
                float centerAngle = -190 + 55 / 2;
                // 计算扇形选区的中心点坐标
                float iconX = (float) (centerX + radius * Math.cos(Math.toRadians(centerAngle)) +(width * Math.cos(Math.toRadians(centerAngle)) / 2));
                float iconY = (float) (centerY + radius * Math.sin(Math.toRadians(centerAngle)) +(width * Math.sin(Math.toRadians(centerAngle)) / 2));
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.arrow_down_float);
                canvas.drawBitmap(bitmap, iconX - bitmap.getWidth() / 2, iconY - bitmap.getHeight() / 2, paint);
            }
            {
                paint.setColor(Color.DKGRAY);
                RectF bigrestf = new RectF(centerX - radius-width, centerY - radius-width, centerX + radius+width, centerY + radius+width);
                canvas.drawArc(bigrestf,54-190,54,true,paint);
                float centerAngle = 55-190 + 55 / 2;
                // 计算扇形选区的中心点坐标
                float iconX = (float) (centerX + radius * Math.cos(Math.toRadians(centerAngle)) +(width * Math.cos(Math.toRadians(centerAngle)) / 2));
                float iconY = (float) (centerY + radius * Math.sin(Math.toRadians(centerAngle)) +(width * Math.sin(Math.toRadians(centerAngle)) / 2));
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.arrow_up_float);
                canvas.drawBitmap(bitmap, iconX - bitmap.getWidth() / 2, iconY - bitmap.getHeight() / 2, paint);
            }

            // 遍历每个扇形选区
            for (int i = 0; i < SECTOR_NUM; i++) {
                // 设置画笔颜色
                paint.setColor(SECTOR_COLORS[i]);
                // 计算扇形选区的起始角度和扫过角度
                float startAngle = i * SECTOR_ANGLE - DEVIATION_ANGLE;
                float sweepAngle = SECTOR_ANGLE;
                // 绘制扇形选区
                canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
                // 设置画笔颜色为白色
                paint.setColor(Color.WHITE);
                // 计算扇形选区的中心角度
                float centerAngle = startAngle + sweepAngle / 2;
                // 计算扇形选区的中心点坐标
                float iconX = (float) (centerX + radius * Math.cos(Math.toRadians(centerAngle)) / 2);
                float iconY = (float) (centerY + radius * Math.sin(Math.toRadians(centerAngle)) / 2);
                // 获取扇形选区的图标
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), SECTOR_ICONS[i]);
                // 绘制扇形选区的图标
                canvas.drawBitmap(bitmap, iconX - bitmap.getWidth() / 2, iconY - bitmap.getHeight() / 2, paint);

            }
        }
    }


    // 重写onTouchEvent方法，监听触摸事件

    public boolean myTouchEvent(MotionEvent event) {

        // 获取触摸点的坐标
        float x = event.getX()-132;
        float y = event.getY()-71;
        // 判断触摸事件的类型
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下事件
                Log.w(TAG, "Down ");
                // 设置按钮状态为按下
                isPressed = true;
                // 重绘视图
                invalidate();
                // 返回true，表示消费了该事件
                return true;
            case MotionEvent.ACTION_MOVE: // 移动事件
                // 如果按钮状态为按下
                if (isPressed) {

                        // 计算触摸点和中心点的连线与X轴的夹角
                        float angle = (float) Math.toDegrees(Math.atan2(y , x ));

                        // 将角度转换为正值
                        if (angle < 0) {
                            angle += 360;
                        }
                        double distance = Math.sqrt((x*x)+(y*y));
                        if (taskIsRun){
                            initTask();
                            if (task1IsRun&&angle<210){
                                task1.cancel();
                                timer.schedule(task2,0, dt);
                                Log.w(TAG, "task1 cancel,task2 run" );
                                task1IsRun=false;
                                task2IsRun=true;
                            }
                            if (task2IsRun&&angle>210){
                                task2.cancel();
                                timer.schedule(task1,0, dt);
                                Log.w(TAG, "task2 cancel,task1 run" );
                                task2IsRun=false;
                                task1IsRun=true;
                            }

                        }
                        if (!taskIsRun){
                            if (distance>=radius&&distance<=radius+width){
                                initTask();
                                if (angle>210){
                                    TimerTask t1=task1;
                                    timer.schedule(t1,0, dt);
                                    Log.w(TAG, "task1 run" );
                                    task1IsRun=true;
                                    taskIsRun=true;
                                }
                                if (angle<210){
                                    TimerTask t2=task2;
                                    timer.schedule(t2,0, dt);
                                    Log.w(TAG, "task2 run" );
                                    task2IsRun=true;
                                    taskIsRun=true;
                                }
                            }
                        }

                        // 计算触摸点所在的扇形选区的索引
                        int index = (int) ((angle + (SECTOR_ANGLE/SECTOR_NUM)) / SECTOR_ANGLE) % SECTOR_NUM;
                        // 如果索引和之前的不同，表示触摸点移动到了另一个扇形选区
                        if (index != selectedSector&&distance<radius) {
                            // 更新被选中的扇形选区的索引
                            selectedSector = index;
                            // 执行相应的操作，例如改变按钮的效果
                            value=10;
                            // 这里只是打印一条日志，具体的操作可以根据需求自定义
                            Log.d(TAG, "Selected sector: " + selectedSector);
                            Log.d(TAG, "angle: "+angle);
                            taskIsRun=false;
                            task1IsRun=false;
                            task2IsRun=false;

                        }

                }
                // 返回true，表示消费了该事件
                return true;
            case MotionEvent.ACTION_UP: // 抬起事件
                // 设置按钮状态为未按下
                isPressed = false;
                // 重置被选中的扇形选区的索引
                isReady = !isReady;

                taskIsRun=false;
                task1IsRun=false;
                task2IsRun=false;
                Log.w(TAG, "all cancel");

                // 重绘视图
                invalidate();
                // 返回true，表示消费了该事件
                return true;
            default: // 其他事件
                // 返回父类的处理方法
                return super.onTouchEvent(event);
        }
    }

    // 重写onSizeChanged方法，获取视图的宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 计算扇形选区的半径，取宽高中的较小值的一半
        radius = Math.min(w, h) / 2f;
        // 计算扇形选区的中心点坐标
        centerX = w -100;
        centerY = h -100;
        // 设置扇形选区的外接矩形的左上右下坐标
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isReady() {
        return isReady;
    }

    public String getItemType() {
        Log.d(TAG, "getItemType: "+selectedSector+","+ITEM_TYPE[selectedSector]);
        return ITEM_TYPE[selectedSector];
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    private void initTask(){

        task1= new TimerTask() {
            @Override
            public void run() {
                if (n1 < 8) {
                    value+=0.5;
                } else if (n1<16) {
                    value+=1;
                } else {
                    value+=5;
                }
                n1++;
                if (!task1IsRun){
                    cancel();
                    n1=0;
                }
            }
        };
        task2 = new TimerTask() {
            @Override
            public void run() {
                if (n2 < 8) {
                    value-=0.5;
                } else if (n2<16) {
                    value-=1;
                } else  {
                    value-=5;
                }
                n2++;
                if (!task2IsRun){
                    cancel();
                    n2=0;
                }
            }
        };
    }
}
