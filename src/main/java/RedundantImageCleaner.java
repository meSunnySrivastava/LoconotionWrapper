
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedundantImageCleaner {
    private final static String imageInHTMLRegex = "assets\\/images\\/(.+?)\"";
    private final static Pattern pattern = Pattern.compile(imageInHTMLRegex);

    private static final String imagesFolderLocation = "/Users/sunnysrivastava/IdeaProjects/loconotion/dist/welcome-to-my-piece-of-internet/assets/images";
    private static final String htmlFolderLocation = "/Users/sunnysrivastava/IdeaProjects/loconotion/dist/welcome-to-my-piece-of-internet";


    public void cleanReduntantImagesfromAssetFolder() throws IOException {
//        System.out.println();

        getHtmlVsImageFileName(getGeneratedFileNameVsMainFileName());
        System.out.println("FINISHED");

    }

//    private void replaceRandomImageNamesWithActualImageName(Map<String, List<String>> htmlVsImageFileName,
//                                                            Map<String, String> generatedFileNameVsMainFileName){
//
//    }

    private Map<String, List<String>> getHtmlVsImageFileName(Map<String, String> generatedFileNameVsMainFileName) throws IOException {
        File htmlFolder = new File(htmlFolderLocation);
        System.out.println("Processing html Folder Location: "+ htmlFolderLocation);
        Map<String, List<String>> HtmlVsImageFileName = new HashMap<>();
        Matcher matcher;
        List<String> images = null;
        for(File file : htmlFolder.listFiles()){
            System.out.println("Processing file in html folder: "+ file.getName()+" file type:" + file.isFile());

            if(!file.isDirectory() && !".DS_Store".equalsIgnoreCase(file.getName())){
                String content = Files.readString(file.toPath());
                matcher = pattern.matcher(content);
                System.out.println("Got html file: "+ file.getName());
                //System.out.println(Files.readAllLines(file.toPath()));
                images = new ArrayList<>();String randomName = null;

                    while(matcher.find()){
                         randomName = matcher.group(1);
                        images.add(randomName);

                        if(null != generatedFileNameVsMainFileName.get(randomName)){
                            System.out.println("Replacing randomName:"+ randomName+" with: " + generatedFileNameVsMainFileName.get(randomName));
                            content = content.replace(randomName, generatedFileNameVsMainFileName.get(randomName));
                            Path p = new File(imagesFolderLocation+"/"+randomName).toPath();
                            if(p.toFile().exists()){
                                System.out.println("Deleting file: " + p.toString());
                                Files.delete(p);
                            }
                        }

                    }


                    Files.write(file.toPath(), content.getBytes());
                    System.out.println("Found "+images.size()+" images in html file: "+ file.getName() +" Complete List >"+ images);
                    HtmlVsImageFileName.put(file.getName(), images);
            }
        }
        return HtmlVsImageFileName;
    }

    private Map<String, String> getGeneratedFileNameVsMainFileName() throws IOException {
        File imagesFolder = new File(imagesFolderLocation);
        Map<String, String> generatedFileVsActualFile = new HashMap<>();
        ClassLoader classLoader = main.class.getClassLoader();
        File poemLogo =  new File(classLoader.getResource("poemLogo.jpeg").getFile());
        Files.copy( poemLogo.toPath(),new File(imagesFolderLocation + "/poemLogo.jpeg").toPath(), StandardCopyOption.REPLACE_EXISTING);

        File poemLogoThumbnail =  new File(classLoader.getResource("poemLogoThumbnail.jpeg").getFile());
        Files.copy( poemLogoThumbnail.toPath(),new File(imagesFolderLocation + "/poemLogoThumbnail.jpeg").toPath(), StandardCopyOption.REPLACE_EXISTING);
        File sweLogo =  new File(classLoader.getResource("sweLogo.jpeg").getFile());
        Files.copy( sweLogo.toPath(),new File(imagesFolderLocation + "/sweLogo.jpeg").toPath(), StandardCopyOption.REPLACE_EXISTING);


        File sweLogoThumbnail =  new File(classLoader.getResource("sweLogoThumbnail.jpeg").getFile());
        Files.copy( sweLogoThumbnail.toPath(),new File(imagesFolderLocation + "/sweLogoThumbnail.jpeg").toPath(), StandardCopyOption.REPLACE_EXISTING);

        for(File file : imagesFolder.listFiles()){
            if(!file.isDirectory()){
                String actualFile = findActualFileNameForGeneratedImage(file);
//                logger.error("actualFile:" + actualFile + " for generated Image"+ file.getName());
                if(null != actualFile){
                    generatedFileVsActualFile.put(file.getName(), actualFile);
                }
            }
        }
        //System.out.println(generatedFileVsActualFile);
        return generatedFileVsActualFile;
    }

    private String findActualFileNameForGeneratedImage(final File generatedImageFile) throws IOException {

        ImageComparator comparator = new ImageComparator();

        ClassLoader classLoader = main.class.getClassLoader();
        File poemLogo =  new File(classLoader.getResource("poemLogo.jpeg").getFile());

        File poemLogoThumbnail =  new File(classLoader.getResource("poemLogoThumbnail.jpeg").getFile());
        File sweLogo =  new File(classLoader.getResource("sweLogo.jpeg").getFile());


        File sweLogoThumbnail =  new File(classLoader.getResource("sweLogoThumbnail.jpeg").getFile());


        if(comparator.visuallyCompare(generatedImageFile, poemLogo)){
            return "poemLogo.jpeg";
        }else if(comparator.visuallyCompare(generatedImageFile, poemLogoThumbnail)){
            return "poemLogoThumbnail.jpeg";
        }else if(comparator.visuallyCompare(generatedImageFile, sweLogo)){
            return "sweLogo.jpeg";
        }else if(comparator.visuallyCompare(generatedImageFile, sweLogoThumbnail)){
            return "sweLogoThumbnail.jpeg";
        }
            return null;
    }
}
