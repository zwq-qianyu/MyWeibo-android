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

@WebServlet("/AndroidHandleLikeServlet")
public class AndroidHandleLikeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //获取out对象

        String weiboid=request.getParameter("weiboid");
        String creatorid=request.getParameter("creatorid");
        String creatorname=request.getParameter("creatorname");
        String time=request.getParameter("time");
        String bylikeid=request.getParameter("bylikeid");


        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select weiboid,creatorname,creatorid,time,bylikeid from liketab where weiboid='"+weiboid+"' and 'creatorid'="+creatorid;
            ResultSet rs = stmt.executeQuery(sql);
            boolean flag=false;        //判断这条点赞记录是否已经存在
            while(rs.next()){
                // 通过字段检索
                flag=true;
//                System.out.println("?????");
            }
            rs.close();
            if(!flag){  //没有这次点赞
                sql ="insert into liketab(weiboid,creatorname,creatorid,time,bylikeid) values"
                        + "('"+weiboid+"','"+creatorname+"','"+creatorid+"','"+time+"','"+bylikeid+"')";
                stmt.execute(sql);
                out.print("liked");
            }
            else {      // 如果已经点赞过了，就取消这次点赞
                sql = "delete from liketab where weiboid='"+weiboid+"' and 'creatorid'="+creatorid;
                stmt.execute(sql);
                out.print("cancel");
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
