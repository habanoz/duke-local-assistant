package com.habanoz.duke.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
@RestController
public class StubController {
    @GetMapping("maintenanceStatus")
    public String getMaintenanceStatus() {
        System.out.println("getMaintenanceStatus");
        return "";
    }

    // {"authType":"None","aadAuthority":"","aadClientId":"","aadApiScope":"api:///access_as_user"}
    @GetMapping(value = "authConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    public String authConfig() {
        return "{\"authType\":\"None\",\"aadAuthority\":\"\",\"aadClientId\":\"\",\"aadApiScope\":\"api:///access_as_user\"}";
    }

    // [{"id":"58044b20-b6b7-46cc-9dba-0ac63eb8bd7e","title":"Copilot @ 07.06.2024 14:59:44","createdOn":"2024-06-07T14:59:44.790399+03:00","systemDescription":"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.","safeSystemDescription":"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.","memoryBalance":0.5,"enabledPlugins":[],"version":"2.0"},{"id":"2ee84e14-8f89-413e-945e-824552ad4982","title":"Copilot @ 07.06.2024 14:59:36","createdOn":"2024-06-07T14:59:36.7391519+03:00","systemDescription":"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.","safeSystemDescription":"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.","memoryBalance":0.5,"enabledPlugins":[],"version":"2.0"}]
    @GetMapping(value = "chats", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getChats() {
        return "[{\"id\":\"58044b20-b6b7-46cc-9dba-0ac63eb8bd7e\",\"title\":\"Copilot @ 07.06.2024 14:59:44\",\"createdOn\":\"2024-06-07T14:59:44.790399+03:00\",\"systemDescription\":\"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.\",\"safeSystemDescription\":\"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.\",\"memoryBalance\":0.5,\"enabledPlugins\":[],\"version\":\"2.0\"},{\"id\":\"2ee84e14-8f89-413e-945e-824552ad4982\",\"title\":\"Copilot @ 07.06.2024 14:59:36\",\"createdOn\":\"2024-06-07T14:59:36.7391519+03:00\",\"systemDescription\":\"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.\",\"safeSystemDescription\":\"This is a chat between an intelligent AI bot named Copilot and one or more participants. SK stands for Semantic Kernel, the AI platform used to build the bot. The AI was trained on data through 2021 and is not aware of events that have occurred since then. It also has no ability to access data on the Internet, so it should not claim that it can or say that it will go and look things up. Try to be concise with your answers, though it is not required. Knowledge cutoff: {{$knowledgeCutoff}} / Current date: {{TimePlugin.Now}}.\",\"memoryBalance\":0.5,\"enabledPlugins\":[],\"version\":\"2.0\"}]";
    }

    // {"memoryStore":{"types":["Volatile","TextFile","Qdrant","AzureAISearch"],"selectedType":"TextFile"},"availablePlugins":[{"name":"Klarna Shopping","manifestDomain":"https://www.klarna.com","key":""}],"version":"1.0.0.0","isContentSafetyEnabled":false}
    @GetMapping(value = "info", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getInfo() {
        return "{\"memoryStore\":{\"types\":[\"Volatile\",\"TextFile\",\"Qdrant\",\"AzureAISearch\"],\"selectedType\":\"TextFile\"},\"availablePlugins\":[{\"name\":\"Klarna Shopping\",\"manifestDomain\":\"https://www.klarna.com\",\"key\":\"\"}],\"version\":\"1.0.0.0\",\"isContentSafetyEnabled\":false}";
    }

    // [{"id":"46f1fd44-0dfb-4c31-8dd2-056742da46e0","userId":"c05c61eb-65e4-4223-915a-fe72b0c9ece1","chatId":"58044b20-b6b7-46cc-9dba-0ac63eb8bd7e"}]
    // [{"id":"a6891829-8e0c-4547-9c4f-ea5fbb9e01af","userId":"c05c61eb-65e4-4223-915a-fe72b0c9ece1","chatId":"2ee84e14-8f89-413e-945e-824552ad4982"}]
    @GetMapping(value = "chats/{chatId}/participants", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getParticipants(@PathVariable("chatId") String chatId) {
        if (chatId.equals("58044b20-b6b7-46cc-9dba-0ac63eb8bd7e"))
            return "[{\"id\":\"46f1fd44-0dfb-4c31-8dd2-056742da46e0\",\"userId\":\"c05c61eb-65e4-4223-915a-fe72b0c9ece1\",\"chatId\":\"58044b20-b6b7-46cc-9dba-0ac63eb8bd7e\"}]";
        else if (chatId.equals("2ee84e14-8f89-413e-945e-824552ad4982"))
            return "[{\"id\":\"a6891829-8e0c-4547-9c4f-ea5fbb9e01af\",\"userId\":\"c05c61eb-65e4-4223-915a-fe72b0c9ece1\",\"chatId\":\"2ee84e14-8f89-413e-945e-824552ad4982\"}]";
        throw new IllegalArgumentException("unknown chatId");
    }

    // [{"timestamp":"2024-06-07T14:59:44.7904402+03:00","userId":"Bot","userName":"Bot","chatId":"58044b20-b6b7-46cc-9dba-0ac63eb8bd7e","content":"Hello, thank you for democratizing AI's productivity benefits with open source! How can I help you today?","id":"d3c8db92-984b-4cc5-b944-1fafbf33fd42","authorRole":1,"prompt":"","citations":null,"type":0,"tokenUsage":{"audienceExtraction":0,"userIntentExtraction":0,"metaPromptTemplate":0,"responseCompletion":0,"workingMemoryExtraction":0,"longTermMemoryExtraction":0}}]
    // [{"timestamp":"2024-06-07T14:59:36.834731+03:00","userId":"Bot","userName":"Bot","chatId":"2ee84e14-8f89-413e-945e-824552ad4982","content":"Hello, thank you for democratizing AI's productivity benefits with open source! How can I help you today?","id":"21107ad3-838d-45d9-95d3-1d91169076e2","authorRole":1,"prompt":"","citations":null,"type":0,"tokenUsage":{"audienceExtraction":0,"userIntentExtraction":0,"metaPromptTemplate":0,"responseCompletion":0,"workingMemoryExtraction":0,"longTermMemoryExtraction":0}}]
    @GetMapping(value = "chats/{chatId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMessages(@PathVariable("chatId") String chatId) {
        if (chatId.equals("58044b20-b6b7-46cc-9dba-0ac63eb8bd7e"))
            return "[{\"timestamp\":\"2024-06-07T14:59:44.7904402+03:00\",\"userId\":\"Bot\",\"userName\":\"Bot\",\"chatId\":\"58044b20-b6b7-46cc-9dba-0ac63eb8bd7e\",\"content\":\"Hello, thank you for democratizing AI's productivity benefits with open source! How can I help you today?\",\"id\":\"d3c8db92-984b-4cc5-b944-1fafbf33fd42\",\"authorRole\":1,\"prompt\":\"\",\"citations\":null,\"type\":0,\"tokenUsage\":{\"audienceExtraction\":0,\"userIntentExtraction\":0,\"metaPromptTemplate\":0,\"responseCompletion\":0,\"workingMemoryExtraction\":0,\"longTermMemoryExtraction\":0}}]";
        else if (chatId.equals("2ee84e14-8f89-413e-945e-824552ad4982"))
            return "[{\"timestamp\":\"2024-06-07T14:59:36.834731+03:00\",\"userId\":\"Bot\",\"userName\":\"Bot\",\"chatId\":\"2ee84e14-8f89-413e-945e-824552ad4982\",\"content\":\"Hello, thank you for democratizing AI's productivity benefits with open source! How can I help you today?\",\"id\":\"21107ad3-838d-45d9-95d3-1d91169076e2\",\"authorRole\":1,\"prompt\":\"\",\"citations\":null,\"type\":0,\"tokenUsage\":{\"audienceExtraction\":0,\"userIntentExtraction\":0,\"metaPromptTemplate\":0,\"responseCompletion\":0,\"workingMemoryExtraction\":0,\"longTermMemoryExtraction\":0}}]";
        throw new IllegalArgumentException("unknown chatId");
    }

    // {"token":null,"region":null,"isSuccess":false}
    @GetMapping(value = "speechToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSpeechToken() {
        return "{\"token\":null,\"region\":null,\"isSuccess\":false}\n";
    }


    // new chat
    // [{"timestamp":"2024-06-07T14:59:44.7904402+03:00","userId":"Bot","userName":"Bot","chatId":"58044b20-b6b7-46cc-9dba-0ac63eb8bd7e","content":"Hello, thank you for democratizing AI's productivity benefits with open source! How can I help you today?","id":"d3c8db92-984b-4cc5-b944-1fafbf33fd42","authorRole":1,"prompt":"","citations":null,"type":0,"tokenUsage":{"audienceExtraction":0,"userIntentExtraction":0,"metaPromptTemplate":0,"responseCompletion":0,"workingMemoryExtraction":0,"longTermMemoryExtraction":0}}]
    @PostMapping(value = "chats/{chatId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatResponse postMessages(@PathVariable("chatId") String chatId, @RequestBody ChatRequest body) {
        return new ChatResponse("Microsoft.SemanticKernel.KernelArguments",
                List.of(new Pair("chatId", chatId), new Pair("messageType", "0"),
                        new Pair("userId", "c05c61eb-65e4-4223-915a-fe72b0c9ece1"),
                        new Pair("userName", "Default User"),
                        new Pair("message", body.input),
                        new Pair("input", "Yes, I'm an AI bot designed to assist you with various tasks and provide information. How can I assist you today?"),
                        new Pair("tokenUsage", "{\"metaPromptTemplate\":256,\"workingMemoryExtraction\":585,\"longTermMemoryExtraction\":627,\"responseCompletion\":25}"),
                        new Pair("messageId", UUID.randomUUID().toString())
                ));
    }

    public record ChatRequest(String input, List<Pair> variables) {
    }

    public record Pair(String key, String value) {
    }

    public record ChatResponse(String value, List<Pair> variables) {
    }
}
