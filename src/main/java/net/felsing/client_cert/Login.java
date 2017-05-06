/*
 * Copyright (c) 2016. by Christian Felsing
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.felsing.client_cert;

import com.google.gson.JsonPrimitive;
import net.felsing.client_cert.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;



@WebServlet(urlPatterns = "/login", loadOnStartup = 1)
public class Login extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);
    private boolean servletIsReady = false;


    private void session () {

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;
        session();
        try {
            logger.info("User " + SecurityUtils.getSubject().getPrincipal().toString() + " logged out");
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            // don't care about exception
            logger.debug("Called logout even user was not logged in");
        }
        resp.sendRedirect("./");
    }

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;
        session();
        PrintWriter pw = resp.getWriter();

        if (!SecurityUtils.getSubject().isAuthenticated()) {

            String username = req.getParameter("loginusername");
            String password = req.getParameter("loginpassword");

            logger.debug("Method: " + req.getMethod());
            logger.debug("Username: " + username);
            if (password != null) {
                logger.debug("Password: ***");
            } else {
                logger.debug("Password is null");
            }

            try {
                AuthenticationToken token = new UsernamePasswordToken(username, password);
                Subject currentUser = SecurityUtils.getSubject();
                currentUser.login(token);
                Session session = currentUser.getSession();
                session.setAttribute("someKey", "blahfaseltest");
                logger.info("Authentication succeeded [" + username + "]");
                req.getRequestDispatcher("/").forward(req, resp);
            } catch (AuthenticationException e) {
                logger.info("Authentication failed [" + username + "]");
                logger.debug ("Exception", (Object[]) e.getStackTrace());
                resp.sendRedirect("failed.jsp");
            } catch (Exception e) {
                pw.println("Internal Error, not logged in");
                logger.error ("Stack trace", (Object[]) e.getStackTrace());
            }
        } else {
            logger.error("Login called even user is already logged in");
            pw.print(new JsonPrimitive(Constants.unsupportedRequest));
        }
        
        pw.flush();
    }

    
    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Servlet ready for service");
        servletIsReady = true;
    }

} // class
