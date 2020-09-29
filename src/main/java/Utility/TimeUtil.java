package Utility;

public class TimeUtil {

    public static long getSeconds (long time) {
        time /= 1000000000l;
        return time;
    }

    public static long getTimeDifference (long currentTradeTime, long intervalTradeTime) {
        return Math.abs(currentTradeTime - intervalTradeTime);
    }
}
