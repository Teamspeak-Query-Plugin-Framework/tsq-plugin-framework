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
 * <p>ConfigMessages class.</p>
 *
 * @author TAXSET
 * @since 2.0.0
 *
 * This config contains all framework messages.
 * @version $Id: $Id
 */
public class ConfigMessages extends Config {

    /**
     * <p>Constructor for ConfigMessages.</p>
     */
    public ConfigMessages(Logger logger) {
        super("configs//messages.properties", logger);
        // Setting Default Values
        setDefaultValue("chatCommandUnknown", "Command not found.", CheckType.STRING);
        setDefaultValue("shellMotd", "You are connected to the TSQPF remote shell. UNAUTHORIZED ACCESS IS PROHIBITED!", CheckType.STRING);
    }

}
