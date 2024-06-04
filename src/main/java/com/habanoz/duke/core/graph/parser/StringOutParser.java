package com.habanoz.duke.core.graph.parser;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;

import java.util.function.Function;

public class StringOutParser extends BaseNode {
    public StringOutParser() {
        super(func());
    }

    private static Function<Dict, Dict> func() {
        return input -> Dict.sin(input.getChatResponse().getResult().getOutput().getContent());
    }
}
