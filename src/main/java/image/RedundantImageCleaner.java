package image;

import common.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cleaner to remove multiple copies of same logo files from Assets folder and just keep one
 */
public class RedundantImageCleaner {
    private final static Logger logger = LoggerFactory.getLogger(RedundantImageCleaner.class);


    private final static String imageInHTMLRegex = "assets\\/images\\/(.+?)\"";
    private final static Pattern pattern = Pattern.compile(imageInHTMLRegex);

    private final static ClassLoader classLoader = RedundantImageCleaner.class.getClassLoader();


    public static File poemLogo;
    public static File poemLogoThumbnail;
    public static File sweLogo;
    public static File sweLogoThumbnail;

    public void cleanRedundantImagesFromAssetFolder() throws IOException {
        logger.info("Going to clean redundant images in image-folder: {}", ApplicationProperties.get("imageFolder"));
        replaceImageFileNameInHtml(getGeneratedImageVsLogoTypeMappings());
        logger.info("Finished cleaning redundant images in image-folder: {}", ApplicationProperties.get("imageFolder"));

    }

    /**
     * Replace references to Random Name generated logo files in HTML files with actual Logo names
     * @param generatedImageVsLogoTypeMappings
     * @throws IOException
     */
    private void replaceImageFileNameInHtml(final Map<String, String> generatedImageVsLogoTypeMappings) throws IOException {
        logger.debug("Replacing Random Image names to actual Logo Image names in HTML files.");

        File htmlFolder = new File(ApplicationProperties.get("htmlFolder"));

        Matcher matcher;
        List<String> images = null;
        String content;
        String randomName;
        for(File file : Objects.requireNonNull(htmlFolder.listFiles(),
                "Cannot Find specified htmlFolder: "+ApplicationProperties.get("htmlFolder") )){

            if (!file.isDirectory() && (file.getName()).endsWith("html")) {
                logger.debug("Processing HTML File: {}", file.getName());
                content = Files.readString(file.toPath());
                matcher = pattern.matcher(content);
                images = new ArrayList<>();

                while (matcher.find()) {
                    randomName = matcher.group(1);
                    images.add(randomName);
                    if (null != generatedImageVsLogoTypeMappings.get(randomName)) {
                        content = content.replace(randomName, generatedImageVsLogoTypeMappings.get(randomName));

                        logger.debug("Replaced random Img name: {} with Logo name: {}", randomName,
                                generatedImageVsLogoTypeMappings.get(randomName));

                        DeleteRandomImageFile(randomName);
                    }

                }

                Files.write(file.toPath(), content.getBytes());
                logger.debug("Random Image Names replaced for HTML {} : {}",
                        file.getName(), images);

                logger.debug("Done Replacing Random Image names to actual Logo Image names in HTML file {}",
                        file.getName());
                content = null;
            }
        }
    }

    /**
     * Delete RandomName Logo File generated after replacing the references
     * @param randomName
     * @throws IOException
     */
    private void DeleteRandomImageFile(String randomName) throws IOException {
        Path randomImage = new File(ApplicationProperties.get("imageFolder")+"/"+ randomName)
                .toPath();

        if(randomImage.toFile().exists()){
            logger.debug("Deleting random Img: {}", randomImage.toString());
            Files.delete(randomImage);
        }
    }

    /**
     * Check every image and see if its just copy of any Logo files
     * If so maintain the corresponding logo name in a map
     * @return map with logo name for generated images who are copy
     * @throws IOException
     */
    private Map<String, String> getGeneratedImageVsLogoTypeMappings() throws IOException {
        copyLogoImagesToAssetsFolder();
        logger.debug("Creating Generated Vs Logo type Mappings");
        Map<String, String> generatedVsLogoMapping = new HashMap<>();
        File imagesFolder = new File(ApplicationProperties.get("imageFolder"));

        for(File file : Objects.requireNonNull(imagesFolder.listFiles(),
                "Cannot Find specified imagesFolder: "+ApplicationProperties.get("imageFolder") )){

            if(!file.isDirectory()){

                String logoFile = findLogoTypeForGeneratedImage(file);
                logger.debug("{file={},Logo={}}",file.getName(),logoFile);

                if(null != logoFile){
                    generatedVsLogoMapping.put(file.getName(), logoFile);
                }
            }
        }
        logger.debug("generatedVsLogoMapping: {}" , generatedVsLogoMapping);
        return generatedVsLogoMapping;
    }

    /**
     * Check if passed generated Image is just a copy of Logo file
     * @param generatedImageFile
     * @return
     * @throws IOException
     */
    private String findLogoTypeForGeneratedImage(final File generatedImageFile) throws IOException {
        logger.debug("Finding Logo type for img: {}",generatedImageFile.getName());
        if(ImageComparator.visuallySameImage(generatedImageFile, poemLogo)){
            return poemLogo.getName();
        }else if(ImageComparator.visuallySameImage(generatedImageFile, poemLogoThumbnail)){
            return poemLogoThumbnail.getName();
        }else if(ImageComparator.visuallySameImage(generatedImageFile, sweLogo)){
            return sweLogo.getName();
        }else if(ImageComparator.visuallySameImage(generatedImageFile, sweLogoThumbnail)){
            return sweLogoThumbnail.getName();
        }
        logger.debug("Found image:{} which is not a logo.",generatedImageFile.getName());
        return null;
    }


    /**
     * Copy actual image files to Assets folder
     * @throws IOException
     */
    private void copyLogoImagesToAssetsFolder() throws IOException {
            initializeFileReferences();
            logger.debug("Copying Logo Images from Resource folder to Assets Folder");

            Files.copy(poemLogo.toPath(),
                    new File(ApplicationProperties.get("imageFolder") + "/poemLogo.jpeg").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy( poemLogoThumbnail.toPath(),
                    new File(ApplicationProperties.get("imageFolder") + "/poemLogoThumbnail.jpeg").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy( sweLogo.toPath(),
                    new File(ApplicationProperties.get("imageFolder") + "/sweLogo.jpeg").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy( sweLogoThumbnail.toPath(),
                    new File(ApplicationProperties.get("imageFolder") + "/sweLogoThumbnail.jpeg").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

        File fevicon = new File(
                Objects.requireNonNull(
                                classLoader.getResource("favicon.png"),
                                "favicon.png is not present in the resource folder.")
                        .getFile());

        Files.copy( fevicon.toPath(),
                new File(ApplicationProperties.get("imageFolder") + "/favicon.png").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Initialize references to logo images from resource folder
     */
    private void initializeFileReferences() {
        logger.debug("Initializing File References for Logo Images from Resource folder");
        poemLogo = new File(
                Objects.requireNonNull(
                                classLoader.getResource("poemLogo.jpeg"),
                                "poemLogo.jpeg is not present in the resource folder.")
                        .getFile());

        poemLogoThumbnail = new File(
                Objects.requireNonNull(
                                classLoader.getResource("poemLogoThumbnail.jpeg"),
                                "poemLogoThumbnail.jpeg is not present in the resource folder.")
                        .getFile());

        sweLogo = new File(
                Objects.requireNonNull(
                                classLoader.getResource("sweLogo.jpeg"),
                                "sweLogo.jpeg is not present in the resource folder.")
                        .getFile());

        sweLogoThumbnail = new File(
                Objects.requireNonNull(
                                classLoader.getResource("sweLogoThumbnail.jpeg"),
                                "sweLogoThumbnail.jpeg is not present in the resource folder.")
                        .getFile());


    }
}
