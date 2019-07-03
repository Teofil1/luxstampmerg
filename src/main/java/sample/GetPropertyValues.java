package sample;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.*;
import java.util.Properties;

public class GetPropertyValues {

    String propFileName = "src/main/resources/config.properties";

    public String getValueFromProperies(String nameValues)  {
        String value = "";
        try {
            InputStream input = new FileInputStream(propFileName);
            Properties prop = new Properties();
            prop.load(input);
            value = prop.getProperty(nameValues);
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public void setValueFromProperies(String nameValues,Double value) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(propFileName);
            config.setProperty(nameValues, value.toString());
            config.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void setLastUsedCoefficientStampsPositionX(Double value) throws IOException {
        setValueFromProperies("last_used_coefficient_for_zero_position_X_stamps", value);
    }

    public void setLastUsedCoefficientStampsPositionY(Double value) throws IOException {
        setValueFromProperies("last_used_coefficient_for_zero_position_Y_stamps", value);
    }

    public void setLastUsedWidthStamp(Double value) throws IOException {
        setValueFromProperies("last_used_width_stamp", value);
    }

    public Double getDefaultCoefficientStampsPositionX() throws IOException {
        return Double.valueOf(getValueFromProperies("default_coefficient_for_zero_position_X_stamps"));
    }

    public Double getDefaultCoefficientStampsPositionY() throws IOException {
        return Double.valueOf(getValueFromProperies("default_coefficient_for_zero_position_Y_stamps"));
    }

    public Double getDefaultWidthStamp() throws IOException {
        return Double.valueOf(getValueFromProperies("deafult_width_stamp"));
    }

    public Double getLastUsedCoefficientStampsPositionX() throws IOException {
        return Double.valueOf(getValueFromProperies("last_used_coefficient_for_zero_position_X_stamps"));
    }

    public Double getLastUsedCoefficientStampsPositionY() throws IOException {
        return Double.valueOf(getValueFromProperies("last_used_coefficient_for_zero_position_Y_stamps"));
    }

    public Double getLastUsedWidthStamp() throws IOException {
        return Double.valueOf(getValueFromProperies("last_used_width_stamp"));
    }






}
