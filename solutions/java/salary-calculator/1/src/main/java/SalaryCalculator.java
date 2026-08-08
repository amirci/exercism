public class SalaryCalculator {
    private static final double BASE_SALARY = 1000.0;
    private static final double MAXIMUM_SALARY = 2000.0;

    public double salaryMultiplier(int daysSkipped) {
        return daysSkipped < 5 ? 1.0 : 0.85;
    }

    public int bonusMultiplier(int productsSold) {
        return productsSold < 20 ? 10 : 13;
    }

    public double bonusForProductsSold(int productsSold) {
        return productsSold * bonusMultiplier(productsSold);
    }

    public double finalSalary(int daysSkipped, int productsSold) {
        var salary = BASE_SALARY * salaryMultiplier(daysSkipped) + bonusForProductsSold(productsSold);

        return salary > MAXIMUM_SALARY ? MAXIMUM_SALARY : salary;
    } 
}
