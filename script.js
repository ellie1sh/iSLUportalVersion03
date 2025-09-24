// Student data with different payment statuses
const students = {
    "2250605": {
        id: "2250605",
        course: "BSIT 2",
        name: "Ashel John D. Bimmuyag",
        status: "unpaid",
        amountDue: 6500.00,
        remainingBalance: 20383.00,
        fees: [
            { date: "", description: "BEGINNING BALANCE", amount: 0.00 },
            { date: "08/07/2025", description: "PAYMENT RECEIVED (00453512)", amount: -21500.00 },
            { date: "09/13/2025", description: "TUITION FEE @320.00/u", amount: 9020.00 },
            { date: "09/13/2025", description: "TUITION FEE @1167.00/u", amount: 10503.00 },
            { date: "09/13/2025", description: "TUITION FEE @434.00/u", amount: 1302.00 },
            { date: "09/13/2025", description: "OTHER FEES", amount: 6784.00 },
            { date: "09/13/2025", description: "OTHER/LAB FEE(S)", amount: 14064.00 },
            { date: "09/13/2025", description: "PMS WaterDrinkingSystem (JV100486)", amount: 60.00 },
            { date: "09/13/2025", description: "Internationalization Fee (JV100487)", amount: 150.00 }
        ]
    },
    "2255146": {
        id: "2255146",
        course: "BSIT 2",
        name: "Sherlie O. Rivera",
        status: "paid",
        amountDue: 0.00,
        remainingBalance: 13183.00,
        fees: [
            { date: "", description: "BEGINNING BALANCE", amount: 0.00 },
            { date: "08/11/2025", description: "PAYMENT RECEIVED (00161556C)", amount: -21200.00 },
            { date: "08/20/2025", description: "PAYMENT RECEIVED (00162355C)", amount: -7500.00 },
            { date: "09/13/2025", description: "TUITION FEE @320.00/u", amount: 9020.00 },
            { date: "09/13/2025", description: "TUITION FEE @1167.00/u", amount: 10503.00 },
            { date: "09/13/2025", description: "TUITION FEE @434.00/u", amount: 1302.00 },
            { date: "09/13/2025", description: "OTHER FEES", amount: 6784.00 },
            { date: "09/13/2025", description: "OTHER/LAB FEE(S)", amount: 14064.00 },
            { date: "09/13/2025", description: "PMS WaterDrinkingSystem (JV100486)", amount: 60.00 },
            { date: "09/13/2025", description: "Internationalization Fee (JV100487)", amount: 150.00 }
        ]
    }
};

// Current student (can be changed to simulate different accounts)
let currentStudentId = "2250605";
let currentStudent = students[currentStudentId];

// Payment methods configuration
const paymentMethods = {
    unionbank: {
        title: "Payment through UPay by UnionBank",
        hasCharges: false,
        hasPaymentOptions: false
    },
    dragonpay: {
        title: "Payment through Dragon Pay",
        hasCharges: true,
        hasPaymentOptions: true,
        serviceCharge: 25.00,
        additionalFee: 133.16
    },
    bpi: {
        title: "Payment through BPI Online",
        hasCharges: false,
        hasPaymentOptions: false
    },
    bdo: {
        title: "Payment through BDO Online",
        hasCharges: false,
        hasPaymentOptions: false
    },
    "bdo-bills": {
        title: "Payment through BDO Bills Payment",
        hasCharges: false,
        hasPaymentOptions: false
    },
    bukas: {
        title: "Bukas Tuition Installment Plans",
        hasCharges: false,
        hasPaymentOptions: false
    }
};

// Initialize the page
function initializePage() {
    updateStudentInfo();
    updatePaymentStatus();
    updateFeeBreakdown();
}

// Update student information
function updateStudentInfo() {
    const studentIdElement = document.querySelector('.student-id');
    const studentNameElement = document.querySelector('.student-name');
    
    if (studentIdElement) {
        studentIdElement.textContent = `${currentStudent.id} | ${currentStudent.course}`;
    }
    if (studentNameElement) {
        studentNameElement.textContent = currentStudent.name;
    }
}

// Update payment status section based on student status
function updatePaymentStatus() {
    const statusSection = document.getElementById('paymentStatusSection');
    const student = currentStudent;
    
    let statusHTML = '';
    
    if (student.status === 'unpaid') {
        statusHTML = `
            <div class="payment-amount">
                <div class="amount-label">Your amount due for <strong>PRELIM</strong> is:</div>
                <div class="amount-due">₱ ${formatNumber(student.amountDue)}</div>
                <div class="remaining-balance-label">Your remaining balance as of September 17, 2025 is:</div>
                <div class="remaining-balance">₱ ${formatNumber(student.remainingBalance)}</div>
                <div class="status-message unpaid">
                    <strong>PRELIM STATUS: NOT PAID.</strong> Please pay before prelim exams. Ignore if you're SLU Dependent or Full TOF Scholar.
                    <div class="verification-note">For verification on unposted payments after 'as of' date, please email sas@slu.edu.ph</div>
                </div>
            </div>
        `;
    } else if (student.status === 'paid') {
        statusHTML = `
            <div class="payment-amount">
                <div class="amount-label">Your amount due for <strong>PRELIM</strong> is:</div>
                <div class="amount-due paid">₱ ${formatNumber(student.amountDue)}</div>
                <div class="remaining-balance-label">Your remaining balance as of September 17, 2025 is:</div>
                <div class="remaining-balance positive">₱ ${formatNumber(student.remainingBalance)}</div>
                <div class="status-message paid">
                    <strong>PRELIM STATUS: PAID.</strong> Permitted to take the exams.
                </div>
            </div>
        `;
    }
    
    statusSection.innerHTML = statusHTML;
}

