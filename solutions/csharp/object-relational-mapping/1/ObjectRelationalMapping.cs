public class Orm : IDisposable
{
    private Database database;

    public Orm(Database database)
    {
        this.database = database;
    }

    public void Begin()
    {
        database.BeginTransaction();
    }

    public void Write(string data)
    {
        RunOrDispose(() => database.Write(data));
    }

    public void Commit()
    {
        RunOrDispose(database.EndTransaction);
    }

    public void Dispose() => database.Dispose();

    private void RunOrDispose(Action action)
    {
        try
        {
            action();
        }
        catch
        {
            Dispose();
        }
    }
}
