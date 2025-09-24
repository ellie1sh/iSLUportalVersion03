// Global variables
let currentUser = null;
let currentGrades = [];

// API Base URL (placeholder for future backend integration)
const API_BASE = '';

// Mock data structure matching the image format
const mockGradesData = [
    {
        classCode: '7024',
        courseNumber: 'NSTP-CWTS 1',
        units: 3,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9454',
        courseNumber: 'GSTS',
        units: 3,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9455',
        courseNumber: 'GEM1',
        units: 3,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9456',
        courseNumber: 'CTE 103',
        units: 3,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9457',
        courseNumber: 'IT 211',
        units: 3,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9458A',
        courseNumber: 'IT 212',
        units: 2,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9458B',
        courseNumber: 'IT 212L',
        units: 1,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9459A',
        courseNumber: 'IT 213',
        units: 2,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9459B',
        courseNumber: 'IT 213L',
        units: 1,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    },
    {
        classCode: '9547',
        courseNumber: 'FIT OA',
        units: 2,
        prelimGrade: null,
        midtermGrade: null,
        tentativeFinalGrade: null,
        finalGrade: 'Not Yet Submitted',
        weights: null
    }
];

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    // Check if user is already logged in
    const token = localStorage.getItem('authToken');
    if (token) {
        validateTokenAndLoadApp(token);
    } else {
        showLoginScreen();
    }

    // Setup event listeners
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    // Login form
    document.getElementById('loginForm').addEventListener('submit', handleLogin);

    // Navigation links
    setupNavigationListeners();
}

// Setup navigation listeners
function setupNavigationListeners() {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Remove active class from all nav items
            document.querySelectorAll('.nav-item').forEach(item => {
                item.classList.remove('active');
            });
            
            // Add active class to clicked item
            this.closest('.nav-item').classList.add('active');
            
            // Handle navigation based on the clicked item
            const navText = this.querySelector('.nav-text').textContent.trim();
            handleNavigation(navText);
        });
    });
}

// Handle navigation
function handleNavigation(navText) {
    switch(navText) {
        case 'Home':
            // Navigate to home page
            window.location.href = 'index.html';
            break;
        case 'Statement of Accounts':
            // Navigate to statement of accounts
            window.location.href = 'index.html';
            break;
        case 'Grades':
            // Already on grades page, just refresh data
            loadGradesData();
            break;
        default:
            // For other navigation items, show placeholder message
            showPlaceholderMessage(navText);
            break;
    }
}

// Show placeholder message for unimplemented features
function showPlaceholderMessage(feature) {
    showNotification(`${feature} functionality is under development and will be available soon.`, 'info');
}

