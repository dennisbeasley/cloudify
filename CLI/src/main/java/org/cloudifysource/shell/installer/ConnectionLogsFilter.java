package org.cloudifysource.shell.installer;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The purpose of this class is to suppress communication error while the agent is being bootstrapped or teared down.
 * @author itaif
 *
 */
public class ConnectionLogsFilter {

	// overriding logger filters is very tricky, since the Logger.getLogger() may return a different instance
	// than the one used by XAP.
	// therefore Logger.getLogger must be called during static initialization for it to work.
	private static Logger[] loggers = new Logger[] {
		Logger.getLogger("net.jini.discovery.LookupDiscovery"),
		Logger.getLogger("net.jini.discovery.LookupLocatorDiscovery"),
		Logger.getLogger("net.jini.lookup.ServiceDiscoveryManager"),
		Logger.getLogger("com.gigaspaces.lrmi.nio")
	};

	private final Filter[] filters;
		
	public ConnectionLogsFilter() {
		filters = new Filter[loggers.length];
		for (int i = 0 ; i < loggers.length ; i++) {
			filters[i] = loggers[i].getFilter();
		}
	}

	public void supressConnectionErrors() {
		for (int i = 0 ; i < loggers.length ; i++) {
			supressConnectionErrors(loggers[i],filters[i]);
		}
	}
	
	private void supressConnectionErrors(final Logger logger, final Filter filter) {

		Filter newFilter = new Filter() {
			@Override
			public boolean isLoggable(LogRecord record) {
				boolean isLoggable = true;
				
				Throwable t = record.getThrown();
				if ((filter != null && 
					!filter.isLoggable(record)) ||
					
					(t != null && isConnectExceptionOrCause(t) &&
					record.getLevel().intValue() <= Level.WARNING.intValue())) {
					 
					isLoggable = false;
				}
				
				return isLoggable;
			}

			private boolean isConnectExceptionOrCause(Throwable t) {
				while (t != null) {
					if (isConnectException(t)) {
						return true;
					}
					t= t.getCause();
				}
				
				return false;
			}
			private boolean isConnectException(Throwable t) {
				return (t instanceof java.net.ConnectException) ||
					   (t instanceof java.rmi.ConnectException);
			}
		};
		
		logger.setFilter(newFilter);
	}
	
	void restoreConnectionErrors() {
		for (int i = 0 ; i < loggers.length ; i++) {
			loggers[i].setFilter(filters[i]);
		}
	}
	
}