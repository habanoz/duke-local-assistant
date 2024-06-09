package com.habanoz.duke.core.graph.help;

import com.habanoz.duke.core.graph.base.SimpleNode;
import com.habanoz.duke.core.model.NodeFunction;

public class PrintNode extends SimpleNode {
    public PrintNode() {
        super(func());
    }

    private static NodeFunction func(){
        return (input, eventSink)-> input.map(s->{
            System.out.println(s);
            return s;
        });
    }
}
