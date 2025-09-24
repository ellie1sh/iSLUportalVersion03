# Test Login Accounts

The following test accounts have been created for testing the login functionality:

## Available Test Accounts

| Student ID | Password | Student Name |
|------------|----------|--------------|
| 2250001 | password123 | John Michael Doe |
| 2250002 | mypassword | Jane Elizabeth Smith |
| 2250003 | test123 | Robert James Johnson |
| 2250004 | student456 | Maria Carmen Williams |
| 2250005 | login789 | David Alexander Brown |

## How to Login

1. Run the application: `java -cp src Login`
2. Enter any of the Student IDs from the table above
3. Enter the corresponding password
4. Click the "Login" button

## Files Created

- `Database.txt` - Main student database with student information
- `UserPasswordID.txt` - Password lookup file
- `paymentLogs.txt` - Payment transaction logs (initially empty)

## Notes

- All test accounts use sample birth dates and basic information
- The authentication system is now working correctly
- You can create new accounts using the "Account Request" link in the login form