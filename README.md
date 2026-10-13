# Smart Office Issue & Maintenance Platform

## 🔗 Live Demo
**https://smart-office-fpm2.onrender.com**

> Hosted on a free tier: the first load can take up to a minute to wake up. Demo data resets when the app restarts.

| Role | Login |
|---|---|
| Admin | admin@smartoffice.local / Office@2026 |
| Employee | Click **Register** to create an account |

A Spring Boot 4 + Java 21 maintenance-management application with separate **USER** and **ADMIN** workflows, JWT authentication, role-based authorization, issue tracking, technician assignment, notifications, attachments, status history, statistics and a browser-based UI.

## Main workflow

### Employee / USER
1. Register account.
2. Login.
3. Open the employee dashboard.
4. Report an issue with category, priority and optional attachment.
5. View only their own issues.
6. Open issue details and follow the status timeline.
7. Receive in-app notifications when an administrator assigns a technician or changes status.
8. Mark notifications as read.
9. Update profile name/phone through `PUT /auth/me/profile`.

### ADMIN
1. Login using the first-run admin account configured by environment variables.
2. Open the admin dashboard.
3. View all issues and statistics.
4. Review issues.
5. Assign available technicians.
6. Move issues through `NEW -> REVIEWED -> ASSIGNED -> IN_PROGRESS -> RESOLVED -> CLOSED`.
7. Manage employees and technicians.
8. Manage issue categories.
9. Review issue history and maintenance data.
10. Send email notifications when SMTP is configured.

## Technology

- Java 21
- Spring Boot 4.1.1
- Spring MVC / REST
- Spring Data JPA / Hibernate
- MySQL
- Spring Security
- JWT (JJWT)
- BCrypt password hashing
- Bean Validation
- Spring Mail
- Springdoc OpenAPI / Swagger
- Actuator
- HTML/CSS/JavaScript frontend

## Run locally

### 1. Requirements

- JDK 21
- MySQL 8.x
- Internet access for Maven dependency download on the first build

### 2. Create the database

The application uses database `smart_office`. The JDBC URL is configured to create it automatically when the MySQL user has permission to do so. You can also create it manually:

```sql
CREATE DATABASE smart_office;
```

### 3. Configure environment variables

Copy `.env.example` values into your operating-system environment. At minimum configure:

```text
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=your-long-random-secret-at-least-32-characters
ADMIN_EMAIL=admin@yourcompany.com
ADMIN_PASSWORD=ChangeThisPassword123!
ADMIN_NAME=Smart Office Admin
ADMIN_PHONE=9999999999
```

Email variables are optional unless you want SMTP email sending.

**Do not commit real passwords, JWT secrets or mail app passwords to GitHub.**

### 4. Start the application

Windows:

```bat
mvnw.cmd spring-boot:run
```

or, after Maven is installed:

```bat
mvn spring-boot:run
```

The application runs on:

`http://localhost:9067`

### 5. Login

Open:

`http://localhost:9067/`

The first application startup creates the configured admin account automatically. Normal employees can create their own USER accounts from the registration tab.

## Important security behavior

- Passwords are stored as BCrypt hashes.
- JWTs are stored in an HttpOnly `access_token` cookie for the browser workflow.
- APIs also accept `Authorization: Bearer <token>` for API clients.
- USER accounts can access only their own issues.
- ADMIN accounts can access all management APIs.
- Employee and technician management is ADMIN-only.
- Issue status changes and technician assignment are ADMIN-only.
- Issue deletion is ADMIN-only.
- `/auth/me` returns the currently authenticated profile.
- Logout clears the JWT cookie.

For production HTTPS, set the JWT cookie's `Secure` flag to true and use a strong random `JWT_SECRET`.

## Swagger

After startup:

`http://localhost:9067/swagger-ui/index.html`

## Health check

`http://localhost:9067/actuator/health`

## GitHub checklist

Before pushing:

- Do not upload `.env` files.
- Do not upload database passwords or SMTP passwords.
- Do not upload generated `target/` files.
- Do not upload real user attachments.
- Keep `.env.example` as the safe configuration template.
- Change the default first-run admin password before deployment.

## Project structure

```text
src/main/java/com/smartoffice
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── security
├── service
└── SmartOfficeApplication.java

src/main/resources/static
├── index.html
├── dashboard.html
├── admin-dashboard.html
├── issues.html
├── issue-details.html
├── create-issue.html
├── notifications.html
├── employees.html
├── technicians.html
└── auth.js
```
