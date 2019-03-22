package com.example.hl.myweibo.Model;

import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PersonalInf {
    public static String id;   // 手机号tel  在HomePageActivity中初始化的
    public static String name;  //用户nickname

    public static void sendMail(Context mContext, String title, String text) {
        // 调用系统发邮件
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // 设置文本格式
        emailIntent.setType("text/plain");
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(Intent.createChooser(emailIntent, "Choose an application to share:"));
    }

    public static String  getTime(){
        SimpleDateFormat formatter  =   new   SimpleDateFormat   ("yyyy-MM-dd+HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String  str  =  formatter.format(curDate);
        return str;
    }

}
