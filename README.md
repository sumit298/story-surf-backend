# StorySurf API 📚

A comprehensive REST API for a story sharing platform built with Spring Boot. Users can create, share, and interact with stories through likes, comments, and mood reactions.

## 🚀 Features

- **User Management**: Registration, authentication, profile management
- **Story Management**: Create, edit, publish, and discover stories
- **Social Features**: Like stories, add reflections and mood reactions
- **Role-Based Access**: Reader, Author, and Admin roles
- **Search & Discovery**: Search stories by title, content, tags, and author
- **Admin Panel**: Story moderation and user management

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **Database**: H2 (development), easily configurable for PostgreSQL/MySQL
- **Authentication**: JWT tokens
- **Documentation**: RESTful API with comprehensive endpoints

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## 🏃‍♂️ Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd demo
```

### 2. Run the Application
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### 3. Access H2 Database Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## 📚 API Documentation

### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register new user |
| POST | `/api/users/login` | User login |
| GET | `/api/users/profile` | Get user profile |
| PUT | `/api/users/profile` | Update profile |
| PUT | `/api/users/settings` | Update user settings |

### Story Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/stories/public` | Get published stories | No |
| GET | `/api/stories/public/{id}` | Get story by ID | No |
| GET | `/api/stories/public/search` | Search stories | No |
| GET | `/api/stories/public/trending` | Get trending stories | No |
| GET | `/api/stories/public/popular` | Get popular stories | No |
| GET | `/api/stories/public/latest` | Get latest stories | No |
| POST | `/api/stories` | Create story | Yes (Author) |
| GET | `/api/stories/my-stories` | Get user's stories | Yes |
| PUT | `/api/stories/{id}` | Update story | Yes (Owner) |
| DELETE | `/api/stories/{id}` | Delete story | Yes (Owner) |
| POST | `/api/stories/{id}/like` | Like story | Yes |

### Admin Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| PUT | `/api/stories/admin/{id}/publish` | Publish story | Yes (Admin) |
| PUT | `/api/stories/admin/{id}/approve` | Approve story | Yes (Admin) |
| PUT | `/api/stories/admin/{id}/archive` | Archive story | Yes (Admin) |
| GET | `/api/stories/admin/stats` | Get story statistics | Yes (Admin) |

## 🧪 Testing the API

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "age": 25,
    "bio": "Story enthusiast"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Create a Story (Requires AUTHOR role)
```bash
curl -X POST http://localhost:8080/api/stories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "My Adventure",
    "content": "Once upon a time...",
    "tags": ["adventure", "fiction"],
    "contextTime": "MEDIUM"
  }'
```

## 👥 User Roles

- **READER**: Can view published stories, like stories, add reflections
- **AUTHOR**: Can create, edit, and manage their own stories
- **ADMIN**: Can moderate stories, manage users, access admin endpoints

### Promoting Users
To promote a user to AUTHOR role (for testing):
1. Access H2 Console: `http://localhost:8080/h2-console`
2. Run SQL: `UPDATE users SET role = 'AUTHOR' WHERE id = {USER_ID};`

## 🗂️ Project Structure

```
src/main/java/com/storyapi/demo/
├── Controller/          # REST controllers
├── Service/            # Business logic
├── Repository/         # Data access layer
├── Entity/            # JPA entities
├── dto/               # Data transfer objects
├── config/            # Configuration classes
└── mapper/            # Entity-DTO mappers
```

## 🔧 Configuration

### Database Configuration
Edit `src/main/resources/application.properties`:

```properties
# H2 Database (Development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# JWT Configuration
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000
```

### Security Configuration
- JWT-based authentication
- Role-based authorization
- CORS enabled for all origins
- H2 console accessible in development

## 🚀 Deployment

### Production Database
Replace H2 with PostgreSQL/MySQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/storysurf
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Environment Variables
```bash
export JWT_SECRET=your-production-secret
export DB_URL=your-database-url
export DB_USERNAME=your-db-username
export DB_PASSWORD=your-db-password
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🐛 Known Issues

- Story creation requires manual role promotion (will be automated in future versions)
- In-memory H2 database resets on application restart

## 🔮 Future Enhancements

- [ ] Email verification for user registration
- [ ] Password reset functionality
- [ ] File upload for story images
- [ ] Real-time notifications
- [ ] Story categories and advanced filtering
- [ ] User following system
- [ ] Story bookmarking

## 📞 Support

For support, email support@storysurf.com or create an issue in this repository.

---

**Happy Storytelling!** 📖✨