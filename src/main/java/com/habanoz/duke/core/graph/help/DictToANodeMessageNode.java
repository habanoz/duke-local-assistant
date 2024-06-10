package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.ANodeMessage;
import com.habanoz.duke.core.model.Dict;

public class DictToANodeMessageNode extends SimpleNode {
    protected DictToANodeMessageNode(String key) {
        super((input, eventPublisher) -> input.map(s -> new ANodeMessage(((Dict) s).getVal(key))));
    }
}
