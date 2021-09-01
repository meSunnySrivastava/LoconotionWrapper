package runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileMover {
    private final static Logger logger = LoggerFactory.getLogger(FileMover.class);
    public final Path parentFolder;
    public final Path targetFolder;
    public String htmlTarget;
    public String cssTarget;
    public String fontsTarget;
    public String imagesTarget;
    public String jsTarget;


    public FileMover(String parentFolder) {
        this.parentFolder = Paths.get(parentFolder);
        File target = new File(String.format("%s%s%s", Paths.get(
                        parentFolder).getParent().toString(),
                File.separator,
                "targetFolder"
        ));
        this.targetFolder = target.toPath();
        if(target.mkdir())
        logger.error("Created Target Folder: {}", target.toPath());

    }

    public void moveFilesToSingleFolder(){
        try {
            List<Path> allFilesPath = listAllFiles(this.parentFolder);
//            System.out.println(allFilesPath.get(0).getFileName());
//            allFilesPath.forEach(System.out::println);
            Map<String,List<Path>> typeVsFilesPath = groupPathsByType(allFilesPath);
            Boolean success = createAssetsFolderInTarget();
            System.out.print(">>>>>>>>> success");
            typeVsFilesPath.forEach(this::moveToTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveToTarget(String key, List<Path> value) {
        switch (key) {
            case "html":
                value.forEach(path -> moveFileToTarget(path, this.htmlTarget) );
                break;
            case "css":
                value.forEach(path -> moveFileToTarget(path, this.cssTarget) );
                break;
            case "images":
                value.forEach(path -> moveFileToTarget(path, this.imagesTarget) );
                break;
            case "fonts":
                value.forEach(path -> moveFileToTarget(path, this.fontsTarget) );
                break;
            case "js":
                value.forEach(path -> moveFileToTarget(path, this.jsTarget) );
                break;
        }
    }

    private void moveFileToTarget(Path path, String target) {
        String newFile = String.format("%s%s%s",
                target,
                File.separator,
                path.getFileName().toString());
        try {
            Files.createFile(Paths.get(newFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.error("new File: {}",newFile);
        if(path.toFile().exists()) {
            try {
                logger.error("File Move result: {}",Files.move(
                        path,
                        Paths.get(newFile),
                        StandardCopyOption.REPLACE_EXISTING
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Boolean createAssetsFolderInTarget() {
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

        return
                new File(cssTarget).mkdirs() &&
                new File(fontsTarget).mkdirs() &&
                new File(imagesTarget).mkdirs() &&
                new File(jsTarget).mkdirs();
    }

    private Map<String, List<Path>> groupPathsByType(List<Path> allFilesPath) {
        Map<String, List<Path>> typeVsFilesPath = new HashMap<>();
        List<Path> images = new ArrayList<>();
        List<Path> html = new ArrayList<>();
        List<Path> css = new ArrayList<>();
        List<Path> fonts = new ArrayList<>();
        List<Path> js = new ArrayList<>();

        allFilesPath.forEach(path -> {
            logger.debug("path : {}",path.getFileName().toString());
            if(path.toString().endsWith("jpeg")
                || path.toString().endsWith("png"))
                images.add(path);
            else if(path.toString().endsWith("html"))
                html.add(path);
            else if(path.toString().endsWith("css"))
                css.add(path);
            else if(path.toString().endsWith("js"))
                js.add(path);
            else
                fonts.add(path);
        });

        typeVsFilesPath.put("images", images);
        typeVsFilesPath.put("html", html);
        typeVsFilesPath.put("css", css);
        typeVsFilesPath.put("fonts", fonts);
        typeVsFilesPath.put("js", js);

        logger.debug("typeVsFilesPath : {}",typeVsFilesPath);
        return typeVsFilesPath;
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
