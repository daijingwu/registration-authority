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

import net.felsing.client_cert.utilities.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
