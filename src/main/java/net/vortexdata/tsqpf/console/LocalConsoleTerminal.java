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

import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.framework.*;

import java.util.Scanner;

@Deprecated
/**
 * <p>LocalConsoleTerminal class.</p>
 *
 * @author TAXSET
 * @version $Id: $Id
 */
public class LocalConsoleTerminal implements VirtualTerminal {

    private Logger logger;
    private Scanner scanner;
    private LocalConsole localConsole;

    /**
     * <p>Constructor for LocalConsoleTerminal.</p>
     *
     * @param user a {@link net.vortexdata.tsqpf.authenticator.User} object.
     * @param localConsole a {@link net.vortexdata.tsqpf.console.LocalConsole} object.
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public LocalConsoleTerminal(User user, LocalConsole localConsole, FrameworkContainer frameworkContainer) {
        logger = frameworkContainer.getFrameworkLogger();
        scanner = new Scanner(System.in);
        this.localConsole = localConsole;
    }

    /** {@inheritDoc} */
    @Override
    public void println(String msg) {
        System.out.println(msg);
    }

    /** {@inheritDoc} */
    public void print(String msg) {
        System.out.print(msg);
    }

    /** {@inheritDoc} */
    @Override
    public String readln() {
        return scanner.nextLine();
    }

    /** {@inheritDoc} */
    @Override
    public void logout() {
        localConsole.logout();
    }

}
