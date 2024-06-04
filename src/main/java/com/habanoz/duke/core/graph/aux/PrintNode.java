package com.habanoz.duke.core.graph.aux;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;

import java.util.function.Function;

/**
 * For testing purposes!
 */
public class PrintNode extends BaseNode {

    public PrintNode(String name, Object value) {
        super(func(name, value));
    }


    private static Function<Dict, Dict> func(String name, Object value) {
        System.out.println(value);
        return input -> input.extend(name, value);
    }

}
