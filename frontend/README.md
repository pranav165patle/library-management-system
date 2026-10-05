# Library Management Frontend

React + Vite frontend for the existing Spring Boot Library Management API.

## Run

1. Start the Spring Boot backend on `http://localhost:8080`.
2. Make sure MySQL/database is running.
3. From this folder:

```bash
npm install
npm run dev
```

Open `http://localhost:5173`.

## API
The UI calls:
- `/api/books`
- `/api/members`
- `/api/categories`
- `/api/borrowings`
- `/api/borrowings/issue`
- `/api/borrowings/return/{id}`
