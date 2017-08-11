package xyz.strikezero.summary;

import java.io.IOException;

/**
 * Created by junji on 2017/8/7.
 */
public interface Summary {
    public String getSummary(String query, String text) throws IOException;
}
