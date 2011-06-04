package de.openended.cloudurlwatcher.cron;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScheduleTest {
    private static final long MINUTE = 60 * 1000L;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    /** naive assumption */
    private static final long MONTH = DAY * 31;
    /** naive assumption */
    private static final long YEAR = DAY * 365;

    @Test
    public void testMINUTELY() {
        assertEquals(MINUTE, Schedule.MINUTELY.getInterval().toDurationMillis());
    }

    @Test
    public void testHOURLY() {
        assertEquals(HOUR, Schedule.HOURLY.getInterval().toDurationMillis());
    }

    @Test
    public void testDAILY() {
        assertEquals(DAY, Schedule.DAILY.getInterval().toDurationMillis());
    }

    @Test
    public void testWEEKLY() {
        assertEquals(WEEK, Schedule.WEEKLY.getInterval().toDurationMillis());
    }

    @Test
    public void testMONTHLY() {

    }

    @Test
    public void testYEARLY() {

    }
}
