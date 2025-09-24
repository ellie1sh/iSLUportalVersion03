// Global variables
let currentUser = null;
let currentAccount = null;
let paymentMethods = [];

// API Base URL
const API_BASE = '';

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    // Check if user is already logged in
    const token = localStorage.getItem('authToken');
    if (token) {
        validateTokenAndLoadApp(token);
    } else {
        showLoginScreen();
    }

    // Set current date
    document.getElementById('currentDate').textContent = new Date().toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });

    // Setup event listeners
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    // Login form
    document.getElementById('loginForm').addEventListener('submit', handleLogin);

    // Modal close events
    window.onclick = function(event) {
        const paymentModal = document.getElementById('paymentModal');
        const gradesModal = document.getElementById('gradesModal');
        const reminderModal = document.getElementById('reminderModal');
        
        if (event.target === paymentModal) {
            closePaymentModal();
        }
        if (event.target === gradesModal) {
            closeGradesModal();
        }
        if (event.target === reminderModal) {
            closeReminderModal();
        }
    };
}

// Validate token and load app
async function validateTokenAndLoadApp(token) {
    try {
        const response = await fetch(`${API_BASE}/api/student/account`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const accountData = await response.json();
            currentAccount = accountData;
            await loadUserData();
            showMainApp();
        } else {
            localStorage.removeItem('authToken');
            showLoginScreen();
        }
    } catch (error) {
        console.error('Token validation failed:', error);
        localStorage.removeItem('authToken');
        showLoginScreen();
    }
}

