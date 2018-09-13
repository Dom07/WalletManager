package com.dom5230.utility.walletmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new Home());
        ft.commit();
        updateBottomMenu(1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_category: {
                break;
            }
            case R.id.action_manage_categories:{
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new ManageCategories());
                ft.commit();
                ft.addToBackStack("Manage Categories");
                updateBottomMenu(3);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:{
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new Home());
                ft.commit();
                break;
            }
            case R.id.nav_transaction_history:{
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new TransactionHistory());
                ft.commit();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onBottomItemSelected(View view){
        int viewId = view.getId();
        switch (viewId){
            case R.id.ivRecentTransaction:{
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                TransactionHistory transactionHistory = new TransactionHistory();
                ft.replace(R.id.content_frame, transactionHistory);
                ft.commit();
                updateBottomMenu(0);
                break;
            }

            case R.id.ivHome:{
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Home home = new Home();
                ft.replace(R.id.content_frame, home);
                ft.commit();
                updateBottomMenu(1);
                break;
            }

            case R.id.ivAssessment:{
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AssessmentFragment assessmentFragment = new AssessmentFragment();
                ft.replace(R.id.content_frame, assessmentFragment);
                ft.commit();
                updateBottomMenu(2);
                break;
            }
        }
        return true;
    }

    public void updateBottomMenu(int position){
        ImageView ivRecentTransaction = findViewById(R.id.ivRecentTransaction);
        ImageView ivHome = findViewById(R.id.ivHome);
        ImageView ivAssessment = findViewById(R.id.ivAssessment);

        if(position == 0){
            ivRecentTransaction.setImageResource(R.drawable.ic_history_highlighted_24dp);
            ivHome.setImageResource(R.drawable.ic_home_black_24dp);
            ivAssessment.setImageResource(R.drawable.ic_assessment_black_24dp);
        } else if (position == 1){
            ivHome.setImageResource(R.drawable.ic_home_highlighted_24dp);
            ivRecentTransaction.setImageResource(R.drawable.ic_history_black_24dp);
            ivAssessment.setImageResource(R.drawable.ic_assessment_black_24dp);
        } else if(position == 2){
            ivHome.setImageResource(R.drawable.ic_home_black_24dp);
            ivRecentTransaction.setImageResource(R.drawable.ic_history_black_24dp);
            ivAssessment.setImageResource(R.drawable.ic_assessment_highlighted_24dp);
        } else{

        }
    }
}
