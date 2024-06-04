package com.habanoz.duke.core.graph.aux;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;

import java.util.function.Function;

public class AssignNode extends BaseNode {
    public AssignNode(String name, BaseNode node) {
        super(func(name, node));
    }

    public AssignNode(String name, Object value) {
        super(func(name, value));
    }

    private static Function<Dict, Dict> func(String name, BaseNode node) {
        return input -> input.extend(name, node.call(input).getVal());
    }

    private static Function<Dict, Dict> func(String name, Object value) {
        return input -> input.extend(name, value).getVal();
    }

}
