const express = require('express');
const sqlite3 = require('sqlite3').verbose();
const cors = require('cors');
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;
const JWT_SECRET = 'your-secret-key-change-in-production';

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(express.static('public'));

// Database setup
const db = new sqlite3.Database('./university.db', (err) => {
    if (err) {
        console.error('Error opening database:', err);
    } else {
        console.log('Connected to SQLite database');
        initializeTables();
    }
});

// Initialize database tables
function initializeTables() {
    // Students table
    db.run(`CREATE TABLE IF NOT EXISTS students (
        id TEXT PRIMARY KEY,
        student_number TEXT UNIQUE NOT NULL,
        first_name TEXT NOT NULL,
        middle_name TEXT,
        last_name TEXT NOT NULL,
        course TEXT NOT NULL,
        year_level INTEGER NOT NULL,
        email TEXT UNIQUE NOT NULL,
        password TEXT NOT NULL,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    )`);

    // Accounts table
    db.run(`CREATE TABLE IF NOT EXISTS accounts (
        id TEXT PRIMARY KEY,
        student_id TEXT NOT NULL,
        semester TEXT NOT NULL,
        academic_year TEXT NOT NULL,
        total_assessment REAL NOT NULL DEFAULT 0,
        total_paid REAL NOT NULL DEFAULT 0,
        remaining_balance REAL NOT NULL DEFAULT 0,
        prelim_amount_due REAL NOT NULL DEFAULT 0,
        midterm_amount_due REAL NOT NULL DEFAULT 0,
        final_amount_due REAL NOT NULL DEFAULT 0,
        prelim_status TEXT DEFAULT 'UNPAID',
        midterm_status TEXT DEFAULT 'UNPAID',
        final_status TEXT DEFAULT 'UNPAID',
        exam_permission TEXT DEFAULT 'NOT_PERMITTED',
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (student_id) REFERENCES students (id)
    )`);

    // Transactions table
    db.run(`CREATE TABLE IF NOT EXISTS transactions (
        id TEXT PRIMARY KEY,
        student_id TEXT NOT NULL,
        account_id TEXT NOT NULL,
        transaction_type TEXT NOT NULL, -- ASSESSMENT, PAYMENT, FEE
        description TEXT NOT NULL,
        amount REAL NOT NULL,
        payment_method TEXT,
        payment_reference TEXT,
        transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
        status TEXT DEFAULT 'COMPLETED',
        FOREIGN KEY (student_id) REFERENCES students (id),
        FOREIGN KEY (account_id) REFERENCES accounts (id)
    )`);

    // Grades table
    db.run(`CREATE TABLE IF NOT EXISTS grades (
        id TEXT PRIMARY KEY,
        student_id TEXT NOT NULL,
        subject_code TEXT NOT NULL,
        subject_name TEXT NOT NULL,
        units REAL NOT NULL,
        prelim_grade REAL,
        midterm_grade REAL,
        final_grade REAL,
        semester_grade REAL,
        remarks TEXT,
        semester TEXT NOT NULL,
        academic_year TEXT NOT NULL,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (student_id) REFERENCES students (id)
    )`);

    // Payment methods table
    db.run(`CREATE TABLE IF NOT EXISTS payment_methods (
        id TEXT PRIMARY KEY,
        method_name TEXT NOT NULL,
        method_code TEXT UNIQUE NOT NULL,
        service_fee REAL DEFAULT 0,
        percentage_fee REAL DEFAULT 0,
        is_active BOOLEAN DEFAULT 1,
        description TEXT
    )`);

    // Insert default payment methods
    insertDefaultPaymentMethods();
    insertSampleData();
}

