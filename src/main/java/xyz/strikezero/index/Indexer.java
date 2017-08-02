package xyz.strikezero.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import xyz.strikezero.config.Config;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by junji on 2017/4/22.
 */
public class Indexer {
    private IndexWriter iw = null;

    public Indexer(){
        this(Index.getInstance().getPath(), Config.INDEXER_ANALYZER);
    }

    public Indexer(String path, Analyzer analyzer) {
        Directory dir = null;
        try {
            dir = FSDirectory.open(Paths.get(path));
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iw = new IndexWriter(dir, iwc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Document doc){
        if(iw != null){
            try {
                iw.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.err.println("IndexWriter is null!");
        }
    }

    public void update(Term term, Document doc){
        if(iw != null){
            try {
                iw.updateDocument(term, doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.err.println("IndexWriter is null!");
        }
    }

    public void commit(){
        try {
            iw.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            iw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
