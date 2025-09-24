#!/bin/bash

# Saint Louis University - Grades System Runner
# This script launches the Java-based grades system

echo "Saint Louis University - Grades System"
echo "======================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java JDK 8 or higher"
    exit 1
fi

# Check if classes are compiled
if [ ! -d "target/classes/edu/slu/grades" ]; then
    echo "Compiling Java classes..."
    mkdir -p target/classes
    javac -d target/classes src/main/java/edu/slu/grades/*.java
    
    if [ $? -ne 0 ]; then
        echo "Error: Compilation failed"
        exit 1
    fi
    echo "Compilation successful!"
    echo ""
fi

# Show menu
echo "Select an option:"
echo "1. Launch System Launcher (Recommended)"
echo "2. Launch Student Portal"  
echo "3. Launch Faculty Portal"
echo "4. Launch Demo Mode (Both Interfaces)"
echo ""
read -p "Enter your choice (1-4): " choice

case $choice in
    1)
        echo "Starting System Launcher..."
        java -cp target/classes edu.slu.grades.GradesSystemLauncher
        ;;
    2)
        echo "Starting Student Portal..."
        java -cp target/classes edu.slu.grades.GradesApplication
        ;;
    3)
        echo "Starting Faculty Portal..."
        java -cp target/classes edu.slu.grades.FacultyGradeManager
        ;;
    4)
        echo "Starting Demo Mode..."
        echo "This will launch both Student and Faculty interfaces"
        java -cp target/classes edu.slu.grades.GradesSystemLauncher &
        sleep 2
        echo "Demo mode activated! Use the launcher to select 'Demo Mode'"
        ;;
    *)
        echo "Invalid choice. Please run the script again and select 1-4"
        exit 1
        ;;
esac

echo ""
echo "Thank you for using the Saint Louis University Grades System!"