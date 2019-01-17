package com.example.hl.myweibo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.hl.myweibo.Http.SendRequest;
import com.example.hl.myweibo.Model.PersonalInf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class WriteWeiboActivity extends AppCompatActivity {
    public static String TAG="WriteWeiboActivity";

    private String baseURL="http://"+SendRequest.ip+":8080/MyWeiboServlet/writeWeibo?";
    private String url=null;

    private EditText mContentEdiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_weibo);

        mContentEdiText=(EditText)findViewById(R.id.write_weibo_edittext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // 为菜单设置图标
        getMenuInflater().inflate(R.menu.menu_write_weibo , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "00000000000");
        switch (item.getItemId()){
            case R.id.item_sendweibo:

                HomePageItem weibo=new HomePageItem();

                weibo.setmUserID(PersonalInf.id);
                weibo.setmContent(mContentEdiText.getText().toString()); //微博内容

                SimpleDateFormat formatter  =   new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
                Date curDate =  new Date(System.currentTimeMillis());
                String  str  =  formatter.format(curDate);
                Log.d(TAG, "时间："+str);
                weibo.setmCreateDate(str);  //时间

                weibo.setmUserName(PersonalInf.name);

                weibo.setmPortraitURL(""); //考虑上传图片
                weibo.setWeiboURL(""); //微博内容图片


                weibo.setmCommentInf("");
                weibo.setmLikeInf("");

                List list= HomePageList.get(this).getMainPageList();

                list.add(weibo);

                String weiboid=weibo.getmId().toString();  //微博ID
                String userid= PersonalInf.id; //用户ID，从全局静态变量获得
                String st=weibo.getmContent();  //微博内容
                String st1=st.replace("\n",".####.");
                String content=st1.replaceAll(" ",".@@@@.");

                String date=weibo.getmCreateDate();  //时间
                String weiboURL=weibo.getWeiboURL();   //图片地址
                String commentInf=weibo.getmCommentInf(); //评论
                String likeInf=weibo.getmLikeInf(); //点赞
                String username=weibo.getmUserName();


                url=baseURL+"weiboid="+weiboid+"&userid="+userid+"&content="+content+"&date="+date+"&weiboURL="+weiboURL+"&commentInf="+commentInf+"&likeInf="+likeInf+"&username="+username;
                Log.d(TAG,url);

                new UploadNewWeibo().execute();//上传数据库

                //finish();
                break;
            default:

                break;
        }

            return true;
    }



    private class UploadNewWeibo extends AsyncTask<Void,Void,Void> {
        String response;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG,response);

            if(response.equals("writeweibosuccess")){
                Toast.makeText(WriteWeiboActivity.this,"发送成功！",Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(WriteWeiboActivity.this,"发送失败，请重试",Toast.LENGTH_SHORT).show();
            }



        }

        @Override
        protected Void doInBackground(Void... params) {
            try{

                response=new SendRequest().getUrlString(url); //得到返回结果的String
//                Log.d(TAG, "结果："+response);
            }catch (Exception e){
                e.printStackTrace();

            }
            return null;
        }
    }



    static public Intent newIntent(Context packageContext){
        Intent intent=new Intent(packageContext,WriteWeiboActivity.class);
        return intent;
    }

}
