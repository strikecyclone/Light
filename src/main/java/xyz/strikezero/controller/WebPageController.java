package xyz.strikezero.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.strikezero.config.Config;
import xyz.strikezero.index.Index;
import xyz.strikezero.index.Indexer;
import xyz.strikezero.index.Searcher;
import xyz.strikezero.model.WebPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by song on 17-4-19.
 */
@RestController
@RequestMapping("/webpages")
public class WebPageController {
    private static Logger logger = LoggerFactory.getLogger(WebPageController.class);

    /**
     * /           get     indexes overview
     * /operate    get     add, delete, edit indexes
     * /json       post    process request from /operate
     * *      /xml        post    process request from /operate
     * /search     get     search indexes
     * /json       get     process request from /search
     * *      /xml        get     process request from /search
     */

    @RequestMapping("/")
    public ModelAndView overview() {
        return new ModelAndView("webpages/overview");
    }

    @RequestMapping("/operate")
    public ModelAndView operate() {
        return new ModelAndView("webpages/operate");
    }

    @RequestMapping(value = "/json", method = RequestMethod.POST)
    public Map<String, Object> postJSON(@RequestBody List<WebPage> webPageList) {
        Index.getInstance().getLock().writeLock().lock();
        for (WebPage webPage : webPageList) {
            if (Index.getInstance().getSearcher().contains(webPage.primaryKey()))
                Index.getInstance().getIndexer().update(webPage.primaryKey(),webPage.toDocument());
            else
                Index.getInstance().getIndexer().add(webPage.toDocument());
            Index.getInstance().unModifiedAddOne();
            logger.info("update -> url={}", webPage.getUrl());
        }
        if (Index.getInstance().needCommit()) {
            Index.getInstance().getIndexer().commit();
            logger.info("commit -> {}urls", Index.getInstance().getUnModified());
            Index.getInstance().reset();
        }
        Index.getInstance().getLock().writeLock().unlock();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("message", "Update " + webPageList.size() + " url(s)!");
        return map;
    }

    @RequestMapping(value = "/xml", method = RequestMethod.POST)
    public Map<String, Object> postXML(@RequestBody List<WebPage> webPageList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("message", "Update " + webPageList.size() + " url(s)!");
        return map;
    }

    @RequestMapping("/search")
    public ModelAndView search() {
        return new ModelAndView("webpages/search");
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public Map<String, Object> getJSON(@RequestParam String q, @RequestParam int rows, @RequestParam boolean highlight) {
        long time_start = System.currentTimeMillis();
        List<WebPage> results = null;
        if (Config.CACHE.contains(q)) {
            logger.info("Use cache of {}", q);
            WebPage[] cache = Config.CACHE.get(q);
            results = new ArrayList<WebPage>(rows);
            for (int i = 0; i < cache.length; i++)
                results.add(cache[i]);
        } else {
            Index.getInstance().getLock().readLock().lock();
            results = Index.getInstance().getSearcher().search(q, rows, highlight);
            Index.getInstance().getLock().readLock().unlock();
            //-----------------------------add cache--------------------------------------------
            if (results.size() > 0)
                Config.CACHE.add(q, (WebPage[]) (results.toArray(new WebPage[0])));
            //------------------------------end-------------------------------------------------
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("time", (System.currentTimeMillis() - time_start) / 1000.0);
        map.put("nums", results.size());
        map.put("results", results);
        System.out.println(map);
        return map;
    }

}
