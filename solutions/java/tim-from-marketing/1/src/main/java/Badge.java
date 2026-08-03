class Badge {
    public String print(Integer id, String name, String department) {
        var departmentLabel = department == null ? "OWNER" : department.toUpperCase();
        var idPrefix = id == null ? "" : "[" + id + "] - ";

        return idPrefix + name + " - " + departmentLabel;
    }
}
