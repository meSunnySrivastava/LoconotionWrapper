package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    private static final Properties applicationProperties = new Properties();
    static {
        try(InputStream inputStream = ApplicationProperties.class.
                getClassLoader().getResourceAsStream("application.properties")){
        applicationProperties.load(inputStream);
        }catch(Exception e){
            logger.error("Exception occurred while loading application properties",e);
        }
    }

    public static String get(String property){
       return applicationProperties.getProperty(property);
    }
}
