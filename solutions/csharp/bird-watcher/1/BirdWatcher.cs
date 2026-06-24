class BirdCount(int[] birdsPerDay)
{
    private readonly int[] birdsPerDay = birdsPerDay;

    public static int[] LastWeek() => [0, 2, 5, 3, 7, 8, 4];

    public int Today() => birdsPerDay[^1];

    public void IncrementTodaysCount() => birdsPerDay[^1]++;

    public bool HasDayWithoutBirds() => birdsPerDay.Any(count => count == 0);

    public int CountForFirstDays(int numberOfDays) => birdsPerDay.Take(numberOfDays).Sum();

    public int BusyDays() => birdsPerDay.Count(count => count >= 5);
}
