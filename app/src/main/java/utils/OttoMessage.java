package utils;

/**
 * Created by neikila on 15.12.15.
 */
public class OttoMessage {
    protected MessageType messageType;

    public MessageType getMessageType() {
        return messageType;
    }

    public enum MessageType {
        ResultArticle,
        CleanSuccess,
        BitmapReady,
        UpdateAdapter
    }

    protected OttoMessage() {}
}
