package xyz.strikezero.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import xyz.strikezero.config.Config;
import xyz.strikezero.model.WebPage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by junji on 2017/4/22.
 */
public class Searcher {
    private IndexReader reader = null;
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    public Searcher() {
        this(Index.getInstance().getPath(), Config.SEARCHER_ANALYZER);
    }

    public Searcher(String path, Analyzer analyzer) {
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
            searcher = new IndexSearcher(reader);
            parser = new MultiFieldQueryParser(Config.FIELDS, analyzer, Config.BOOSTS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<WebPage> search(String q, /*int start,*/ int rows, boolean highlight) {
        if (reader == null || searcher == null || parser == null) {
            //------------------------log------------------------------------
            System.err.println("Searcher was't initialized!");
            return null;
        }

        List<WebPage> list = new ArrayList<WebPage>();
        Query query = null;
        try {
            query = parser.parse(q);
            Date start = new Date();
            TopDocs docs = searcher.search(query, rows);
            System.out.println("Find " + docs.totalHits + " docs");
            System.out.println("=====================================================================================");
            for (int i = 0; i < docs.scoreDocs.length; i++) {
                Document doc = searcher.doc(docs.scoreDocs[i].doc);
                WebPage webPage = new WebPage(doc);
                list.add(webPage);
                System.out.println(list.get(i));
            }
            Date end = new Date();
            System.out.println("Time: " + (end.getTime() - start.getTime()) + "ms");
            System.out.println("=====================================================================================");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public boolean contains(Term term) {
        if (reader == null || searcher == null || parser == null) {
            //------------------------log------------------------------------
            System.err.println("Searcher was't initialized!");
            return false;
        }

        int ret = 0;
        try {
            Query query = new TermQuery(term);
            TopDocs hits = searcher.search(query, 1);
            ret = hits.totalHits;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return ret > 0;
        }
    }

    public void close() {
        try {
            if (reader != null)//tmp code will be remove in the future.
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
