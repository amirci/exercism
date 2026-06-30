public static class Languages
{
    public static List<string> NewList() => [];

    public static List<string> GetExistingLanguages() => [MostExcitingLanguage, "Clojure", "Elm"];

    public static List<string> AddLanguage(List<string> languages, string language)
    {
        languages.Add(language);
        return languages;
    }

    public static int CountLanguages(List<string> languages) => languages.Count;

    public static bool HasLanguage(List<string> languages, string language) => languages.Contains(language);

    public static List<string> ReverseList(List<string> languages)
    {
        var copy = languages.ToList();
        copy.Reverse();
        return copy;
    }

    public static bool IsExciting(List<string> languages) =>
        languages is [MostExcitingLanguage, ..] or [_, MostExcitingLanguage] or [_, MostExcitingLanguage, _];

    public static List<string> RemoveLanguage(List<string> languages, string language)
    {
        languages.Remove(language);
        return languages;
    }

    public static bool IsUnique(List<string> languages) => CountLanguages(languages) == languages.Distinct().Count();

    private const string MostExcitingLanguage = "C#";
}
