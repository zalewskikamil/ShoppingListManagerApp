# Shopping List Manager App



## Introduction


This is a backend Spring Boot application that allows you to create and manage shopping lists. The application uses Spring Boot, Spring Data JPA and Spring Security with JWT for authentication and authorization.



## Requirements


- Java 21 or higher

- Maven 3.9.5 or higher

- PostgreSQL database



## Installation

1. Clone the repository:

```git clone https://github.com/zalewskikamil/ShoppingListManagerApp.git```

```cd spring-boot-app ```


2. Build the project using Maven (**before this step, follow the instructions in the section Configuration File application.properties**):

```mvn clean install ```

## Configuration File `application.properties`


Configure the database connection, mail settings and first admin account data in `src/main/resources/application.properties`:

- Ensure correct URL, username and password for your  PostgreSQL database    
  ```spring.datasource.url=jdbc:postgresql://localhost:5432/yourDB```
  ```spring.datasource.username=yourDBUsername```
  ```spring.datasource.password=yourDBPassword ```

- Ensure correct username and password to your Gmail account (if you use another email account, provide complete settings)

  ```spring.mail.username=example@gmail.com ```

  ```spring.mail.password=passwordToMailAccount ```

- Ensure first admin account data
- 

  ```admin.username=example@example.com```  
  ```admin.password=yourPassword ```

## Running the Application

To run the Spring Boot application, use the following command:

```mvn spring-boot:run```   
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

- Write unit and integration tests
- Develop the frontend for the application

## Tools which I used to build this app

- Java 21

- Spring Boot

- Spring Security

- Jwt

- Spring Data Jpa

- Hibernate

- PostgreSQL

- JUnit5

- Mockito

- SLF4J