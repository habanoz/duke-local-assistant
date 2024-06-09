package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.Dict;
import com.habanoz.duke.core.model.NodeFunction;

import java.util.function.Function;

public class ActionNode extends SimpleNode {
    public ActionNode(NodeFunction func) {
        super(func);
    }

    public ActionNode(Function<Dict, Dict> func) {
        this((input, eventSink) -> input.map(s -> {
                    if (s instanceof Dict d) {
                        // System.out.println("Action Node:" + d.map().toString().substring(0, Math.min(d.map().toString().length(), 150)));
                        Dict apply = func.apply(d);
                        return apply;
                    }
                    throw new IllegalArgumentException("Only dict expected");
                })
        );
    }

}
