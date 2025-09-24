# Saint Louis University - Student Portal System

A comprehensive dual-platform student management system featuring both desktop and web applications for grades management and statement of accounts processing.

## 🏗️ System Architecture

This project consists of two integrated applications:

### 1. 🖥️ **Java Swing Desktop Application** - Grades Management System
- **Student Portal**: View grades and academic information
- **Faculty Portal**: Real-time grade management and updates
- **System Launcher**: Choose between student and faculty interfaces

### 2. 🌐 **Node.js Web Application** - Statement of Accounts System
- **Payment Processing**: Multiple payment channels with fee calculation
- **Account Management**: Balance tracking and payment history
- **Grade Access Control**: Payment-based access to academic records

## ✨ Features

### Desktop Application (Java Swing)
- **📊 Student Grades Interface**
  - Real-time grade viewing with professional UI
  - Comprehensive grade legend and grading system information
  - Support for Prelim, Midterm, and Final grades
  - Course information display with units and class codes

- **👨‍🏫 Faculty Grade Manager**
  - Real-time grade input and updates
  - Student roster management
  - Direct integration with student portal for instant updates
  - Comprehensive grade tracking across all periods

- **🚀 System Launcher**
  - Unified entry point for both interfaces
  - Demo mode with side-by-side windows
  - Professional university branding

### Web Application (Node.js + Express)
- **💰 Payment Processing**
  - Multiple payment channels:
    - UnionBank UPay Online (No fees)
    - Dragonpay Payment Gateway (₱25 + 2% processing)
    - BPI Online (₱15 service fee)
    - BDO Online (₱20 service fee)
    - BDO Bills Payment (₱10 service fee)
    - Bukas Tuition Installment Plans (3.5% processing)

- **📋 Account Management**
  - Real-time balance tracking
  - Payment history and transaction logs
  - Automatic status updates (UNPAID → PAID)
  - Exam permission management

- **🔐 Grade Access Control**
  - Payment-based access to grades
  - Secure authentication system
  - Integration with payment status

## 🛠️ Prerequisites

### For Java Desktop Application
- **Java 17** or higher
- **Maven 3.6** or higher

### For Web Application
- **Node.js 16** or higher
- **npm** (comes with Node.js)

## 🚀 Installation & Setup

### 1. Clone and Setup
```bash
git clone <repository-url>
cd workspace
```

### 2. Setup Web Application
```bash
# Install Node.js dependencies
npm install

# Start the web server
npm start
```
The web application will be available at `http://localhost:3000`

### 3. Setup Desktop Application
```bash
# Compile Java application
mvn clean compile

# Run the system launcher
mvn exec:java -Dexec.mainClass="edu.slu.grades.GradesSystemLauncher"
```

### Alternative: Run Individual Applications
```bash
# Run Statement of Accounts (Console)
mvn exec:java -Dexec.mainClass="edu.slu.accounts.StatementOfAccountsApp"

# Run Student Grades Portal
mvn exec:java -Dexec.mainClass="edu.slu.grades.GradesApplication"

# Run Faculty Grade Manager
mvn exec:java -Dexec.mainClass="edu.slu.grades.FacultyGradeManager"
```

## 🎯 Demo Credentials

### Web Application
| Student Number | Password | Name                    | Course | Status  |
|----------------|----------|-------------------------|--------|---------|
| 2024001        | password | Juan Carlos Dela Cruz   | BSIT 2 | UNPAID  |
| 2024002        | password | Maria Elena Santos      | BSCS 3 | UNPAID  |

### Desktop Application
- **Student**: 2024001 / password
- **Faculty**: Use the faculty interface to update grades in real-time

## 📖 How to Use

### Web Application Flow
1. **Login** → Use demo credentials (2024001 / password)
2. **View Account** → See payment status and balance (₱15,000 due for prelims)
3. **Choose Payment Method** → Select from 6 available channels
4. **Process Payment** → Real-time fee calculation and payment processing
5. **Access Grades** → Available only after payment completion
6. **View Transaction History** → Complete audit trail of all transactions

### Desktop Application Flow
1. **Launch System** → Run the GradesSystemLauncher
2. **Choose Interface** → Student Portal, Faculty Portal, or Demo Mode
3. **Student Portal** → Login and view grades in real-time
4. **Faculty Portal** → Update grades and see instant student notifications
5. **Demo Mode** → Experience both interfaces simultaneously

## 💡 Key Features Demonstrated

### Real-Time Integration
- Faculty grade updates appear instantly in student portal
- Automatic notifications for grade changes
- Synchronized data across both interfaces

### Payment Processing
```
Example: Dragonpay Payment
Base Amount:      ₱15,000.00
Service Fee:      ₱25.00
Processing Fee:   ₱300.00 (2%)
─────────────────────────────
TOTAL AMOUNT:     ₱15,325.00
```

