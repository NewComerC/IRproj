package com.cjm.moni.web;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cjm.moni.entity.Stopword;
import com.cjm.moni.service.StopwordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author agp
 * @since 2018-11-30
 */
@RestController
@RequestMapping("/stopword")
public class StopwordController {

    @Autowired
    private StopwordService stopwordService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    private List<Stopword> test() {
        return stopwordService.selectList(new EntityWrapper<>());
    }
}
