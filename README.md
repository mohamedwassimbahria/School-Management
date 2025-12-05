# School Management Application

A full-stack school management system built with Spring Boot (backend) and Angular (frontend), featuring JWT authentication, student CRUD operations, CSV import/export, and Docker deployment.

## 🏗️ Project Architecture

### Backend (Spring Boot 3.2.0)
- **Framework**: Spring Boot with Java 17
- **Security**: JWT-based authentication with Spring Security
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA with Hibernate
- **API Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5 with Mockito
- **Build Tool**: Maven

### Frontend (Angular 17+)
- **Framework**: Angular with TypeScript
- **UI Library**: Angular Material
- **State Management**: Reactive Forms
- **HTTP Client**: Angular HttpClient with interceptors
- **Routing**: Angular Router with guards

## 📋 Features

### Authentication
- ✅ JWT-based authentication system
- ✅ Admin registration and login
- ✅ Password hashing with BCrypt
- ✅ Token-based authorization for protected routes

### Student Management
- ✅ Full CRUD operations (Create, Read, Update, Delete)
- ✅ Search students by ID or username
- ✅ Filter students by level
- ✅ Pagination support
- ✅ CSV export functionality
- ✅ CSV import functionality

### Technical Features
- ✅ RESTful API with proper HTTP status codes
- ✅ Global exception handling
- ✅ DTO pattern for data transfer
- ✅ MapStruct for entity-DTO mapping
- ✅ Swagger API documentation
- ✅ Docker containerization
- ✅ CORS configuration
- ✅ Input validation

## 🚀 Quick Start with Docker Compose

### Prerequisites
- Docker (version 20.10+)
- Docker Compose (version 2.0+)

### Running the Application

1. **Clone the repository**
```bash
git clone <your-repository-url>
cd <project-directory>
```

2. **Start all services**
```bash
docker-compose up --build
```

This single command will:
- Build the Spring Boot backend
- Build the Angular frontend
- Set up MySQL database
- Start all services with proper networking

3. **Access the application**
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

4. **Default credentials**
```
Username: admin
Password: password
```

### Stopping the Application
```bash
docker-compose down
```

### Clean restart (remove volumes)
```bash
docker-compose down -v
docker-compose up --build
```

## 🛠️ Local Development Setup

### Backend Setup

1. **Prerequisites**
   - Java 17 or higher
   - Maven 3.9+
   - MySQL 8.0+

2. **Database setup**
```sql
CREATE DATABASE schooldb;
```

3. **Configure application**
Edit `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/schooldb
    username: root
    password: your_password
```

4. **Run the backend**
```bash
cd backend
./mvnw spring-boot:run
```

Or build and run:
```bash
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

5. **Run tests**
```bash
./mvnw test
```

### Frontend Setup

1. **Prerequisites**
   - Node.js 18+ and npm

2. **Install dependencies**
```bash
cd frontend
npm install
```

3. **Run development server**
```bash
npm start
```

The application will be available at http://localhost:4200

4. **Build for production**
```bash
npm run build
```

## 📚 API Documentation

### Authentication Endpoints

#### Register Admin
```http
POST /api/admin/register
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

**Responses:**
- `200 OK`: Admin registered successfully
- `409 CONFLICT`: Username already exists
- `400 BAD REQUEST`: Invalid input

#### Login
```http
POST /api/admin/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

**Response:**
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Student Endpoints (Protected - Requires JWT)

All student endpoints require the `Authorization` header:
```
Authorization: Bearer <your-jwt-token>
```

#### Get All Students (with pagination, search, and filter)
```http
GET /api/students?searchTerm=john&level=FIRST_GRADE&page=0&size=10
```

#### Get Student by ID
```http
GET /api/students/{id}
```

#### Create Student
```http
POST /api/students
Content-Type: application/json

{
  "username": "john.doe",
  "level": "FIRST_GRADE"
}
```

#### Update Student
```http
PUT /api/students/{id}
Content-Type: application/json

{
  "username": "john.updated",
  "level": "SECOND_GRADE"
}
```

#### Delete Student
```http
DELETE /api/students/{id}
```

#### Export Students (CSV)
```http
GET /api/students/export
```

#### Import Students (CSV)
```http
POST /api/students/import
Content-Type: multipart/form-data

