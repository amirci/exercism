public static class SquareRoot
{
    public static int Root(int number) =>
        number == 1
            ? 1
            : Enumerable.Range(1, number / 2)
                .First(candidate => candidate * candidate == number);
}