// Handle login
async function handleLogin(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const loginData = {
        student_number: formData.get('student_number'),
        password: formData.get('password')
    };

    try {
        const response = await fetch(`${API_BASE}/api/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        const result = await response.json();

        if (response.ok) {
            localStorage.setItem('authToken', result.token);
            currentUser = result.student;
            await loadUserData();
            showMainApp();
        } else {
            showError('Invalid credentials. Please try again.');
        }
    } catch (error) {
        console.error('Login failed:', error);
        showError('Login failed. Please check your connection and try again.');
    }
}

// Load user data
async function loadUserData() {
    const token = localStorage.getItem('authToken');
    
    try {
        // Load account information
        const accountResponse = await fetch(`${API_BASE}/api/student/account`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (accountResponse.ok) {
            currentAccount = await accountResponse.json();
        }

        // Load transactions
        const transactionsResponse = await fetch(`${API_BASE}/api/student/transactions`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (transactionsResponse.ok) {
            const transactions = await transactionsResponse.json();
            currentAccount.transactions = transactions;
        }

        // Load payment methods
        const paymentMethodsResponse = await fetch(`${API_BASE}/api/payment-methods`);
        if (paymentMethodsResponse.ok) {
            paymentMethods = await paymentMethodsResponse.json();
        }

        // Update UI
        updateStudentInfo();
        updatePaymentStatus();
        updateFeeBreakdown();
        updatePaymentButtons();
        updateGradesSection();

        // Show reminder for unpaid students
        if (currentAccount.prelim_status === 'UNPAID') {
            setTimeout(showReminderModal, 3000);
        }

    } catch (error) {
        console.error('Failed to load user data:', error);
        showError('Failed to load account information.');
    }
}

// Update student information
function updateStudentInfo() {
    if (!currentAccount) return;

    const avatarPlaceholder = document.getElementById('avatarPlaceholder');
    const studentInfo = document.getElementById('studentInfo');
    const studentName = document.getElementById('studentName');

    const initials = `${currentAccount.first_name[0]}${currentAccount.last_name[0]}`;
    avatarPlaceholder.textContent = initials;

    studentInfo.textContent = `${currentAccount.student_number} | ${currentAccount.course} ${currentAccount.year_level}`;
    studentName.textContent = `${currentAccount.first_name} ${currentAccount.middle_name || ''} ${currentAccount.last_name}`.trim();
}

// Update payment status
function updatePaymentStatus() {
    if (!currentAccount) return;

    const statusSection = document.getElementById('paymentStatusSection');
    const isPaid = currentAccount.prelim_status === 'PAID';
    const isPermitted = currentAccount.exam_permission === 'PERMITTED';

    let statusHTML = `
        <div class="payment-amount">
            <div class="amount-label">Your amount due for <strong>PRELIM</strong> is:</div>
            <div class="amount-due ${isPaid ? 'paid' : ''}">₱ ${formatNumber(currentAccount.prelim_amount_due)}</div>
            <div class="remaining-balance-label">Your remaining balance as of ${new Date().toLocaleDateString()} is:</div>
            <div class="remaining-balance ${currentAccount.remaining_balance <= 0 ? 'positive' : ''}">₱ ${formatNumber(Math.abs(currentAccount.remaining_balance))}</div>
            <div class="status-message ${isPaid ? 'paid' : 'unpaid'}">
                <strong>PRELIM STATUS: ${currentAccount.prelim_status}.</strong> 
                ${isPaid 
                    ? 'Permitted to take the exams.' 
                    : 'Please pay before prelim exams. Ignore if you\'re SLU Dependent or Full TOF Scholar.'}
                ${!isPaid ? '<div class="verification-note">For verification on unposted payments after \'as of\' date, please email sas@slu.edu.ph</div>' : ''}
            </div>
        </div>
    `;

    statusSection.innerHTML = statusHTML;
}

// Update fee breakdown
function updateFeeBreakdown() {
    if (!currentAccount || !currentAccount.transactions) return;

    const tableBody = document.getElementById('feeTableBody');
    let tableHTML = '';

    // Add beginning balance
    tableHTML += `
        <tr>
            <td></td>
            <td>BEGINNING BALANCE</td>
            <td class="amount-cell">0.00</td>
        </tr>
    `;

    // Add transactions
    currentAccount.transactions.forEach(transaction => {
        const date = new Date(transaction.transaction_date).toLocaleDateString('en-US', {
            month: '2-digit',
            day: '2-digit',
            year: 'numeric'
        });

        const isPayment = transaction.transaction_type === 'PAYMENT';
        const amount = Math.abs(transaction.amount);
        const amountClass = isPayment ? 'negative' : 'positive';
        const formattedAmount = formatNumber(amount);
        const displayAmount = isPayment ? `(${formattedAmount})` : formattedAmount;

        tableHTML += `
            <tr>
                <td>${date}</td>
                <td>${transaction.description}</td>
                <td class="amount-cell ${amountClass}">${displayAmount}</td>
            </tr>
        `;
    });

    tableBody.innerHTML = tableHTML;
}

// Update payment buttons
function updatePaymentButtons() {
    const paymentButtonsContainer = document.getElementById('paymentButtons');
    const isPaid = currentAccount.prelim_status === 'PAID';
    const hasOutstanding = currentAccount.prelim_amount_due > 0;

    let buttonsHTML = '';

    if (isPaid || !hasOutstanding) {
        buttonsHTML = `
            <div class="payment-complete">
                <div class="success">
                    ✅ Your prelim payment has been completed. You are now permitted to take the exams.
                </div>
            </div>
        `;
    } else {
        paymentMethods.forEach(method => {
            const methodCode = method.method_code.toLowerCase().replace('_', '-');
            buttonsHTML += `
                <button class="payment-btn ${methodCode}-btn" onclick="openPaymentModal('${method.method_code}')">
                    <span class="payment-logo">${getPaymentLogo(method.method_code)}</span>
                    <span>${method.method_name}</span>
                </button>
            `;
        });
    }

    paymentButtonsContainer.innerHTML = buttonsHTML;
}

// Get payment method logo
function getPaymentLogo(methodCode) {
    const logos = {
        'UNIONBANK': 'UB',
        'DRAGONPAY': '🐉',
        'BPI': 'BPI',
        'BDO': 'BDO',
        'BDO_BILLS': 'BDO',
        'BUKAS': '⛰️'
    };
    return logos[methodCode] || '💳';
}

// Update grades section
function updateGradesSection() {
    const gradesSection = document.getElementById('gradesSection');
    const isPaid = currentAccount.prelim_status === 'PAID';

    if (isPaid) {
        gradesSection.style.display = 'block';
        loadGrades();
    } else {
        gradesSection.style.display = 'none';
    }
}

// Load grades
async function loadGrades() {
    const token = localStorage.getItem('authToken');
    const gradesContent = document.getElementById('gradesContent');

    try {
        const response = await fetch(`${API_BASE}/api/student/grades`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const grades = await response.json();
            displayGrades(grades, gradesContent);
        } else {
            gradesContent.innerHTML = '<div class="error">Unable to load grades at this time.</div>';
        }
    } catch (error) {
        console.error('Failed to load grades:', error);
        gradesContent.innerHTML = '<div class="error">Failed to load grades.</div>';
    }
}

// Display grades
function displayGrades(grades, container) {
    if (!grades || grades.length === 0) {
        container.innerHTML = '<div class="loading">No grades available yet.</div>';
        return;
    }

    let gradesHTML = '';
    grades.forEach(grade => {
        const prelimGrade = grade.prelim_grade || 'TBA';
        gradesHTML += `
            <div class="grade-item">
                <div class="subject-info">
                    <div class="subject-code">${grade.subject_code}</div>
                    <div class="subject-name">${grade.subject_name}</div>
                </div>
                <div class="grade-value">${prelimGrade}</div>
            </div>
        `;
    });

    container.innerHTML = gradesHTML;
}

// Open payment modal
async function openPaymentModal(methodCode) {
    const method = paymentMethods.find(m => m.method_code === methodCode);
    if (!method) return;

    const modal = document.getElementById('paymentModal');
    const modalTitle = document.getElementById('modalTitle');
    const paymentDetails = document.getElementById('paymentDetails');
    const paymentForm = document.getElementById('paymentForm');

    modalTitle.textContent = `Payment through ${method.method_name}`;

    // Calculate payment amount with fees
    try {
        const response = await fetch(`${API_BASE}/api/calculate-payment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            },
            body: JSON.stringify({
                payment_method_code: methodCode,
                base_amount: currentAccount.prelim_amount_due
            })
        });

        if (response.ok) {
            const calculation = await response.json();
            displayPaymentModal(calculation, method);
        }
    } catch (error) {
        console.error('Failed to calculate payment:', error);
        showError('Failed to calculate payment amount.');
        return;
    }

    modal.style.display = 'block';
}

