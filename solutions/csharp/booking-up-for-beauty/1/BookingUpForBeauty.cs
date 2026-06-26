static class Appointment
{
    /// <summary>
    /// Parses a textual appointment date into a <see cref="DateTime"/>.
    /// </summary>
    /// <param name="appointmentDateDescription">
    /// A date and time string in an en-US format, such as
    /// "7/25/2019 13:45:00", "June 3, 2019 11:30:00", or
    /// "Thursday, December 5, 2019 09:00:00".
    /// </param>
    /// <returns>
    /// The matching appointment date and time.
    /// For example, "7/25/2019 13:45:00" should return
    /// <c>new DateTime(2019, 7, 25, 13, 45, 0)</c>.
    /// </returns>
    public static DateTime Schedule(string appointmentDateDescription) => DateTime.Parse(appointmentDateDescription);

    /// <summary>
    /// Checks whether an appointment date is before the current date and time.
    /// </summary>
    /// <param name="appointmentDate">The appointment date to compare with <see cref="DateTime.Now"/>.</param>
    /// <returns>
    /// <c>true</c> for appointments in the past, such as <c>DateTime.Now.AddMinutes(-1)</c>;
    /// <c>false</c> for appointments in the future, such as <c>DateTime.Now.AddMinutes(1)</c>.
    /// </returns>
    public static bool HasPassed(DateTime appointmentDate) => appointmentDate < DateTime.Now;

    /// <summary>
    /// Checks whether an appointment starts in the afternoon.
    /// </summary>
    /// <param name="appointmentDate">The appointment date and time to inspect.</param>
    /// <returns>
    /// <c>true</c> when the time is greater than or equal to 12:00:00 and less than 18:00:00.
    /// For example, 12:00:00 and 17:59:59 should return <c>true</c>, while
    /// 11:59:59 and 18:00:00 should return <c>false</c>.
    /// </returns>
    public static bool IsAfternoonAppointment(DateTime appointmentDate) => appointmentDate.Hour is >= 12 and < 18;

    /// <summary>
    /// Builds the customer-facing appointment description.
    /// </summary>
    /// <param name="appointmentDate">The appointment date and time to describe.</param>
    /// <returns>
    /// A sentence using the appointment's en-US date and time formatting.
    /// For example, <c>new DateTime(2019, 3, 29, 15, 0, 0)</c> should return
    /// <c>"You have an appointment on 3/29/2019 3:00:00 PM."</c>.
    /// </returns>
    public static string Description(DateTime appointmentDate)
    {
        return appointmentDate.ToString("'You have an appointment on' M/d/yyyy h:mm:ss tt'.'");
    }

    /// <summary>
    /// Returns the salon's anniversary date for the current year.
    /// </summary>
    /// <returns>
    /// September 15 of <see cref="DateTime.Now"/>'s year at midnight.
    /// For example, when the current year is 2026, this should return
    /// <c>new DateTime(2026, 9, 15)</c>.
    /// </returns>
    public static DateTime AnniversaryDate() => new(DateTime.Now.Year, 9, 15);
}
