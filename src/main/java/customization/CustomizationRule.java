package customization;

import java.util.HashMap;

public class CustomizationRule {
    public final String type;
    public final HashMap<String,String> args;

    public String getType() {
        return type;
    }

    public HashMap<String, String> getArgs() {
        return args;
    }

    public CustomizationRule(String type, HashMap<String, String> args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public String toString() {
        return "CustomizationRule{" +
                "type='" + type + '\'' +
                ", args=" + args +
                '}';
    }
}
