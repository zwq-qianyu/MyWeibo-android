package com.example.hl.myweibo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.hl.myweibo.Http.SendRequest;
import com.example.hl.myweibo.Model.EditDialog;
import com.example.hl.myweibo.Model.PersonalInf;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class HomePageFragment extends Fragment {
    public static String TAG="HomePageFragment";

    private String baseURL="http://"+ SendRequest.ip+":8080/MyWeiboServlet/addFriends?";
    private String url=null;

    private RecyclerView mHomePageRecyclerView;
    private HomePageItemAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_list, container, false);
        mHomePageRecyclerView=(RecyclerView)view.findViewById(R.id.homepage_recycler_view);
        mHomePageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        HomePageList homePageList=HomePageList.get(getActivity());
        List<HomePageItem>list=homePageList.getMainPageList();
        mAdapter=new HomePageItemAdapter(list);
        mHomePageRecyclerView.setAdapter(mAdapter); //绑定adapter

        return view;
    }

    private class HomePageItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private HomePageItem mHomePageItem;

        private TextView mUserName;
        private TextView mDate;
        private EditText editText;
        private RelativeLayout mRelativeLayout_repost, mRelativeLayout_comment, mRelativeLayout_like;


        public void bind(HomePageItem homePageItem){

            mHomePageItem = homePageItem;


            mUserName.setText(mHomePageItem.getmUserName());
            String str=mHomePageItem.getmCreateDate();
            str.replace('+',' ');
            mDate.setText(str);
            editText.setText(mHomePageItem.getmContent());

        }

        public HomePageItemHolder(LayoutInflater inflater,ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_weibo,parent,false));
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //进去详细微博界面
                    Log.d(TAG,"详细");
                    Intent intent=DetailWeiboActivity.newIntent(getActivity(),mHomePageItem.getmId());
                    startActivity(intent);
                }
            });

            mUserName=(TextView)itemView.findViewById(R.id.item_homepage_username);
            mDate=(TextView)itemView.findViewById(R.id.item_homepage_date);
            editText=(EditText)itemView.findViewById(R.id.item_homepage_edittext);

            mRelativeLayout_repost=(RelativeLayout)itemView.findViewById(R.id.relative_weibo_repost);
            mRelativeLayout_comment=(RelativeLayout)itemView.findViewById(R.id.relative_weibo_comment);
            mRelativeLayout_like=(RelativeLayout)itemView.findViewById(R.id.relative_weibo_like);

            mRelativeLayout_repost.setOnClickListener(this);  //绑定监听事件
            mRelativeLayout_comment.setOnClickListener(this);
            mRelativeLayout_like.setOnClickListener(this);

        }

        private void resetSelected() {  //设置为没有点击
            mRelativeLayout_repost.setSelected(false);
            mRelativeLayout_comment.setSelected(false);
            mRelativeLayout_like.setSelected(false);
        }

        @Override
        public void onClick(View v) {  //转发、评论和点赞事件的触发
            switch (v.getId()) {
                case R.id.relative_weibo_repost:{
                    resetSelected();
                    mRelativeLayout_repost.setSelected(true);

                    break;
                }
                case R.id.relative_weibo_comment:{
                    resetSelected();
                    mRelativeLayout_comment.setSelected(true);

                    break;
                }
                case R.id.relative_weibo_like:{
                    resetSelected();
                    mRelativeLayout_like.setSelected(true);
                    break;
                }

                default:
                    break;
            }
        }
    }

    private class HomePageItemAdapter extends RecyclerView.Adapter<HomePageItemHolder>{

        private List<HomePageItem> mHomePageList;

        public HomePageItemAdapter(List<HomePageItem> list){
            mHomePageList=list;
        }

        @Override
        public HomePageItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            return new HomePageItemHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(HomePageItemHolder holder, int position) {
            HomePageItem item=mHomePageList.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mHomePageList.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home_page,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // 添加好友
            case R.id.item_find_friend:
                //alert_edit();
                find_Edot_Dialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // 添加好友对话框
    public void find_Edot_Dialog(){
        final EditDialog editDialog = new EditDialog(getActivity());
        editDialog.setTitle("请输入对方的账号");
        editDialog.setYesOnclickListener("确定", new EditDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String phone) {
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "输入不能为空！",Toast.LENGTH_LONG).show();
                } else {

                    //Toast.makeText(getActivity(), phone,Toast.LENGTH_LONG).show();
                    String userid=PersonalInf.id;

                    if(phone.equals(userid)){
                        Toast.makeText(getActivity(), "您已经在关注自己啦",Toast.LENGTH_LONG).show();
                    }
                    else{
                        url=baseURL+"userid="+userid+"&friendid="+phone;
                        new FindFriend().execute();


                        editDialog.dismiss();
                        //让软键盘隐藏
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getApplicationWindowToken(), 0);
                    }


                }
            }
        });
        editDialog.setNoOnclickListener("取消", new EditDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

    // 添加好友
    private class FindFriend extends AsyncTask<Void,Void,Void> {
        String response;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,response);
            if(response.equals("success")){
                Toast.makeText(getActivity(), "关注成功！",Toast.LENGTH_LONG).show();
            }
            else if(response.equals("friendexsit")){
                Toast.makeText(getActivity(), "你已经关注该用户了",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(), "该用户不存在，请输入正确的用户ID",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{

                response=new SendRequest().getUrlString(url); //得到返回结果的String

            }catch (Exception e){
                e.printStackTrace();

            }

            return null;
        }
    }


    // 获取微博信息列表
    private class getWeiboListInf extends AsyncTask<String,Void,Void> {
        String response;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,response);
            try{
                JSONObject jsonObject=new JSONObject(response);
                JSONArray jsonArray=jsonObject.getJSONArray("weibos");  //解析返回的数据
                JSONObject obj=null;

                List list=HomePageList.get(getActivity()).getMainPageList();
                list.clear();
                for(int i=0;i<jsonArray.length();i++){
                    obj=jsonArray.getJSONObject(i);
                    String weiID=obj.getString("id");
                    String str=obj.getString("content");
                    String content=str.replaceAll(".####.", "\n");


                    String time=obj.getString("time");
                    String createname=obj.getString("createname");
                    String like_inf=obj.getString("like_inf");
                    String comment_inf=obj.getString("comment_inf");
                    String picurl=obj.getString("picurl");
                    String creatorid=obj.getString("creatorid");

                    HomePageItem weiboItem = new HomePageItem();

                    weiboItem.setmId(UUID.fromString(weiID));
                    weiboItem.setmContent(content);
                    weiboItem.setmCreateDate(time);
                    weiboItem.setmUserName(createname);
                    weiboItem.setmLikeInf(like_inf);
                    weiboItem.setmCommentInf(comment_inf);
                    weiboItem.setWeiboURL(picurl);
                    weiboItem.setmUserID(creatorid);

                    list.add(weiboItem);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            // 在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                String url1 = params[0];
                response=new SendRequest().getUrlString(url1); //得到返回结果的String

            }catch (Exception e){
                e.printStackTrace();

            }

            return null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        String url1="http://"+ SendRequest.ip+":8080/MyWeiboServlet/getContent?"+"userid="+PersonalInf.id;
        new getWeiboListInf().execute(url1);

    }



    //    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_one:{
//                TextView textView = getActivity().findViewById(R.id.main_tvTabMenuItemChannelNum);
//                textView.setText("11");
//                textView.setVisibility(View.VISIBLE);
//                break;
//            }·
//            case R.id.btn_two:{
//                TextView textView = getActivity().findViewById(R.id.main_tvTabMenuItemMessageNum);
//                textView.setText("22");
//                textView.setVisibility(View.VISIBLE);
//                break;
//            }
//            case R.id.btn_three:{
//                TextView textView = getActivity().findViewById(R.id.main_tvTabMenuItemBetterNum);
//                textView.setText("33");
//                textView.setVisibility(View.VISIBLE);
//                break;
//            }
//            case R.id.btn_four:{
//                TextView textView = getActivity().findViewById(R.id.main_tvTabMenuItemMyNum);
//                textView.setText("55");
//                textView.setVisibility(View.VISIBLE);
//                break;
//            }
//            default:break;
//        }
//    }

}
