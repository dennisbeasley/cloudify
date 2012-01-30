package org.cloudifysource.esc.shell.listner;

import java.util.logging.Logger;

import org.cloudifysource.esc.installer.AgentlessInstallerListener;

public class CliAgentlessInstallerListener extends AbstractEventListener implements AgentlessInstallerListener {

	private Logger logger = Logger.getLogger(CliAgentlessInstallerListener.class.getName());
	
	@Override
	public void onInstallerEvent(String eventName, Object... args) {
		logger.info(getFormattedMessage(eventName, args));
	}
	


}