### Grade Access Control
- **Before Payment**: "❌ ACCESS DENIED - Prelim payment required"
- **After Payment**: Full grade access with GPA calculation
- **Real-time Updates**: Faculty changes appear instantly

## 🏗️ Technical Architecture

### Desktop Application (Java Swing)
```
src/main/java/edu/slu/grades/
├── GradesSystemLauncher.java    # Main launcher
├── GradesApplication.java       # Student portal
├── FacultyGradeManager.java     # Faculty interface
├── Student.java                 # Student data model
├── Grade.java                   # Grade data model
└── Course.java                  # Course data model
```

### Web Application (Node.js)
```
├── server.js                    # Express server & API
├── package.json                 # Dependencies
├── public/
│   ├── index.html              # Main web interface
│   ├── grades.html             # Grades page
│   ├── app.js                  # Frontend JavaScript
│   ├── grades.js               # Grades functionality
│   └── styles.css              # Styling
└── university.db               # SQLite database
```

### Statement of Accounts (Java Console)
```
src/main/java/edu/slu/accounts/
├── StatementOfAccountsApp.java  # Main console app
├── model/                       # Data models
├── dao/                         # Database access
├── service/                     # Business logic
├── ui/                          # Console interface
└── util/                        # Utilities
```

## 🗄️ Database Schema

### Web Application (SQLite)
- **students** - Student information and authentication
- **accounts** - Financial accounts and payment status
- **transactions** - All payment and fee records
- **grades** - Academic grades with access control
- **payment_methods** - Available payment channels

### Desktop Application
- Uses in-memory data structures for demonstration
- Real-time synchronization between student and faculty interfaces

## 🎨 User Interface

### Desktop Application
- Professional Swing UI with Saint Louis University branding
- Real-time grade updates with visual notifications
- Comprehensive grade legend and academic information
- Side-by-side demo mode for testing

### Web Application
- Modern responsive design
- Interactive payment processing
- Real-time balance updates
- Professional university portal styling

## 🔧 Development

### Project Structure
```
/workspace/
├── src/main/java/              # Java source code
│   ├── edu/slu/accounts/       # Statement of Accounts system
│   └── edu/slu/grades/         # Grades management system
├── public/                     # Web application files
├── target/                     # Compiled Java classes
├── server.js                   # Node.js web server
├── pom.xml                     # Maven configuration
├── package.json                # Node.js dependencies
└── university.db               # SQLite database
```

### Dependencies

#### Java (Maven)
- SQLite JDBC Driver
- Gson for JSON processing
- BCrypt for password hashing
- SLF4J for logging
- JUnit for testing

#### Node.js (npm)
- Express.js web framework
- SQLite3 database driver
- CORS middleware
- BCrypt password hashing
- JWT authentication
- UUID generation

## 🚦 Running the Applications

### Quick Start - All Systems
```bash
# Terminal 1: Start web server
npm start

# Terminal 2: Launch desktop application
mvn exec:java -Dexec.mainClass="edu.slu.grades.GradesSystemLauncher"
```

### Individual Applications
```bash
# Web application only
npm start

# Console Statement of Accounts
mvn exec:java -Dexec.mainClass="edu.slu.accounts.StatementOfAccountsApp"

# Desktop Grades System
mvn exec:java -Dexec.mainClass="edu.slu.grades.GradesSystemLauncher"
```

## 📋 Payment Methods & Fees

| Method | Service Fee | Processing Fee | Total Fee (₱15,000)* |
|--------|-------------|----------------|---------------------|
| UnionBank UPay | ₱0.00 | 0% | ₱0.00 |
| Dragonpay | ₱25.00 | 2% | ₱325.00 |
| BPI Online | ₱15.00 | 0% | ₱15.00 |
| BDO Online | ₱20.00 | 0% | ₱20.00 |
| BDO Bills | ₱10.00 | 0% | ₱10.00 |
| Bukas | ₱0.00 | 3.5% | ₱525.00 |

*Based on ₱15,000 prelim payment amount

## 🎓 Academic System

### Grading Scale
- **Undergraduate**: Passing grade 75% | Failure: Below 75%
- **Graduate School**: Passing grade 85% | Failure: Below 85%

### Grade Legends
- **P** - Passed | **HP** - High Pass
- **INC** - Incomplete | **WP** - Withdrawal w/ Permission
- **D** - Dropped | **F** - Failure
- **NC** - No Credit | **NFE** - No Final Examination

## 📄 License

This project is created for educational purposes as a demonstration of a comprehensive university student management system integrating both desktop and web technologies.

---

**Note**: This is a demo system with simulated payment processing. In a production environment, integrate with actual payment gateways, implement proper security measures, and use enterprise-grade databases.

**System Requirements**: Java 17+, Node.js 16+, Maven 3.6+