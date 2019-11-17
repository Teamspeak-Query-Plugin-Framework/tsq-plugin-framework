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
