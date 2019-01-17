package com.example.hl.myweibo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hl.myweibo.Http.SendRequest;

public class RegisterActivity extends AppCompatActivity {
    private EditText mTel;
    private EditText mNickName;
    private EditText mPassWord;
    private EditText mConfrimPassWord;
    private EditText mValidateCode;
    private Button RegisterBotton;
    private String mCode="123"; //验证码

    String url;  //请求的url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTel=(EditText)findViewById(R.id.registerpage_tel_edittext);
        mNickName=(EditText)findViewById(R.id.registerpage_nikename_edittext);
        mPassWord=(EditText)findViewById(R.id.registerpage_passwd_edittext);
        mConfrimPassWord=(EditText)findViewById(R.id.registerpage_confirmpasswd_edittext);
        mValidateCode=(EditText)findViewById(R.id.registerpage_validateCode_edittext);
        RegisterBotton=(Button)findViewById(R.id.registerpage_register_button);


        RegisterBotton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String tel=mTel.getText().toString();
                String nickname=mNickName.getText().toString();
                String passwd=mPassWord.getText().toString();
                String confPass=mConfrimPassWord.getText().toString();
                String validateCode=mValidateCode.getText().toString();

                if(tel.equals("")){
                    Toast.makeText(RegisterActivity.this, "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.equals("")){
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(confPass.equals("")){
                    Toast.makeText(RegisterActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validateCode.equals("")){
                    Toast.makeText(RegisterActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!passwd.equals(confPass)){
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!mCode.equals(validateCode)){
                    Toast.makeText(RegisterActivity.this, "验证码错误，请输入正确的验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                //向后台发送register请求
                String baseurl="http://"+ SendRequest.ip+":8080/MyWeiboServlet/register?";
                url=baseurl+"tel="+tel+"&password="+passwd+"&nickname="+nickname;
                new RequestForRegister().execute();
            }
        });

    }


    // 发请求并处理回复
    private class RequestForRegister extends AsyncTask<Void,Void,Void> {
        String result;

        // 接收输入参数和返回计算结果
        @Override
        protected Void doInBackground(Void... params) {
            try{
                result=new SendRequest().getUrlString(url); //得到返回结果的String
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("RegisterActivity",result);
            if(result.equals("yes")){  // 表明注册成功
                Toast.makeText(RegisterActivity.this, "注册成功，现在返回登录界面！", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(result.equals("no")){
                Toast.makeText(RegisterActivity.this, "该用户已被注册，请输入正确的手机号！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    static public Intent newIntent(Context packageContext){
        Intent intent=new Intent(packageContext,RegisterActivity.class);
        return intent;
    }

}
