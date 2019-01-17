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

// 登录  ok
//@WebServlet("/AndroidLoginServlet")
@WebServlet("/login")
public class AndroidLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String tel = request.getParameter("tel");
        String passwd = request.getParameter("passwd");

        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select tel,password,nickname from userinf where tel='"+tel+"'";
            ResultSet rs = stmt.executeQuery(sql);
            PrintWriter out = response.getWriter();

            boolean flag=false;        //判断结果是否存在
            while(rs.next()){
                // 通过字段检索
                flag=true;
                String sqlpasswdString=rs.getString("password");
                if(sqlpasswdString.equals(passwd)){
                    String nikename=rs.getString("nickname");
                    out.print(nikename);  //密码正确
                }
                else{
                    out.print("wrongpassword");  //密码正确
                }

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
