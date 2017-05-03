package net.felsing.client_cert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(urlPatterns = "/login", loadOnStartup = 1)
public class Login extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Login.class);
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtils.getSubject().logout();
        req.getRequestDispatcher("/").forward(req,resp);
    }

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();

        String username = req.getParameter("loginusername");
        String password = req.getParameter("loginpassword");

        logger.debug("Method: " + req.getMethod());
        logger.debug("Username: " + username);
        if (password!=null) {
            logger.debug("Password: ***");
        } else {
            logger.debug("Password is null");
        }

        try {
            AuthenticationToken token = new UsernamePasswordToken(username, password);
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            logger.info ("Authentication succeeded [" + username + "]");
            req.getRequestDispatcher("/").forward(req,resp);
        } catch (AuthenticationException e) {
            pw.println("Authentication failed");
            logger.info ("Authentication failed [" + username + "]");
        } catch (Exception e) {
            pw.println("Internal Error, not logged in");
            logger.error(e.getStackTrace());
        }
        pw.flush();
    }

    
    @Override
    public void init() throws ServletException {
        super.init();
    }
} // class
