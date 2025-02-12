package org.kd4.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import org.kd4.httpserver.util.Json;

public class ConfigurationManager {
  private static ConfigurationManager myConfigurationManager;
  private static Configuration myCurrentConfiguration;
  private ConfigurationManager() {

  }

  public static ConfigurationManager getInstance() {
    if(Objects.isNull(myConfigurationManager)) {
      myConfigurationManager = new ConfigurationManager();
    }
    return myConfigurationManager;
  }

  /**
  * Used to load a configuration file by the path provided
  * */
  public void loadConfigurationFile(String filePath) {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (FileNotFoundException e) {
      throw new HttpConfigurationException(e);
    }
    StringBuffer sb = new StringBuffer();
    int i;
    try {
      while((i=fileReader.read()) != -1){
        sb.append((char)i);
      }
    } catch (IOException e) {
      throw new HttpConfigurationException(e);
    }

    JsonNode conf = null;
    try {
      conf = Json.parse(sb.toString());
    } catch (JsonProcessingException e) {
      throw new HttpConfigurationException("Error in parsing the configuration file", e);
    }
    try {
      myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
    } catch (JsonProcessingException e) {
      throw new HttpConfigurationException("Error in parsing the configuration file, internal", e);
    }
  }

  /**
   * Returns the current loaded configuration
   */
  public Configuration getCurrentConfiguration() {
    if (myCurrentConfiguration == null) {
      throw new HttpConfigurationException("No Current Configuration Set.");
    }
    return myCurrentConfiguration;
  }
}
