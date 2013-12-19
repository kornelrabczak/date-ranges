package pl.thecookiezen.date.ranges;

import com.google.common.base.Optional;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;

/**
 * @author Korneliusz Rabczak
 */
public class RangeTest {

    @Test
    public void shouldFindIntersectionBetweenTwoDatesInterval() {
        // given
        Interval interval1 = new Interval(
                new DateTime(), new DateTime()
        );

        Interval interval2 = new Interval(
                new DateTime(), new DateTime()
        );

        // when
        if (interval1.abuts(interval2) || interval1.overlaps(interval2)) {
            Interval actual = interval1.overlap(interval2);
        }

        // then

    }

    @Data
    private static class DateInterval {
        private Optional<DateTime> from;
        private Optional<DateTime> to;
        private Optional<Interval> interval;

        public DateInterval(Optional<DateTime> from, Optional<DateTime> to) {
            if (from.isPresent() && to.isPresent()) {
                interval = Optional.of(new Interval(from.get(), to.get()));
            }
            this.from = from;
            this.to = to;
        }

        public DateInterval join(DateInterval interval) {
            Optional<DateTime> from = Optional.absent();
            Optional<DateTime> to = Optional.absent();
            if (this.from.isPresent() && interval.getFrom().isPresent()) {
                long min = Math.min(this.from.get().getMillis(), interval.getFrom().get().getMillis());
                from = Optional.of(new DateTime(min));
            }
            if (this.to.isPresent() && interval.getFrom().isPresent()) {
                long max = Math.min(this.to.get().getMillis(), interval.getTo().get().getMillis());
                to = Optional.of(new DateTime(max));
            }
            return new DateInterval(from, to);
        }

        public boolean isConnected(DateInterval dateInterval) {
            if (interval.isPresent() && dateInterval.getInterval().isPresent()) {
                return interval.get().abuts((dateInterval.getInterval().get()))
                        || interval.get().overlaps(dateInterval.getInterval().get());
            }

            return getFromMilis() >= dateInterval.getFromMilis() && getToMilis() <= dateInterval.getToMilis()
                    || getFromMilis() <= dateInterval.getFromMilis() && getToMilis() >= dateInterval.getToMilis()
                    || getFromMilis() <= dateInterval.getFromMilis() && getToMilis() >= dateInterval.getFromMilis();
        }

        private long getFromMilis() {
            if (from.isPresent()) {
                return from.get().getMillis();
            }
            return Long.MIN_VALUE;
        }

        private long getToMilis() {
            if (to.isPresent()) {
                return to.get().getMillis();
            }
            return Long.MAX_VALUE;
        }

    }
}
