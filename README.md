# PathLabDemo

A Spring Boot demo application for a Path Lab login system with device-based access control.

## Features

- Custom login page with device detection (desktop only allowed)
- Spring Security with in-memory user
- Thymeleaf templates
- Logging with Log4j2 (controlled via `application.properties`)
- Simple dashboard view

## Requirements

- Java 17+
- Maven 3.6+
- (Optional) IDE like VS Code or IntelliJ

## Getting Started

### 1. Build the project

```sh
mvn clean install
```

### 2. Run the application

```sh
mvn spring-boot:run
```

The app will start at:  
[http://localhost:8080/path-lab/loginPage](http://localhost:8080/path-lab/loginPage)

### 3. Login Credentials

- **Username:** `user`
- **Password:** `password`

> Only desktop browsers are allowed to log in. Mobile/tablet devices will be redirected to an error page.

## Logging

Log levels are controlled via [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
logging.level.root=INFO
logging.level.com.lab.demo.Controller=DEBUG
logging.level.com.lab.demo.config=DEBUG
```

## Running Tests

```sh
mvn test
```

## Project Structure

```
src/
  main/
    java/com/lab/demo/Controller/         # Controllers
    java/com/lab/demo/config/             # Security config
    resources/templates/                  # Thymeleaf views
    resources/application.properties      # App config
  test/
    java/com/lab/demo/Controller/         # Controller tests
```

## License

This project is for demonstration purposes.