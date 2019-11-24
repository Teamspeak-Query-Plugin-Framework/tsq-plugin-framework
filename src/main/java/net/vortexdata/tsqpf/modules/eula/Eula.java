/*
 *
 *  Teamspeak Query Plugin Framework
 *
 *  Copyright (C) 2019 - 2020 VortexdataNET
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package net.vortexdata.tsqpf.modules.eula;

import net.vortexdata.tsqpf.console.*;
import net.vortexdata.tsqpf.exceptions.*;

import java.io.*;

public class Eula {

    private Logger logger;

    public Eula(Logger logger) {
        this.logger = logger;
    }

    public boolean check() throws OutdatedEulaException {

        BufferedReader eulaBr = null;
        BufferedReader frameworkBr = null;

        try {
            InputStream fwBrIn = getClass().getResourceAsStream("/project.properties");
            eulaBr = new BufferedReader(new FileReader("eula.txt"));
            String[] eulaVersion = eulaBr.readLine().split(":")[1].split("\\.");
            frameworkBr = new BufferedReader(new InputStreamReader(fwBrIn));
            while (frameworkBr.ready()) {
                String cLine = frameworkBr.readLine();
                if (cLine.contains("version=")) {
                    String[] version = cLine.split("=")[1].split("\\.");
                    if (!eulaVersion[0].equals(version[0]) || !eulaVersion[1].equals(version[1]) || !eulaVersion[2].equals(version[2].split("-")[0])) {
                        logger.printWarn("Your eula.txt is outdated and will be regenerated.");
                        eulaBr.close();
                        frameworkBr.close();
                        throw new OutdatedEulaException();
                    } else {
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
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
        try {
            InputStream fwBrIn = getClass().getResourceAsStream("/eula.txt");
            BufferedReader eulaBr = new BufferedReader(new InputStreamReader(fwBrIn));
            BufferedWriter eulaBw = new BufferedWriter(new FileWriter("eula.txt", false));

            while (eulaBr.ready()) {
                eulaBw.write(eulaBr.readLine() + "\n");
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
            create();
        }
        return status;
    }

}
