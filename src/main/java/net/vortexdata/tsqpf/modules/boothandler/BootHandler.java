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

package net.vortexdata.tsqpf.modules.boothandler;

/**
 * Records statistics during boot process
 *
 * @author Sandro Kierner
 * @since 1.0.0
 * @version $Id: $Id
 */
public class BootHandler {

    private long bootStartTime = 0;
    private long bootEndTime = 0;

    /**
     * Sets the timestamp of when the boot process started.
     */
    public void setBootStartTime() {
        bootStartTime = System.currentTimeMillis();
    }

    /**
     * Sets the timestamp of when the boot process finished.
     */
    public void setBootEndTime() {
        bootEndTime = System.currentTimeMillis();
    }

    /**
     * <p>getBootTime.</p>
     *
     * @return Returns the total time that passed during boot
     */
    public float getBootTime() {
        return bootEndTime - bootStartTime;
    }

    /**
     * <p>Getter for the field <code>bootStartTime</code>.</p>
     *
     * @return a float.
     */
    public float getBootStartTime() {
        return bootStartTime;
    }

}
