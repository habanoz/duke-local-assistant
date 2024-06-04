package com.habanoz.duke.core.graph.aux;

import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.model.Dict;

import java.util.function.Function;

public class ActionNone extends BaseNode {
    public ActionNone(Function<Dict, Dict> func) {
        super(func);
    }
}
