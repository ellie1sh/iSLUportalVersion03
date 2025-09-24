# Saint Louis University - Statement of Accounts System

A comprehensive Java-based Statement of Accounts management system for university students with payment processing, grades management, and access control.

## Features

### 🎓 Student Management
- Student authentication and session management
- Account creation with financial assessment
- Personal information management

### 💰 Payment Processing
- Multiple payment channels with different fee structures:
  - **UnionBank UPay Online** - No fees
  - **Dragonpay Payment Gateway** - ₱25 service fee + 2% processing fee
  - **BPI Online** - ₱15 service fee
  - **BDO Online** - ₱20 service fee
  - **BDO Bills Payment** - ₱10 service fee
  - **Bukas Tuition Installment Plans** - 3.5% processing fee

### 📊 Grades Management
- Secure grade access (only after payment)
- Prelim grades with GPA calculation
- Grade status and performance indicators

### 🔐 Access Control
- Payment-based access to grades
- Exam permission management
- Session-based authentication

### 💾 Database Features
- SQLite database with full CRUD operations
- Transaction history tracking
- Real-time balance updates

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Installation & Setup

1. **Clone or extract the project**
   ```bash
   cd /workspace
   ```

2. **Compile the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn exec:java
   ```

   Or alternatively:
   ```bash
   mvn clean package
   java -jar target/statement-of-accounts-1.0.0.jar
   ```

## Demo Credentials

The system comes with pre-loaded demo data:

| Student Number | Password | Name                    | Course | Status  |
|----------------|----------|-------------------------|--------|---------|
| 2024001        | password | Juan Carlos Dela Cruz   | BSIT 2 | UNPAID  |
| 2024002        | password | Maria Elena Santos      | BSCS 3 | UNPAID  |
| 2024003        | password | Jose Miguel Fernandez   | BSIT 1 | UNPAID  |
| 2024004        | password | Ana Sophia Martinez     | BSCS 2 | UNPAID  |
| 2024005        | password | Luis Antonio Rodriguez  | BSIT 4 | UNPAID  |

## How to Use

### 1. Login
- Use any of the demo credentials above
- Student Number: `2024001`
- Password: `password`

### 2. View Statement of Accounts
- Shows current payment status
- Displays amount due for prelims (₱15,000)
- Shows remaining balance
- Indicates exam permission status

### 3. Make Payment
- Choose from 6 different payment methods
- Each method has different fees
- Real-time fee calculation
- Payment confirmation and reference generation

### 4. View Grades
- **Before Payment**: Access denied message
- **After Payment**: Full grade access with GPA calculation
- Prelim grades for all enrolled subjects

### 5. Transaction History
- Complete breakdown of all fees and payments
- Formatted date and amount display
- Payment references and methods

## System Flow

1. **Student Login** → Authentication
2. **View Account** → Shows unpaid status (₱15,000 due)
3. **Choose Payment Method** → Calculate fees
4. **Process Payment** → Update balances and status
5. **Access Grades** → Now available after payment
6. **Exam Permission** → Automatically granted

## Key Features Demonstrated

### Payment Processing
```
Base Amount:      ₱15,000.00
Service Fee:      ₱25.00 (Dragonpay)
Processing Fee:   ₱300.00 (2%)
─────────────────────────────
TOTAL AMOUNT:     ₱15,325.00
```

### Grade Access Control
- **Before Payment**: "❌ ACCESS DENIED - Prelim payment required"
- **After Payment**: Full grade display with GPA calculation

### Real-time Updates
- Payment immediately updates account balance
- Status changes from UNPAID → PAID
- Exam permission changes from NOT_PERMITTED → PERMITTED
- Grade access becomes available instantly

## Technical Architecture

### Models
- `Student` - Student information and authentication
- `Account` - Financial account with payment status
- `Transaction` - Payment and fee records
- `Grade` - Academic grades with access control
- `PaymentMethod` - Payment channels with fee structures

### Services
- `AuthenticationService` - Login and session management
- `AccountService` - Financial account operations
- `PaymentService` - Payment processing with fees
- `GradeService` - Grade management with access control
- `DataInitializationService` - Sample data setup

### Data Access
- `StudentDAO` - Student database operations
- `AccountDAO` - Account database operations
- `TransactionDAO` - Transaction database operations
- `GradeDAO` - Grade database operations

### Database Schema
- **students** - Student personal information
- **accounts** - Financial accounts and payment status
- **transactions** - All financial transactions
- **grades** - Academic grades and subjects
- **payment_methods** - Available payment channels

## Payment Methods & Fees

| Method | Service Fee | Processing Fee | Total Fee Example* |
|--------|-------------|----------------|-------------------|
| UnionBank UPay | ₱0.00 | 0% | ₱0.00 |
| Dragonpay | ₱25.00 | 2% | ₱325.00 |
| BPI Online | ₱15.00 | 0% | ₱15.00 |
| BDO Online | ₱20.00 | 0% | ₱20.00 |
| BDO Bills | ₱10.00 | 0% | ₱10.00 |
| Bukas | ₱0.00 | 3.5% | ₱525.00 |

*Based on ₱15,000 payment amount

## Sample Output

```
═══════════════════════════════════════════════════════════════
                    STATEMENT OF ACCOUNTS                      
                   FIRST SEMESTER, 2025-2026                  
═══════════════════════════════════════════════════════════════

Your amount due for PRELIM is: ₱15,000.00
Your remaining balance is: ₱45,000.00

PRELIM STATUS: UNPAID
EXAM PERMISSION: NOT_PERMITTED

⚠️  Please pay before prelim exams.
   Ignore if you're SLU Dependent or Full TOF Scholar.
```

## Development

### Project Structure
```
src/main/java/edu/slu/accounts/
├── model/          # Data models
├── dao/            # Database access objects
├── service/        # Business logic services
├── ui/             # User interface
├── util/           # Utility classes
└── StatementOfAccountsApp.java  # Main application
```

### Dependencies
- SQLite JDBC Driver
- BCrypt for password hashing
- Gson for JSON processing
- SLF4J for logging
- JUnit for testing

## License

This project is created for educational purposes as a demonstration of a university statement of accounts system.

---

**Note**: This is a demo system with simulated payment processing. In a production environment, integrate with actual payment gateways and implement proper security measures.