### Saint Louis University — Statement of Accounts (Node/Express + SQLite)

A Node.js + Express web app with a SQLite database that demonstrates a Statement of Accounts system with payment processing and grades access control. The server seeds demo data on first run and serves a simple frontend from the `public` directory.

### Tech Stack
- Node.js, Express
- SQLite3 (file: `university.db`)
- JWT (auth), CORS, body-parser

### Prerequisites
- Node.js 18+ and npm

### Setup
1) Install dependencies
```bash
npm install
```
2) Start the server
```bash
npm start
```
3) Open the app
- Visit `http://localhost:3000`

On first run, the app creates tables and seeds demo students, accounts, transactions, payment methods, and subjects.

### Demo Login
- Student Number: `2024001`, Password: `password`
- Student Number: `2024002`, Password: `password`

### Features
- Student login with JWT
- Statement of Accounts with prelim amount due and remaining balance
- Multiple payment methods with service and percentage fees
- Payment calculation and processing (updates account status)
- Grades access gated until prelim payment is completed

### Frontend
- Served from `public/`
- `index.html` displays Statement of Accounts, payment methods, and a quick grades section
- Clicking “View Full Grades Page” goes to `grades.html` (demo grades UI)

### API Endpoints
- POST `/api/login` — body: `{ student_number, password }` → `{ token, student }`
- GET `/api/student/account` — auth: Bearer token
- GET `/api/student/transactions` — auth: Bearer token
- GET `/api/payment-methods`
- POST `/api/calculate-payment` — auth; body: `{ payment_method_code, base_amount }`
- POST `/api/process-payment` — auth; body: `{ payment_method_code, amount, payment_reference? }`
- GET `/api/student/grades` — auth; requires prelim paid

### Database
- File: `university.db` in project root
- To reset the demo, stop the server, delete `university.db`, and start again

### Configuration
- Port: `PORT` env var (default 3000)
- JWT secret: update `JWT_SECRET` in `server.js` for non-demo use

### Project Structure
```
server.js
public/
  ├─ index.html
  ├─ app.js
  ├─ styles.css
  ├─ grades.html
  ├─ grades.js
  └─ grades-styles.css
university.db
package.json
```

### Notes
- This is a demo. Password checking is simplified for seeded users. Do not use as-is in production.
- Payment integrations are simulated. Replace with real gateways and secure flows for production.