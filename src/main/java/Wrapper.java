import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Wrapper {

    private final static Logger logger = LoggerFactory.getLogger(Wrapper.class);
    public static void main(String[] args) {
        try {
            new RedundantImageCleaner().cleanRedundantImagesFromAssetFolder();
        } catch (Exception e) {
            logger.error("Exception Occurred while running the wrapper",e);
        }
    }
}
