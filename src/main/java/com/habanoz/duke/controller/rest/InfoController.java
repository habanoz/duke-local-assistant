package com.habanoz.duke.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
@RestController
public class InfoController {
    @GetMapping(value = "info", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getInfo() {
        return "{\"memoryStore\":{\"types\":[\"Volatile\",\"TextFile\",\"Qdrant\",\"AzureAISearch\"],\"selectedType\":\"TextFile\"},\"availablePlugins\":[{\"name\":\"Klarna Shopping\",\"manifestDomain\":\"https://www.klarna.com\",\"key\":\"\"}],\"version\":\"1.0.0.0\",\"isContentSafetyEnabled\":false}";
    }
}
