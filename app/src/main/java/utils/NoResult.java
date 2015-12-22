package utils;

import java.util.ArrayList;
import java.util.List;

import wikipedia.Article;

/**
 * Created by neikila on 15.12.15.
 */
public class NoResult extends OttoMessage {
    public NoResult() {
        messageType = MessageType.NoResult;
    }
}
