# GymMonitor-Lite

GymMonitor-Lite is a Kotlin/Spring Boot backend API project inspired by the GymMaster members app, with additional staff features based on the main GymMaster system.  
It provides functionality for both **members** and **staff** of a gym, including class booking, attendance tracking, membership management, and staff scheduling.

This project was primarily built to practice my Kotlin skills while leveraging my existing experience with Spring Boot, PostgreSQL, and REST APIs. It demonstrates real-world, role-based API design, clean architecture principles, and Kotlin-specific development practices—showing how I can build maintainable, production-ready backend systems similar to those used at GymMaster.

This API is designed for demo purposes, with preconfigured users and a Postman collection included to test all endpoints.

## 🛠 Tech Stack

- **Language:** Kotlin  
- **Framework:** Spring Boot  
- **Database:** PostgreSQL (with JPA/Hibernate)  
- **Authentication:** JWT (JSON Web Tokens)  
- **Testing / API Demo:** Postman  
- **Containerization / Deployment:** Docker  
- **Other Tools:** Spring Security, Gradle, LocalDateTime / JPA date handling


## 📌 API Endpoints

> **Base URL:** All endpoints begin with  
> `http://localhost:8080/api`

## 🔐 Authentication

### `POST /auth/signup`
Registers a **member** account for a specific club using a club code and membership plan.

**Request body**
```json
{
  "name": "Alice Wong",
  "email": "alice@example.com",
  "password": "Secret123!",
  "clubCode": "DTF001",
  "membershipPlanId": 1
}
```

**Responses**
- `200 OK` — `{ "message": "User registered successfully" }`
- `400 Bad Request` — invalid club code / plan or mismatched club
- `409 Conflict` — email already in use

**Notes**
- Emails are stored in lowercase to allow for case-insentitive login, and to avoid duplicate accounts with different casing.
- After signing up, you will not be automatically logged in to the account.
- Only new member users can be created.

---

### `POST /auth/login`
Authenticates with email & password. If successful, returns a JWT token that can be used to authenticate as that user.

**Request body**
```json
{
  "email": "alice@example.com",
  "password": "Secret123!"
}
```

**Response**
```json
{ "token": "<JWT>" }
```

**Notes**
- Email is case-insensitive. All emails are converted to lowercase before checking credentials.
- After logging in, you need to include the returned JWT token in the Authorization header (as Bearer Token) with each request to be authenticated as that user. The included postman tests use placeholders like {{staff_token}} and {{member_token}}, so you shouldn't need to manually type in your JWTs, just ensure the environment variables are set.

---

### `GET /auth/check`
Checks if the current user is authenticated. Returns user details if logged in, otherwise returns 401 Unauthorized.

**Request body**
```json
{
  "email": "alice@example.com",
  "password": "Secret123!"
}
```

**Response (if authenticated)**
```json
{
  "id": 101,
  "name": "Alice Wong",
  "role": "MEMBER"
}
```

**Response (if not authenticated)**
```json
{ "message": "Unauthorized" }
```

---

## 👤 Member Endpoints

> For these endpoints, you must be authenticated as a user with the MEMBER role.

### `GET /member/dashboard`
Returns a dashboard overview for the authenticated member. This includes their total number of bookings, their list of upcoming bookings (which includes the name of the class, location, start time, and duration in minutes), their total number of recorded visits to the club, and the amount of money they owe the club in dollars.

**Response example**
```json
{
  "message": "Alice Wong's Dashboard",
  "Total Bookings": 2,
  "Upcoming Bookings": [
    {
      "className": "Sunrise Power Yoga",
      "locationName": "Studio A",
      "startTime": "2025-08-20T06:30:00",
      "durationMinutes": 60
    },
    {
      "className": "Spin 45",
      "locationName": "Spin Room",
      "startTime": "2025-08-21T18:00:00",
      "durationMinutes": 45
    }
  ],
  "Total Visits": 14,
  "Amount owed to the club": "$0.00"
}
```

---

### `GET /member/timetable?date=YYYY-MM-DD`
Returns the timetable of classes at the member’s club. The `date` parameter is optional. If `date` is included, only classes which start on that day are returned. If it is omitted, all upcoming classes are returned.

