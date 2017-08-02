package xyz.strikezero.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.strikezero.config.Config;
import xyz.strikezero.model.WebPage;

/**
 * Created by junji on 2017/7/23.
 */
public class FIFO extends Cache {
    private static Logger logger = LoggerFactory.getLogger(FIFO.class);
    private int location;

    public FIFO() {
        location = 0;
    }

    @Override
    public void add(String query, WebPage[] results) {
        if (needReplacement()) {
            String pre = getList().get(location);
            logger.info("delete cache for {}", pre);
            getIndex().remove(pre);
            getList().set(location, query);
            getCache().set(location, results);
        } else {
            getList().add(location, query);
            getCache().add(location, results);
            //setCurNum(location + 1);
        }
        getIndex().put(query, location);
        logger.info("add cache for {}", query);
        location = (location + 1) % Config.MAX_CACHE_NUM;
    }
}
