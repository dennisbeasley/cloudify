package org.cloudifysource.usm.jmx;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import com.gigaspaces.cloudify.dsl.Plugin;
import com.gigaspaces.cloudify.dsl.context.ServiceContext;


public abstract class AbstractJmxPlugin implements Plugin{
	private static java.util.logging.Logger logger =
			java.util.logging.Logger.getLogger(AbstractJmxPlugin.class.getName());
	
	//protected List<JmxTarget> targets = new LinkedList<JmxTarget>();
	protected List<JmxAttribute> targets = new LinkedList<JmxAttribute>();
	
	protected String host = "127.0.0.1";
	protected int port;
	protected JmxGenericClient client;
	
	protected String username;
	protected String password;

	public int getPort() {
		return port;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public void setConfig(final Map<String, Object> config) {
	
		final Set<Entry<String, Object>> entries = config.entrySet();
		for (final Entry<String, Object> entry : entries) {
			try {
	
				if (entry.getKey().equalsIgnoreCase("port")) {
					this.setPort((Integer) entry.getValue());
				} else if (entry.getKey().equalsIgnoreCase("host")) {
					this.setHost((String) entry.getValue());
				} else if (entry.getKey().equalsIgnoreCase("username")) {
					this.username = (String) entry.getValue();
				} else if (entry.getKey().equalsIgnoreCase("password")) {
					this.password = (String) entry.getValue();
				} else {
					
					List<String> list = (List<String>) entry.getValue();
					JmxAttribute att = new JmxAttribute(list.get(0), list.get(1), entry.getKey());
					this.targets.add(att);
					//final JmxTarget target = JmxTargetParser.parse((String) entry.getValue());
					//target.setDispName(entry.getKey());
					//this.targets.add(target);
	
				}
				
			} catch (final Exception e) {
				logger.log(Level.SEVERE,
						"Failed to process Jmx Configuration entry: " + entry.getKey() + "=" + entry.getValue());
			}
		}
	
	}

	public List<JmxAttribute> getTargets() {
		return targets;
	}

	public void setTargets(final List<JmxAttribute> targets) {
		this.targets = targets;
	}

	protected Map<String, Object> getJmxAttributes() {
		if (this.client == null) {
			client = new JmxGenericClient();
			client.setHost(this.host);
			client.setPort(this.port);
			client.setTargets(this.targets);
			// client.getAttrs(); // TODO - test this
		}
	
		return client.getAttributes();
	}
	
	@Override
	public void setServiceContext(ServiceContext context) {	
		// ignore
	}
	

}