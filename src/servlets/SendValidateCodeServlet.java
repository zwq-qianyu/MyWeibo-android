package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import config.Config;

@WebServlet("/SendValidateCodeServlet")
public class SendValidateCodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String postUrl = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

        int mobile_code = (int)((Math.random()*9+1)*100000); //获取随机数

        String account = "C68463862"; //查看用户名是登录用户中心->验证码短信->产品总览->APIID
        String password = "f704438ed9910f86e067444f48c143ec";  //查看密码请登录用户中心->验证码短信->产品总览->APIKEY
        String user_id = request.getParameter("user_id");
        System.out.println("phone:"+user_id);
        Config.ValidateCode = String.valueOf(mobile_code);
        String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");
        System.out.println(content);
        try {
            URL url = new URL(postUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);//允许连接提交信息
            connection.setRequestMethod("POST");//网页提交方式“GET”、“POST”
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "Keep-Alive");
            StringBuffer sb = new StringBuffer();
            sb.append("account="+account);
            sb.append("&password="+password);
            sb.append("&mobile="+user_id);
            sb.append("&content="+content);
            OutputStream os = connection.getOutputStream();
            os.write(sb.toString().getBytes());
            os.close();

            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            System.out.println("发送验证码结果："+result);
            in.close();
            PrintWriter out = response.getWriter();
            out.println(Config.ValidateCode);  // 将验证码发送到android端
        } catch (IOException e) {
            System.out.print("发送验证码失败：");
            e.printStackTrace();
        }
    }
}
