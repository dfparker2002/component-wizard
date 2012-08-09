package org.eclipse.plugins.cq.component.wizard.wizards;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateHelper {


    /**
     * Create a pattern string from Map. The Map shell contain a pair of parameters
     *  - template token and Value.
     * @param tokens
     * @return create a string for regular expression
     */
    private static String createPattern(Map<String, String> tokens) {

        StringBuilder sb = new StringBuilder();
        sb.append("\\");
        for (String token : tokens.keySet()) {
            sb.append(token); sb.append("|");
        }
        return  sb.toString();
    }

    /**
     * Fill template with values.
     * @param template string loaded from file
     * @param tokens
     * @return String
     */
    public static String fillTemplate(String template, Map<String, String> tokens) {

    	Pattern pattern = Pattern.compile(createPattern(tokens));
        Matcher matcher = pattern.matcher(template);

        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
        	String repl = matcher.group(0);
        	if (repl != null && tokens.get(repl) != null) {
        		matcher.appendReplacement(sb, tokens.get(repl));
        	}
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
