package runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileMover {
    private final static Logger logger = LoggerFactory.getLogger(FileMover.class);
    public final Path targetFolder;
    public final Path sourceFolder;
    public String htmlTarget;
    public String cssTarget;
    public String fontsTarget;
    public String imagesTarget;
    public String jsTarget;


    public FileMover(String sourceFolder ,String targetFolder) {
        this.sourceFolder = Paths.get(sourceFolder);
        this.targetFolder = Paths.get(targetFolder);
        populateTargetFolders();
        if(!this.targetFolder.toFile().exists() || !this.sourceFolder.toFile().exists())
        logger.error("Source or target folder doesn't exist \nSource= {}\nTarget= {} ",sourceFolder, targetFolder);

    }

    public void moveFilesToTargetFolder(){
        try {
            logger.error("Going to move files from \n {} \n to \n {}", this.sourceFolder, this.targetFolder);
            List<Path> allFilesPath = listAllFiles(this.sourceFolder);
            allFilesPath.forEach(this::moveFileToTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveFileToTarget(Path path) {
        logger.error("Moving file: {}", path);
        String target = null;
        if (path.toString().endsWith("jpeg")
                || path.toString().endsWith("png")) {
            target = this.imagesTarget;
        } else if (path.toString().endsWith("html")) {
           target = this.htmlTarget;
        } else if (path.toString().endsWith("css")) {
            target = this.cssTarget;
        } else if (path.toString().endsWith("js")) {
            target = this.jsTarget;
        } else {
            target = this.fontsTarget;
        }

        String newFile = String.format("%s%s%s",
                Objects.requireNonNull(target),
                File.separator,
                path.getFileName().toString());
        if(path.toFile().exists()) {
            try {
                logger.error("File Move result: {}",Files.move(
                        path,
                        Paths.get(newFile),
                        StandardCopyOption.REPLACE_EXISTING
                ));
            } catch (IOException e) {
                logger.error("Exception occurred while moveFileToTarget for file: {}", path,e);
            }

        }
    }

    private void populateTargetFolders() {
        this.htmlTarget = this.targetFolder.toString();
        this.cssTarget = String.format("%s%s%s%s%s",
                this.targetFolder.toString(),
                File.separator,
                "assets",
                File.separator,
                "css");
        this.fontsTarget = String.format("%s%s%s%s%s",
                this.targetFolder.toString(),
                File.separator,
                "assets",
                File.separator,
                "fonts");

        this.imagesTarget = String.format("%s%s%s%s%s",
                this.targetFolder.toString(),
                File.separator,
                "assets",
                File.separator,
                "images");

        this.jsTarget = String.format("%s%s%s%s%s",
                this.targetFolder.toString(),
                File.separator,
                "assets",
                File.separator,
                "js");

    }

    private List<Path> listAllFiles(Path parentFolderPath) throws IOException {
        List<Path> listOfAllFiles;
        try (Stream<Path> walk = Files.walk(parentFolderPath)) {
            listOfAllFiles = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return listOfAllFiles;
    }
}
