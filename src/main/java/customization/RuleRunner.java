package customization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleRunner {
    private final static Logger logger = LoggerFactory.getLogger(RuleRunner.class);
    public final File htmlFolder;

    public RuleRunner(String htmlFolder) {
        this.htmlFolder = new File(htmlFolder);
    }

    public void runCustomizationRulesOnHtmlFiles(List<CustomizationRule> rules){
        getHtmlFiles().forEach(file -> {
            logger.debug("Running customization rules on file: {}", file.toString());
            rules.forEach(rule -> {
                try {
                    applyRule(rule,file);
                } catch (IOException e) {
                    logger.error("Exception occurred while applying customization rule {} on file {}",
                            rule,
                            file.toString());
                }
            });
        });
    }

    private List<Path> getHtmlFiles(){
        List<Path> htmlFiles = new ArrayList<>();
        for(File file : Objects.requireNonNull(htmlFolder.listFiles(),
                "Cannot Find specified htmlFolder: "+ htmlFolder )){
            if (!file.isDirectory() && (file.getName()).endsWith("html")) {
                htmlFiles.add(file.toPath());
            }
        }
        return htmlFiles;
    }

    private void applyRule(CustomizationRule rule, Path file) throws IOException {
        if (rule.file.equals("*") || (rule.file.equals(file.getFileName().toString()))) {
            if ("replace".equals(rule.getType())) {
                applyReplaceRule(rule, file);
            } else if ("delete".equals(rule.getType())) {
                applyDeleteRule(rule, file);
            } else if ("rename".equals(rule.getType())) {
                applyRenameRule(rule, file);
            } else if ("replaceWithRegex".equals(rule.getType())) {
                applyReplaceWithRegexRule(rule, file);
            }
        }
    }

    private void applyReplaceWithRegexRule(CustomizationRule rule, Path file) throws IOException {
        if(file.toFile().exists()) {
            String content = Files.readString(file);
            Matcher matcher = Pattern.compile(rule.getArgs().get("regex")).matcher(content);
            if(matcher.find()){
                logger.debug("content matchesRegex {}", rule.getArgs().get("regexName"));
                String matchedString = matcher.group(Integer.parseInt(rule.getArgs().get("group")));
                content = content.replace(matchedString,
                        rule.getArgs().get("with"));
                Files.write(file, content.getBytes());
            }
        }
    }

    private void applyReplaceRule(CustomizationRule rule, Path file) throws IOException {
        if(file.toFile().exists()) {
            String content = Files.readString(file);
            boolean contains = content.contains(rule.getArgs().get("what"));
            if(contains){
                logger.debug("Replacing string: {} with {}", rule.getArgs().get("what"),rule.getArgs().get("with"));
                content = content.replace(rule.getArgs().get("what"),
                        rule.getArgs().get("with"));
                Files.write(file, content.getBytes());
            }
        }
    }

    private void applyDeleteRule(CustomizationRule rule, Path file) throws IOException {
        if(file.toFile().exists()){
            logger.debug("Deleting file {} due to customization rule.", file.toString());
            Files.delete(file);
        }
    }

    private void applyRenameRule(CustomizationRule rule, Path file){
        if(file.toFile().exists()) {
            logger.debug("Renaming file {} due to customization rule. Success={}", file.toString(),
            file.toFile().renameTo(new File(file.getParent()+ File.separator+rule.args.get("to")))
            );
        }
    }

}
