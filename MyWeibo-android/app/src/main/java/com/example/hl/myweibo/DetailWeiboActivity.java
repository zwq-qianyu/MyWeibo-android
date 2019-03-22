package com.example.hl.myweibo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hl.myweibo.Http.SendRequest;
import com.example.hl.myweibo.Model.Comment;
import com.example.hl.myweibo.Model.CommentDialog;
import com.example.hl.myweibo.Model.EditDialog;
import com.example.hl.myweibo.Model.PersonalInf;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailWeiboActivity extends AppCompatActivity implements View.OnClickListener{
    public static String TAG="DetailWeiboActivity";

    private HomePageItem weiboitem;

    private RelativeLayout mRelativeLayout_repost, mRelativeLayout_comment, mRelativeLayout_like;

    private TextView mCreateNameTextView;
    private TextView mCreateDateTextView;
    private EditText mContentEditText;
    private UUID itemID;


    private List<Comment> mCommentList=new ArrayList<>();
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weibo);

        itemID=(UUID)getIntent().getSerializableExtra("itemID");  //得到实体类
        weiboitem=HomePageList.get(this).getMainPageItem(itemID);
        Log.d(TAG,weiboitem.toString());

        mCreateNameTextView=(TextView)findViewById(R.id.detail_weibo__username);
        mCreateDateTextView=(TextView)findViewById(R.id.detail_weibo__date);
        mContentEditText=(EditText)findViewById(R.id.item_homepage_edittext);

        mCreateNameTextView.setText(weiboitem.getmUserName());
        mCreateDateTextView.setText(weiboitem.getmCreateDate());
        mContentEditText.setText(weiboitem.getmContent());


        mRelativeLayout_repost=(RelativeLayout)findViewById(R.id.relative_detail_repost_menu);
        mRelativeLayout_comment=(RelativeLayout)findViewById(R.id.relative_detail_comment_menu);
        mRelativeLayout_like=(RelativeLayout)findViewById(R.id.relative_detail_like_menu);



        mRelativeLayout_repost.setOnClickListener(this);  //绑定监听事件
        mRelativeLayout_comment.setOnClickListener(this);
        mRelativeLayout_like.setOnClickListener(this);


        String commentInf=weiboitem.getmCommentInf();


        adapter=new CommentAdapter(DetailWeiboActivity.this,R.layout.item_comment,mCommentList);
        ListView listView=(ListView)findViewById(R.id.comment_listview);
        listView.setAdapter(adapter);



