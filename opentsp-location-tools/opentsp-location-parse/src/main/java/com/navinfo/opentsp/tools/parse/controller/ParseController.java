package com.navinfo.opentsp.tools.parse.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Created by zhanhk on 2017/4/18.
 */
@RestController
@RequestMapping("/tools")
public class ParseController {

    @RequestMapping(value = "/parse", method = RequestMethod.GET ,produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String parseData(){
        return "Hello word";
    }
}
