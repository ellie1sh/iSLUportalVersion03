package edu.slu.accounts.service;

import edu.slu.accounts.model.PaymentMethod;
import edu.slu.accounts.util.IdGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Service for handling payment processing and payment methods
 */
public class PaymentService {
    private final List<PaymentMethod> paymentMethods;
    private final AccountService accountService;

    public PaymentService() {
        this.accountService = new AccountService();
        this.paymentMethods = initializePaymentMethods();
    }

    /**
     * Initialize available payment methods
     */
    private List<PaymentMethod> initializePaymentMethods() {
        return Arrays.asList(
            new PaymentMethod("UnionBank UPay Online", "UNIONBANK", 0.0, 0.0, 
                            "Direct bank transfer with no additional fees"),
            new PaymentMethod("Dragonpay Payment Gateway", "DRAGONPAY", 25.0, 2.0, 
                            "Multi-channel payment gateway with service fee"),
            new PaymentMethod("BPI Online", "BPI", 15.0, 0.0, 
                            "BPI online banking with minimal service fee"),
            new PaymentMethod("BDO Online", "BDO", 20.0, 0.0, 
                            "BDO online banking with service fee"),
            new PaymentMethod("BDO Bills Payment", "BDO_BILLS", 10.0, 0.0, 
                            "BDO bills payment service"),
            new PaymentMethod("Bukas Tuition Installment Plans", "BUKAS", 0.0, 3.5, 
                            "Flexible installment plans with processing fee")
        );
    }

    /**
     * Get all available payment methods
     */
    public List<PaymentMethod> getAvailablePaymentMethods() {
        return paymentMethods.stream()
                           .filter(PaymentMethod::isActive)
                           .toList();
    }

    /**
     * Get payment method by code
     */
    public Optional<PaymentMethod> getPaymentMethodByCode(String methodCode) {
        return paymentMethods.stream()
                           .filter(method -> method.getMethodCode().equals(methodCode))
                           .findFirst();
    }

    /**
     * Calculate payment amount with fees
     */
    public PaymentMethod.PaymentCalculation calculatePayment(String methodCode, double baseAmount) {
        Optional<PaymentMethod> methodOpt = getPaymentMethodByCode(methodCode);
        
        if (methodOpt.isPresent()) {
            return methodOpt.get().calculatePayment(baseAmount);
        }
        
        // Return calculation with no fees if method not found
        return new PaymentMethod.PaymentCalculation(baseAmount, 0.0, 0.0, 0.0, baseAmount);
    }

    /**
     * Display payment methods
     */
    public void displayPaymentMethods() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                    ONLINE PAYMENT CHANNELS                    ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Tuition fees can be paid via the available online payment channels.\n");