// Insert default payment methods
function insertDefaultPaymentMethods() {
    const paymentMethods = [
        { id: uuidv4(), method_name: 'UnionBank UPay Online', method_code: 'UNIONBANK', service_fee: 0, percentage_fee: 0, is_active: 1 },
        { id: uuidv4(), method_name: 'Dragonpay Payment Gateway', method_code: 'DRAGONPAY', service_fee: 25.00, percentage_fee: 2.0, is_active: 1 },
        { id: uuidv4(), method_name: 'BPI Online', method_code: 'BPI', service_fee: 15.00, percentage_fee: 0, is_active: 1 },
        { id: uuidv4(), method_name: 'BDO Online', method_code: 'BDO', service_fee: 20.00, percentage_fee: 0, is_active: 1 },
        { id: uuidv4(), method_name: 'BDO Bills Payment', method_code: 'BDO_BILLS', service_fee: 10.00, percentage_fee: 0, is_active: 1 },
        { id: uuidv4(), method_name: 'Bukas Tuition Installment Plans', method_code: 'BUKAS', service_fee: 0, percentage_fee: 3.5, is_active: 1 }
    ];

    paymentMethods.forEach(method => {
        db.run(`INSERT OR IGNORE INTO payment_methods (id, method_name, method_code, service_fee, percentage_fee, is_active, description) 
                VALUES (?, ?, ?, ?, ?, ?, ?)`,
            [method.id, method.method_name, method.method_code, method.service_fee, method.percentage_fee, method.is_active, method.description],
            (err) => {
                if (err && !err.message.includes('UNIQUE constraint failed')) {
                    console.error('Error inserting payment method:', err);
                }
            }
        );
    });
}

// Insert sample data
function insertSampleData() {
    const sampleStudents = [
        {
            id: uuidv4(),
            student_number: '2024001',
            first_name: 'Juan',
            middle_name: 'Santos',
            last_name: 'Dela Cruz',
            course: 'BSIT',
            year_level: 2,
            email: 'juan.delacruz@student.slu.edu.ph',
            password: '$2b$10$example' // This would be properly hashed
        },
        {
            id: uuidv4(),
            student_number: '2024002',
            first_name: 'Maria',
            middle_name: 'Garcia',
            last_name: 'Santos',
            course: 'BSCS',
            year_level: 3,
            email: 'maria.santos@student.slu.edu.ph',
            password: '$2b$10$example'
        }
    ];

    sampleStudents.forEach(student => {
        db.run(`INSERT OR IGNORE INTO students (id, student_number, first_name, middle_name, last_name, course, year_level, email, password) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
            [student.id, student.student_number, student.first_name, student.middle_name, student.last_name, 
             student.course, student.year_level, student.email, student.password],
            function(err) {
                if (err && !err.message.includes('UNIQUE constraint failed')) {
                    console.error('Error inserting student:', err);
                } else if (this.changes > 0) {
                    createStudentAccount(student);
                }
            }
        );
    });
}

// Create student account with initial assessment
function createStudentAccount(student) {
    const accountId = uuidv4();
    const totalAssessment = 45000.00; // Sample total assessment
    const prelimAmount = 15000.00; // Prelim payment requirement
    
    db.run(`INSERT INTO accounts (id, student_id, semester, academic_year, total_assessment, 
            remaining_balance, prelim_amount_due, prelim_status, exam_permission) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
        [accountId, student.id, 'FIRST SEMESTER', '2025-2026', totalAssessment, 
         totalAssessment, prelimAmount, 'UNPAID', 'NOT_PERMITTED'],
        (err) => {
            if (err) {
                console.error('Error creating account:', err);
            } else {
                // Add initial assessment transactions
                addInitialAssessments(student.id, accountId);
                addSampleGrades(student.id);
            }
        }
    );
}

// Add initial assessment transactions
function addInitialAssessments(studentId, accountId) {
    const assessments = [
        { description: 'TUITION FEE @320.00/u', amount: 9020.00 },
        { description: 'TUITION FEE @1167.00/u', amount: 10503.00 },
        { description: 'TUITION FEE @434.00/u', amount: 1302.00 },
        { description: 'OTHER FEES', amount: 6784.00 },
        { description: 'OTHER/LAB FEE(S)', amount: 14064.00 },
        { description: 'PMS WaterDrinkingSystem (JV100486)', amount: 60.00 },
        { description: 'Internationalization Fee (JV100487)', amount: 150.00 },
        { description: 'Miscellaneous Fees', amount: 3117.00 }
    ];

    assessments.forEach(assessment => {
        db.run(`INSERT INTO transactions (id, student_id, account_id, transaction_type, description, amount) 
                VALUES (?, ?, ?, ?, ?, ?)`,
            [uuidv4(), studentId, accountId, 'ASSESSMENT', assessment.description, assessment.amount]
        );
    });
}

// Add sample grades
function addSampleGrades(studentId) {
    const subjects = [
        { code: 'IT 101', name: 'Programming Fundamentals', units: 3 },
        { code: 'IT 102', name: 'Data Structures', units: 3 },
        { code: 'IT 103', name: 'Database Systems', units: 3 },
        { code: 'GE 101', name: 'Mathematics in the Modern World', units: 3 },
        { code: 'GE 102', name: 'Purposive Communication', units: 3 }
    ];

    subjects.forEach(subject => {
        db.run(`INSERT INTO grades (id, student_id, subject_code, subject_name, units, semester, academic_year) 
                VALUES (?, ?, ?, ?, ?, ?, ?)`,
            [uuidv4(), studentId, subject.code, subject.name, subject.units, 'FIRST SEMESTER', '2025-2026']
        );
    });
}

// Authentication middleware
function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
        return res.sendStatus(401);
    }

    jwt.verify(token, JWT_SECRET, (err, user) => {
        if (err) return res.sendStatus(403);
        req.user = user;
        next();
    });
}

