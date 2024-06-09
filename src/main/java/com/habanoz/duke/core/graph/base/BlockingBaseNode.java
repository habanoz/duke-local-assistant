package com.habanoz.duke.core.graph.base;

import com.habanoz.duke.core.model.Dict;

import java.util.function.Function;


public class BlockingBaseNode {
    protected final Function<Dict, Dict> func;

    public BlockingBaseNode(Function<Dict, Dict> func) {
        this.func = func;
    }

    public BlockingBaseNode chain(BlockingBaseNode other) {
        return new BlockingBaseNode(input -> {
            Dict interimOutput = call(input);
            return other.call(interimOutput);
        });
    }

    public Dict call(Dict dict) {
        return func.apply(dict);
    }

}
