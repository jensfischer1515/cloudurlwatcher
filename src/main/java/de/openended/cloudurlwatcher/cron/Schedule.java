package de.openended.cloudurlwatcher.cron;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public enum Schedule {

    MINUTELY {
        @Override
        public Interval getInterval() {
            DateTime end = getCurrentDateTime().withMillisOfSecond(0).withSecondOfMinute(0);
            DateTime start = end.minusMinutes(1);
            return new Interval(start, end);
        }
    },
    HOURLY {
        @Override
        public Interval getInterval() {
            DateTime end = getCurrentDateTime().withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0);
            DateTime start = end.minusHours(1);
            return new Interval(start, end);
        }
    },
    DAILY {
        @Override
        public Interval getInterval() {
            DateTime end = getCurrentDateTime().withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(0);
            DateTime start = end.minusDays(1);
            return new Interval(start, end);
        }
    },
    WEEKLY {
        @Override
        public Interval getInterval() {
            DateTime end = getCurrentDateTime().withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(0)
                    .withDayOfWeek(1);
            DateTime start = end.minusWeeks(1);
            return new Interval(start, end);
        }
    },
    MONTHLY {
        @Override
        public Interval getInterval() {
            DateTime end = getCurrentDateTime().withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(0)
                    .withDayOfMonth(1);
            DateTime start = end.minusMonths(1);
            return new Interval(start, end);
        }
    },
    YEARLY {
        @Override
        public Interval getInterval() {
            DateTime end = getCurrentDateTime().withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(0)
                    .withDayOfYear(1);
            DateTime start = end.minusYears(1);
            return new Interval(start, end);
        }
    };

    protected DateTime getCurrentDateTime() {
        return new DateTime();
    }

    public abstract Interval getInterval();

}
