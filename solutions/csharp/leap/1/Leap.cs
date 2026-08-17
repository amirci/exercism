public static class Leap
{
    public static bool IsLeapYear(int year)
    {
        bool IsMultipleOf(int divisor) => year % divisor == 0;

        return IsMultipleOf(4) && (!IsMultipleOf(100) || IsMultipleOf(400));
    }
}
