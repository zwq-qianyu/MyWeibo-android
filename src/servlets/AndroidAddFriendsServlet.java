package servlets;

import config.Config;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// 添加好友
//@WebServlet("/AndroidFindServlet")
@WebServlet("/addFriends")
public class AndroidAddFriendsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();  //获取out对象

        String friendid=request.getParameter("friendid");
        String userid=request.getParameter("userid");

        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select tel from userinf where tel='"+friendid+"'";     //查找是否有该用户
            ResultSet rs = stmt.executeQuery(sql);

            String friendsSet=null;

            boolean flag=false;        //判断结果是否存在
            while(rs.next()){
                // 通过字段检索
                flag=true;

                sql = "select friendsid from userinf where tel='"+userid+"'"; //查找当前用户，得到好友集合
                ResultSet rs1=stmt.executeQuery(sql);

                while(rs1.next()){

                    friendsSet=rs1.getString("friendsid");



                    if(friendsSet.indexOf(friendid)!=-1){ //表示存在该好友了
                        out.print("friendexsit");
                    }
                    else{
                        System.out.println("t4"+userid);
                        String str=friendsSet+"+++++"+friendid;
                        sql= "update userinf set friendsid='"+str+"' where tel='"+userid+"'"; //更新user的friend字段,添加好友
                        stmt.execute(sql);
                        out.print("success");
                    }
                }
                rs1.close();
            }
            if(!flag){  //用户不存在
                out.print("notexist");
            }
            // 关闭连接
            rs.close();
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
            }catch(SQLException se3){
                se3.printStackTrace();
            }
        }
    }
}
