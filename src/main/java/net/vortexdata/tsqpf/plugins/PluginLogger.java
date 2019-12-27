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

package net.vortexdata.tsqpf.plugins;

import net.vortexdata.tsqpf.framework.Framework;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Wrapper class adding prefixes for logging framework
 *
 * @author Michael Wiesinger
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class PluginLogger extends Logger {

    private PluginContainer pluginContainer;

    public PluginLogger(Framework Framework, PluginContainer container) {
        super(Framework.getFrameworkContainer());
        pluginContainer = container;
    }

    /**
     * Logs a message to console or log file on debug level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printDebug(String message) {
        super.printDebug("[" + pluginContainer.getPluginName() + "] " + message);
    }

    /**
     * Logs a message to console or log file on error level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printError(String message) {
        super.printError("[" + pluginContainer.getPluginName() + "] " + message);
    }

    /**
     * Logs a message to console or log file on warning level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printWarn(String message) {
        super.printWarn("[" + pluginContainer.getPluginName() + "] " + message);
    }

    /**
     * Logs a message to console or log file on info level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printInfo(String message) {
        super.printInfo("[" + pluginContainer.getPluginName() + "] " + message);
    }
}