// Display payment modal content
function displayPaymentModal(calculation, method) {
    const paymentDetails = document.getElementById('paymentDetails');
    const paymentForm = document.getElementById('paymentForm');

    // Payment summary
    let summaryHTML = `
        <div class="payment-summary">
            <div class="payment-row">
                <span>Base Amount:</span>
                <span>₱ ${formatNumber(calculation.base_amount)}</span>
            </div>
    `;

    if (calculation.service_fee > 0) {
        summaryHTML += `
            <div class="payment-row">
                <span>Service Fee:</span>
                <span>₱ ${formatNumber(calculation.service_fee)}</span>
            </div>
        `;
    }

    if (calculation.percentage_fee > 0) {
        summaryHTML += `
            <div class="payment-row">
                <span>Processing Fee:</span>
                <span>₱ ${formatNumber(calculation.percentage_fee)}</span>
            </div>
        `;
    }

    summaryHTML += `
            <div class="payment-row">
                <span>Total Amount:</span>
                <span>₱ ${formatNumber(calculation.total_amount)}</span>
            </div>
        </div>
    `;

    paymentDetails.innerHTML = summaryHTML;

    // Payment form
    let formHTML = `
        <div class="payment-form">
            <div class="form-group">
                <label for="paymentReference">Payment Reference (Optional):</label>
                <input type="text" id="paymentReference" placeholder="Enter reference number">
            </div>
    `;

    if (method.method_code === 'DRAGONPAY') {
        formHTML += `
            <div class="form-group">
                <label for="paymentChannel">Payment Channel:</label>
                <select id="paymentChannel">
                    <option value="gcash">GCash</option>
                    <option value="paymaya">PayMaya</option>
                    <option value="bank">Bank Transfer</option>
                    <option value="otc">Over the Counter</option>
                </select>
            </div>
        `;
    }

    formHTML += `
            <button class="proceed-btn" onclick="processPayment('${method.method_code}', ${calculation.total_amount})">
                Proceed with Payment
            </button>
        </div>
    `;

    paymentForm.innerHTML = formHTML;
}

