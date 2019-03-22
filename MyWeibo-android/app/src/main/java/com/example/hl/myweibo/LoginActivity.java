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
import com.example.hl.myweibo.Model.PersonalInf;

public class LoginActivity extends AppCompatActivity {
    private EditText mTelEditText;
    private EditText mPassWordEditText;
    private Button mLoginButton;
    private Button mForgetPassWdButton;
    private Button mRegisterButton;

    private String baseURL="http://"+SendRequest.ip+":8080/MyWeiboServlet/login";
    private String url=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("LoginActivity","t1");

        mTelEditText=(EditText)findViewById(R.id.login_tel);
        mPassWordEditText=(EditText)findViewById(R.id.login_password);
        mLoginButton=(Button)findViewById(R.id.login);
        mForgetPassWdButton=(Button)findViewById(R.id.login_forget_password);
        mRegisterButton=(Button)findViewById(R.id.login_register);

        // 注册按钮监听事件
        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=RegisterActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });

        // 登录操作监听事件
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String tel=mTelEditText.getText().toString();
                String passwd=mPassWordEditText.getText().toString();

                if(tel.equals("")){
                    Toast.makeText(LoginActivity.this, "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwd.equals("")){
                    Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                url=baseURL+"?tel="+tel+"&passwd="+passwd;
                new RequestForLogin().execute();
            }
        });



    }

    // 发送登录请求并跳转到主页面
    private class RequestForLogin extends AsyncTask<Void,Void,Void>{
        String response;

        // 在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Log.d("LoginActivity","response:");
                response=new SendRequest().getUrlString(url); //得到返回结果的String
                Log.d("LoginActivity",response);
            }catch (Exception e){
                e.printStackTrace();

            }

            return null;
        }

        // 当后台操作结束时，此方法将会被调用
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("LoginActivity",response);
            if(!response.equals("notexist")&&!response.equals("wrongpassword")){
                Log.d("LoginActivity","成功");
                Intent i=HomePageActivity.newIntent(LoginActivity.this);
                //PersonalInf.id=mTelEditText.getText().toString();

                i.putExtra("tel",mTelEditText.getText().toString());  //将id传递给主界面
                i.putExtra("nikename",response);

                startActivity(i);
                finish();
            }
            else if(response.equals("notexist")){
                Toast.makeText(LoginActivity.this, "该用户不存在！", Toast.LENGTH_SHORT).show();
            }
            else if(response.equals("wrongpassword")){
                Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
            }
        }


    }

    static public Intent newIntent(Context packageContext){
        Intent intent=new Intent(packageContext,LoginActivity.class);
        return intent;
    }
}
