import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/AndroidRegisterServlet")
public class AndroidRegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();     //获取out对象
        String tel = request.getParameter("tel"); //获取手机号
        String password = request.getParameter("password");  //获取密码
        String nickname = request.getParameter("nickname");  //获取姓名
        System.out.println(tel);
        System.out.println(password);

        String sqltel=null;  //返回的电话号码查询语句
        Connection conn = null;
        Statement stmt = null;

        try {
            // 注册 JDBC 驱动器
            Class.forName(Config.JDBC_DRIVER);
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select tel from userinf  where tel='"+tel+"'";
            ResultSet rs = stmt.executeQuery(sql);

            boolean flag=false;        //判断该用户（查询结果）是否存在
            while(rs.next()){
                // 通过字段检索，如果查到，就置为true
                flag=true;
            }
            if(flag){  //用户名已存在
                out.print("no");
            }
            else{  //用户不存在，插入这条记录，即注册
                sql ="insert into userinf(tel,password,friendsid,likeid,commentid,picurl,nickname) values('"+tel+"','"+password+"','','','','','"+nickname+"')";
                stmt.execute(sql);
                sql="insert into personalinf(userid,nickname,sa,school,tel,emotion,sign) values('"+
                        tel+"','"+nickname+"','','','','','')";
                stmt.execute(sql);
                out.print("yes");
            }
            // 完成后关闭连接
            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally {
            // 关闭资源的块
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                // 处理 JDBC 错误
                se2.printStackTrace();
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
