package expOne;

public class Definition {

    private double topPerformersThreshold;
    private String useExperienceMultiplier;
    private double periodLimit;

    public Definition() {
    }

    public Definition(double topPerformersThreshold, String useExperienceMultiplier, double periodLimit) {
        this.topPerformersThreshold = topPerformersThreshold;
        this.useExperienceMultiplier = useExperienceMultiplier;
        this.periodLimit = periodLimit;
    }

    public double getTopPerformersThreshold() {
        return topPerformersThreshold;
    }

    public void setTopPerformersThreshold(double topPerformersThreshold) {
        this.topPerformersThreshold = topPerformersThreshold;
    }

    public String getUseExperienceMultiplier() {
        return useExperienceMultiplier;
    }

    public void setUseExperienceMultiplier(String useExperienceMultiplier) {
        this.useExperienceMultiplier = useExperienceMultiplier;
    }

    public double getPeriodLimit() {
        return periodLimit;
    }

    public void setPeriodLimit(double periodLimit) {
        this.periodLimit = periodLimit;
    }
}
