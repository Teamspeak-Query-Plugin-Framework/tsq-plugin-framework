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

package net.vortexdata.tsqpf.configs;

import net.vortexdata.tsqpf.console.*;

import java.util.HashMap;

/**
 * ConfigMain class
 *
 * @author Sandro Kierner
 * @since 1.0.0
 * @version $Id: $Id
 */
public class ConfigMain extends Config {

    /**
     * <p>Constructor for ConfigMain.</p>
     */
    public ConfigMain(Logger logger) {
        super("configs//main.properties", logger);
        // Setting Default Values
        setDefaultValue("serverAddress", "127.0.0.1", CheckType.STRING);
        setDefaultValue("queryPort", "10011", CheckType.INTEGER);
        setDefaultValue("queryUser", "serveradmin", CheckType.STRING);
        setDefaultValue("queryPassword", "password", CheckType.STRING);
        setDefaultValue("virtualServerId", "1", CheckType.INTEGER);
        setDefaultValue("clientNickname", "TSQP Framework", CheckType.STRING);
        setDefaultValue("reconnectStrategy", "exponentialBackoff", CheckType.STRING);
        setDefaultValue("remoteShellPort", "12342", CheckType.INTEGER);
        setDefaultValue("heartbeatPort", "12343", CheckType.INTEGER);
        setDefaultValue("enableRemoteShell", "false", CheckType.BOOLEAN);
        setDefaultValue("enableHeartbeat", "false", CheckType.BOOLEAN);
        setDefaultValue("acceptEula", "false", CheckType.BOOLEAN);
        setDefaultValue("enableExceptionReporting", "true", CheckType.BOOLEAN);
        setDefaultValue("floodRate", "default", CheckType.STRING);
    }

}
