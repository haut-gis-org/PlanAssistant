package com.hgo.planassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.hgo.planassistant.App;
import com.hgo.planassistant.R;
import com.hgo.planassistant.adapter.FragmentAdapter;
import com.hgo.planassistant.fragement.HomeFragment;
import com.hgo.planassistant.fragement.PlanFragment;
import com.hgo.planassistant.fragement.RecordFragment;

import java.util.ArrayList;
import java.util.List;

import static com.hgo.planassistant.App.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawer;
    private FloatingActionButton fab;

    private NavigationView navigationView;
    private View headview;

    private TextView tv_nickname, tv_introduction;

    private Context mainactivity_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initnavi_view();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainactivity_context = this;

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        headview = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        // navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_header = headerView.findViewById(R.id.nav_header);
        nav_header.setOnClickListener(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(this);

        TabLayout mTabLayout = findViewById(R.id.tab_layout_main);
        ViewPager mViewPager = findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_title_main_1));
        titles.add(getString(R.string.tab_title_main_2));
        titles.add(getString(R.string.tab_title_main_3));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new PlanFragment());
        fragments.add(new RecordFragment());
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);


        tv_nickname = (TextView) headview.findViewById(R.id.nav_header_nickname);
        tv_introduction = (TextView) headview.findViewById(R.id.nav_header_introduction);

    }
    private void initnavi_view(){
        //检测是否已登录
        if (AVUser.getCurrentUser() != null) {

            //移除登录相关菜单
            navigationView.getMenu().removeGroup(R.id.nav_non_account);
            Log.i("MainActivity","用户名: " + AVUser.getCurrentUser().getUsername());
            Log.i("MainActivity","简介: " + AVUser.getCurrentUser().getString("introduction"));
            // 设置用户名和简介
            tv_nickname.setText(AVUser.getCurrentUser().getUsername());
            tv_introduction.setText(AVUser.getCurrentUser().get("introduction").toString());
        }else{
            //移除账户相关菜单
            navigationView.getMenu().removeGroup(R.id.nav_account);
        }
    }
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Log.i("MainActitvity","MainActivity Page Change！");
        }

        @Override
        public void onPageSelected(int position) {

            if (position == 2) {
                fab.show();
            } else {
                fab.hide();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Log.i("MainActitvity","MainActivity PageScrollStateChanged！");
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_main_about:
//                Intent aboutIntent = new Intent(this, AboutActivity.class);
//                startActivity(aboutIntent);
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.action_main_settings:
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                break;
//            case R.id.action_main_feedback:
//                Intent myAppsIntent = new Intent(this, MyAppsActivity.class);
//                startActivity(myAppsIntent);
//                break;
        }
        return super.onOptionsItemSelected(item);
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_menu_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
                break;
            case R.id.nav_changepassword:
                AVUser.requestPasswordResetInBackground(AVUser.getCurrentUser().getEmail(), new RequestPasswordResetCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
//                            Toast.makeText(MainActivity.this, "重置密码邮箱已发送，请检查您的邮箱！", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(getWindow().getDecorView().findViewById(R.id.nav_changepassword), getString(R.string.main_snack_bar), Snackbar.LENGTH_LONG)
//                                    .setAction(getString(R.string.main_snack_bar_action), view -> {
//                                    }).show();
                            new AlertDialog.Builder(mainactivity_context)
                                    .setMessage("重置密码邮件已发送至您的邮箱，请注意查收！")
                                    .setPositiveButton(getString(R.string.dialog_ok), null)
                                    .show();
                        } else {
                            e.printStackTrace();
                            Log.i("MainActivity",e.toString());
                            new AlertDialog.Builder(mainactivity_context)
                                    .setMessage("出现错误，错误原因为: "+ e.toString())
                                    .setPositiveButton(getString(R.string.dialog_ok), null)
                                    .show();
                        }
                    }
                });
                break;
            case R.id.nav_info:
                break;
            case R.id.nav_logout:
                AVUser.getCurrentUser().logOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                Snackbar.make(v, getString(R.string.main_snack_bar), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.main_snack_bar_action), view -> {
                        }).show();
                break;
            default:
                Log.i("MainActitvity","MainActivity Click！");
                break;
        }
    }
}
