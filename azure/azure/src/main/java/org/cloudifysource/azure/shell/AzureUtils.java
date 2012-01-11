package org.cloudifysource.azure.shell;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.cloudifysource.azure.AzureDeploymentException;
import org.cloudifysource.azure.AzureSlot;

import com.gigaspaces.cloudify.shell.ShellUtils;
import com.gigaspaces.cloudify.shell.commands.CLIException;

public class AzureUtils {

    // .NET wrapper for azure API commands
    private static final String AZURECONFIG_EXE = "plugins/azure/azureconfig.exe";

    // Helper util for adding certificates to remote desktop
    private static final String ENCUTIL_EXE = "plugins/azure/encutil.exe";
    
    // azure related properties
    private static final String AZURE_PROPERTIES_FILENAME = "plugins/azure/azure.properties";

    private static File toAbsoluteFile(String filepath) {
        File file = new File(filepath);
        if (!file.isAbsolute()) {
            File azureDirectory = ShellUtils.getCliDirectory();
            file = new File(azureDirectory, filepath);
        }
        return file;
    }

    public static File getAzureConfigEXE() {
        return toAbsoluteFile(AZURECONFIG_EXE);
    }
    
    public static File getEncUtilEXE() {
        return toAbsoluteFile(ENCUTIL_EXE);
    }
    
    public static File getAzurePropertiesFile() {
        return toAbsoluteFile(AZURE_PROPERTIES_FILENAME);
    }

    public static Properties getAzureProperties() throws IOException {
        File propertiesFile = getAzurePropertiesFile();
        return ShellUtils.loadProperties(propertiesFile);
    }

    public static String getProperty(Properties properties, String name) {
        String value = properties.getProperty(name);
        if (value == null) {
            throw new IllegalArgumentException(AzureUtils.getAzurePropertiesFile().getAbsolutePath()  + " does not contain property " + name);
        }
        return value;
    }
    
    public static File getFileProperty(Properties properties, String name) throws CLIException {
        String pathname = getProperty(properties,name);
        if (pathname == null || pathname.length() == 0) {
            throw new CLIException("Cannot find property " + name + " in file " + AzureUtils.getAzurePropertiesFile().getAbsolutePath());
        }
        File file = toAbsoluteFile(pathname);
        if (!file.exists()) {
            throw new CLIException(file.getAbsolutePath() + " does not exist.");
        }
        return file;
    }
    
    public static File createTempCscfgFile(String hostedServiceName, AzureSlot slot) throws AzureDeploymentException {
        try {
            File tempFile = File.createTempFile(hostedServiceName + "_"+ slot.getSlot(), ".cscfg");
            tempFile.deleteOnExit();
            return tempFile;
        } catch (IOException e) {
            throw new AzureDeploymentException("Cannot read azure configuration", e);
        }
    }
   
}