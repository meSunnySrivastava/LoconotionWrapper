package runner;

import common.ApplicationProperties;
import customization.RuleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AsyncRunner implements runner{

    private final static Logger logger = LoggerFactory.getLogger(AsyncRunner.class);

    public void createWebsite(){
        try {
            long start = System. currentTimeMillis();
            getPageLinks().forEach(this::runLoconotionWithProcessBuilder);
            logger.error("Took {} ms to create the website.", System.currentTimeMillis() - start);
        } catch (IOException e) {
            logger.error("IO Exception occurred while creating Website",e);
        } catch (URISyntaxException e) {
            logger.error("URI Syntax Exception occurred while creating Website",e);
        }
    }


    private List<String> getPageLinks() throws IOException, URISyntaxException {
        URL rulesFileUrl = RuleParser.class.getClassLoader().getResource("page.links");
        if (null != rulesFileUrl) {
            return Files.readAllLines(Paths.get(rulesFileUrl.toURI()));
        }
        return new ArrayList<>();
    }

    private void runLoconotionWithProcessBuilder(String pageUrl){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(ApplicationProperties.get("locoFolder")));
        processBuilder.inheritIO().command("bash", "-c", getLoconotionCommand(pageUrl, true));
        try {
            Process process = processBuilder.start();
        } catch (IOException e) {
            logger.error("Exception occurred while running loconotion with processBuilder for {}",pageUrl,e);
        }
    }

}
