package com.example.hl.myweibo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.app.*;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hl.myweibo.Model.PersonalInf;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout mLayoutHome, mLayoutWrite, rmLayoutMessage, mLayoutPerson;

    private TextView tvTabMenuChannel, tvTabMenuChannelNum, tvTabMenuMessage, tvTabMenuMessageNum, tvTabMenuBetter,
            tvTabMenuBetterNum, tvTabMenuMy, tvTabMenuMyNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        PersonalInf.id=getIntent().getStringExtra("tel");
        PersonalInf.name=getIntent().getStringExtra("nikename");

        bindView();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment==null){
            fragment= new HomePageFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }

    }

    // 绑定监听器
    private void bindView() {

        mLayoutHome = (RelativeLayout)findViewById(R.id.relativelayout_bottom_menu_home);
        mLayoutWrite = (RelativeLayout)findViewById(R.id.relativelayout_bottom_menu_write);
        rmLayoutMessage = (RelativeLayout)findViewById(R.id.relativelayout_bottom_menu_message);
        mLayoutPerson =(RelativeLayout) findViewById(R.id.relativelayout_bottom_menu_person);


        tvTabMenuChannel = (TextView)findViewById(R.id.main_tvTabMenuItemChannel);
        tvTabMenuChannelNum = (TextView)findViewById(R.id.main_tvTabMenuItemChannelNum);
        tvTabMenuMessage = (TextView)findViewById(R.id.main_tvTabMenuItemMessage);
        tvTabMenuMessageNum = (TextView)findViewById(R.id.main_tvTabMenuItemMessageNum);
        tvTabMenuBetter = (TextView)findViewById(R.id.main_tvTabMenuItemBetter);
        tvTabMenuBetterNum = (TextView)findViewById(R.id.main_tvTabMenuItemBetterNum);
        tvTabMenuMy = (TextView)findViewById(R.id.main_tvTabMenuItemMy);
        tvTabMenuMyNum = (TextView)findViewById(R.id.main_tvTabMenuItemMyNum);

        mLayoutHome.setOnClickListener(this);
        mLayoutWrite.setOnClickListener(this);
        rmLayoutMessage.setOnClickListener(this);
        mLayoutPerson.setOnClickListener(this);
    }

    // 将tabMenu的选择全部置空
    private void resetSelected() {
        tvTabMenuChannel.setSelected(false);
        tvTabMenuMessage.setSelected(false);
        tvTabMenuBetter.setSelected(false);
        tvTabMenuMy.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativelayout_bottom_menu_home:{
                resetSelected();
                tvTabMenuChannel.setSelected(true);
                Toast.makeText(HomePageActivity.this, tvTabMenuChannelNum.getText(), Toast.LENGTH_SHORT).show();
                tvTabMenuChannelNum.setVisibility(View.GONE);
                tvTabMenuChannelNum.setText("0");
                break;
            }
            case R.id.relativelayout_bottom_menu_write:{
                resetSelected();
                tvTabMenuMessage.setSelected(true);
                Intent intent = WriteWeiboActivity.newIntent(HomePageActivity.this);
                startActivity(intent);

//                tvTabMenuMessageNum.setVisibility(View.GONE);
//                tvTabMenuMessageNum.setText("0");
                break;
            }
            case R.id.relativelayout_bottom_menu_message:{
                resetSelected();
                tvTabMenuBetter.setSelected(true);
                Toast.makeText(HomePageActivity.this, tvTabMenuBetterNum.getText(), Toast.LENGTH_SHORT).show();
                tvTabMenuBetterNum.setVisibility(View.GONE);
                tvTabMenuBetterNum.setText("0");
                break;
            }
            case R.id.relativelayout_bottom_menu_person:{
                resetSelected();
                tvTabMenuMy.setSelected(true);
                Toast.makeText(HomePageActivity.this, tvTabMenuMyNum.getText(), Toast.LENGTH_SHORT).show();
                tvTabMenuMyNum.setVisibility(View.GONE);
                tvTabMenuMyNum.setText("0");
                break;
            }
            default:break;
        }
    }

    static public Intent newIntent(Context packageContext){
        Intent intent=new Intent(packageContext,HomePageActivity.class);
        return intent;
    }
}