// Validate token and load app
async function validateTokenAndLoadApp(token) {
    try {
        // For now, we'll simulate token validation
        // In the future, this will make an API call to validate the token
        const userData = JSON.parse(localStorage.getItem('userData'));
        if (userData) {
            currentUser = userData;
            showMainApp();
            loadGradesData();
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
        // Mock login for demo purposes
        // In the future, this will make an API call to authenticate
        if (loginData.student_number && loginData.password === 'password') {
            const mockUser = {
                student_number: loginData.student_number,
                first_name: 'QUERUBIN',
                last_name: 'RIVERA',
                course: 'IT',
                year_level: '2nd Year'
            };
            
            const token = 'mock-token-' + Date.now();
            localStorage.setItem('authToken', token);
            localStorage.setItem('userData', JSON.stringify(mockUser));
            
            currentUser = mockUser;
            showMainApp();
            loadGradesData();
        } else {
            showError('Invalid credentials. Please try again.');
        }
    } catch (error) {
        console.error('Login failed:', error);
        showError('Login failed. Please check your connection and try again.');
    }
}

// Load grades data
async function loadGradesData() {
    const gradesTableBody = document.getElementById('gradesTableBody');
    const headerStudentInfo = document.getElementById('headerStudentInfo');
    
    // Update header with student info
    if (currentUser) {
        headerStudentInfo.textContent = `${currentUser.first_name} ${currentUser.last_name}`;
    }
    
    try {
        // Show loading state
        gradesTableBody.innerHTML = '<tr><td colspan="8" class="loading-cell">Loading grades...</td></tr>';
        
        // Simulate API delay
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // For now, use mock data
        // In the future, this will fetch from the backend API
        // const response = await fetch(`${API_BASE}/api/student/grades`, {
        //     headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
        // });
        // const grades = await response.json();
        
        const grades = mockGradesData;
        currentGrades = grades;
        displayGrades(grades);
        
    } catch (error) {
        console.error('Failed to load grades:', error);
        gradesTableBody.innerHTML = '<tr><td colspan="8" class="loading-cell error">Failed to load grades. Please try again later.</td></tr>';
    }
}

// Display grades in the table
function displayGrades(grades) {
    const gradesTableBody = document.getElementById('gradesTableBody');
    
    if (!grades || grades.length === 0) {
        gradesTableBody.innerHTML = '<tr><td colspan="8" class="loading-cell">No grades available yet.</td></tr>';
        return;
    }
    
    let tableHTML = '';
    grades.forEach(grade => {
        tableHTML += `
            <tr>
                <td>${grade.classCode}</td>
                <td>${grade.courseNumber}</td>
                <td class="grade-value">${grade.units}</td>
                <td class="grade-value ${getGradeClass(grade.prelimGrade)}">${formatGradeValue(grade.prelimGrade)}</td>
                <td class="grade-value ${getGradeClass(grade.midtermGrade)}">${formatGradeValue(grade.midtermGrade)}</td>
                <td class="grade-value ${getGradeClass(grade.tentativeFinalGrade)}">${formatGradeValue(grade.tentativeFinalGrade)}</td>
                <td class="grade-value ${getGradeClass(grade.finalGrade)}">${formatGradeValue(grade.finalGrade)}</td>
                <td class="grade-value">${formatGradeValue(grade.weights)}</td>
            </tr>
        `;
    });
    
    gradesTableBody.innerHTML = tableHTML;
}

// Format grade value for display
function formatGradeValue(value) {
    if (value === null || value === undefined) {
        return '<span class="grade-pending">-</span>';
    }
    if (typeof value === 'string') {
        return value;
    }
    if (typeof value === 'number') {
        return value.toFixed(1);
    }
    return value;
}

// Get CSS class for grade styling
function getGradeClass(grade) {
    if (grade === null || grade === undefined) {
        return 'grade-pending';
    }
    if (typeof grade === 'number') {
        if (grade >= 75) {
            return 'grade-passed';
        } else {
            return 'grade-failed';
        }
    }
    if (typeof grade === 'string') {
        const upperGrade = grade.toUpperCase();
        if (upperGrade.includes('INC') || upperGrade.includes('INCOMPLETE')) {
            return 'grade-incomplete';
        }
        if (upperGrade.includes('NOT YET') || upperGrade.includes('PENDING')) {
            return 'grade-pending';
        }
        if (upperGrade.includes('FAILED') || upperGrade === 'F') {
            return 'grade-failed';
        }
        if (upperGrade === 'P' || upperGrade.includes('PASSED')) {
            return 'grade-passed';
        }
    }
    return '';
}

// Simulate real-time grade updates (for future faculty integration)
function simulateGradeUpdate(classCode, gradeType, newGrade) {
    const gradeIndex = currentGrades.findIndex(grade => grade.classCode === classCode);
    if (gradeIndex !== -1) {
        currentGrades[gradeIndex][gradeType] = newGrade;
        displayGrades(currentGrades);
        
        // Show notification about the update
        showNotification(`Grade updated for ${currentGrades[gradeIndex].courseNumber}: ${gradeType} = ${newGrade}`, 'success');
    }
}

// Function to be called by faculty backend (placeholder)
function updateGradeFromFaculty(studentNumber, classCode, gradeData) {
    // This function will be called by the faculty backend when grades are updated
    // For now, it's a placeholder that shows how the integration will work
    
    if (currentUser && currentUser.student_number === studentNumber) {
        const gradeIndex = currentGrades.findIndex(grade => grade.classCode === classCode);
        if (gradeIndex !== -1) {
            // Update the grade data
            Object.assign(currentGrades[gradeIndex], gradeData);
            
            // Refresh the display
            displayGrades(currentGrades);
            
            // Show real-time notification
            showNotification(`New grade posted for ${currentGrades[gradeIndex].courseNumber}!`, 'success');
            
            // Optional: Play notification sound
            playNotificationSound();
        }
    }
}

// Play notification sound for grade updates
function playNotificationSound() {
    // Create a simple beep sound for notifications
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();
    
    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);
    
    oscillator.frequency.value = 800;
    oscillator.type = 'sine';
    
    gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);
    
    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + 0.5);
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
    localStorage.removeItem('userData');
    currentUser = null;
    currentGrades = [];
    showLoginScreen();
}

// Utility functions
function showError(message) {
    showNotification(message, 'error');
}

function showSuccess(message) {
    showNotification(message, 'success');
}

function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 10000;
        min-width: 300px;
        max-width: 500px;
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        font-size: 14px;
        font-weight: 500;
        animation: slideInNotification 0.3s ease-out;
        cursor: pointer;
    `;
    
    // Set colors based on type
    switch(type) {
        case 'error':
            notification.style.backgroundColor = '#f8d7da';
            notification.style.color = '#721c24';
            notification.style.border = '1px solid #f5c6cb';
            break;
        case 'success':
            notification.style.backgroundColor = '#d4edda';
            notification.style.color = '#155724';
            notification.style.border = '1px solid #c3e6cb';
            break;
        case 'info':
        default:
            notification.style.backgroundColor = '#d1ecf1';
            notification.style.color = '#0c5460';
            notification.style.border = '1px solid #bee5eb';
            break;
    }
    
    notification.textContent = message;
    
    // Add close functionality
    notification.addEventListener('click', () => {
        notification.style.animation = 'slideOutNotification 0.3s ease-in forwards';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    });
    
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentNode) {
            notification.style.animation = 'slideOutNotification 0.3s ease-in forwards';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
            }, 300);
        }
    }, 5000);
}

// Add notification animations to CSS dynamically
const notificationStyles = `
    @keyframes slideInNotification {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOutNotification {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;

// Add styles to document
const styleSheet = document.createElement('style');
styleSheet.textContent = notificationStyles;
document.head.appendChild(styleSheet);

// Export functions for global access (for future faculty integration)
window.updateGradeFromFaculty = updateGradeFromFaculty;
window.simulateGradeUpdate = simulateGradeUpdate;
window.logout = logout;

// Demo function to simulate faculty updating grades (for testing)
function demoGradeUpdate() {
    setTimeout(() => {
        simulateGradeUpdate('7024', 'prelimGrade', 85.5);
    }, 3000);
    
    setTimeout(() => {
        simulateGradeUpdate('9454', 'prelimGrade', 92.0);
    }, 5000);
    
    setTimeout(() => {
        simulateGradeUpdate('9455', 'midtermGrade', 88.5);
    }, 7000);
}

// Uncomment the line below to see demo grade updates
// setTimeout(demoGradeUpdate, 2000);