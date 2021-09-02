package customization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RuleParser {

    private final static Logger logger = LoggerFactory.getLogger(RuleParser.class);
    public static final List<CustomizationRule> ruleStore = new LinkedList<>();

    static{
        loadRulesToRuleStore();
        logger.error("Rule store: {}",ruleStore);
    }

    public List<CustomizationRule> getRuleStore(){
        return ruleStore;
    }

    private static void loadRulesToRuleStore() {
        try{
            getRuleDefinitions().forEach(RuleParser::parseRuleDefinition);
        }catch(Exception e){
            logger.error("Exception occurred while loading customization rule",e);
        }
    }

    private static void parseRuleDefinition(String each) {
        String[] split = each.split(":");
        HashMap<String,String> args;
        logger.error("Rule definition: {} with type: {}",each,split[0]);
        if("replace".equals(split[0])){
                args = new HashMap<>();
                args.put("this", split[1]);
                if(split.length == 3){
                 args.put("with", split[2]);
                }else{
                    args.put("with", "");
                }
                ruleStore.add(new CustomizationRule("replace", args));
                logger.debug("Replace Rule: {} added",each);

        }else  if("delete".equals(split[0])){
            args = new HashMap<>();
            args.put("this", split[1]);
            ruleStore.add(new CustomizationRule("delete", args));
            logger.debug("delete Rule: {} added",each);

        }else if("rename".equals(split[0])){
            args = new HashMap<>();
            args.put("this", split[1]);
            args.put("to", split[2]);
            ruleStore.add(new CustomizationRule("rename", args));
            logger.debug("rename Rule: {} added",each);
        }
    }


    private static List<String> getRuleDefinitions() throws IOException, URISyntaxException {
        URL rulesFileUrl = RuleParser.class.getClassLoader().getResource("customization.rule");
        if (null != rulesFileUrl) {
            return Files.readAllLines(Paths.get(rulesFileUrl.toURI()));
        }
        return new ArrayList<>();
    }
}
