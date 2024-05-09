


# Shopping List Manager App

  

## Introduction

  

This is a backend Spring Boot application that allows you to create and manage shopping lists. The application uses Spring Boot, Spring Data JPA, and Spring Security with JWT for authentication and authorization.

  

## Requirements

  

- Java 21 or higher

- Maven 3.9.5 or higher

- MySQL database

  

## Installation

1. Clone the repository:

```git clone https://github.com/zalewskikamil/ShoppingListManagerApp.git```

```cd spring-boot-app ```

  

2. Build the project using Maven:

```mvn clean install ```

  

## Database Configuration

### Local MySQL Database

Use MySQLWorkbench to create database schema

  

1. Open MySQLWorkbench

2. Connect to the appropriate database instance

3. Select the `schema.sql` file in MySQL Workbench

4. Open the file and execute it on the database

  

## Configuration Files `application.properties`

  

Configure the database connection, mail settings and first admin account data in `src/main/resources/application.properties`:  

- Ensure correct username and password for your database  

```spring.datasource.username=yourUsername``` ```spring.datasource.password=yourPassword ```  

- Ensure correct properties to your mail settings (example for Gmail  mail account, provide correct username and password)

```spring.mail.host=smtp.gmail.com```

```spring.mail.port=587 ```

```spring.mail.username=example@gmail.com ```

```spring.mail.password=passwordToMailAccount ```

```spring.mail.properties.mail.smtp.auth=true ```

```spring.mail.properties.mail.smtp.starttls.enable=true ```

```spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com ```  

- Ensure first admin account data  

```admin.username=example@example.com```
```admin.password=yourPassword ```  

Ensure correct url to production database in `src/main/resources/application-prod.properties`  

```spring.datasource.url=jdbc:mysql://localhost:3306/shopping_lists```  

Ensure correct url to test database in `src/main/resources/application-test.properties`  

```spring.datasource.url=jdbc:mysql://localhost:3306/tests```  

## Running the Application  

To run the Spring Boot application, use the following command:  

```mvn spring-boot:run -Dspring-boot.run.profiles=prod```  

The application will be available at `http://localhost:8080`.

 To check how the application works, use application testing tools such as Postman.

## Main Functionalities of Shopping List Manager App
- Login

- Registration

- Adding a shopping list

- Sharing list

- Viewing details of created and shared lists

- Adding items to the list

- Updating items  

## Admin Functionalities

Additionally admin has a few more functionalities:  

- Downloading log file 

- Blocking / unblocking user's account 

- Granting admin status to user 

- Sending message to all users 

## TODO

-   Develop the frontend for the application

## Tools which I used to build this app 

- Java 21 

- Spring Boot 

- Spring Security 

- Jwt 

- Spring Data Jpa 

- Hibernate 

- MySQL

- JUnit5

- Mockito

- SLF4J