**Response example** (array of `TimetableEntryDto`)
```json
[
  {
    "className": "Sunrise Power Yoga",
    "instructorName": "Jordan Lee",
    "locationName": "Studio A",
    "startTime": "2025-08-20T06:30:00",
    "durationMinutes": 60,
    "currentBookings": 7,
    "maxCapacity": 20
  },
  {
    "className": "Spin 45",
    "instructorName": "Taylor Kim",
    "locationName": "Spin Room",
    "startTime": "2025-08-20T18:00:00",
    "durationMinutes": 45,
    "currentBookings": 12,
    "maxCapacity": 18
  }
]
```

**Errors**
- `400 Bad Request` — invalid `date` format (expect `YYYY-MM-DD`)

**Notes**
- This timetable shows classes that are happening at the club, regardless of if the member user has booked them or not. For a timetable of classes the member has booked, this can be seen on the member's dashboard.

---

### `GET /member/membership`
Returns membership details (club, join date, visits, plan, next billing, amount due).

**Response example**
```json
{
  "clubName": "Downtown Fitness",
  "dateJoined": "2025-08-01T22:10:12Z",
  "totalVisits": 14,
  "membershipPlanName": "Weekly Unlimited",
  "nextBillingDate": "2025-08-25",
  "amountDue": "$0.00"
}
```

---

### `POST /member/scan-in`
Records a visit (simulates scanning in at the door).

**Response example**
```json
{ "message": "Scan successful", "totalVisits": 15 }
```

**Notes**
- Staff cannot use this scan-in endpoint as in a real-world scenario, I would assume they would have a separate clock-in/clock-out system.

---

## 🧭 Class Endpoints

### `GET /classes/{id}`
Returns details for a specific class.

**Response example** (`GymClassDetailsDto`)
```json
{
  "name": "Sunrise Power Yoga",
  "instructorName": "Jordan Lee",
  "startTime": "2025-08-20T06:30:00",
  "endTime": "2025-08-20T07:30:00",
  "clubName": "Downtown Fitness",
  "locationName": "Studio A",
  "description": "A high-energy morning flow."
}
```

---

### `POST /classes/{id}/book`  
**Requires MEMBER role.** Books the class for the authenticated member.

**Success**
```json
{ "message": "Successfully booked class" }
```

**Errors**
- `404 Not Found` — user or class not found
- `403 Forbidden` — user is not a member
- `400 Bad Request` — already booked / class full / weekly limit reached / class in the past

---

## 🧑‍🏫 Staff Endpoints

> For these endpoints, you must be authenticated as a user with the STAFF role.

### `GET /staff/members`
Lists all members in the staff user’s club.

**Response example** (array of `StaffViewMemberSummaryDto`)
```json
[
  { "id": 101, "name": "Alice Wong", "membershipPlanName": "Weekly 3x", "owesUs": "$10.00" },
  { "id": 102, "name": "Ben Carter", "membershipPlanName": "5 Classes per Week", "owesUs": "$6.50" }
]
```

---

### `GET /staff/schedule?date=YYYY-MM-DD`
Returns classes **taught by the authenticated staff member**. The `date` parameter is optional. If `date` is included, only classes which start on that day are returned. If it is omitted, all upcoming classes are returned.

**Response example** (array of `StaffScheduleEntryDto`)
```json
[
  {
    "className": "Sunrise Power Yoga",
    "locationName": "Studio A",
    "startTime": "2025-08-20T06:30:00",
    "durationMinutes": 60,
    "currentBookings": 7,
    "maxCapacity": 20
  }
]
```

**Errors**
- `400 Bad Request` — invalid `date` format (expect `YYYY-MM-DD`)

---

### `POST /staff/classes`
Creates a new class with the authenticated staff member as the instructor.

**Request body** (`CreateGymClassRequestDto`)
```json
{
  "locationId": 3,
  "name": "Evening Yoga",
  "description": "Relaxing flow yoga session",
  "startTime": "2025-08-20T18:00:00",
  "endTime": "2025-08-20T19:00:00",
  "maxCapacity": 20
}
```

**Response**
- Returns the created class **including its generated `id`**. Example:
```json
{
  "id": 42,
  "name": "Evening Yoga",
  "description": "Relaxing flow yoga session",
  "startTime": "2025-08-20T18:00:00",
  "endTime": "2025-08-20T19:00:00",
  "locationName": "Yoga Studio",
  "maxCapacity": 20
}
```

