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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class RaContextListener implements ServletContextListener {
    private static Logger logger = LoggerFactory.getLogger(RaContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        String shiroIniFile = PropertyLoader.getProperties().getProperty("shiroini");
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(shiroIniFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        logger.info("Shiro loaded, using " + shiroIniFile);
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {


    }

} // class
