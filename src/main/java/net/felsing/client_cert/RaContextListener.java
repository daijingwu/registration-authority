package net.felsing.client_cert;

import net.felsing.client_cert.utilities.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by cfadm on 01.05.17.
 *
 */

public class RaContextListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(Servlet.class);

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // do all the tasks that you need to perform just after the server starts
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(PropertyLoader.getProperties().getProperty("shiroini"));
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        logger.info ("Shiro loaded");

        //Notification that the web application initialization process is starting
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        //Notification that the servlet context is about to be shut down.
    }

}
