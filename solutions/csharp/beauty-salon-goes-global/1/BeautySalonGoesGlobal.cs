using System.Collections.Generic;
using System.Globalization;
using System.Runtime.InteropServices;

public enum Location
{
    NewYork,
    London,
    Paris
}

public enum AlertLevel
{
    Early,
    Standard,
    Late
}

public static class Appointment
{
    private static readonly Dictionary<Location, CultureInfo> LocationToCulture = new()
    {
        { Location.NewYork, CultureInfo.GetCultureInfo("en-US") },
        { Location.London, CultureInfo.GetCultureInfo("en-GB") },
        { Location.Paris, CultureInfo.GetCultureInfo("fr-FR") },
    };

    private static readonly Dictionary<Location, string> UnixTimeZoneIds = new()
    {
        { Location.NewYork, "America/New_York" },
        { Location.London, "Europe/London" },
        { Location.Paris, "Europe/Paris" },
    };

    private static readonly Dictionary<Location, string> WindowsTimeZoneIds = new()
    {
        { Location.NewYork, "Eastern Standard Time" },
        { Location.London, "GMT Standard Time" },
        { Location.Paris, "W. Europe Standard Time" },
    };

    private static readonly Dictionary<Location, string> LocationToTimeZoneId =
        RuntimeInformation.IsOSPlatform(OSPlatform.Windows) ? WindowsTimeZoneIds : UnixTimeZoneIds;

    private static readonly Dictionary<AlertLevel, TimeSpan> AlertOffsets = new()
    {
        { AlertLevel.Early, TimeSpan.FromDays(1) },
        { AlertLevel.Standard, new TimeSpan(1, 45, 0) },
        { AlertLevel.Late, TimeSpan.FromMinutes(30) },
    };

    public static DateTime ShowLocalTime(DateTime dtUtc) =>
        TimeZoneInfo.ConvertTimeFromUtc(dtUtc, TimeZoneInfo.Local);

    public static DateTime Schedule(string dateToParse, Location location)
    {
        var localTime = DateTime.Parse(dateToParse);
        var timeZone = TimeZoneFor(location);

        return TimeZoneInfo.ConvertTimeToUtc(localTime, timeZone);
    }

    public static DateTime GetAlertTime(DateTime appointment, AlertLevel alertLevel) =>
        appointment - AlertOffsets[alertLevel];

    public static bool HasDaylightSavingChanged(DateTime dt, Location location)
    {
        var timeZone = TimeZoneFor(location);

        return timeZone.IsDaylightSavingTime(dt) != timeZone.IsDaylightSavingTime(dt.AddDays(-7));
    }

    public static DateTime NormalizeDateTime(string dtStr, Location location) =>
        DateTime.TryParse(dtStr, LocationToCulture[location], DateTimeStyles.None, out var dateTime)
            ? dateTime
            : DateTime.MinValue;

    private static TimeZoneInfo TimeZoneFor(Location location) =>
        TimeZoneInfo.FindSystemTimeZoneById(LocationToTimeZoneId[location]);
}
