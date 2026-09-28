# VisitorPass

## Problem Statement
In gated residential communities, tracking and verifying expected visitors at the gate is typically manual. Security guards need to constantly verify incoming guests over phone calls, which causes delays and can lead to unverified entry. Furthermore, there's no reliable digital trace of entry and revocation of passes.

## Features
- Create Flats and Residents
- Residents can create pre-approvals for their expected visitors.
- The system generates a single-use 6-digit OTP for entry.
- Residents can view their pending visitor approvals.
- Residents can revoke an un-used approval.
- Expiration of approval once the time window elapses.
- Security uses an API/dashboard to validate the OTP, effectively recording the entry timestamp and blocking future re-use.

## Technology Stack
- Java 17
- Spring Boot 3.1
- Spring Web
- Spring Data JPA
- Jakarta Bean Validation
- MySQL 8.x
- Maven
- Swagger/OpenAPI using SpringDoc
- HTML5, CSS3, JavaScript

## Database
Uses a MySQL database `visitor_pass_db`.
Four main entities:
1. `Flat`: Details of the flat including number, block, and floor.
2. `Resident`: Details of the person creating approvals. Belongs to a flat.
3. `VisitorPreApproval`: The core pre-approval document.
4. `OTPPass`: Stores the 6-digit OTP, generation info, usage info, and expiry time.

## Architecture
Layered Clean Architecture:
`Controller → Service → Repository → Database`
- Controllers handles REST endpoints and request parsing.
- Services handle business rules like validation logic, revocation logic, expiration logic.
- Repositories are for DB queries.

## Business Rules
1. **OTP can only be used once**: Checking OTP at the gate marks it used. A second attempt will give HTTP 409 Conflict.
2. **Revoked/expired approvals cannot allow entry**: If an approval is REVOKED or EXPIRED, security validation API immediately blocks entry (HTTP 409 Conflict).

## API List
### Flat
- `POST /api/flats`: Create flat
- `GET /api/flats`: Get all
- `GET /api/flats/{id}`: Get by ID

### Resident
- `POST /api/residents`: Create resident
- `GET /api/residents`: Get all
- `GET /api/residents/{id}`: Get by ID
- `GET /api/residents/{residentId}/pre-approvals/pending`: Get pending approvals for a resident.

### Visitor Pre-Approval
- `POST /api/pre-approvals`: Create approval (returns OTP)
- `GET /api/pre-approvals`: Get all
- `GET /api/pre-approvals/{id}`: Get by ID
- `PUT /api/pre-approvals/{id}/revoke`: Revoke approval

### Entry
- `POST /api/entry/validate`: Security OTP check. Requires `{"otp": "XXXXXX"}`.

## How to Run
1. Create a MySQL database named `visitor_pass_db`.
2. Configure credentials in `application.properties`.
3. Run the Spring Boot application using Maven: `mvn spring-boot:run`.
4. Open the frontend dashboard at `http://localhost:8085/`.
5. Open Swagger at `http://localhost:8085/swagger-ui.html`.

## Testing
1. Create a flat and a resident.
2. Use the resident to create a pre-approval (sets expected visit date). Note the returned OTP.
3. Test OTP via the Security section of the UI.
4. Try again and verify failure.
5. Create a new approval and revoke it. Try the OTP and verify failure.
