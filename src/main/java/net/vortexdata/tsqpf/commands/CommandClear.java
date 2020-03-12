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

package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

/**
 * <p>CommandClear class.</p>
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 2.0.0
 * @version $Id: $Id
 */
public class CommandClear extends CommandInterface {

    /**
     * <p>Constructor for CommandClear.</p>
     *
     * @param logger a {@link net.vortexdata.tsqpf.console.Logger} object.
     */
    public CommandClear(Logger logger) {
        super(logger);
        CommandInterface.allowAllGroups(this);
        setDescription("Clears the terminal screen.");
    }

    private static final String clearString = new String(new char[50]).replace('\0','\n');

    /** {@inheritDoc} */
    @Override
    public void execute(String[] args, IShell shell) {
        shell.getPrinter().print(clearString);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "clear";
    }

}
