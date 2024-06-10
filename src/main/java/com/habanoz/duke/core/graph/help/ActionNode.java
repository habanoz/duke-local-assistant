package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.Dict;

import java.util.function.Function;

public class ActionNode extends SimpleNode {


    public ActionNode(Function<Dict, Dict> func) {
        super((input, eventSink) -> input.map(s -> {
                    if (s instanceof Dict d) {
                        return func.apply(d);
                    }
                    throw new IllegalArgumentException("Only dict expected");
                })
        );
    }

}
