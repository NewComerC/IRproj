package com.cjm.moni.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/", method = { RequestMethod.GET,
        RequestMethod.POST })
public class webController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    private String test() {
        return "/test";
    }

}
