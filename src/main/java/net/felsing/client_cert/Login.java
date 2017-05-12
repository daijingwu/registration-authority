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
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;



@WebServlet(urlPatterns = "/login", loadOnStartup = 1)
public class Login extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);
    private boolean servletIsReady = false;


    private void cleanUp (Session sessionId) {
        DefaultSecurityManager securityManager = (DefaultSecurityManager) SecurityUtils.getSecurityManager();
        DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
        Collection<Session> activeSessions = sessionManager.getSessionDAO().getActiveSessions();
        for (Session session: activeSessions){
            if (sessionId.equals(session.getId())) {
                session.stop();
            }
        }
    }


    private void session() {
        Session session = SecurityUtils.getSubject().getSession(true);
        if (session!=null) {
            final String someKey = "someKey";
            if (session.getAttribute(someKey) != null) {
                String v = session.getAttribute(someKey).toString();
                logger.debug("get session variable " + someKey + ": " + v);
            } else {
                session.setAttribute(someKey, "blahfaseltest");
                logger.debug("set session variable " + someKey);
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;

        try {
            logger.info("User " + SecurityUtils.getSubject().getPrincipal().toString() + " logged out");
            SecurityUtils.getSubject().logout();
            cleanUp(SecurityUtils.getSubject().getSession());
        } catch (Exception e) {
            // don't care about exception
            logger.debug("Called logout even user was not logged in");
        }
        resp.sendRedirect("./");
    }


    private void login(HttpServletRequest req, HttpServletResponse resp, String username, String password) throws IOException {
        // let run shiro into exception if username or password is empty
        // org.apache.shiro.realm.activedirectory.ActiveDirectoryRealm authenticates otherwise
        if (username.matches("^$") || password.matches("^$")) {
            resp.sendRedirect("failed.jsp");
            return;
        }

        try {
            AuthenticationToken token =new UsernamePasswordToken(username, password);
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            session();
            logger.info("Authentication succeeded [" + username + "]");
            req.getRequestDispatcher("/").forward(req, resp);
        } catch (AuthenticationException e) {
            logger.info("Authentication failed [" + username + "]");
            logger.debug("Exception", (Object[]) e.getStackTrace());
            resp.sendRedirect("failed.jsp");
        } catch (Exception e) {
            resp.sendError(500, "Internal error");
            logger.error("Stack trace", (Object[]) e.getStackTrace());
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;

        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        session();
        PrintWriter pw = resp.getWriter();

        if (!SecurityUtils.getSubject().isAuthenticated()) {

            String username = req.getParameter(Constants.formUsername);
            String password = req.getParameter(Constants.formPassword);

            logger.debug("Method: " + req.getMethod());
            logger.debug("Username: " + username);
            if (password != null) {
                logger.debug("Password: ***");
            } else {
                logger.debug("Password is null");
            }

            login(req, resp, username, password);
        } else {
            logger.error("Login called even user is already logged in");
            pw.print(new JsonPrimitive(Constants.unsupportedRequest));
        }

        pw.flush();
    }


    @Override
    public void init() throws ServletException {
        super.init();
        servletIsReady = true;
    }

} // class
