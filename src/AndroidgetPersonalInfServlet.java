import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/AndroidgetPersonalInfServlet")
public class AndroidgetPersonalInfServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); //获取out对象

        String userid=request.getParameter("userid");

        Connection conn = null;
        Statement stmt = null;

        try{
            // 注册 JDBC 驱动器
            Class.forName("com.mysql.jdbc.Driver");
            // 打开一个连接
            conn = DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select userid,nickname,sa,school,tel,emotion,sign from personalinf where userid='"+userid+"'";
            ResultSet rs = stmt.executeQuery(sql);
            JSONObject obj = new JSONObject();
            boolean flag=false;        //判断是否结果是否存在
            while(rs.next()){
                // 通过字段检索
                flag=true;
                String nickname = rs.getString("nickname");
                String sa = rs.getString("sa");
                String school = rs.getString("school");
                String tel = rs.getString("tel");
                String emotion = rs.getString("emotion");
                String sign = rs.getString("sign");

                obj.put("nickname", nickname);
                obj.put("sa", sa);
                obj.put("school", school);
                obj.put("tel", tel);
                obj.put("emotion", emotion);
                obj.put("sign", sign);
            }
            rs.close();
            if(flag) { //返回数据
                out.print(obj.toString());
            }
            else {
                out.print("notexist");
            }
            // 关闭连接
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
