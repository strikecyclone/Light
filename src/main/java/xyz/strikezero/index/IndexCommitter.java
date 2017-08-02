package xyz.strikezero.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class IndexCommitter extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(IndexCommitter.class);

    @Override
    public void run() {
        if (Index.getInstance().isModified()) {
            //commit
            Index.getInstance().getLock().writeLock().lock();
            if (Index.getInstance().isModified()) {
                logger.info("Auto commit {}url(s)",Index.getInstance().getUnModified());
                Index.getInstance().getIndexer().commit();
                Index.getInstance().reset();
            }else {
                logger.debug("There is nothing need to be auto committed");
            }
            Index.getInstance().getLock().writeLock().unlock();
            // rest status
            Index.getInstance().reset();
        }
    }
}
