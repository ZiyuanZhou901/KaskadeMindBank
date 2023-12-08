package com.kaskademindbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ImportController {

    @RequestMapping("/import")
    public String imports() {
        return "import";
    }

    @RequestMapping("/browse")
    public String browse() {
        return "browse";
    }

    @RequestMapping("/tryit")
    public String tryit() {
        return "tryit";
    }

    @RequestMapping("/mycol")
    public String mycol() {
        return "mycol";
    }

}
