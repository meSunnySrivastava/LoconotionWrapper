package runner;

import common.ApplicationProperties;

import java.io.File;
import java.util.HashMap;

public class SyncRunner implements runner{
    private final HashMap<String,String> args;


    public SyncRunner(HashMap<String,String> args) {
        this.args = args;
    }

    public void run(){
        runLoconotion(args.get("pageUrl"));
    }

    private void runLoconotionForSinglePage(String pageUrl){
        ProcessBuilder processBuilder = getProcessBuilder();
        processBuilder.inheritIO().command("bash", "-c", getLoconotionCommand(pageUrl, true));
    }

    private void runLoconotion(String pageUrl){
        ProcessBuilder processBuilder = getProcessBuilder();
        processBuilder.inheritIO().command("bash", "-c", getLoconotionCommand(pageUrl, false));
    }



}
