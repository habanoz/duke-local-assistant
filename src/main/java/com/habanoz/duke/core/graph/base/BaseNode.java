package com.habanoz.duke.core.graph.base;

import com.habanoz.duke.core.model.Dict;

import java.util.function.Function;


public class BaseNode {
    protected final Function<Dict, Dict> func;

    public BaseNode(Function<Dict, Dict> func) {
        this.func = func;
    }

    public BaseNode chain(BaseNode other) {
        return new BaseNode(input -> {
            Dict interimOutput = call(input);
            return other.call(interimOutput);
        });
    }

    public Dict call(Dict dict) {
        return func.apply(dict);
    }

}
