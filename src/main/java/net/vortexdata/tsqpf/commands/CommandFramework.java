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

import net.vortexdata.tsqpf.*;
import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

public class CommandFramework extends CommandInterface {

    private FrameworkContainer frameworkContainer;

    public CommandFramework(FrameworkContainer frameworkContainer) {
        super(frameworkContainer.getFrameworkLogger());
        this.frameworkContainer = frameworkContainer;
        groups.add(UserGroup.ROOT);
    }

    @Override
    public String getHelpMessage() {
        return "Controls framework operations.";
    }

    @Override
    public void execute(String[] args, IShell shell) {


        if (args.length > 0) {


            if (args[0].equalsIgnoreCase("reload")) {
                frameworkContainer.getFramework().hibernate();
                frameworkContainer.getFramework().wakeup(frameworkContainer.getTs3Query());
            } else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("exit") || args[0].equalsIgnoreCase("shutdown")) {
                frameworkContainer.getFramework().shutdown();
            } else if (args[0].equalsIgnoreCase("kill")) {
                System.exit(0);
            } else if (args[0].equalsIgnoreCase("stats")) {
                shell.getPrinter().println("Available cores: \t\t\t" + Runtime.getRuntime().availableProcessors());
                shell.getPrinter().println("Memory usage: \t\t\t\t" + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()));
                shell.getPrinter().println("Operating system: \t\t\t" + System.getProperty("os.name"));
                shell.getPrinter().println("OS version: \t\t\t\t" + System.getProperty("os.version"));
                shell.getPrinter().println("Java version: \t\t\t\t" + System.getProperty("java.version"));
                shell.getPrinter().println("Java vendor: \t\t\t\t" + System.getProperty("java.vendor"));
            } else {
                shell.getPrinter().println("framework "+ args[0] +": unknown parameters");
                shell.getPrinter().println("Try 'framework <reload | kill | stop | stats>'");
            }


        } else {
            shell.getPrinter().println("framework: missing parameters");
            shell.getPrinter().println("Try 'framework <reload | kill | stop | stats>'");
        }


    }

    @Override
    public String getName() {
        return "framework";
    }

}
