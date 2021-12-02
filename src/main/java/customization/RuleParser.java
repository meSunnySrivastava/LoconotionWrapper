package customization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RuleParser {

    private final static Logger logger = LoggerFactory.getLogger(RuleParser.class);
    public static List<CustomizationRule> ruleStore = null;

    static{
        loadRulesToRuleStore();
        logger.error("Rule store: {}",ruleStore);
    }

    public List<CustomizationRule> getRuleStore(){
        return ruleStore;
    }

     private static void loadRulesToRuleStore() {
        File rules = new File(
                Objects.requireNonNull(
                                RuleParser.class.getClassLoader().
                                        getResource("customizationRule.json"),
                                "customizationRule.json is not present in the resource folder.")
                        .getFile());

        try {
            Gson gson = new Gson();
            ruleStore = gson.fromJson(Files.readString(rules.toPath()),
                    new TypeToken<ArrayList<CustomizationRule>>(){}.getType());
        } catch (IOException e) {
            logger.error("Exception occurred while loading customization rule",e);
        }
    }
}
