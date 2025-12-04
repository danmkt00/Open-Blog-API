# OpenBlog API

A simple RESTful blog application built with **Spring Boot**, **Spring Data JPA**, **PostgreSQL**, and **JWT authentication**.  

Users can create posts, comment, and like posts. Admins can manage all content.  
**Non-authorized users can view all posts, comments, and likes**, but only authenticated users can create, update, or delete posts, comments, or likes.

---

## Features

- **User Management**
  - Register and login with email/password.
  - JWT-based authentication.
  - Roles: `USER` and `ADMIN`.

- **Posts**
  - Create, read, update, delete posts.
  - Users can update/delete their own posts.
  - Admins can update/delete any post.
  - View comment count and like count per post.

- **Comments**
  - Users can add, update, and delete comments on posts.
  - Admins can manage all comments.

- **Likes**
  - Users can like/unlike posts.
  - See which users liked a post with timestamp.

- **Security**
  - JWT authentication.
  - Role-based authorization.
  - Only authenticated users can create, update, delete posts/comments/likes.

---

## Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA  
- **Database:** PostgreSQL  
- **Build Tool:** Maven  
- **Authentication:** JWT  
- **Validation:** Jakarta Validation (`@NotBlank`, `@Size`, etc.)

---

## OpenBlog API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|---------|-------------|---------------|
| POST   | /auth/register | Register a new user | No |
| POST   | /auth/login    | Login and receive JWT token | No |

| GET    | /posts         | Get all posts | No |
| GET    | /posts/{id}    | Get a single post | No |
| POST   | /posts         | Create a new post | Yes |
| PUT    | /posts/{id}    | Update entire post (owner/admin) | Yes |
| DELETE | /posts/{id}    | Delete a post (owner/admin) | Yes |

| GET    | /posts/{postId}/comments | Get all comments for a post | No |
| POST   | /posts/{postId}/comments | Add a comment to a post | Yes |
| PUT    | /comments/{id}           | Update comment (author/admin) | Yes |
| DELETE | /comments/{id}           | Delete comment (author/admin) | Yes |

| POST   | /posts/{postId}/likes    | Toggle like on a post | Yes |
| GET    | /posts/{postId}/likes    | Get all likes for a post with usernames and timestamps | No |

---

## Initialization & Notes

Before running the application, you need to configure your `application.properties` with your database and JWT values:

```properties
# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/openblog
spring.datasource.username=YOUR_USERNAME       # Replace with your PostgreSQL username
spring.datasource.password=YOUR_PASSWORD       # Replace with your PostgreSQL password

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
app.jwt.secret=YOUR_SECRET                     # Replace with a strong secret key
app.jwt.expiration-ms=3600000                  # Token expiration in milliseconds (1 hour)



