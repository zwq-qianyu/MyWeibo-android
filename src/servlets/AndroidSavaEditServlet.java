package servlets;

import config.Config;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// 更新个人信息
//@WebServlet("/AndroidSavaEditServlet")
@WebServlet("/updateUserInfo")
public class AndroidSavaEditServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //获取out对象

        String userid=request.getParameter("userid").replaceAll("@@@@@@"," ");
        String nickname=request.getParameter("nickname").replaceAll("@@@@@@"," ");
        String sa=request.getParameter("sa").replaceAll("@@@@@@"," ");
        String school=request.getParameter("school").replaceAll("@@@@@@"," ");
        String tel=request.getParameter("tel").replaceAll("@@@@@@"," ");
        String emotion=request.getParameter("emotion").replaceAll("@@@@@@"," ");
        String sign=request.getParameter("sign").replaceAll("@@@@@@"," ");

        Connection conn = null;
        Statement stmt = null;

        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select userid,nickname,sa,school,tel,emotion,sign from personalinf where userid='"+userid+"'";
            ResultSet rs = stmt.executeQuery(sql);
            JSONObject obj=new JSONObject();
            boolean flag=false;        //判断是否结果是否存在
            while(rs.next()){
                // 通过字段检索
                flag=true;
            }
            rs.close();
            if(flag) { //返回数据
                //更新personalinf表
                sql="update personalinf set nickname='"+nickname+"',sa='"+sa+"',school='"+
                        school+"',tel='"+tel+"',emotion='"+emotion+"',sign='"+sign+"' where userid='"+userid+"'";
                stmt.execute(sql);

                //更新userinf表
                sql="update userinf set nickname='"+nickname+"' where tel='"+userid+"'";
                stmt.execute(sql);
                out.print("success");
            }
            else {
                out.print("unsuccess");
            }

            // 完成后关闭连接
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
