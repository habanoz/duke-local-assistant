package com.habanoz.duke.graph;

import com.habanoz.duke.core.graph.aux.PrintNode;
import com.habanoz.duke.core.graph.base.BaseNode;
import com.habanoz.duke.core.graph.prompt.PromptStringNode;
import com.habanoz.duke.core.model.Dict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

class BaseNodeTest {
    @Test
    void testSimpleChain() {
        BaseNode runnable1 = new BaseNode(dict -> Dict.sin(dict.getInt("a") * dict.getInt("b")));
        BaseNode runnable2 = new BaseNode(dict -> Dict.sin(dict.getInt() + 111));

        Dict res = runnable1.chain(runnable2).call(Dict.map("a", 12, "b", 15));

        Assertions.assertTrue(res.isSingleton());
        Assertions.assertEquals(291, (Integer) res.getVal());
    }

    @Test
    void testPromptChain() {
        Supplier<String> stringSupplier = () -> "Hey there!";
        BaseNode prompt = new PromptStringNode("Tom is ${relation} of ${name}");
        BaseNode llm = new BaseNode(dict -> Dict.sin(stringSupplier.get() + " : " + dict.str()));

        Dict res = prompt.chain(llm).call(Dict.map("relation", "Father", "name", "Joe"));

        Assertions.assertTrue(res.isSingleton());
        Assertions.assertEquals("Hey there! : Tom is Father of Joe", res.str());
    }

    @Test
    void testChain(){
        Dict input = Dict.map("A", 1);
        Dict output = new PrintNode("C", 33).chain(new PrintNode("D", 22)).chain(new PrintNode("E", 10)).call(input);
        System.out.println(output);
    }
}