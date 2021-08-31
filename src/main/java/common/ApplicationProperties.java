package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    public static String imageFolder;
    public static String htmlFolder;
    public static String locoRepo;
    public static String locoBranch;

    static {
        try(InputStream inputStream = ApplicationProperties.class.
                getClassLoader().getResourceAsStream("application.properties")){
        Properties applicationProperties = new Properties();
        applicationProperties.load(inputStream);
        imageFolder = applicationProperties.getProperty("image-folder");
        htmlFolder = applicationProperties.getProperty("html-folder");
        locoRepo = applicationProperties.getProperty("loco-repo");
        locoBranch = applicationProperties.getProperty("loco-branch");
        }catch(Exception e){
            logger.error("Exception occurred while loading application properties during",e);
        }
    }
}
