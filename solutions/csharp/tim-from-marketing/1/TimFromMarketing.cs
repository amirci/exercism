static class Badge
{
    public static string Print(int? id, string name, string? department)
    {
        var departmentLabel = department?.ToUpperInvariant() ?? "OWNER";
        var maybeIdPrefix = id is not null ? $"[{id}] - " : "";
        return $"{maybeIdPrefix}{name} - {departmentLabel}";
    }
}