// Update fee breakdown table
function updateFeeBreakdown() {
    const tableBody = document.getElementById('feeTableBody');
    const student = currentStudent;
    
    let tableHTML = '';
    
    student.fees.forEach(fee => {
        const amountClass = fee.amount < 0 ? 'negative' : (fee.amount > 0 ? 'positive' : '');
        const formattedAmount = fee.amount === 0 ? '0.00' : formatNumber(Math.abs(fee.amount));
        const sign = fee.amount < 0 ? '(' : '';
        const closingSign = fee.amount < 0 ? ')' : '';
        
        tableHTML += `
            <tr>
                <td>${fee.date}</td>
                <td>${fee.description}</td>
                <td class="amount-cell ${amountClass}">${sign}${formattedAmount}${closingSign}</td>
            </tr>
        `;
    });
    
    tableBody.innerHTML = tableHTML;
}

// Format number with commas
function formatNumber(num) {
    return num.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
}

// Open payment modal
function openPaymentModal(paymentType) {
    const modal = document.getElementById('paymentModal');
    const modalTitle = document.getElementById('modalTitle');
    const paymentAmount = document.getElementById('paymentAmount');
    const paymentMethodSelect = document.getElementById('paymentMethod');
    const chargesSection = document.getElementById('chargesSection');
    const totalAmountInput = document.getElementById('totalAmount');
    
    const method = paymentMethods[paymentType];
    const baseAmount = currentStudent.amountDue;
    
    modalTitle.textContent = method.title;
    paymentAmount.value = baseAmount;
    
    // Show/hide payment method dropdown
    if (method.hasPaymentOptions) {
        paymentMethodSelect.style.display = 'block';
    } else {
        paymentMethodSelect.style.display = 'none';
    }
    
    // Show/hide charges section
    if (method.hasCharges) {
        chargesSection.style.display = 'block';
        const totalAmount = baseAmount + method.serviceCharge + method.additionalFee;
        totalAmountInput.value = formatNumber(totalAmount);
    } else {
        chargesSection.style.display = 'none';
    }
    
    modal.style.display = 'block';
}

// Close payment modal
function closePaymentModal() {
    document.getElementById('paymentModal').style.display = 'none';
}

// Process payment
function processPayment() {
    alert('Payment processing would be implemented here. This is a demo version.');
    closePaymentModal();
}

// Show reminder modal (for unpaid students)
function showReminderModal() {
    if (currentStudent.status === 'unpaid') {
        document.getElementById('reminderModal').style.display = 'block';
    }
}

// Close reminder modal
function closeReminderModal() {
    document.getElementById('reminderModal').style.display = 'none';
}

// Switch student account (for demo purposes)
function switchStudent(studentId) {
    if (students[studentId]) {
        currentStudentId = studentId;
        currentStudent = students[studentId];
        initializePage();
    }
}

// Close modals when clicking outside
window.onclick = function(event) {
    const paymentModal = document.getElementById('paymentModal');
    const reminderModal = document.getElementById('reminderModal');
    
    if (event.target === paymentModal) {
        closePaymentModal();
    }
    if (event.target === reminderModal) {
        closeReminderModal();
    }
}

// Add demo buttons to switch between students (for testing)
function addDemoControls() {
    const demoControls = document.createElement('div');
    demoControls.style.cssText = `
        position: fixed;
        top: 10px;
        right: 10px;
        background: white;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        z-index: 1001;
        font-size: 12px;
    `;
    
    demoControls.innerHTML = `
        <div style="margin-bottom: 10px; font-weight: bold;">Demo Controls:</div>
        <button onclick="switchStudent('2250605')" style="margin-right: 5px; padding: 5px 10px; font-size: 11px;">Unpaid Student</button>
        <button onclick="switchStudent('2255146')" style="margin-right: 5px; padding: 5px 10px; font-size: 11px;">Paid Student</button>
        <button onclick="showReminderModal()" style="padding: 5px 10px; font-size: 11px;">Show Reminder</button>
    `;
    
    document.body.appendChild(demoControls);
}

// Initialize the page when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializePage();
    addDemoControls();
    
    // Auto-show reminder for unpaid students after 3 seconds
    if (currentStudent.status === 'unpaid') {
        setTimeout(showReminderModal, 3000);
    }
});

// Export functions for external use
window.switchStudent = switchStudent;
window.showReminderModal = showReminderModal;
window.closeReminderModal = closeReminderModal;
window.openPaymentModal = openPaymentModal;
window.closePaymentModal = closePaymentModal;
window.processPayment = processPayment;