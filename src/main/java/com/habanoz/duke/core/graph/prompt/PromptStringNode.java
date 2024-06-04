package com.habanoz.duke.core.graph.prompt;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;

import java.util.Map;
import java.util.function.Function;

public class PromptStringNode extends BaseNode {
    public PromptStringNode(String template) {
        super(getFunction(template));
    }

    private static Function<Dict, Dict> getFunction(String template) {
        return (dict) -> Dict.sin(replaceOccurrences(template, dict));
    }

    private static String replaceOccurrences(String original, Dict replacements) {
        StringBuilder sb = new StringBuilder(original);

        for (Map.Entry<String, Object> entry : replacements.items()) {
            String holder = "${" + entry.getKey() + "}";
            int start = sb.indexOf(holder);
            if (start >= 0)
                sb.replace(start, start + holder.length(), entry.getValue().toString());
        }

        return sb.toString();
    }


}
