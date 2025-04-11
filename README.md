# Skillip Backend

Spring Boot backend service for the Skillip platform.

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- AWS S3 for file storage

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd skillip-backend
```

2. Create `application.yml` in `src/main/resources/`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/skillip
    username: your_username
    password: your_password
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your_jwt_secret_key
  expiration: 86400

aws:
  access-key: your_aws_access_key
  secret-key: your_aws_secret_key
  s3:
    bucket: your_bucket_name
```

3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Authentication
- POST `/auth/login` - User login

### Users
- POST `/users/` - Create new user
- GET `/users/{email}` - Get user by email
- POST `/users/become_talent` - Convert user to talent
- POST `/users/{email}/profile-image` - Upload profile image

## Security

All endpoints except `/auth/**` require JWT authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```