package com.example.Accounting_Application;

// 导入相关的包

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

// 定义一个自定义控件类，继承自View
public class PieChartView extends View {

    // 定义一些常量和变量
    private Paint paint; // 画笔
    private RectF rectF; // 扇形选区的外接矩形
    private float radius; // 扇形选区的半径
    private float centerX; // 扇形选区的中心点X坐标
    private float centerY; // 扇形选区的中心点Y坐标
    private double[] datas; // 数据集
    private String[] texts; // 文字集
    private int[] colors; // 颜色集
    private float total; // 数据的总和
    private float startAngle; // 扇形选区的起始角度
    private double sweepAngle; // 扇形选区的扫过角度
    private static final String TAG = "PieChartView";

    // 构造方法
    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        // 初始化扇形选区的外接矩形
        rectF = new RectF();
        // 初始化数据集，文字集，颜色集
        datas = new double[4];
        texts = new String[4];
        colors = new int[4];
    }

    // 重写onDraw方法，绘制扇形统计图
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 如果数据集为空，直接返回
        if (datas==null||datas.length==0) return;
        // 计算扇形选区的中心点坐标
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        // 计算扇形选区的半径，取宽高中的较小值的一半
        radius = Math.min(getWidth(), getHeight()) / 2f;
        // 设置扇形选区的外接矩形的左上右下坐标
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        // 遍历每个数据
        for (int i = 0; i < datas.length; i++) {
            // 设置画笔颜色
            paint.setColor(colors[i]);
            // 计算扇形选区的扫过角度
            sweepAngle = datas[i] / total * 360f;
            // 绘制扇形选区
            canvas.drawArc(rectF, startAngle, (float) sweepAngle, true, paint);
            // 绘制扇形选区的文字
            drawText(canvas, i);
            // 更新扇形选区的起始角度
            startAngle += sweepAngle;
        }
    }

    // 绘制扇形选区的文字
    private void drawText(Canvas canvas, int i) {
        Log.d(TAG, "drawText: ");
        // 设置画笔颜色为白色
        paint.setColor(Color.WHITE);
        // 设置画笔字体大小为20
        paint.setTextSize(40);
        // 计算扇形选区的中心角度
        double centerAngle = startAngle + sweepAngle / 2;
        // 计算扇形选区的中心点坐标
        float textX = (float) (centerX + radius * Math.cos(Math.toRadians(centerAngle)) / 2);
        float textY = (float) (centerY + radius * Math.sin(Math.toRadians(centerAngle)) / 2);
        // 绘制扇形选区的文字
        canvas.drawText(texts[i], textX, textY, paint);
    }

    // 定义一个函数，根据输入的四组浮点数与字符串，绘制扇形统计图
    public void fun(String a_str, Double a_float, String b_str, Double b_float, String c_str, Double c_float, String d_str, Double d_float) {
        // 将输入的数据赋值给数据集
        datas[0] = a_float;
        datas[1] = b_float;
        datas[2] = c_float;
        datas[3] = d_float;
        // 将输入的字符串赋值给文字集
        texts[0] = a_str;
        texts[1] = b_str;
        texts[2] = c_str;
        texts[3] = d_str;
        // 随机生成颜色赋值给颜色集
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        }
        // 计算数据的总和
        total = 0;
        for (Double data : datas) {
            total += data;
        }
        // 重绘视图
        invalidate();
    }
    public boolean isEmpty(PieChartView pieChartView){
        return Arrays.equals(datas, pieChartView.datas);
    }
}

