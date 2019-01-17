package servlets;

import config.Config;
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

@WebServlet("/AndroidGetCommentListServlet")
public class AndroidGetCommentListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //获取out对象

        String requestweiboid=request.getParameter("weiboid");

        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select comid,creatorname,creatorid,content,time,weiboid,bycomid from commentab where weiboid='"+requestweiboid+"' order by time desc";
            ResultSet rs = stmt.executeQuery(sql);

            JSONObject objects=new JSONObject();
            JSONArray arr=new JSONArray();
            JSONObject obj=null;

            boolean flag=false;        //判断结果是否存在
            while(rs.next()){
                // 通过字段检索
                flag=true;
                obj=new JSONObject();
                String comid=rs.getString("comid");
                String creatorname=rs.getString("creatorname");
                String creatorid=rs.getString("creatorid");
                String content=rs.getString("content");
                String time=rs.getString("time");
                String weiboid=rs.getString("weiboid");
                String bycomid=rs.getString("bycomid");

                obj.put("comid", comid);
                obj.put("creatorname", creatorname);
                obj.put("creatorid", creatorid);
                obj.put("content", content);
                obj.put("time", time);
                obj.put("weiboid", weiboid);
                obj.put("bycomid", bycomid);

                arr.add(obj);
            }
            if(flag){  // 有评论
                objects.put("comments", arr);
                out.print(objects.toString());
            }
            else {
                out.print("nocomment");
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
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
