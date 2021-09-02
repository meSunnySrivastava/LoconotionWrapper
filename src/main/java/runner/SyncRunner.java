package runner;

import common.ApplicationProperties;

import java.io.File;
import java.io.IOException;

public class SyncRunner implements runner{
    private final String pageUrl;

    public SyncRunner(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public void createWebsite() throws IOException, InterruptedException {
        runLoconotion(pageUrl);
    }

    private void runLoconotionForSinglePage(String pageUrl){
        ProcessBuilder processBuilder = getProcessBuilder();
        processBuilder.inheritIO().command("bash", "-c", getLoconotionCommand(pageUrl, true));
    }

    private void runLoconotion(String pageUrl) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = getProcessBuilder();
        processBuilder.inheritIO().command("bash", "-c", getLoconotionCommand(pageUrl, false));
        processBuilder.directory(new File(ApplicationProperties.get("locoFolder")));
        processBuilder.start().waitFor();


    }



}
