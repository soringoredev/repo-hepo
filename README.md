# Client Manager - CRUD App (Spring Boot)

This is a simple CRUD application written in Java using Spring Boot. The app allows you to manage clients: create, read, update and delete. The data is stored in MySQL database.

## Features

- Add new client
- View list of clients
- Edit client information
- Delete client
- Search client
- Export all clients to Excel file

## Technologies Used

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Web
- MySQL
- Maven

## How to Run the App Locally

1. **Clone the project:**

```bash
 git clone https://github.com/BarisTekir/cv-project-app.git
 cd cv-project-app
```

2. **Configure the MySQL database:**

   2.1.Make sure you have MySQL installed and running on your machine.
   Then, create a database for the project:

   2.2.Create a database: clients_db

   2.3. Update the `application.properties` file. Replace your password with your actual MySQL root password
     (or the user you configured).

   
3. **Run the application**
    
    3.1.***Use the Maven wrapper:***
    ```bash
    ./mvnw spring-boot:run
    ```
   3.2.***Or, if Maven is installed globally:***
    ```bash
     mvn spring-boot:run
    ```
   3.3.***Once started, the app will be available at:http://localhost:9090***

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.