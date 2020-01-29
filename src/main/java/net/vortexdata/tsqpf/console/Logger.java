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

package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.framework.*;

/**
 * <p>Abstract Logger class.</p>
 *
 * @author TAXSET
 * @version $Id: $Id
 */
public abstract class Logger {

    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for Logger.</p>
     *
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public Logger(FrameworkContainer frameworkContainer) {
        this.frameworkContainer = frameworkContainer;
    }

    /**
     * <p>printInfo.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public void printInfo(String message) {
        frameworkContainer.getRootLogger().info(message);
    }

    /**
     * <p>printDebug.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public void printDebug(String message) {
        frameworkContainer.getRootLogger().debug(message);
    }

    /**
     * <p>printError.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param e a {@link java.lang.Exception} object.
     */
    public void printError(String message, Exception e) {
        frameworkContainer.getRootLogger().error(message, e);
    }

    /**
     * <p>printError.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public void printError(String message) {
        frameworkContainer.getRootLogger().error(message);
    }

    /**
     * <p>printWarn.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param e a {@link java.lang.Exception} object.
     */
    public void printWarn(String message, Exception e) {
        frameworkContainer.getRootLogger().warn(message, e);
    }

    /**
     * <p>printWarn.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public void printWarn(String message) {
        frameworkContainer.getRootLogger().warn(message);
    }

}
