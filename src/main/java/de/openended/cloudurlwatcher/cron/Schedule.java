package de.openended.cloudurlwatcher.cron;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public enum Schedule {

    MINUTELY {
        @Override
        protected DateTime start() {
            return end().minusMinutes(1);
        }

        @Override
        protected DateTime end() {
            return now().withMillisOfSecond(0).withSecondOfMinute(0);
        }
    },
    HOURLY {
        @Override
        protected DateTime start() {
            return end().minusHours(1);
        }

        @Override
        protected DateTime end() {
            return MINUTELY.end().withMinuteOfHour(0);
        }
    },
    DAILY {
        @Override
        protected DateTime start() {
            return end().minusDays(1);
        }

        @Override
        protected DateTime end() {
            return HOURLY.end().withHourOfDay(0);
        }
    },
    WEEKLY {
        @Override
        protected DateTime start() {
            return end().minusWeeks(1);
        }

        @Override
        protected DateTime end() {
            return DAILY.end().withDayOfWeek(1);
        }
    },
    MONTHLY {
        @Override
        protected DateTime start() {
            return end().minusMonths(1);
        }

        @Override
        protected DateTime end() {
            return DAILY.end().withDayOfMonth(1);
        }
    },
    YEARLY {
        @Override
        protected DateTime start() {
            return end().minusYears(1);
        }

        @Override
        protected DateTime end() {
            return DAILY.end().withDayOfYear(1);
        }
    };

    public Interval getInterval() {
        return new Interval(start(), end());
    }

    protected DateTime now() {
        return new DateTime();
    }

    protected abstract DateTime end();

    protected abstract DateTime start();

}
