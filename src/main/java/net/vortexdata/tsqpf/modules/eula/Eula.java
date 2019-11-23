package net.vortexdata.tsqpf.modules.eula;

import net.vortexdata.tsqpf.console.*;
import net.vortexdata.tsqpf.exceptions.*;

import java.io.*;

public class Eula {

    private Logger logger;
    private boolean isValid;

    public Eula(Logger logger) {
        this.logger = logger;
        isValid = false;
    }

    public boolean check() throws OutdatedEulaException {
        File eulaFile = new File(getClass().getClassLoader().getResource("eula.txt").getFile());
        File frameworkFile = new File(getClass().getClassLoader().getResource("project.properties").getFile());

        BufferedReader eulaBr = null;
        BufferedReader frameworkBr = null;

        try {
            eulaBr = new BufferedReader(new FileReader(eulaFile));
            String[] eulaVersion = eulaBr.readLine().split(":")[1].split("\\.");
            frameworkBr = new BufferedReader(new FileReader(frameworkFile));
            while (frameworkBr.ready()) {
                String cLine = frameworkBr.readLine();
                if (cLine.contains("version=")) {
                    String[] version = cLine.split("=")[1].split("\\.");
                    if (!eulaVersion[0].equals(version[0]) || !eulaVersion[1].equals(version[1]) || !eulaVersion[2].equals(version[2].split("-")[0])) {
                        logger.printWarn("Your eula.txt is outdated and will be regenerated.");
                        eulaBr.close();
                        frameworkBr.close();
                        throw new OutdatedEulaException();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.printError("Failed to get eula.txt resource.");
            return false;
        } catch (IOException e) {
            logger.printError("Failed to read eula.txt resource.");
            return false;
        } finally {
            try {
                if (eulaBr != null)
                    eulaBr.close();
                if (frameworkBr != null)
                    frameworkBr.close();
            } catch (IOException e) {
                logger.printWarn("Failed to close BufferedReaders in Eula class.");
            }
        }

        return true;
    }

    public boolean create() {
        File eulaFile = new File(getClass().getClassLoader().getResource("eula.txt").getFile());
        try {
            BufferedReader eulaBr = new BufferedReader(new FileReader(eulaFile));
            BufferedWriter eulaBw = new BufferedWriter(new FileWriter("eula.txt", false));

            while (eulaBr.ready()) {
                eulaBw.write(eulaBr.readLine());
            }

            eulaBr.close();
            eulaBw.close();
        } catch (IOException e) {
            logger.printError("Failed to read and write eula.txt whilst trying to create it.");
            return false;
        }

        return true;

    }

    public boolean init() throws OutdatedEulaException {
        boolean status = false;
        try {
            if (!check()) {
                logger.printDebug("Failed to find eula.txt, attempting to create one...");
                if (!create()) {
                    logger.printError("Failed to create eula.txt.");
                } else {
                    logger.printDebug("Successfully created eula.txt.");
                    status = true;
                }
            } else {
                logger.printDebug("Up-to-date eula.txt found, creation skipped.");
            }
        } catch (OutdatedEulaException e) {
            throw new OutdatedEulaException();
        }
        return status;
    }

}
