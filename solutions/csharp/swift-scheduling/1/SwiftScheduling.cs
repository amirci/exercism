public static class SwiftScheduling
{
    public static DateTime DeliveryDate(DateTime meetingStart, string description)
    {
        return description switch
        {
            "NOW" => meetingStart.AddHours(2),
            "ASAP" when IsBeforeOnePm(meetingStart) => meetingStart.Date + FivePm,
            "ASAP" => meetingStart.Date.AddDays(1) + OnePm,
            "EOW" when IsMondayToWednesday(meetingStart) => EndOfWeekFriday(meetingStart),
            "EOW" => EndOfWeekSunday(meetingStart),
            ['Q', var quarter] => QuarterDeliveryDate(meetingStart, quarter - '0'),
            var d when d.EndsWith('M') => MonthDeliveryDate(meetingStart, int.Parse(d[..^1])),
            _ => default,
        };
    }

    private static bool IsBeforeOnePm(DateTime dt) =>
        dt.TimeOfDay < OnePm;

    private static bool IsMondayToWednesday(DateTime dt) =>
        dt.DayOfWeek is DayOfWeek.Monday or DayOfWeek.Tuesday or DayOfWeek.Wednesday;

    private static DateTime EndOfWeekFriday(DateTime dt) =>
        dt.Date.AddDays(DaysUntil(dt, DayOfWeek.Friday)) + FivePm;

    private static DateTime EndOfWeekSunday(DateTime dt) =>
        dt.Date.AddDays(DaysUntil(dt, DayOfWeek.Sunday)) + EightPm;

    private static DateTime MonthDeliveryDate(DateTime meetingStart, int month)
    {
        var year = meetingStart.Month < month ? meetingStart.Year : meetingStart.Year + 1;
        var firstDay = new DateTime(year, month, 1, 8, 0, 0);

        return FirstWorkday(firstDay);
    }

    private static DateTime QuarterDeliveryDate(DateTime meetingStart, int quarter)
    {
        var year = CurrentQuarter(meetingStart) <= quarter ? meetingStart.Year : meetingStart.Year + 1;
        var month = quarter * 3;
        var lastDay = new DateTime(year, month, DateTime.DaysInMonth(year, month), 8, 0, 0);

        return LastWorkday(lastDay);
    }

    private static int CurrentQuarter(DateTime dt) =>
        ((dt.Month - 1) / 3) + 1;

    private static DateTime FirstWorkday(DateTime dt) =>
        dt.DayOfWeek switch
        {
            DayOfWeek.Saturday => dt.AddDays(2),
            DayOfWeek.Sunday => dt.AddDays(1),
            _ => dt,
        };

    private static DateTime LastWorkday(DateTime dt) =>
        dt.DayOfWeek switch
        {
            DayOfWeek.Saturday => dt.AddDays(-1),
            DayOfWeek.Sunday => dt.AddDays(-2),
            _ => dt,
        };

    private static int DaysUntil(DateTime dt, DayOfWeek dayOfWeek) =>
        ((int)dayOfWeek - (int)dt.DayOfWeek + 7) % 7;

    private static readonly TimeSpan OnePm = new(13, 0, 0);
    private static readonly TimeSpan FivePm = new(17, 0, 0);
    private static readonly TimeSpan EightPm = new(20, 0, 0);
}
