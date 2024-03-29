package com.example.Accounting_Application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Accounting_Application.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 定义一个数组，用于存储不同的用户名
    private String[] userNames = {"公有资金", "私有资金"};
    // 定义一个变量，用于记录当前的用户名的索引
    private int currentIndex = 0;
    // 定义一个 TextView 对象，用于显示用户名
    private TextView userNameTextView;
    // 定义一个 ImageButton 对象，用于切换用户名
    private ImageButton imageButton;


    private List<Item> itemList = new ArrayList<>();

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private CircleButtonView circleButtonView; // 声明自定义控件对象

    public static ItemAdapter adapter;

    private FloatingActionButton fab;

    private Animation animation = null;

    SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd");

    private static final String TAG = "MainActivity";

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        circleButtonView = findViewById(R.id.circle_button); // 获取自定义控件对象

        //从数据库中读取数据
        if (!LitePal.findAll(Item.class).isEmpty()) {
            String today = format2.format(new Date());
            Log.d(TAG, "onCreate: " + today);
            itemList = LitePal.where("item_date like ?", today.substring(0, 10) + "%").find(Item.class);
        }

        TextView tv = (TextView) findViewById(R.id.tip);

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String temptype = "";
                double tempvalue = 0;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((View) tv.getParent()).setVisibility(View.VISIBLE);
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_scale_2big);
                        ((View) tv.getParent()).startAnimation(animation);
                        fab.setVisibility(View.INVISIBLE);
                        circleButtonView.myTouchEvent(event);
                        circleButtonView.setReady(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        circleButtonView.myTouchEvent(event);
                        if (temptype != circleButtonView.getItemType() && tempvalue != circleButtonView.getValue()) {
                            tv.setText(circleButtonView.getItemType() + "," + circleButtonView.getValue());
                        }
                        temptype = circleButtonView.getItemType();
                        tempvalue = circleButtonView.getValue();


                        break;
                    case MotionEvent.ACTION_UP:
                        fab.setVisibility(View.VISIBLE);
                        circleButtonView.myTouchEvent(event);
                        circleButtonView.setReady(false);
                        adapter.AddItem(Item.saveItem(new Item(circleButtonView.getItemType(), circleButtonView.getItemType(), circleButtonView.getValue(), format1.format(new Date()))));

                        ((View) tv.getParent()).setVisibility(View.INVISIBLE);
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_scale_2small);
                        ((View) tv.getParent()).startAnimation(animation);
                        break;
                }

                return false;
            }
        });

        // 获取 RecyclerView 的引用
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ItemAdapter(MainActivity.this, recyclerView, itemList);
        recyclerView.setAdapter(adapter);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // 将每个菜单ID作为一组ID传递，因为每个菜单都应被视为顶级目的地。
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
/*

 */
        // 菜单的点击事件
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, AnalysisActivity.class);
                startActivity(intent);
                return false;
            }
        });


        // 获取导航视图的引用
        NavigationView navigationView1 = findViewById(R.id.nav_view);
        // 通过导航视图获取图片按钮的引用
        imageButton = navigationView1.getHeaderView(0).findViewById(R.id.imagebutton);
        // 通过导航视图获取用户名文本视图的引用
        userNameTextView = navigationView1.getHeaderView(0).findViewById(R.id.user_name);

        // 为图片按钮设置一个点击监听器
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用切换用户名的方法
                switchUserName();
            }
        });
    }

    // 定义一个方法，用于切换用户名
    private void switchUserName() {
        // 将当前的用户名的索引加一
        currentIndex++;
        // 如果当前的用户名的索引超过了数组的长度，就将其重置为零
        if (currentIndex >= userNames.length) {
            currentIndex = 0;
        }
        // 获取当前的用户名
        String currentUserName = userNames[currentIndex];
        // 设置用户名文本视图的文本为当前的用户名
        userNameTextView.setText(currentUserName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //测试条目列表


}