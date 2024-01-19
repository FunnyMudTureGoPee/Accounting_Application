package com.example.Accounting_Application;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AnalysisActivity extends AppCompatActivity {

    SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd");
    private static final String TAG = "AnalysisActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        //获取组件
        Button button = (Button) findViewById(R.id.analysis_button);
        PieChartView pieChartView = (PieChartView) findViewById(R.id.pie_chart_view);
        pieChartView.fun("据",1d,"数",1d,"暂",1d,"无",1d);
        DatePicker datePicker_start = (DatePicker) findViewById(R.id.date_start);
        DatePicker datePicker_end = (DatePicker) findViewById(R.id.date_end);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_start = format2.format(new Date(datePicker_start.getYear()-1900,datePicker_start.getMonth(),datePicker_start.getDayOfMonth()));
                String date_end = format2.format(new Date(datePicker_end.getYear()-1900,datePicker_end.getMonth(),datePicker_end.getDayOfMonth()));
                List<Item> itemList = LitePal.where("item_date between ? and ?", date_start, date_end).find(Item.class);
                Log.d(TAG, "onClick: "+"'"+date_start+"'"+"and"+"'"+date_end+"'");
                Map<String, Double> map = itemList.stream().collect(Collectors.groupingBy(Item::getItem_type,Collectors.summingDouble(Item::getItem_value)));
                // 遍历map的entrySet
                String[] str={"","","",""};
                Log.d(TAG, "onClick: map"+itemList);
                double[] values={0d,0d,0d,0d};
                int i=0;
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    // 对键值对进行操作
                    values[i] =entry.getValue();
                    str[i] = entry.getKey()+entry.getValue();

                    i++;

                }
                pieChartView.fun(str[0],values[0],str[1],values[1],str[2],values[2],str[3],values[3]);
            }
        });


    }
}