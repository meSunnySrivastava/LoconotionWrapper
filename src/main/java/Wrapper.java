//import common.ApplicationProperties;
//import customization.RuleParser;
//import customization.RuleRunner;
//import image.RedundantImageCleaner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import runner.FileMover;
//import runner.SyncRunner;
//
//public class Wrapper {
//
//    private final static Logger logger = LoggerFactory.getLogger(Wrapper.class);
//    public static void main(String[] args) {
//        try {
//            new SyncRunner(ApplicationProperties.get("index")).createWebsite();
//
//            new RedundantImageCleaner().cleanRedundantImagesFromAssetFolder();
//
//            new RuleRunner(ApplicationProperties.get("htmlFolder"))
//            .runCustomizationRulesOnHtmlFiles(RuleParser.ruleStore);
//
//            new FileMover(ApplicationProperties.get("htmlFolder"),
//                    ApplicationProperties.get("websiteFolder"))
//                    .moveFilesToTargetFolder();
//        } catch (Exception e) {
//            logger.error("Exception Occurred while running the wrapper",e);
//        }
//    }
//
//}
//
//
//
