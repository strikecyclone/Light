package xyz.strikezero.adapter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.IOUtils;

import java.io.Reader;
import java.io.StringReader;

/**
 * Created by junji on 2017/4/25.
 * reference: http://iamyida.iteye.com/blog/2220474
 */
public class IKAdapterAnalyzer extends Analyzer{
    private boolean useSmart = false;
    public IKAdapterAnalyzer() {
        this(false);
    }

    public IKAdapterAnalyzer(boolean useSmart) {
        this.useSmart = useSmart;
    }

    protected TokenStreamComponents createComponents(String arg0) {
        Reader reader=null;
        try{
            reader=new StringReader(arg0);
            IKAdapterTokenizer it = new IKAdapterTokenizer(reader, useSmart);
            return new TokenStreamComponents(it);
        }finally {
            IOUtils.closeWhileHandlingException(reader);
        }
    }
}
