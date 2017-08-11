package xyz.strikezero.summary;

import java.io.IOException;

/**
 * Created by junji on 2017/8/10.
 * example url : https://www.ithome.com/html/game/320563.htm
 */
public class BM25Test {
    public static void main(String[] args) {
        String query = "我的世界在游戏机上发布";
        String text = "IT之家8月11日消息 ，根据外媒报道，成功推出《我的世界：故事模式》第一季和第二季第一章后，Telltale Games工作室日前确定该游戏第二季第二章将于8月15日发布。" +
                "据悉，这款游戏是Mojang与Telltale Games工作室合作，基于人气作品《我的世界》开发的剧情章节式游戏。" +
                "第一季与去年推出，受到了不少好评，而第二季第一章也已经发布。" +
                "Telltale表示，在第二季中玩家可以继续第一季的冒险，也可以选择从头开始。" +
                "该公司同时也确认整个第一季的《我的世界：游戏模式》将在8月22日上架任天堂游戏机，售价39.99美元（约合270人民币）。";
        BM25 bm = new BM25();
        System.out.println(bm.getSummary(query, text));
    }
}
