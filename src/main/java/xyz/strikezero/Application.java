package xyz.strikezero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import xyz.strikezero.config.Config;
import xyz.strikezero.index.Index;
import xyz.strikezero.index.IndexCommitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.locks.Condition;

/**
 * Created by junji on 2017/7/19.
 */
@SpringBootApplication
@RestController
public class Application {
    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @RequestMapping("/analyze")
    public ModelAndView analyze() {
        return new ModelAndView("analyze");
    }

    @RequestMapping(value = "/analyze", method = RequestMethod.POST)
    public Map<String, Object> postJSON(@RequestBody Map<String,String> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        String summary = null;
        try {
            summary = Config.SUMMARY.getSummary(params.get("query"), params.get("sentences"));
            map.put("success", true);
            map.put("summary", summary);
        } catch (IOException e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        Config.initialize();
        // auto commit per 5 minutes
        IndexCommitter ic = new IndexCommitter();
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 5 * 60 * 1000;
        timer.scheduleAtFixedRate(ic, delay, intevalPeriod);
    }
}
