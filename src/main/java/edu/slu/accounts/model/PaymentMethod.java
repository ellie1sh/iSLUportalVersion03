package edu.slu.accounts.model;

/**
 * Payment method model class representing available payment channels
 */
public class PaymentMethod {
    private String id;
    private String methodName;
    private String methodCode;
    private double serviceFee;
    private double percentageFee;
    private boolean isActive;
    private String description;

    // Constructors
    public PaymentMethod() {
        this.isActive = true;
    }

    public PaymentMethod(String methodName, String methodCode, double serviceFee, double percentageFee) {
        this();
        this.methodName = methodName;
        this.methodCode = methodCode;
        this.serviceFee = serviceFee;
        this.percentageFee = percentageFee;
    }

    public PaymentMethod(String methodName, String methodCode, double serviceFee, 
                        double percentageFee, String description) {
        this(methodName, methodCode, serviceFee, percentageFee);
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getMethodCode() { return methodCode; }
    public void setMethodCode(String methodCode) { this.methodCode = methodCode; }

    public double getServiceFee() { return serviceFee; }
    public void setServiceFee(double serviceFee) { this.serviceFee = serviceFee; }

    public double getPercentageFee() { return percentageFee; }
    public void setPercentageFee(double percentageFee) { this.percentageFee = percentageFee; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Business methods
    public double calculateTotalFee(double baseAmount) {
        double percentageFeeAmount = (baseAmount * percentageFee) / 100.0;
        return serviceFee + percentageFeeAmount;
    }

    public double calculateTotalAmount(double baseAmount) {
        return baseAmount + calculateTotalFee(baseAmount);
    }

    public PaymentCalculation calculatePayment(double baseAmount) {
        double percentageFeeAmount = (baseAmount * percentageFee) / 100.0;
        double totalFees = serviceFee + percentageFeeAmount;
        double totalAmount = baseAmount + totalFees;

        return new PaymentCalculation(baseAmount, serviceFee, percentageFeeAmount, totalFees, totalAmount);
    }

    // Helper class for payment calculations
    public static class PaymentCalculation {
        private final double baseAmount;
        private final double serviceFee;
        private final double percentageFee;
        private final double totalFees;
        private final double totalAmount;

        public PaymentCalculation(double baseAmount, double serviceFee, double percentageFee, 
                                double totalFees, double totalAmount) {
            this.baseAmount = baseAmount;
            this.serviceFee = serviceFee;
            this.percentageFee = percentageFee;
            this.totalFees = totalFees;
            this.totalAmount = totalAmount;
        }

        public double getBaseAmount() { return baseAmount; }
        public double getServiceFee() { return serviceFee; }
        public double getPercentageFee() { return percentageFee; }
        public double getTotalFees() { return totalFees; }
        public double getTotalAmount() { return totalAmount; }

        @Override
        public String toString() {
            return String.format("PaymentCalculation{baseAmount=%.2f, serviceFee=%.2f, " +
                               "percentageFee=%.2f, totalFees=%.2f, totalAmount=%.2f}", 
                               baseAmount, serviceFee, percentageFee, totalFees, totalAmount);
        }
    }

    @Override
    public String toString() {
        return String.format("PaymentMethod{id='%s', name='%s', code='%s', " +
                           "serviceFee=%.2f, percentageFee=%.2f%%, active=%s}", 
                           id, methodName, methodCode, serviceFee, percentageFee, isActive);
    }
}