        List<PaymentMethod> methods = getAvailablePaymentMethods();
        for (int i = 0; i < methods.size(); i++) {
            PaymentMethod method = methods.get(i);
            System.out.printf("[%d] %s\n", i + 1, method.getMethodName());
            
            if (method.getServiceFee() > 0 || method.getPercentageFee() > 0) {
                System.out.printf("    Fees: ");
                if (method.getServiceFee() > 0) {
                    System.out.printf("₱%.2f service fee", method.getServiceFee());
                }
                if (method.getPercentageFee() > 0) {
                    if (method.getServiceFee() > 0) {
                        System.out.printf(" + ");
                    }
                    System.out.printf("%.1f%% processing fee", method.getPercentageFee());
                }
                System.out.println();
            } else {
                System.out.println("    No additional fees");
            }
            
            if (method.getDescription() != null) {
                System.out.println("    " + method.getDescription());
            }
            System.out.println();
        }
    }

    /**
     * Process payment interactively
     */
    public boolean processPaymentInteractive(String studentId, Scanner scanner) {
        // Get student account
        Optional<edu.slu.accounts.model.Account> accountOpt = accountService.getStudentAccount(studentId);
        if (accountOpt.isEmpty()) {
            System.out.println("❌ Account not found!");
            return false;
        }

        edu.slu.accounts.model.Account account = accountOpt.get();
        double amountDue = account.getPrelimAmountDue();

        if (amountDue <= 0) {
            System.out.println("✅ Your prelim payment is already completed!");
            return true;
        }

        System.out.println("\n" + accountService.getPaymentSummary(studentId));

        // Display payment methods
        displayPaymentMethods();

        // Get payment method selection
        List<PaymentMethod> methods = getAvailablePaymentMethods();
        System.out.print("Select payment method (1-" + methods.size() + "): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > methods.size()) {
                System.out.println("❌ Invalid selection!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input!");
            return false;
        }

        PaymentMethod selectedMethod = methods.get(choice - 1);
        
        // Calculate payment with fees
        PaymentMethod.PaymentCalculation calculation = selectedMethod.calculatePayment(amountDue);
        
        // Display payment summary
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                     PAYMENT SUMMARY                           ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Payment Method: " + selectedMethod.getMethodName());
        System.out.printf("Base Amount:    ₱%.2f\n", calculation.getBaseAmount());
        
        if (calculation.getServiceFee() > 0) {
            System.out.printf("Service Fee:    ₱%.2f\n", calculation.getServiceFee());
        }
        
        if (calculation.getPercentageFee() > 0) {
            System.out.printf("Processing Fee: ₱%.2f (%.1f%%)\n", 
                            calculation.getPercentageFee(), selectedMethod.getPercentageFee());
        }
        
        if (calculation.getTotalFees() > 0) {
            System.out.printf("Total Fees:     ₱%.2f\n", calculation.getTotalFees());
        }
        
        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.printf("TOTAL AMOUNT:   ₱%.2f\n", calculation.getTotalAmount());
        System.out.println("═══════════════════════════════════════════════════════════════");

        // Get payment confirmation
        System.out.print("\nProceed with payment? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("y") && !confirm.equals("yes")) {
            System.out.println("Payment cancelled.");
            return false;
        }

        // Get payment reference (optional)
        System.out.print("Enter payment reference (press Enter to auto-generate): ");
        String paymentReference = scanner.nextLine().trim();
        if (paymentReference.isEmpty()) {
            paymentReference = IdGenerator.generatePaymentReference();
        }

        // Process the payment
        boolean paymentSuccess = accountService.processPayment(
            studentId, 
            calculation.getTotalAmount(), 
            selectedMethod.getMethodCode(), 
            paymentReference
        );

        if (paymentSuccess) {
            System.out.println("\n🎉 PAYMENT SUCCESSFUL! 🎉");
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println("Payment Reference: " + paymentReference);
            System.out.println("Amount Paid: ₱" + String.format("%.2f", calculation.getTotalAmount()));
            System.out.println("Payment Method: " + selectedMethod.getMethodName());
            System.out.println("Status: COMPLETED");
            System.out.println("═══════════════════════════════════════════════════════════════");
            
            // Check if now permitted for exams
            if (accountService.canTakeExams(studentId)) {
                System.out.println("🎓 You are now PERMITTED to take prelim exams!");
                System.out.println("📊 You can now view your grades!");
            }
            
            return true;
        } else {
            System.out.println("❌ Payment failed. Please try again.");
            return false;
        }
    }

    /**
     * Process payment with specific amount and method
     */
    public boolean processPayment(String studentId, double amount, String paymentMethodName) {
        // Find payment method by name
        Optional<PaymentMethod> methodOpt = paymentMethods.stream()
            .filter(method -> method.getMethodName().equals(paymentMethodName))
            .findFirst();
        
        if (methodOpt.isEmpty()) {
            return false;
        }
        
        PaymentMethod method = methodOpt.get();
        String paymentReference = IdGenerator.generatePaymentReference();
        
        // Process the payment through account service
        return accountService.processPayment(studentId, amount, method.getMethodCode(), paymentReference);
    }

    /**
     * Get payment method display info
     */
    public String getPaymentMethodInfo(String methodCode) {
        Optional<PaymentMethod> methodOpt = getPaymentMethodByCode(methodCode);
        
        if (methodOpt.isPresent()) {
            PaymentMethod method = methodOpt.get();
            StringBuilder info = new StringBuilder();
            info.append(method.getMethodName());
            
            if (method.getServiceFee() > 0 || method.getPercentageFee() > 0) {
                info.append(" (");
                if (method.getServiceFee() > 0) {
                    info.append("₱").append(String.format("%.2f", method.getServiceFee()));
                }
                if (method.getPercentageFee() > 0) {
                    if (method.getServiceFee() > 0) {
                        info.append(" + ");
                    }
                    info.append(String.format("%.1f%%", method.getPercentageFee()));
                }
                info.append(" fee)");
            }
            
            return info.toString();
        }
        
        return "Unknown payment method";
    }
}