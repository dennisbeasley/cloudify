package org.cloudifysource.dsl.cloud;

import java.util.HashMap;
import java.util.Map;

import org.cloudifysource.dsl.internal.CloudifyDSLEntity;


/**
 * @author barakme
 *
 */
@CloudifyDSLEntity(name = "template", clazz = CloudTemplate.class, allowInternalNode = true, allowRootNode = false, parent = "cloud")
public class CloudTemplate {

	private String imageId;
    private int machineMemoryMB;
    private String hardwareId;
    private String locationId;
    
    private int numberOfCores = 1;
    
    
    private Map<String, Object> options = new HashMap<String, Object>(); 
    private Map<String, Object> overrides = new HashMap<String, Object>();
    
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public int getMachineMemoryMB() {
		return machineMemoryMB;
	}
	public void setMachineMemoryMB(int machineMemoryMB) {
		this.machineMemoryMB = machineMemoryMB;
	}
	public String getHardwareId() {
		return hardwareId;
	}
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public int getNumberOfCores() {
		return numberOfCores;
	}
	public void setNumberOfCores(int numberOfCores) {
		this.numberOfCores = numberOfCores;
	}
	public Map<String, Object> getOptions() {
		return options;
	}
	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}
	@Override
	public String toString() {
		return "CloudTemplate [imageId=" + imageId + ", machineMemoryMB=" + machineMemoryMB + ", hardwareId="
				+ hardwareId + ", locationId=" + locationId + ", numberOfCores=" + numberOfCores + ", options="
				+ options + ", overrides=" + overrides + "]";
	}
	
	public Map<String, Object> getOverrides() {
		return overrides;
	}
	public void setOverrides(Map<String, Object> overrides) {
		this.overrides = overrides;
	}

	
}