package com.dom5230.utility.walletmanager;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tappx.sdk.android.TappxBanner;
import com.tappx.sdk.android.TappxInterstitial;

public class MainActivity extends AppCompatActivity {
    TappxBanner tappxBanner;
    ViewGroup bannerContainer;

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

        bannerContainer = findViewById(R.id.tappx_banner);
        tappxBanner = new TappxBanner(getBaseContext(), "Pub-43395-Android-4870");
//        tappxBanner.setAdSize(TappxBanner.AdSize.BANNER_320x50);
        tappxBanner.setAdSize(TappxBanner.AdSize.SMART_BANNER);
        bannerContainer.addView(tappxBanner);
        tappxBanner.loadAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tappxBanner!=null){
            tappxBanner.destroy();
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
        int id = item.getItemId();

        if (id == R.id.action_manage_categories){
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new ManageCategories());
                ft.commit();
                ft.addToBackStack("Manage Categories");
                updateBottomMenu(3);
            }
        return super.onOptionsItemSelected(item);
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
        }
        return true;
    }

    public void updateBottomMenu(int position){
        ImageView ivRecentTransaction = findViewById(R.id.ivRecentTransaction);
        ImageView ivHome = findViewById(R.id.ivHome);

        if(position == 0){
            ivRecentTransaction.setImageResource(R.drawable.ic_history_highlighted_24dp);
            ivHome.setImageResource(R.drawable.ic_home_black_24dp);
        } else if (position == 1){
            ivHome.setImageResource(R.drawable.ic_home_highlighted_24dp);
            ivRecentTransaction.setImageResource(R.drawable.ic_history_black_24dp);
        }
    }
}
