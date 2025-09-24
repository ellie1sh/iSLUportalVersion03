# Saint Louis University - Student Portal System

## Overview

A comprehensive **Java Swing-based Student Portal System** that provides students with access to all their academic information and services in one integrated application. The system has been completely converted from HTML/CSS/JavaScript to pure Java, maintaining the original UI/UX design and functionality.

## Features

### 🔐 Authentication System
- Secure login with database-backed authentication
- Session management
- Demo credentials available for testing

### 🏠 Dashboard (Home)
- Welcome message with student information
- Quick overview cards showing:
  - Payment Status
  - Grade Access
  - Attendance Percentage
  - Current Semester
- Recent activities feed
- Important notices and reminders

### 💰 Statement of Accounts
- Complete payment status overview
- Fee breakdown table with detailed transactions
- Multiple online payment channels:
  - UnionBank UPay Online
  - Dragonpay Payment Gateway
  - BPI Online
  - BDO Online
  - BDO Bills Payment
  - Bukas Tuition Installment Plans
- Real-time payment processing
- Payment confirmation and receipt generation

### 📊 Grades
- Comprehensive grades table with:
  - Class Code
  - Course Number
  - Units
  - Prelim, Midterm, Tentative Final, and Final Grades
  - Weights
- Color-coded grade display (green for passing, red for failing)
- Grade access control based on payment status
- Legend with grade interpretations
- Passing grade information for undergraduate and graduate levels

### 📅 Schedule
- Weekly class schedule in table format
- Room assignments and time slots
- Visual schedule layout

### ✅ Attendance
- Attendance summary cards
- Detailed attendance by subject
- Attendance percentage calculations
- Color-coded attendance status

### 📄 Additional Modules
- **Transcript of Records**: Official transcript access
- **Curriculum Checklist**: Academic progress tracking
- **Medical Record**: Health information management
- **Personal Details**: Student profile management
- **Journal/Periodical**: Academic resources access
- **Downloadables/About ISLU**: University information and resources

## Technical Architecture

### Technology Stack
- **Language**: Java 21
- **UI Framework**: Java Swing
- **Database**: SQLite (embedded)
- **Build System**: Maven-compatible structure
- **Authentication**: Custom implementation with SHA-256 hashing

### Project Structure
```
src/main/java/
├── edu/slu/portal/
│   ├── StudentPortalApp.java          # Main application entry point
│   └── ui/
│       ├── StudentPortalUI.java       # Main portal interface
│       └── panels/                    # Individual content panels
│           ├── BasePanel.java         # Base class for all panels
│           ├── HomePanel.java         # Dashboard
│           ├── StatementOfAccountsPanel.java
│           ├── GradesPanel.java
│           ├── SchedulePanel.java
│           ├── AttendancePanel.java
│           └── ... (other panels)
├── edu/slu/accounts/                  # Existing account services
│   ├── service/                       # Business logic services
│   ├── model/                         # Data models
│   ├── dao/                          # Database access objects
│   └── util/                         # Utility classes
└── edu/slu/grades/                   # Grade-related classes
    ├── Grade.java
    ├── Student.java
    └── ...
```

### Key Components

#### StudentPortalUI
- Main application window with login and portal interfaces
- Sidebar navigation with all menu items
- Dynamic content panel switching
- Responsive layout design

#### BasePanel
- Abstract base class for all content panels
- Common UI components and styling
- Utility methods for creating cards, buttons, and layouts

#### Service Layer
- **AuthenticationService**: User login and session management
- **AccountService**: Payment and account management
- **GradeService**: Grade access and display
- **PaymentService**: Payment processing and methods

## Installation and Setup

### Prerequisites
- Java 21 or higher
- No additional dependencies required (pure Java implementation)

### Running the Application

1. **Compile the project**:
   ```bash
   # Create target directory
   mkdir -p target/classes
   
   # Compile all Java files
   find src -name "*.java" -exec javac -cp src/main/java -d target/classes {} +
   ```

2. **Run the application**:
   ```bash
   java -cp target/classes edu.slu.portal.StudentPortalApp
   ```

### Demo Credentials
- **Student 1**: `2024001` / `password` (Unpaid status)
- **Student 2**: `2024002` / `password` (Paid status)

## Features in Detail

### Login System
- Clean, modern login interface
- University branding and styling
- Demo credentials display
- Error handling and validation

### Navigation
- Sidebar with 11 main menu items
- Visual icons for each section
- Active menu highlighting
- Smooth transitions between panels

### Payment Integration
- Multiple payment gateway support
- Real-time payment processing simulation
- Payment confirmation dialogs
- Status updates after successful payment

### Grade Management
- Grade access control based on payment status
- Comprehensive grade display with proper formatting
- Real-time grade updates capability
- Educational grade legend and passing criteria

### Responsive Design
- Scalable UI components
- Proper window sizing and layout
- Scroll support for long content
- Modern card-based design

## Database Schema

The application uses the existing database schema with tables for:
- Students
- Accounts
- Transactions
- Grades
- Payment methods

## Security Features

- Password hashing using SHA-256 with salt
- Session management
- Input validation
- Secure payment processing

## Future Enhancements

- Real-time notifications
- Document upload/download functionality
- Email integration
- Mobile-responsive web version
- Advanced reporting features

## Support and Maintenance

The system is designed for easy maintenance and extension:
- Modular panel-based architecture
- Consistent styling through BasePanel
- Comprehensive error handling
- Logging and debugging support

## License

Copyright © 2025 Saint Louis University Inc. All rights reserved.

---

**Note**: This system has been completely converted from HTML/CSS/JavaScript to pure Java Swing while maintaining all original functionality and UI/UX design principles. The application provides a comprehensive student portal experience with modern interface design and robust functionality.