// Routes

// Student login
app.post('/api/login', (req, res) => {
    const { student_number, password } = req.body;

    db.get(`SELECT * FROM students WHERE student_number = ?`, [student_number], async (err, student) => {
        if (err) {
            return res.status(500).json({ error: 'Database error' });
        }

        if (!student) {
            return res.status(401).json({ error: 'Invalid credentials' });
        }

        // In a real app, you'd compare with bcrypt
        // const validPassword = await bcrypt.compare(password, student.password);
        const validPassword = password === 'password'; // Simplified for demo

        if (!validPassword) {
            return res.status(401).json({ error: 'Invalid credentials' });
        }

        const token = jwt.sign({ studentId: student.id }, JWT_SECRET);
        res.json({ token, student: { ...student, password: undefined } });
    });
});

// Get student account information
app.get('/api/student/account', authenticateToken, (req, res) => {
    const studentId = req.user.studentId;

    const query = `
        SELECT 
            s.id, s.student_number, s.first_name, s.middle_name, s.last_name, s.course, s.year_level,
            a.id as account_id, a.semester, a.academic_year, a.total_assessment, a.total_paid, 
            a.remaining_balance, a.prelim_amount_due, a.midterm_amount_due, a.final_amount_due,
            a.prelim_status, a.midterm_status, a.final_status, a.exam_permission
        FROM students s
        JOIN accounts a ON s.id = a.student_id
        WHERE s.id = ? AND a.semester = 'FIRST SEMESTER' AND a.academic_year = '2025-2026'
    `;

    db.get(query, [studentId], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Database error' });
        }

        if (!result) {
            return res.status(404).json({ error: 'Student account not found' });
        }

        res.json(result);
    });
});

// Get student transaction history
app.get('/api/student/transactions', authenticateToken, (req, res) => {
    const studentId = req.user.studentId;

    db.all(`SELECT * FROM transactions WHERE student_id = ? ORDER BY transaction_date DESC`, 
        [studentId], (err, transactions) => {
        if (err) {
            return res.status(500).json({ error: 'Database error' });
        }

        res.json(transactions);
    });
});

// Get payment methods with fees
app.get('/api/payment-methods', (req, res) => {
    db.all(`SELECT * FROM payment_methods WHERE is_active = 1`, (err, methods) => {
        if (err) {
            return res.status(500).json({ error: 'Database error' });
        }

        res.json(methods);
    });
});

// Calculate payment amount with fees
app.post('/api/calculate-payment', authenticateToken, (req, res) => {
    const { payment_method_code, base_amount } = req.body;

    db.get(`SELECT * FROM payment_methods WHERE method_code = ?`, [payment_method_code], (err, method) => {
        if (err) {
            return res.status(500).json({ error: 'Database error' });
        }

        if (!method) {
            return res.status(404).json({ error: 'Payment method not found' });
        }

        const serviceFee = method.service_fee;
        const percentageFee = (base_amount * method.percentage_fee) / 100;
        const totalFees = serviceFee + percentageFee;
        const totalAmount = base_amount + totalFees;

        res.json({
            base_amount,
            service_fee: serviceFee,
            percentage_fee: percentageFee,
            total_fees: totalFees,
            total_amount: totalAmount,
            method_name: method.method_name
        });
    });
});

