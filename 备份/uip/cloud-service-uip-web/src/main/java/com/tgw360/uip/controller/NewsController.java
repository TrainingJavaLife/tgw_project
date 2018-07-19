package com.tgw360.uip.controller;

import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.entity.News;
import com.tgw360.uip.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by 危宇 on 2017/11/23 10:37
 */
@CrossOrigin
@RestController
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 新闻Id查询新闻详情
     * /news/{id}
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/news/{id}")
    public Object findNewsById(@PathVariable("id") long id) throws Exception {
        News news = newsService.findNewsById(id);
        CommonResult<News> commonResult = new CommonResult<>();
        commonResult.setData(news);
        return commonResult;
    }

    /**
     * 新闻搜索接受脚本并高亮显示标题及内容
     * @param script
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value="/news/search",method = RequestMethod.POST)
    public Object searchByScript(@RequestBody String script) throws Exception{
        List<News> newsList = newsService.searchByScript(script);
        CommonResult<List<News>> commonResult = new CommonResult<>();
        commonResult.setData(newsList);
        return commonResult;
    }
}
