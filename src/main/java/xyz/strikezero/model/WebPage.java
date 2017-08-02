package xyz.strikezero.model;

import org.apache.lucene.document.*;
import org.apache.lucene.index.Term;

/**
 * Created by junji on 2017/7/19.
 */
public class WebPage {
    private long id;
    private String url;
    private String title;
    private String content;
    private double score;

    public WebPage() {
    }

    public WebPage(long id, String url, String title, String content, double score) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.content = content;
        this.score = score;
    }

    public WebPage(Document doc) {
        this.id = 0;
        this.url = doc.get("url");
        this.title = doc.get("title");
        this.content = doc.get("content");
        this.score = 0.0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.add(new LongPoint("id", this.id));
        doc.add(new StringField("url", this.url, Field.Store.YES));
        doc.add(new TextField("title", this.title, Field.Store.YES));
        doc.add(new TextField("content", this.content, Field.Store.YES));
        doc.add(new DoublePoint("score", this.score));
        return doc;
    }

    public Term primaryKey() {
        return new Term("url", this.url);
    }

    @Override
    public String toString() {
        return "WebPage{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                '}';
    }
}
