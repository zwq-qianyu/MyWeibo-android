import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "AndroidGeiWeiboListServlet")
public class AndroidGeiWeiboListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //获取out对象

        String userid=request.getParameter("userid");

        JSONObject objects=new JSONObject();
        JSONArray arr=new JSONArray();

        Connection conn = null;
        Statement stmt = null;

        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();

            String sql;

            sql = "select friendsid from userinf where tel='"+userid+"'";
            ResultSet rs = stmt.executeQuery(sql);

//            System.out.println("tttttt");

            String friendsSet=null;
            while(rs.next()){
                friendsSet=rs.getString("friendsid");
            }
            rs.close();

            String sqlwhere="";
            if(friendsSet!=null&&!friendsSet.equals("")){
                String [] str_friend=friendsSet.split("\\+\\+\\+\\+\\+");
                int len=str_friend.length;
                for(int i=0;i<len-1;i++){
                    sqlwhere=sqlwhere+"'"+str_friend[i]+"',";
                }
                sqlwhere=sqlwhere+"'"+str_friend[len-1]+"'";  // 最后一个不用加逗号
                sqlwhere=sqlwhere+",'"+userid+"'";  // 还有自己发的不能忘
            }
            else{
                sqlwhere="'"+userid+"'";
            }


            System.out.println("展示微博信息的人物列表："+sqlwhere);


            sql = "select * from weibo where creatorid in ("+sqlwhere+") order by time desc";     //查找是否有该用户
            ResultSet rs1 = stmt.executeQuery(sql);


            JSONObject obj=null;
            while(rs1.next()){
                // 通过字段检索
                String id=rs1.getString("id");
                String content=rs1.getString("content");
                String time=rs1.getString("time");
                String createname=rs1.getString("createname");
                String like_inf=rs1.getString("like_inf");
                String comment_inf=rs1.getString("comment_inf");
                String picurl=rs1.getString("picurl");
                String creatorid=rs1.getString("creatorid");

                obj=new JSONObject();
                obj.put("id", id);
                obj.put("content", content);
                obj.put("time", time);
                obj.put("createname", createname);
                obj.put("like_inf", like_inf);
                obj.put("comment_inf", comment_inf);
                obj.put("picurl", picurl);
                obj.put("creatorid", creatorid);

                arr.add(obj);

            }

            objects.put("weibos", arr);
            System.out.print(objects.toString());
            out.print(objects.toString());
            rs1.close();

            // 关闭数据库连接
            stmt.close();
            conn.close();
        }catch(SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch(Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 最后是用于关闭资源的块
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

    }
}
