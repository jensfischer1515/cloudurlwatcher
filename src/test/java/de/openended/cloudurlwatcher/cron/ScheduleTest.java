package de.openended.cloudurlwatcher.cron;

import static org.junit.Assert.assertEquals;

import org.joda.time.Period;
import org.junit.Test;

public class ScheduleTest {

    @Test
    public void testMINUTELY() {
        Period period = Schedule.MINUTELY.getInterval().toPeriod();
        assertEquals(0, period.getMillis());
        assertEquals(0, period.getSeconds());
        assertEquals(1, period.getMinutes());
    }

    @Test
    public void testHOURLY() {
        Period period = Schedule.HOURLY.getInterval().toPeriod();
        assertEquals(0, period.getMillis());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMinutes());
        assertEquals(1, period.getHours());
    }

    @Test
    public void testDAILY() {
        Period period = Schedule.DAILY.getInterval().toPeriod();
        assertEquals(0, period.getMillis());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getHours());
        assertEquals(1, period.getDays());
    }

    @Test
    public void testWEEKLY() {
        Period period = Schedule.WEEKLY.getInterval().toPeriod();
        assertEquals(0, period.getMillis());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getDays());
        assertEquals(1, period.getWeeks());
    }

    @Test
    public void testMONTHLY() {
        Period period = Schedule.MONTHLY.getInterval().toPeriod();
        assertEquals(0, period.getMillis());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getWeeks());
        assertEquals(1, period.getMonths());
    }

    @Test
    public void testYEARLY() {
        Period period = Schedule.YEARLY.getInterval().toPeriod();
        assertEquals(0, period.getMillis());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getMonths());
        assertEquals(1, period.getYears());
    }
}
