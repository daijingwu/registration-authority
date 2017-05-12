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
