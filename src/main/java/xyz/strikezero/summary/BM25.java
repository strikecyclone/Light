package xyz.strikezero.summary;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Created by junji on 2017/8/7.
 */
public class BM25 implements Summary {
    public static double k1 = 2.0;
    public static double k2 = 1.0;
    public static double b = 0.75;

    public static String regEx = "。|！|？|；";

    private Analyzer analyzer = new IKAnalyzer();

    public String getSummary(String query, String text){
        String[] sentences = text.split(regEx);
        int sentNum = sentences.length;
        double[] scores = new double[sentNum];
        double[] dl = new double[sentNum];
        List<Double> qf = new ArrayList<Double>();

        Map<String, Integer> queries = new HashMap<String, Integer>();
        StringReader reader = new StringReader(query);
        IKSegmenter ik = new IKSegmenter(reader, true);
        Lexeme lex = null;
        int index = 0;
        try {
            while ((lex = ik.next()) != null) {
                if (queries.containsKey(lex.getLexemeText())) {
                    double queryCount = qf.get(queries.get(lex.getLexemeText()));
                    qf.set(queries.get(lex.getLexemeText()), queryCount + 1);
                } else {
                    queries.put(lex.getLexemeText(), index);
                    qf.add(1.0);
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int qNum = queries.size();
        int[] nq = new int[qNum];
        double[] idf = new double[qNum];
        double avgdl = (double) text.length() / (double) sentNum;
        double[][] f = new double[sentNum][qNum];

        for (int i = 0; i < sentences.length; i++) {
            boolean flag = false;
            StringReader sr = new StringReader(sentences[i]);
            IKSegmenter iks = new IKSegmenter(sr, true);
            Lexeme lexeme = null;
            try {
                while ((lexeme = iks.next()) != null) {
                    Iterator iter = queries.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String keyword = (String) (entry.getKey());
                        int loc = (Integer) (entry.getValue());
                        if (lexeme.getLexemeText().equals(keyword)) {
                            f[i][loc] += 1;
                            if (!flag) {
                                flag = true;
                                nq[loc] += 1;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < qNum; i++)
            idf[i] = Math.log((sentNum - nq[i] + 0.5) / (nq[i] + 0.5));

        double maxScore = 0.0;
        int maxLocation = 0;
        for (int i = 0; i < sentences.length; i++) {
            dl[i] = sentences[i].length();
            double K = k1 * (1 - b + b * (dl[i] / avgdl));
            for (int j = 0; j < qNum; j++) {
                scores[i] += idf[j] * (f[i][j] / dl[i]) * (k1 + 1) * (qf.get(j) / query.length()) * (k2 + 1) / (((f[i][j] / dl[i]) + K) * ((qf.get(j) / query.length()) + k2));
            }
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxLocation = i;
            }
        }
        return sentences[maxLocation];
    }
}
