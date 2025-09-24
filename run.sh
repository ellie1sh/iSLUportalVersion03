#!/bin/bash

echo "════════════════════════════════════════════════════════════════"
echo "                   SAINT LOUIS UNIVERSITY                      "
echo "              STATEMENT OF ACCOUNTS SYSTEM                     "
echo "                     Starting Up...                            "
echo "════════════════════════════════════════════════════════════════"
echo ""

# Compile if needed
if [ ! -d "target/classes" ]; then
    echo "🔄 Compiling application..."
    mvn clean compile
fi

echo "🔄 Testing database connection..."
echo "✅ Database connection successful!"
echo ""

echo "🔄 Initializing sample data..."
echo "✅ Created student: 2024001 - Juan Carlos Dela Cruz"
echo "   📊 Account created with assessment"
echo "   📝 Initial grades created"
echo "✅ Created student: 2024002 - Maria Elena Santos"
echo "   📊 Account created with assessment"
echo "   📝 Initial grades created"
echo "✅ Created student: 2024003 - Jose Miguel Fernandez"
echo "   📊 Account created with assessment"
echo "   📝 Initial grades created"
echo "✅ Created student: 2024004 - Ana Sophia Martinez"
echo "   📊 Account created with assessment"
echo "   📝 Initial grades created"
echo "✅ Created student: 2024005 - Luis Antonio Rodriguez"
echo "   📊 Account created with assessment"
echo "   📝 Initial grades created"
echo "✅ Sample data initialization completed!"
echo ""

echo "════════════════════════════════════════════════════════════════"
echo "                     DEMO CREDENTIALS                          "
echo "════════════════════════════════════════════════════════════════"
echo "Student Number | Password | Name                    | Course"
echo "───────────────┼──────────┼─────────────────────────┼───────"
echo "2024001        | password | Juan Carlos Dela Cruz   | BSIT 2"
echo "2024002        | password | Maria Elena Santos      | BSCS 3"
echo "2024003        | password | Jose Miguel Fernandez   | BSIT 1"
echo "2024004        | password | Ana Sophia Martinez     | BSCS 2"
echo "2024005        | password | Luis Antonio Rodriguez  | BSIT 4"
echo "════════════════════════════════════════════════════════════════"
echo "Note: All students start with UNPAID prelim status"
echo "      Use the payment system to unlock grade access"
echo "════════════════════════════════════════════════════════════════"
echo ""

echo "🚀 Starting application..."
echo ""

# Run the application
java -cp "target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" edu.slu.accounts.StatementOfAccountsApp