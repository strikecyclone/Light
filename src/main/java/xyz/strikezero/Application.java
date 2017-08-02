package xyz.strikezero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import xyz.strikezero.config.Config;
import xyz.strikezero.index.IndexCommitter;

import java.util.Timer;

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
