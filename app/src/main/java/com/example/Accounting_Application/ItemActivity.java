package com.example.Accounting_Application;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.Accounting_Application.ItemAdapter;

public class ItemActivity extends AppCompatActivity {

    private Item item;

    private FloatingActionButton button;
    private static final String TAG = "ItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        //获取传入item参数
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        //绑定一大堆控件
        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView imageView= (ImageView) findViewById(R.id.item_image_view);
        EditText editText_Type = (EditText) findViewById(R.id.item_content_type);
        EditText editText_Name = (EditText) findViewById(R.id.item_content_name);
        EditText editText_Value = (EditText) findViewById(R.id.item_content_value);
        EditText editText_Notes = (EditText) findViewById(R.id.item_content_notes);
        button = (FloatingActionButton) findViewById(R.id.item_content_Rer);
        //setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(item.getItem_name());
        //Glide.with(this).load(item.getItem_image()).load(imageView);
        imageView.setImageResource(item.getItem_image());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        editText_Type.setHint(item.getItem_type());
        editText_Name.setHint(item.getItem_name());
        editText_Value.setHint(Double.toString(item.getItem_value()));
        editText_Notes.setHint(item.getItem_notes());

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+(editText_Type.getText().toString().isEmpty()));
                item.setItem_name(editText_Type.getHint().toString());
                item.setItem_name(editText_Name.getHint().toString());
                item.setItem_value(Double.parseDouble(editText_Value.getHint().toString()));
                item.setItem_notes(editText_Notes.getHint().toString());
                if (!editText_Name.getText().toString().isEmpty()){
                    item.setItem_name(editText_Name.getText().toString());
                }
                if (!editText_Value.getText().toString().isEmpty()){
                    item.setItem_value(Double.parseDouble(editText_Value.getText().toString()));
                }
                if (!editText_Notes.getText().toString().isEmpty()){
                    item.setItem_notes(editText_Notes.getText().toString());
                }

                Toast.makeText(ItemActivity.this, "修改完毕", Toast.LENGTH_SHORT).show();

                int pos=intent.getIntExtra("position",0);
                Log.d(TAG, "onClick: "+pos);
                MainActivity.adapter.ReNewItem(pos,item);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}