// Process payment
async function processPayment(methodCode, amount) {
    const paymentReference = document.getElementById('paymentReference')?.value || '';
    
    try {
        const response = await fetch(`${API_BASE}/api/process-payment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            },
            body: JSON.stringify({
                payment_method_code: methodCode,
                amount: amount,
                payment_reference: paymentReference
            })
        });

        const result = await response.json();

        if (response.ok) {
            closePaymentModal();
            showSuccess(`Payment processed successfully! Reference: ${result.payment_reference}`);
            
            // Reload user data to reflect changes
            await loadUserData();
            
            // Show grades if now permitted
            if (result.exam_permission === 'PERMITTED') {
                setTimeout(() => {
                    showSuccess('🎉 Congratulations! You can now view your grades and take the prelim exams.');
                }, 2000);
            }
        } else {
            showError(result.error || 'Payment processing failed.');
        }
    } catch (error) {
        console.error('Payment processing failed:', error);
        showError('Payment processing failed. Please try again.');
    }
}

// View grades
async function viewGrades() {
    const isPaid = currentAccount.prelim_status === 'PAID';
    
    if (!isPaid) {
        showError('You must pay your prelim fees before viewing grades.');
        return;
    }

    const modal = document.getElementById('gradesModal');
    const gradesModalContent = document.getElementById('gradesModalContent');
    
    modal.style.display = 'block';
    
    try {
        const token = localStorage.getItem('authToken');
        const response = await fetch(`${API_BASE}/api/student/grades`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const grades = await response.json();
            displayGrades(grades, gradesModalContent);
        } else {
            gradesModalContent.innerHTML = '<div class="error">Unable to access grades. Please ensure your prelim payment is completed.</div>';
        }
    } catch (error) {
        console.error('Failed to load grades:', error);
        gradesModalContent.innerHTML = '<div class="error">Failed to load grades.</div>';
    }
}

// Modal functions
function closePaymentModal() {
    document.getElementById('paymentModal').style.display = 'none';
}

function closeGradesModal() {
    document.getElementById('gradesModal').style.display = 'none';
}

function showReminderModal() {
    if (currentAccount && currentAccount.prelim_status === 'UNPAID') {
        document.getElementById('reminderModal').style.display = 'block';
    }
}

function closeReminderModal() {
    document.getElementById('reminderModal').style.display = 'none';
}

// UI functions
function showLoginScreen() {
    document.getElementById('loginScreen').style.display = 'flex';
    document.getElementById('mainApp').style.display = 'none';
}

function showMainApp() {
    document.getElementById('loginScreen').style.display = 'none';
    document.getElementById('mainApp').style.display = 'flex';
}

function logout() {
    localStorage.removeItem('authToken');
    currentUser = null;
    currentAccount = null;
    showLoginScreen();
}

// Utility functions
function formatNumber(num) {
    return parseFloat(num).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
}

function showError(message) {
    // Create and show error message
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error';
    errorDiv.textContent = message;
    errorDiv.style.position = 'fixed';
    errorDiv.style.top = '20px';
    errorDiv.style.right = '20px';
    errorDiv.style.zIndex = '10000';
    errorDiv.style.minWidth = '300px';
    
    document.body.appendChild(errorDiv);
    
    setTimeout(() => {
        document.body.removeChild(errorDiv);
    }, 5000);
}

function showSuccess(message) {
    // Create and show success message
    const successDiv = document.createElement('div');
    successDiv.className = 'success';
    successDiv.textContent = message;
    successDiv.style.position = 'fixed';
    successDiv.style.top = '20px';
    successDiv.style.right = '20px';
    successDiv.style.zIndex = '10000';
    successDiv.style.minWidth = '300px';
    
    document.body.appendChild(successDiv);
    
    setTimeout(() => {
        document.body.removeChild(successDiv);
    }, 5000);
}

// Export functions for global access
window.openPaymentModal = openPaymentModal;
window.closePaymentModal = closePaymentModal;
window.closeGradesModal = closeGradesModal;
window.showReminderModal = showReminderModal;
window.closeReminderModal = closeReminderModal;
window.viewGrades = viewGrades;
window.logout = logout;
window.processPayment = processPayment;