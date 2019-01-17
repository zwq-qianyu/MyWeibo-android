import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/AndroidPostCommentServlet")
public class AndroidPostCommentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //获取out对象

        String comid=request.getParameter("commenID");
        String creatorName=request.getParameter("creatorName");
        String creatorID=request.getParameter("creatorID");
        String str=request.getParameter("content");
        String content=str.replaceAll(".@@@@.", " ");

        String createTime=request.getParameter("createTime");
        String weiboID=request.getParameter("weiboID");
        String bycomid=request.getParameter("bycomid");

        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql ="insert into commentab(comid,creatorname,creatorid,content,time,weiboid,bycomid) values('"+comid+"','"+creatorName+"','"+creatorID+
                    "','"+content+"','"+createTime+"','"+weiboID+"','"+bycomid+"')";
            stmt.execute(sql);
            out.print("success");



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
            }catch(SQLException se3){
                se3.printStackTrace();
            }
        }
    }
}
