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

import net.vortexdata.tsqpf.authenticator.UserManager;

/**
 * Shell that includes managing the root user account.
 *
 * @author Fabian Gurtner (fabian@profiluefter.me)
 */
public class LocalShell extends Shell implements Runnable {
	private boolean resetRoot;
	private boolean running;
	private Thread thread;

	/**
	 * @param logger    Logger that is used for e.g. the {@link UserManager#UserManager(Logger)}
	 * @param resetRoot Set to true if the root user should be reset.
	 */
	public LocalShell(Logger logger, boolean resetRoot) {
		super("local", System.in, System.out, logger);
		this.resetRoot = resetRoot;
		this.thread = new Thread(this,"LocalShell");
	}

	@Override
	public void run() {
		userManager.reloadUsers();

		logger.printDebug("Looking for root user account...");
		if(!userManager.doesRootUserExist()) {
			userManager.generateRootUser();
		} else {
			logger.printDebug("Root user found.");
			if(resetRoot) {
				userManager.deleteUser("root", true);
				userManager.generateRootUser();
			}
		}

		while(running)
			execute();
	}

	public void start() {
		running = true;
		thread.start();
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		running = false;
	}
}
