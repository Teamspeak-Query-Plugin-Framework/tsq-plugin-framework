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

import net.vortexdata.tsqpf.authenticator.*;
import net.vortexdata.tsqpf.console.*;
import net.vortexdata.tsqpf.framework.*;
import net.vortexdata.tsqpf.plugins.PluginContainer;
import net.vortexdata.tsqpf.plugins.PluginManager;

/**
 * <p>CommandPlugins class.</p>
 *
 * @author Sandro Kierner
 * @since 2.0.0
 * @version $Id: $Id
 */
public class CommandPlugins extends CommandInterface {

    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for CommandPlugins.</p>
     *
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public CommandPlugins(FrameworkContainer frameworkContainer) {
        super(frameworkContainer.getFrameworkLogger());
        groups.add(UserGroup.ROOT);
    }

    /** {@inheritDoc} */
    @Override
    public String getHelpMessage() {
        return "Lists all loaded plugins.";
    }

    /** {@inheritDoc} */
    @Override
    public void execute(String[] args, IShell shell) {

        shell.getPrinter().println("Loaded plugins: ");
        for (PluginContainer pc : PluginManager.getLoadedPlugins()) {
            shell.getPrinter().println(pc.getPluginName());
        }

    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "plugins";
    }

}
