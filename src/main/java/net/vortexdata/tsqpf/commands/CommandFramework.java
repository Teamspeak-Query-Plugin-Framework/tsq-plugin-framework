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

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.configs.ConfigProject;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.framework.*;

import java.text.*;

/**
 * <p>CommandFramework class.</p>
 *
 * @author Sandro Kierner
 * @since 2.0.0
 * @version $Id: $Id
 */
public class CommandFramework extends CommandInterface {

    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for CommandFramework.</p>
     *
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public CommandFramework(FrameworkContainer frameworkContainer) {
        super(frameworkContainer.getFrameworkLogger());
        this.frameworkContainer = frameworkContainer;
        groups.add(UserGroup.ROOT);
        addAvailableArg("reload", "Reloads the whole framework, including plugins.");
        addAvailableArg("stop/exit/shutdown", "Shuts down the framework.");
        addAvailableArg("crash", "Crashes the framework.");
        addAvailableArg("stats", "Shows some stats about the framework.");
        addAvailableArg("kill", "Kills the framework instantly.");
        setDescription("Controls the framework.");
    }

    /** {@inheritDoc} */
    @Override
    public void execute(String[] args, IShell shell) {


        if (args.length > 0) {


            if (args[0].equalsIgnoreCase("reload")) {
                shell.getPrinter().println("Reloading may cause issues with some plugins. We do not offer support on any issues caused by this action.");
                frameworkContainer.getFramework().reload();
            } else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("exit") || args[0].equalsIgnoreCase("shutdown")) {
                frameworkContainer.getFramework().shutdown();
            } else if (args[0].equalsIgnoreCase("crash")) {
                throw new RuntimeException("Intentional crash of framework, invoked via framework command.");
            } else if (args[0].equalsIgnoreCase("kill")) {
                System.exit(0);
            } else if (args[0].equalsIgnoreCase("stats")) {

                DecimalFormat df = new DecimalFormat("#.##");

                shell.getPrinter().println("========================================================================");
                shell.getPrinter().println("Available cores: \t\t\t" + Runtime.getRuntime().availableProcessors());
                shell.getPrinter().println("Memory usage: \t\t\t\t" + df.format((double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) / 1000) + " mb");
                shell.getPrinter().println("Operating system: \t\t\t" + System.getProperty("os.name"));
                shell.getPrinter().println("OS version: \t\t\t\t" + System.getProperty("os.version"));
                shell.getPrinter().println("------------------------------------------------------------------------");
                shell.getPrinter().println("Java version: \t\t\t\t" + System.getProperty("java.version"));
                shell.getPrinter().println("Java vendor: \t\t\t\t" + System.getProperty("java.vendor"));
                shell.getPrinter().println("------------------------------------------------------------------------");
                shell.getPrinter().println("Framework version: \t\t\t" + frameworkContainer.getConfig(new ConfigProject(frameworkContainer.getFrameworkLogger()).getPath()).getProperty("version"));
                shell.getPrinter().println("Framework vendor: \t\t\t" + frameworkContainer.getConfig(new ConfigProject(frameworkContainer.getFrameworkLogger()).getPath()).getProperty("vendor"));
                shell.getPrinter().println("Framework status: \t\t\t" + frameworkContainer.getFrameworkStatus());
                float uptime = System.currentTimeMillis() - frameworkContainer.getBootHandler().getBootStartTime();
                long uptimeMinutes = (long) (uptime / 1000) / 60;
                long uptimeSeconds = (long) (uptime / 1000) % 60;
                shell.getPrinter().println("Framework uptime: \t\t\t" + uptimeMinutes + " minutes " + uptimeSeconds + " seconds");
                shell.getPrinter().println("========================================================================");
            }
            else {
                shell.getPrinter().println("framework "+ args[0] +": unknown parameters");
                shell.getPrinter().println("Try 'framework <reload | kill | stop | stats>'");
            }


        } else {
            shell.getPrinter().println("framework: missing parameters");
            shell.getPrinter().println("Try 'framework <reload | kill | stop | stats>'");
        }


    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "framework";
    }

}
