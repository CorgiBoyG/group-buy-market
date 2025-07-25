package cn.bugstack.types.common;

public class Constants {

    public final static String SPLIT = ",";
    public final static String UNDERLINE = "_";
    public final static String COLON = ":";

    public final static String DYNAMIC_CONFIG_CENTER_REDIS_TOPIC = "DYNAMIC_CONFIG_CENTER_REDIS_TOPIC_";

    public static String getTopic(String application) {
        return DYNAMIC_CONFIG_CENTER_REDIS_TOPIC + application;
    }
}
