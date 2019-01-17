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

// 写微博
//@WebServlet("/AndroidWriteWeiboServlet")
@WebServlet("/writeWeibo")
public class AndroidWriteWeiboServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //获取out对象


        String weiboid=request.getParameter("weiboid");
        String userid=request.getParameter("userid");
        String str=request.getParameter("content");
        //String str1=str.replaceAll(".####.", "\n");
        String content=str.replaceAll(".@@@@.", " ");

        String date=request.getParameter("date");
        String weiboURL=request.getParameter("weiboURL");
        String commentInf=request.getParameter("commentInf");   // 评论
        String likeInf=request.getParameter("likeInf");  // 点赞
        String username=request.getParameter("username");


        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;

            sql ="insert into weibo values('"+weiboid+"','"+content+"','"+date+"','"+username+"','"+likeInf+"','"+commentInf+"','"+weiboURL+"','"+userid+"')";
            stmt.execute(sql);

            out.print("writeweibosuccess");

            // 完成后关闭
            //rs.close();
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
