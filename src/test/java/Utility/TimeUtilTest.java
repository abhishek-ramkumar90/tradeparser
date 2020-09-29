package Utility;

import org.junit.Assert;
import org.junit.Test;

public class TimeUtilTest {

    private int TIME_INTERVAL = 15;
    private long INITIAL_TIME_PERIOD = 1538409748332169137l;
    private long TIME_PERIOD = 1538409748332223252l;
    private long TIME_PERIOD_GREATER_THAN_INTERVAL = 1538409768683741023l;

    @Test
    public void testTimeInSeconds ( ) {
        Assert.assertTrue(TimeUtil.getSeconds(INITIAL_TIME_PERIOD) == 1538409748l);
    }

    @Test
    public void testTimeDifference ( ) {
        Assert.assertTrue(TimeUtil.getSeconds(INITIAL_TIME_PERIOD) - TimeUtil.getSeconds(TIME_PERIOD) == 0);
    }

    @Test
    public void testAbstractTimeDifference ( ) {
        Assert.assertTrue(TimeUtil.getTimeDifference(TimeUtil.getSeconds(INITIAL_TIME_PERIOD), TimeUtil.getSeconds(TIME_PERIOD_GREATER_THAN_INTERVAL)) > TIME_INTERVAL);
    }
}
