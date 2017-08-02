package xyz.strikezero.cache;

import xyz.strikezero.config.Config;
import xyz.strikezero.model.WebPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junji on 2017/7/23.
 */
public abstract class Cache {
    private Map<String, Integer> index;
    private List<WebPage[]> cache;
    private List<String> list;
    //private int curNum;

    public Cache() {
        this.index = new HashMap<String, Integer>();
        this.cache = new ArrayList<WebPage[]>(Config.MAX_CACHE_NUM);
        this.list = new ArrayList<String>(Config.MAX_CACHE_NUM);
        //this.curNum = 0;
    }

    public boolean contains(String query) {
        if(index.containsKey(query)){
            ifContains(index.get(query));
            return true;
        }
        return false;
    }

    protected boolean needReplacement() {
        //return curNum == Config.MAX_CACHE_NUM;
        return cache.size() == Config.MAX_CACHE_NUM;
    }

    public WebPage[] get(String query){
        int i = index.get(query);
        return cache.get(i);
    }

    public abstract void add(String query, WebPage[] results);

    protected Map<String, Integer> getIndex() {
        return index;
    }


    protected List<WebPage[]> getCache() {
        return cache;
    }

    //if contains ..., then we do something
    protected void ifContains(int i){}


    /*protected int getCurNum() {
        return curNum;
    }

    protected void setCurNum(int curNum) {
        this.curNum = curNum;
    }*/

    protected List<String> getList() {
        return list;
    }

}
