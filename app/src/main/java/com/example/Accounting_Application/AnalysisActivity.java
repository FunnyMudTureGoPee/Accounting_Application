package com.example.Accounting_Application;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AnalysisActivity extends AppCompatActivity {

    SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd");
    private static final String TAG = "AnalysisActivity";
    List<Item> items = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        //获取组件
        Button analysis_button = (Button) findViewById(R.id.analysis_button);
        FloatingActionButton analysis_show = (FloatingActionButton) findViewById(R.id.analysis_show);
        PieChartView pieChartView = (PieChartView) findViewById(R.id.pie_chart_view);
        pieChartView.fun("据", 1d, "数", 1d, "暂", 1d, "无", 1d);
        DatePicker datePicker_start = (DatePicker) findViewById(R.id.date_start);
        DatePicker datePicker_end = (DatePicker) findViewById(R.id.date_end);

        // 获取 RecyclerView 的引用
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.analysis_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);


        ItemAdapter adapter = new ItemAdapter(AnalysisActivity.this, recyclerView, items);
        recyclerView.setAdapter(adapter);


        analysis_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        String date_start = format2.format(new Date(datePicker_start.getYear() - 1900, datePicker_start.getMonth(), datePicker_start.getDayOfMonth()));
                        String date_end = format2.format(new Date(datePicker_end.getYear() - 1900, datePicker_end.getMonth(), datePicker_end.getDayOfMonth()));
                        List<Item> itemList = LitePal.where("item_date between ? and ?", date_start, date_end).find(Item.class);
                        items = itemList;
                        Log.d(TAG, "onClick: " + "'" + date_start + "'" + "and" + "'" + date_end + "'");
                        Map<String, Double> map = itemList.stream().collect(Collectors.groupingBy(Item::getItem_type, Collectors.summingDouble(Item::getItem_value)));
                        // 遍历map的entrySet
                        String[] str = {"", "", "", ""};
                        Log.d(TAG, "onClick: map" + itemList);
                        double[] values = {0d, 0d, 0d, 0d};
                        int i = 0;
                        for (Map.Entry<String, Double> entry : map.entrySet()) {
                            // 对键值对进行操作
                            values[i] = entry.getValue();
                            str[i] = entry.getKey() + entry.getValue();

                            i++;

                        }
                        pieChartView.fun(str[0], values[0], str[1], values[1], str[2], values[2], str[3], values[3]);
                        adapter.updateItemList(itemList);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        findViewById(R.id.analysis).setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });
        analysis_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateItemList(new ArrayList<>());
                findViewById(R.id.analysis).setVisibility(View.VISIBLE);
            }
        });


    }
}