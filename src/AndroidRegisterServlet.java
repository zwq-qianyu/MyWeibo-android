import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "AndroidRegisterServlet")
public class AndroidRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "123456";

    public AndroidRegisterServlet(){
        super();
    }

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
            Class.forName("com.mysql.jdbc.Driver");
            // 打开一个连接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select tel from userinf  where tel='"+tel+"'";
            ResultSet rs = stmt.executeQuery(sql);
        }catch(SQLException se){

        }catch(Exception e){

        }finally {

        }
    }
}
