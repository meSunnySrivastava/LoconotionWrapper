import common.ApplicationProperties;
import customization.RuleParser;
import customization.RuleRunner;
import image.RedundantImageCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.SyncRunner;

public class Wrapper {

    private final static Logger logger = LoggerFactory.getLogger(Wrapper.class);
    public static void main(String[] args) {
        try {
            new SyncRunner(ApplicationProperties.get("index")).createWebsite();

            new RedundantImageCleaner().cleanRedundantImagesFromAssetFolder();

            new RuleRunner(ApplicationProperties.get("htmlFolder"))
            .runCustomizationRulesOnHtmlFiles(RuleParser.ruleStore);
        } catch (Exception e) {
            logger.error("Exception Occurred while running the wrapper",e);
        }
    }
}