file: <csv-file>
```

**CSV Format:**
```csv
ID,Username,Level
1,john.doe,FIRST_GRADE
2,jane.smith,SECOND_GRADE
```

## 🎯 Student Levels

The application supports the following student levels:
- `FIRST_GRADE`
- `SECOND_GRADE`
- `THIRD_GRADE`
- `FOURTH_GRADE`
- `FIFTH_GRADE`

## 🧪 Testing

### Backend Tests

The project includes unit tests for:
- Authentication service (login, registration)
- Student service (CRUD operations)

Run tests:
```bash
cd backend
./mvnw test
```

Test coverage includes:
- ✅ Admin registration with duplicate username handling
- ✅ Login with JWT token generation
- ✅ Student CRUD operations
- ✅ Exception handling (ResourceNotFoundException, ConflictException)

## 🔒 Security Features

1. **Password Hashing**: All passwords are hashed using BCrypt
2. **JWT Authentication**: Secure token-based authentication
3. **Protected Routes**: Student endpoints require valid JWT tokens
4. **CORS Configuration**: Properly configured for frontend-backend communication
5. **Input Validation**: Backend validation using Jakarta Validation annotations

## 📊 HTTP Status Codes

The API follows RESTful conventions:

| Code | Status | Usage |
|------|--------|-------|
| 200 | OK | Successful GET/PUT/DELETE |
| 201 | Created | Successful POST (resource created) |
| 204 | No Content | Successful DELETE with no body |
| 400 | Bad Request | Invalid input/validation error |
| 401 | Unauthorized | Missing or invalid JWT token |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate resource (e.g., username) |
| 500 | Internal Server Error | Unexpected server error |

## 🐳 Docker Configuration

### Docker Compose Services

```yaml
services:
  - mysql: Database (port 3306)
  - backend: Spring Boot API (port 8080)
  - frontend: Angular + Nginx (port 4200)
```

### Environment Variables

You can customize the deployment using environment variables in `docker-compose.yml`:

```yaml
DB_HOST=mysql
DB_PORT=3306
DB_NAME=schooldb
DB_USERNAME=school_user
DB_PASSWORD=school_pass
JWT_SECRET=your-secret-key
```

## 📁 Project Structure

```
.
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/example/backend/
│   │   │   │   ├── config/          # Security, JWT, CORS, Swagger
│   │   │   │   ├── controller/      # REST endpoints
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── enums/           # Level enum
│   │   │   │   ├── exception/       # Custom exceptions & handlers
│   │   │   │   ├── mapper/          # MapStruct mappers
│   │   │   │   ├── repository/      # JPA repositories
│   │   │   │   └── service/         # Business logic
│   │   │   └── resources/
│   │   │       └── application.yml  # Configuration
│   │   └── test/                    # Unit tests
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── login/          # Login component
│   │   │   │   └── students/       # Student management
│   │   │   └── services/           # HTTP services & guards
│   │   ├── assets/
│   │   └── index.html
│   ├── Dockerfile
│   └── package.json
│
├── docker-compose.yml
└── README.md
```

## ⚙️ Configuration Notes

### Backend Configuration

Key configuration in `application.yml`:
- Database connection with environment variable support
- JPA/Hibernate settings (DDL auto-update)
- JWT secret and expiration
- Multipart file upload settings (max 10MB)
- Swagger/OpenAPI paths
- Logging levels
- Health check endpoints

### Frontend Configuration

- Angular Material for UI components
- HTTP interceptor for JWT token injection
- Auth guard for route protection
- Proxy configuration for API calls in development

## 🐛 Troubleshooting

### Common Issues

1. **Database connection error**
   - Ensure MySQL is running
   - Check credentials in `application.yml` or environment variables
   - Verify database `schooldb` exists

2. **Port already in use**
   - Backend: Change `SERVER_PORT` in docker-compose.yml
   - Frontend: Modify port in `angular.json`
   - MySQL: Change `DB_PORT` mapping

3. **JWT token invalid**
   - Token expires after 10 hours by default
   - Re-login to get a new token
   - Check `JWT_SECRET` is consistent

4. **CORS errors**
   - Verify frontend origin in `CorsConfig.java`
   - Check allowed methods and headers

5. **Docker build fails**
   - Clean Docker cache: `docker system prune -a`
   - Rebuild: `docker-compose build --no-cache`

## 📈 Future Enhancements

Potential improvements for this project:
- [ ] Rate limiting for login attempts (Bucket4j integration started)
- [ ] Email verification for admin registration
- [ ] Role-based access control (ADMIN, TEACHER, etc.)
- [ ] Student attendance tracking
- [ ] Grade management
- [ ] Export to Excel format
- [ ] Bulk operations
- [ ] Advanced filtering and sorting
- [ ] Audit logging
- [ ] Integration tests
- [ ] CI/CD pipeline

## 📝 Notes

- The default admin user is automatically created on first run
- JWT tokens expire after 10 hours
- CSV import requires exact format (ID, Username, Level)
- Search supports partial matches (case-insensitive)
- Pagination starts at page 0

## 👥 Development

This project was developed as a technical test for a Full-Stack Developer position, demonstrating:
- Clean code architecture (Controllers → Services → Repositories)
- Proper separation of concerns with DTOs
- Comprehensive error handling
- API documentation with Swagger
- Unit testing best practices
- Docker containerization
- Modern frontend development with Angular Material

## 📄 License

This project is for educational and demonstration purposes.
