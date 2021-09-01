import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.FileMover;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wrapper {

    private final static Logger logger = LoggerFactory.getLogger(Wrapper.class);
    public static void main(String[] args) {
        try {
            //runCOmmandTest();

            FileMover f = new FileMover("/Users/sunnysrivastava/IdeaProjects/loconotion/dist/welcome-to-my-piece-of-internet");
            f.moveFilesToSingleFolder();
            //new image.RedundantImageCleaner().cleanRedundantImagesFromAssetFolder();
//            extractLinks();
        } catch (Exception e) {
            logger.error("Exception Occurred while running the wrapper",e);
        }
    }

    static void extractLinks() throws IOException {
        Matcher m = Pattern.compile("Parsing page '(.+?)'").matcher(Files.readString(Paths.get(
                "/Users/sunnysrivastava/IdeaProjects/loconotion/dist/out.txt"
        )));
        List<String> links = new ArrayList<>();
        while(m.find()){
            links.add(m.group(1));
        }
        System.out.println(links.size());
        links.forEach(System.out::println);
    }

    static void runCOmmandTest() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File("/Users/sunnysrivastava/IdeaProjects/loconotion"));

        // -- Linux --

        // Run a shell command
        processBuilder.inheritIO().command("bash", "-c", "python3 loconotion https://sunnysrivastava.notion.site/Welcome-to-my-piece-of-internet-66fe9770f0ef4c4ea50d13d076d80e6d --chromedriver /Users/sunnysrivastava/Downloads/chromedriver");

        // Run a shell script
        //processBuilder.command("path/to/hello.sh");

        // -- Windows --

        // Run a command
        //processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong");

        // Run a bat file
        //processBuilder.command("C:\\Users\\mkyong\\hello.bat");

        try {

            var file = new File("/Users/sunnysrivastava/IdeaProjects/loconotion/dist/out.txt");
            processBuilder.redirectOutput(file);
            Process process = processBuilder.start();
//            StringBuilder output = new StringBuilder();
//
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line + "\n");
//            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
//                System.out.println(output);
                //System.exit(0);
            }  //abnormal...


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
