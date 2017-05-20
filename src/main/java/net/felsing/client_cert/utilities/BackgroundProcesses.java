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

package net.felsing.client_cert.utilities;


import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BackgroundProcesses extends Thread {
    private static Logger logger = LoggerFactory.getLogger(BackgroundProcesses.class);
    private volatile boolean running = true;
    private static Properties properties;
    private static boolean isReady;
    private static boolean ejbcaIsRunning;


    public BackgroundProcesses() {
        isReady = false;
        setName("BackgroundProcesses");
        properties = PropertyLoader.getProperties();
    }


    public void terminate() {
        running = false;
        interrupt();
        while (isAlive()) {
            try {
                join(Constants.threadJoinTimeout);
            } catch (InterruptedException e) {
                logger.warn("terminateWatchdog: " + e.getMessage());
            }
        }
    }


    @Override
    public void run() {
        logger.info("Loading background processes");
        while (running) {
            checkEjbca();
            try {
                Thread.sleep (Constants.threadInterval);
                isReady = true;
            } catch (InterruptedException e) {
                isReady = false;
                interrupt();
            }
        }
        logger.info("Background tasks terminated normally");
    }


    public boolean isReady() {
        return isReady;
    }


    private void checkEjbca() {
        try {
            EjbcaToolBox ejbcaToolBox = new EjbcaToolBox(properties);
            JsonObject jsonObject = ejbcaToolBox.getAvailableCas();
            ejbcaIsRunning = !jsonObject.isJsonNull();
        } catch (Exception e) {
            ejbcaIsRunning = false;
        }

        logger.debug("EJBCA is running: " + Boolean.toString(ejbcaIsRunning));
    }


    public static boolean isEjbcaRunning () {

        return ejbcaIsRunning;
    }

} // class
