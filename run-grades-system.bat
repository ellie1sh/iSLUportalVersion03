@echo off
REM Saint Louis University - Grades System Runner (Windows)
REM This script launches the Java-based grades system

echo Saint Louis University - Grades System
echo ======================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java JDK 8 or higher
    pause
    exit /b 1
)

REM Check if classes are compiled
if not exist "target\classes\edu\slu\grades" (
    echo Compiling Java classes...
    if not exist "target\classes" mkdir target\classes
    javac -d target\classes src\main\java\edu\slu\grades\*.java
    
    if %errorlevel% neq 0 (
        echo Error: Compilation failed
        pause
        exit /b 1
    )
    echo Compilation successful!
    echo.
)

REM Show menu
echo Select an option:
echo 1. Launch System Launcher (Recommended)
echo 2. Launch Student Portal
echo 3. Launch Faculty Portal
echo 4. Launch Demo Mode (Both Interfaces)
echo.
set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" (
    echo Starting System Launcher...
    java -cp target\classes edu.slu.grades.GradesSystemLauncher
) else if "%choice%"=="2" (
    echo Starting Student Portal...
    java -cp target\classes edu.slu.grades.GradesApplication
) else if "%choice%"=="3" (
    echo Starting Faculty Portal...
    java -cp target\classes edu.slu.grades.FacultyGradeManager
) else if "%choice%"=="4" (
    echo Starting Demo Mode...
    echo This will launch both Student and Faculty interfaces
    start java -cp target\classes edu.slu.grades.GradesSystemLauncher
    timeout /t 2 >nul
    echo Demo mode activated! Use the launcher to select 'Demo Mode'
) else (
    echo Invalid choice. Please run the script again and select 1-4
    pause
    exit /b 1
)

echo.
echo Thank you for using the Saint Louis University Grades System!
pause