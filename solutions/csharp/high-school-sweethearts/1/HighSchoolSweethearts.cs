using System.Globalization;

public static class HighSchoolSweethearts
{
    public static string DisplaySingleLine(string studentA, string studentB) =>
        $"{studentA} ♡ {studentB}".PadLeft(41).PadRight(61);

    public static string DisplayBanner(string studentA, string studentB) =>
$@"
     ******       ******
   **      **   **      **
 **         ** **         **
**            *            **
**                         **
**     {studentA} +  {studentB}    **
 **                       **
   **                   **
     **               **
       **           **
         **       **
           **   **
             ***
              *";

    public static string DisplayGermanExchangeStudents(string studentA , string studentB, DateTime start, float hours)
    {
        var culture = CultureInfo.GetCultureInfo("de-DE");
        return string.Format(
            culture,
            "{0} and {1} have been dating since {2:d} - that's {3:N2} hours",
            studentA,
            studentB,
            start,
            hours);
    }
}
