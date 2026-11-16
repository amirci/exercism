import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class SwiftScheduling {
    private record EndOfWeekTarget(DayOfWeek day, LocalTime time) {}

    private static final EndOfWeekTarget FRIDAY_AT_5 =
            new EndOfWeekTarget(DayOfWeek.FRIDAY, LocalTime.of(17, 0));
    private static final EndOfWeekTarget SUNDAY_AT_8 =
            new EndOfWeekTarget(DayOfWeek.SUNDAY, LocalTime.of(20, 0));

    public static LocalDateTime convertToDeliveryDate(LocalDateTime meetingStart, String description) {
        return switch (description) {
            case "NOW" -> meetingStart.plusHours(2);
            case "ASAP" -> asapDate(meetingStart);
            case "EOW" -> endOfWeek(meetingStart);
            default -> variableDate(meetingStart, description);
        };
    }

    private static LocalDateTime asapDate(LocalDateTime meetingStart) {
        var local = meetingStart.toLocalDate();
        return meetingStart.getHour() < 13 ? local.atTime(17, 0) : local.plusDays(1).atTime(13, 0);
    }

    private static LocalDateTime endOfWeek(LocalDateTime meetingStart) {
        var day = meetingStart.getDayOfWeek();
        var local = meetingStart.toLocalDate();
        var target = day.getValue() <= DayOfWeek.WEDNESDAY.getValue() ? FRIDAY_AT_5 : SUNDAY_AT_8;
        return local.with(TemporalAdjusters.nextOrSame(target.day())).atTime(target.time());
    }

    private static LocalDateTime variableDate(LocalDateTime meetingStart, String description) {
        if (description.endsWith("M")) {
            return deliveryDateForMonth(meetingStart, description);
        }

        var quarter = Integer.parseInt(description.substring(1));
        var currentQuarter = (meetingStart.getMonthValue() - 1) / 3 + 1;
        var year = meetingStart.getYear() + (currentQuarter <= quarter ? 0 : 1);
        return lastWorkdayOfQuarter(year, quarter).atTime(8, 0);
    }

    private static LocalDateTime deliveryDateForMonth(LocalDateTime meetingStart, String description) {
        var month = Integer.parseInt(description.substring(0, description.length() - 1));
        var year = meetingStart.getYear() + (meetingStart.getMonthValue() < month ? 0 : 1);
        return firstWorkdayOfMonth(year, month).atTime(8, 0);
    }

    private static LocalDate firstWorkdayOfMonth(int year, int month) {
        return workdayOnOrAfter(LocalDate.of(year, month, 1));
    }

    private static LocalDate workdayOnOrAfter(LocalDate date) {
        while (isWeekend(date)) {
            date = date.plusDays(1);
        }
        return date;
    }

    private static LocalDate lastWorkdayOfQuarter(int year, int quarter) {
        var month = quarter * 3;
        var date = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        return workdayOnOrBefore(date);
    }

    private static LocalDate workdayOnOrBefore(LocalDate date) {
        while (isWeekend(date)) {
            date = date.minusDays(1);
        }
        return date;
    }

    private static boolean isWeekend(LocalDate date) {
        var day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