**Notes**
- Only locations belonging to the staff member’s club may be used (validated in service).
- `400/404` style errors may be returned for invalid location IDs, cross-club usage, etc.


## 🗓️ Scheduled Billing

The system runs a daily scheduler at **2:00 AM** to automatically handle membership billing updates. For each user, it:

1. Checks if their membership billing date is today or has already passed.
2. Updates their billing records with the amount due for their current membership plan.
3. Updates their next billing date according to the plan’s billing period.

This ensures that membership billing information stays up-to-date automatically.


## 🧪 Postman Tests

A Postman collection is included in this repo to test all API endpoints. It contains 32 tests covering both success cases and important error cases for each endpoint.

These tests are intended to be run **in order**, as some depend on data created in previous requests.  

Each Postman test includes a full description of its purpose and expected outcome, with test scripts to validate that the endpoint returns the expected status code.

- **Initial Demo Data:**  
  The app includes pre-populated demo data located in `data.sql`. Each time the application starts, the database is **wiped and repopulated** with this demo data. This ensures a consistent starting state for testing and exploration of all features.

- The collection is preconfigured with environment variables for JWT tokens (e.g. `{{member_token}}`, `{{staff_token}}`), so you **do not need to manually generate or paste tokens** to test endpoints.  
- The initial demo data generates dates and times relative to when the app is run (e.g., ± a certain offset from the current time). This ensures that most tests work correctly regardless of when the app is started.
- For demo purposes:
  - **Club 1** is the only club with meaningful data.  
  - **Demo member user (ActiveGymGoer):**  
    ```json
    {
      "email": "active@dtf.com",
      "password": "password"
    }
    ```
    Use this account to test member-only endpoints like `/member/dashboard` or booking classes. This account is pre-populated with sufficient data to explore and test all member-related functionality in the API.

### Running the Postman Tests

1. **Ensure the app is running**  
   Follow the steps in the "How to Run" section to start the Kotlin app and the PostgreSQL database. The API must be accessible before running the tests.

2. **Open Postman**  
   If you don’t already have a workspace, create one for this project.

3. **Import the collection and environment**  
   - Go to the **postman/** folder in the repo.  
   - Import `GymMonitor-Lite.postman_collection.json` as the collection.  
   - Import `GymMonitor-Lite.postman_environment.json` as the environment.

4. **Select the environment**  
   In Postman, switch to the imported environment (`GymMonitor-Lite Test Environment`) to ensure all environment variables (e.g., `{{member_token}}`, `{{staff_token}}`) are available.

5. **Run the tests**  
   - Open the imported collection.  
   - Run the requests **in order**, as some tests depend on data created in previous requests.  
   - You can run tests individually, or use the Collection Runner to execute the entire suite of tests at once.
   - The login requests include scripts that automatically populate JWT tokens in the environment for subsequent requests.  

6. **Check the results**  
   - Each test validates the expected status code, and the login tests will also validate that a JWT token was given.  
   - Passed requests show **PASSED** in green, failed requests show **FAILED** in red. If the Collection Runner is used, a summary of total tests passed/failed is displayed at the end.



## How to run

> **Note:** You must have Docker running before starting these steps.  
> - **Windows / macOS:** Open **Docker Desktop** to start the Docker Engine.  
> - **Linux:** Ensure the Docker daemon (`dockerd`) is running (usually starts automatically).

0. Before starting the containers, create a `.env` file in the project root with the following variables:

```env
DB_URL=jdbc:postgresql://db:5432/postgres
PG_USER=postgres
PG_PASSWORD=postgres

APP_JWT_SECRET=n2D90Zp8m4hYJxTz7KsA0PqRfWxVlEbCgHsJrT1MvYfW6p8dKqNxRsTuVzYxAzGp
APP_JWT_EXPIRATION_MS=86400000
```

You can use these example values for testing/demo purposes. 
In a production environment, replace APP_JWT_SECRET with a secure random string.

1. Open your terminal.  

2. Navigate to the project folder:

```bash
cd path/to/GymMonitor-Lite
```

3. Start the PostgreSQL database container:

```bash
docker compose up -d db
```

4. Build the Kotlin app container:

```bash
docker compose build
```

5. Start the Kotlin app container:

```bash
3. docker compose up kotlinapp
```
