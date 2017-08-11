package xyz.strikezero.config;

import org.apache.lucene.analysis.Analyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.strikezero.adapter.IKAdapterAnalyzer;
import xyz.strikezero.cache.Cache;
import xyz.strikezero.cache.FIFO;
import xyz.strikezero.cache.LRU;
import xyz.strikezero.index.Index;
import xyz.strikezero.summary.BM25;
import xyz.strikezero.summary.Summary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 17-6-7.
 */
public class Config {
    private static Logger logger = LoggerFactory.getLogger(Config.class);

    public static Analyzer INDEXER_ANALYZER;
    public static Analyzer SEARCHER_ANALYZER;
    public static Map<String, Float> BOOSTS = new HashMap<String, Float>();
    public static String[] FIELDS = null;
    public static int MAX_UNCOMMITTED_NUM;
    public static Cache CACHE = null;
    public static int MAX_CACHE_NUM;
    public static int PER_QUERY_CACHE_MULTIPLE_NUM;
    public static Summary SUMMARY;

    static {
        Index.getInstance().setPath("data/index");
        INDEXER_ANALYZER = new IKAdapterAnalyzer(false);
        SEARCHER_ANALYZER = new IKAdapterAnalyzer(true);

        BOOSTS.put("title", 2.0f);
        BOOSTS.put("content", 0.5f);

        FIELDS = new String[]{"title","content"};
        MAX_UNCOMMITTED_NUM = 10;

        MAX_CACHE_NUM = 10;// cache 10 queries
        PER_QUERY_CACHE_MULTIPLE_NUM = 3;// example: if query for 10 pages, we cache 3*10 pages
        CACHE = new LRU();

        SUMMARY = new BM25();
    }

    public static void initialize(){
        logger.info("Config start to initialize");
    }
}
