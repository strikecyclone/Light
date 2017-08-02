package xyz.strikezero.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.strikezero.config.Config;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Index {
    private static Logger logger = LoggerFactory.getLogger(Index.class);

    private String path;
    private ReadWriteLock lock;
    private int unModified = 0;
    private Searcher searcher;
    private Indexer indexer;

    private static Index index = new Index();

    private Index() {
        this.lock = new ReentrantReadWriteLock();
    }

    public static Index getInstance() {
        return index;
    }

    public Searcher getSearcher() {
        if (searcher == null) {
            if (indexer != null) {
                //indexer.commit();
                indexer.close();
                indexer = null;
            }
            searcher = new Searcher();
        }
        return searcher;
    }

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }

    public Indexer getIndexer() {
        if (indexer == null) {
            if (searcher != null) {
                searcher.close();
                searcher = null;
            }
            indexer = new Indexer();
        }
        return indexer;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    public boolean isModified() {
        return unModified > 0;
    }

    public void reset() {
        unModified = 0;
    }

    public void unModifiedAddOne() {
        unModified++;
    }

    public boolean needCommit() {
        return unModified >= Config.MAX_UNCOMMITTED_NUM;
    }

    public int getUnModified() {
        return unModified;
    }
}
