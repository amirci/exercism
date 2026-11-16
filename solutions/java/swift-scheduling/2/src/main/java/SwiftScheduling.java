import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class SwiftScheduling {
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
        if (day.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
            var friday = meetingStart.toLocalDate().plusDays(DayOfWeek.FRIDAY.getValue() - day.getValue());
            return friday.atTime(17, 0);
        }

        var sunday = meetingStart.toLocalDate().plusDays(DayOfWeek.SUNDAY.getValue() - day.getValue());
        return sunday.atTime(20, 0);
    }

    private static LocalDateTime variableDate(LocalDateTime meetingStart, String description) {
        if (description.endsWith("M")) {
            var month = Integer.parseInt(description.substring(0, description.length() - 1));
            var year = meetingStart.getMonthValue() < month
                    ? meetingStart.getYear()
                    : meetingStart.getYear() + 1;
            return firstWorkdayOfMonth(year, month).atTime(8, 0);
        }

        var quarter = Integer.parseInt(description.substring(1));
        var currentQuarter = (meetingStart.getMonthValue() - 1) / 3 + 1;
        var year = currentQuarter <= quarter
                ? meetingStart.getYear()
                : meetingStart.getYear() + 1;
        return lastWorkdayOfQuarter(year, quarter).atTime(8, 0);
    }

    private static LocalDate firstWorkdayOfMonth(int year, int month) {
        var date = LocalDate.of(year, month, 1);
        while (isWeekend(date)) {
            date = date.plusDays(1);
        }
        return date;
    }

    private static LocalDate lastWorkdayOfQuarter(int year, int quarter) {
        var month = quarter * 3;
        var date = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
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
