package jack.smartbi.servlet;

import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Log4jServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Log4jServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        //从web.xml配置读取，名字一定要和web.xml配置一致
        String file = this.getInitParameter("log4j");
        if (file != null) {
            String path = this.getServletContext().getRealPath(file);
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PropertyConfigurator.configure(resourceAsStream);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