// Process payment
app.post('/api/process-payment', authenticateToken, (req, res) => {
    const studentId = req.user.studentId;
    const { payment_method_code, amount, payment_reference } = req.body;

    db.serialize(() => {
        db.run('BEGIN TRANSACTION');

        // Get current account
        db.get(`SELECT * FROM accounts WHERE student_id = ? AND semester = 'FIRST SEMESTER' AND academic_year = '2025-2026'`, 
            [studentId], (err, account) => {
            if (err) {
                db.run('ROLLBACK');
                return res.status(500).json({ error: 'Database error' });
            }

            // Record payment transaction
            const transactionId = uuidv4();
            const paymentReference = payment_reference || `PAY${Date.now()}`;

            db.run(`INSERT INTO transactions (id, student_id, account_id, transaction_type, description, 
                    amount, payment_method, payment_reference, status) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
                [transactionId, studentId, account.id, 'PAYMENT', 
                 `PAYMENT RECEIVED (${paymentReference})`, -amount, payment_method_code, 
                 paymentReference, 'COMPLETED'], (err) => {
                if (err) {
                    db.run('ROLLBACK');
                    return res.status(500).json({ error: 'Failed to record payment' });
                }

                // Update account balances
                const newTotalPaid = account.total_paid + amount;
                const newRemainingBalance = account.remaining_balance - amount;
                const newPrelimAmountDue = Math.max(0, account.prelim_amount_due - amount);
                
                // Determine new status
                let newPrelimStatus = account.prelim_status;
                let newExamPermission = account.exam_permission;
                
                if (newPrelimAmountDue === 0) {
                    newPrelimStatus = 'PAID';
                    newExamPermission = 'PERMITTED';
                }

                db.run(`UPDATE accounts SET total_paid = ?, remaining_balance = ?, prelim_amount_due = ?, 
                        prelim_status = ?, exam_permission = ?, updated_at = CURRENT_TIMESTAMP 
                        WHERE id = ?`,
                    [newTotalPaid, newRemainingBalance, newPrelimAmountDue, newPrelimStatus, newExamPermission, account.id], 
                    (err) => {
                    if (err) {
                        db.run('ROLLBACK');
                        return res.status(500).json({ error: 'Failed to update account' });
                    }

                    db.run('COMMIT');
                    res.json({ 
                        success: true, 
                        message: 'Payment processed successfully',
                        transaction_id: transactionId,
                        payment_reference: paymentReference,
                        new_status: newPrelimStatus,
                        exam_permission: newExamPermission
                    });
                });
            });
        });
    });
});

// Get student grades (only if prelim is paid)
app.get('/api/student/grades', authenticateToken, (req, res) => {
    const studentId = req.user.studentId;

    // Check if student has paid prelim
    db.get(`SELECT prelim_status FROM accounts WHERE student_id = ? AND semester = 'FIRST SEMESTER' AND academic_year = '2025-2026'`, 
        [studentId], (err, account) => {
        if (err) {
            return res.status(500).json({ error: 'Database error' });
        }

        if (!account || account.prelim_status !== 'PAID') {
            return res.status(403).json({ error: 'Access denied. Prelim payment required to view grades.' });
        }

        // Get grades
        db.all(`SELECT * FROM grades WHERE student_id = ? AND semester = 'FIRST SEMESTER' AND academic_year = '2025-2026'`, 
            [studentId], (err, grades) => {
            if (err) {
                return res.status(500).json({ error: 'Database error' });
            }

            res.json(grades);
        });
    });
});

// Serve static files
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// Grades page
app.get('/grades', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'grades.html'));
});

// Start server
app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});

// Graceful shutdown
process.on('SIGINT', () => {
    db.close((err) => {
        if (err) {
            console.error('Error closing database:', err);
        } else {
            console.log('Database connection closed.');
        }
        process.exit(0);
    });
});