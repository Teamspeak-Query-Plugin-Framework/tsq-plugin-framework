package net.vortexdata.tsqpf.configs;

/*
 * This code is part of the Teamspeak Query Plugin Framework,
 * developed and maintained by VortexdataNET.
 *
 * Copyright (C) 2019 VortexdataNET
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

import java.util.HashMap;

/**
 * ConfigMain class
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class ConfigMain extends Config {

    public ConfigMain() {
        super("configs//main.properties");
        // Creating HashMaps
        defaultValues = new HashMap<String, String>();
        values = new HashMap<String, String>();
        // Setting Default Values
        defaultValues.put("serverAddress", "127.0.0.1");
        defaultValues.put("queryPort", "10011");
        defaultValues.put("queryUser", "serveradmin");
        defaultValues.put("queryPassword", "password");
        defaultValues.put("virtualServerId", "1");
        defaultValues.put("clientNickname", "TSQP Framework");
        defaultValues.put("reconnectStrategy", "exponentialBackoff");
        defaultValues.put("remoteShellPort", "12342");
        defaultValues.put("heartbeatPort", "12343");
        defaultValues.put("enableRemoteShell", "true");
        defaultValues.put("enableHeartbeat", "true");
        defaultValues.put("acceptEula", "false");
    }

}