//        mCommentList=new ArrayList<>();
//
//        Comment comment1=new Comment();
//        comment1.setmUserName("张三");
//        comment1.setmCommentDate("2019-02-02");
//        comment1.setmContent("实打实大所大所阿萨德");
//
//        Comment comment2=new Comment();
//        comment2.setmUserName("d三");
//        comment2.setmCommentDate("2019-02-02");
//        comment2.setmContent("实打的撒啊啊啊啊啊啊啊啊啊啊啊啊啊啊暗杀打算");
//
//        Comment comment3=new Comment();
//        comment3.setmUserName("nihao");
//        comment3.setmCommentDate("2019-02-02");
//        comment3.setmContent("实打实sdsd所阿萨德");
//
//        mCommentList.add(comment3);
//        mCommentList.add(comment2);
//        mCommentList.add(comment1);
//

    }

    public class CommentAdapter extends ArrayAdapter<Comment>{

        private int resourceId;
        public CommentAdapter(Context context,int commentResourceId,List<Comment> objects){
            super(context,commentResourceId,objects);
            resourceId=commentResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Comment comment=getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            TextView userNameTextView=(TextView)view.findViewById(R.id.item_comment_username);
            TextView dateTextView=(TextView)view.findViewById(R.id.item_comment_date);
            EditText contentEditText=(EditText)view.findViewById(R.id.item_comment_content);

            Log.d(TAG,comment.getmUserName());
            Log.d(TAG,comment.getmCommentDate());
            Log.d(TAG,comment.getmContent());


            userNameTextView.setText(comment.getmUserName());
            dateTextView.setText(comment.getmCommentDate());
            contentEditText.setText(comment.getmContent());

            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_detail_weibo, menu);
        return true;
    }

    // 点击分享
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_detail_share:
                Log.d("DetailWeiboActivity","转发分享");
                String title=weiboitem.getmUserName();
                String text=weiboitem.getmContent();
                PersonalInf.sendMail(this,title,text);

                return true;
            default:
                return true;
        }
    }

    public void CreateCommentDialog(){
        final CommentDialog commentDialog = new CommentDialog(this);
        commentDialog.setTitle("评论");
        commentDialog.setYesOnclickListener("确定", new CommentDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String input) {
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(DetailWeiboActivity.this, "输入不能为空！",Toast.LENGTH_LONG).show();
                } else {

                    //Toast.makeText(getActivity(), phone,Toast.LENGTH_LONG).show();
                    Comment comment=new Comment();  //WEIID自动创建
                    String commenID=comment.getmCommenID().toString();
                    String creatorName=PersonalInf.name;
                    String createTime=PersonalInf.getTime();
                    String creatorID=PersonalInf.id;  //当前用户ID，即评论创建者的ID


                    String str1=input.replaceAll(" ",".@@@@."); //替换
                    String content=str1.replaceAll("\n",".####.");


                    String weiboID=weiboitem.getmId().toString();
                    String bycomid=weiboitem.getmUserID();

                    String url="http://"+SendRequest.ip+":8080/MyWeiboServlet/comment?"+"commenID="+commenID+"&creatorName="+creatorName+"&creatorID="+creatorID
                            +"&content="+content+"&createTime="+createTime+"&weiboID="+weiboID+"&bycomid="+bycomid;
                    new postComment().execute(url);

                    commentDialog.dismiss();

                }
            }
        });
        commentDialog.setNoOnclickListener("取消", new CommentDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                commentDialog.dismiss();
            }
        });
        commentDialog.show();
    }


    //  点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_detail_repost_menu:
//                Log.d("DetailWeiboActivity","转发分享2");
//                String title=weiboitem.getmUserName();
//                String text=weiboitem.getmContent();
//                PersonalInf.sendMail(this,title,text);
                break;
            case R.id.relative_detail_comment_menu:
                CreateCommentDialog();

                break;

            case R.id.relative_detail_like_menu:
                mRelativeLayout_like.setSelected(true);
                break;

            default:
                break;
        }

    }


    private class postComment extends AsyncTask<String,Void,Void> {
        String response;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,response);
            if(response.equals("success")){
                Toast.makeText(DetailWeiboActivity.this, "评论成功！",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(DetailWeiboActivity.this, "评论失败！",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                String url=params[0];

                response=new SendRequest().getUrlString(url); //得到返回结果的String

            }catch (Exception e){
                e.printStackTrace();

            }

            return null;
        }
    }


    private class getCommentList extends AsyncTask<String,Void,Void> {
        String response;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,response);
            if(!response.equals("nocomment")){
                //解析JSON对象
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("comments");

                    mCommentList.clear();
                    int len=jsonArray.length();
                    for(int i=0;i<len;i++){
                        JSONObject obj=jsonArray.getJSONObject(i);
                        Comment comment=new Comment();
                        comment.setmCommenID(UUID.fromString(obj.getString("comid")));
                        comment.setmCreatorName(obj.getString("creatorname"));
                        comment.setmCreatorID(obj.getString("creatorid"));
                        comment.setmContent(obj.getString("content"));
                        comment.setmCommentDate(obj.getString("time"));
                        comment.setmWeiboID(obj.getString("weiboid"));
                        comment.setmBycomID(obj.getString("bycomid"));
                        mCommentList.add(comment);
                    }
                    adapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
//            else{
//                Toast.makeText(DetailWeiboActivity.this, "评论失败！",Toast.LENGTH_LONG).show();
//            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                String url=params[0];
                response=new SendRequest().getUrlString(url); //得到返回结果的String
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        String url="http://"+ SendRequest.ip+":8080/MyWeiboServlet/getCommentContent?"+"weiboid="+weiboitem.getmId().toString();
        new getCommentList().execute(url);

        //adapter.notifyDataSetChanged();
    }



    public static Intent newIntent(Context packageContext, UUID itemID){
        Intent intent=new Intent(packageContext,DetailWeiboActivity.class);
        intent.putExtra("itemID",itemID);
        return intent;
    }


}
