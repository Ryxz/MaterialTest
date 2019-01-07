package com.ryx.materialtest.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryx.materialtest.R;
import com.ryx.materialtest.adapter.FruitAdapter;
import com.ryx.materialtest.bean.Fruit;
import com.ryx.materialtest.utils.Constans;
import com.ryx.materialtest.utils.InfoPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout mDrawerLayout;
    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple),new Fruit("Banana",R.drawable.watermelon),
            new Fruit("Orange",R.drawable.orange),new Fruit("Watermelon",R.drawable.watermelon),
            new Fruit("Pear",R.drawable.pear),new Fruit("Grape",R.drawable.grape),
            new Fruit("Pineapple",R.drawable.pineapple),new Fruit("Strawberry",R.drawable.strawberry),
            new Fruit("Cherry",R.drawable.cherry),new Fruit("Mango",R.drawable.mango)};
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;
    private ImageView iconImg;
    private NavigationView navView;
    private TextView nameText;
    private TextView mailText;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        View headView = navView.getHeaderView(0);
        iconImg = headView.findViewById(R.id.icon_image);
        nameText = headView.findViewById(R.id.user_name_header);
        mailText = headView.findViewById(R.id.mail_text);

        setSupportActionBar(toolbar);
        navMenu();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        listenerButton();

        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });

        refreshHeadView();
    }
    private void refreshHeadView() {
        InfoPrefs.init("user_info");
        String path = InfoPrefs.getData(Constans.UserInfo.HEAD_IMAGE);// 获取图片路径

        File file = new File(path);
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(path);
            // 将图片显示到ImageView中
            iconImg.setImageBitmap(bm);
        }else{
            Log.e(TAG,"no file");
            iconImg.setImageResource(R.drawable.th);
        }
        nameText.setText(InfoPrefs.getData(Constans.UserInfo.NAME));
        mailText.setText(InfoPrefs.getData(Constans.UserInfo.MAIL));
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initFruits() {
        fruitList.clear();

        for (int i = 0;i<50;i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    public void navMenu() {

        navView.setCheckedItem(R.id.nav_mine);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent();
                switch (menuItem.getItemId()) {
                    case R.id.nav_mine:
                        intent.setClass(MainActivity.this,MineActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_call:
                        break;
                    case R.id.nav_friends:
                        break;
                    case R.id.nav_location:
                        break;
                    case R.id.nav_mail:
                        intent.setClass(MainActivity.this,MailActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_task:
                        break;
                }
                return true;
            }
        });
    }
    public void listenerButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"you clicked FAB",Toast.LENGTH_SHORT).show();
                Snackbar.make(v,"删除数据",Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,"删除数据",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshHeadView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.backup:
                Toast.makeText(this,"you clicked backup",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"you clicked delete",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"you clicked settings",Toast.LENGTH_SHORT).show();
                break;
                default:
        }
        return true;
    }
}
