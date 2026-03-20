# Asset Tracking System

A RESTful API for tracking physical assets, built with Java 21, Spring Boot 3.2, MySQL, and Redis.

---

## Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

---

## Setup

### 1. Start Redis

```bash
docker run -d -p 6379:6379 redis:alpine
```

### 2. Create MySQL database

```sql
CREATE DATABASE asset_tracking_db;
```

### 3. Update `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    username: your_mysql_username
    password: your_mysql_password
```

### 4. Run

```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`

---

## Default Accounts

| Email | Password | Role |
|---|---|---|
| admin@assettracking.com | admin123 | ADMIN |
| user@assettracking.com | user123 | USER |

---

## API Endpoints

All endpoints except login require: `Authorization: Bearer <token>`

| Method | Endpoint | Description |
|---|---|---|
| POST | /auth/login | Login and get token |
| POST | /auth/logout | Invalidate token |
| POST | /assets | Create asset |
| GET | /assets | Get all assets |
| GET | /assets/{id} | Get asset by ID |
| PATCH | /assets/{id} | Update asset |
| DELETE | /assets/{id} | Soft delete asset |

---

## Quick Test

```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@assettracking.com","password":"admin123"}'

# Create asset (replace TOKEN with value from login)
curl -X POST http://localhost:8080/assets \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"MacBook Pro","category":"Laptop","serialNumber":"MBP-001","location":"Office A"}'

# Get all assets
curl http://localhost:8080/assets \
  -H "Authorization: Bearer TOKEN"

# Search assets
curl "http://localhost:8080/assets?search=laptop" \
  -H "Authorization: Bearer TOKEN"

# Logout
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer TOKEN"
  
  # Update asset (replace 1 with your asset ID)
curl -X PATCH http://localhost:8080/assets/1 \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"location":"Office B","status":"MAINTENANCE","assignedTo":"Jane Smith"}'
```
