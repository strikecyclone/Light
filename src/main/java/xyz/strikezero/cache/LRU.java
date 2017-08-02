package xyz.strikezero.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.strikezero.config.Config;
import xyz.strikezero.model.WebPage;

/**
 * Created by j on 2017/7/24.
 */
public class LRU extends Cache {
    private static Logger logger = LoggerFactory.getLogger(LRU.class);
    private int location;
    private long timestep;
    private long[] time;

    public LRU() {
        location = 0;
        timestep = 0;
        time = new long[Config.MAX_CACHE_NUM];
    }

    @Override
    public void add(String query, WebPage[] results) {
        if (needReplacement()) {
            location = findOldest();
            String pre = getList().get(location);
            logger.info("delete cache for {}", pre);
            getIndex().remove(pre);
            getIndex().put(query, location);
            getList().set(location, query);
            getCache().set(location, results);
            time[location] = timestep;
        } else {
            getIndex().put(query, location);
            getList().add(location, query);
            getCache().add(location, results);
            time[location] = timestep;
            location++;
        }
        logger.info("add cache for {}", query);
        timestep++;
    }

    private int findOldest() {
        long min = time[0];
        int idx = 0;
        for (int i = 1; i < time.length; i++) {
            if (time[i] < min) {
                min = time[i];
                idx = i;
            }
        }
        return idx;
    }

    @Override
    protected void ifContains(int i) {
        time[i] = timestep;
        timestep++;
    }
}
