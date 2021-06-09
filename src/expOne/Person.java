package expOne;

public class Person {

    private String name;
    private double totalSales;
    private double salesPeriod;
    private double experienceMultiplier;

    public Person() {
    }

    public Person(String name, double totalSales, double salesPeriod, double experienceMultiplier) {
        this.name = name;
        this.totalSales = totalSales;
        this.salesPeriod = salesPeriod;
        this.experienceMultiplier = experienceMultiplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getSalesPeriod() {
        return salesPeriod;
    }

    public void setSalesPeriod(double salesPeriod) {
        this.salesPeriod = salesPeriod;
    }

    public double getExperienceMultiplier() {
        return experienceMultiplier;
    }

    public void setExperienceMultiplier(double experienceMultiplier) {
        this.experienceMultiplier = experienceMultiplier;
    }
}
