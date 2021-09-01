package runner;

import common.ApplicationProperties;

import java.io.File;

public interface runner {

    default ProcessBuilder getProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(ApplicationProperties.get("locoFolder")));
        return processBuilder;
    }

    default String getLoconotionCommand(String pageUrl, Boolean singlePage) {
        return "python3 loconotion " + pageUrl +
                (singlePage ? " --single-page " : "") +
                " --chromedriver " + ApplicationProperties.get("chromeDriver");
    }
}
