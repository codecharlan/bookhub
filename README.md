# Mobilise: BookHubApplication

![CI/CD Workflow](https://github.com/codecharlan/bookhub/actions/workflows/Maven.yml/badge.svg)

## Overview

A simple RESTful web service using Java and Spring Boot for a book management system. 
Users can perform various operations such as
1. [ ] Create: Add a new book to the system.
2. [ ] Read: Retrieve details of all books or a specific book by ID.
3. [ ] Update: Modify details of an existing book.
4. [ ] Delete: Remove a book from the system.
5. [ ] Search Feature: An endpoint to search books by title or author.

### Table of Contents
[Installation](#installation)

[Running the Application](#running-the-application)

[Testing the Application](#testing-the-application)

[API Endpoints](#api-endpoints)

[Technologies Used](#technology-used)

[Contributing](#contributing)

License

### Installation
To set up the BookHub application, follow these steps:
1. Clone the repository:
``
   git clone https://github.com/codecharlan/bookhub.git
``
2. Navigate to the project directory:
   `` 
   cd bookhub
``
3. Install the required dependencies using Maven:
   ```shell
   mvn clean install
   ```
4. Build the application
   ```shell
   mvn clean package
   ```
   
### Prerequisites

Input and output data are formatted in JSON.

### Running the Application

To begin using the BookHub Application, follow these steps:


1. Start the application with:
    ```shell
    mvn spring-boot:run
    ```
   The application will start on the default port (8080)
   
### API Endpoints
The API exposes the following endpoints:

| Endpoint                                                   | HTTP Method | Description                           |
|------------------------------------------------------------|-------------|---------------------------------------|
| /api/users/register                                        | POST        | Creates a new user                   |
| /api/users/login                                           | POST        | Logs in a user                       |
| /api/users/logout                                          | POST        | Logs out a user                      |
| /api/books/create                                          | POST        | Creates a new book                   |
| /api/books/edit/{id}                                       | PUT         | Edits an existing book               |
| /api/books/{id}                                            | GET         | Retrieves a book by ID               |
| /api/books/search?searchTerm={searchTerm}                  | GET         | Searches for books by title or author|
| /api/books/all?pageSize={pageSize}&pageNumber={pageNumber} | GET         | Retrieves all books                  |
| /api/books/delete/{id}                                     | DELETE      | Deletes a book by ID                 |
| /api/books/borrow/{id}?borrowCount={borrowCount}           | POST        | Borrows a book                       |
| /api/books/return/{id}?returnCount={returnCount}           | POST        | Returns a borrowed book              |
| /api/books/purchase/{id}?purchaseCount={purchaseCount}     | POST     | Purchases a book                     |
| /api/reviews/all                                           | GET         | Retrieves all reviews                |
| /api/reviews/1                                             | GET         | Retrieves a review by ID             |
| /api/reviews/create?bookId={bookId}                        | POST        | Creates a new review                 |
| /api/reviews/edit/{id}                                     | PUT         | Updates a review                     |
| /api/reviews/delete/{id}                                   | DELETE      | Deletes a review                     |


For comprehensive API usage details, refer to our [Postman Documentation](https://documenter.getpostman.com/view/31876952/2sA3JJ8NRB).

### Testing the Application
The BookHub application includes a set of unit tests to ensure the functionality of the application. To run the tests, use the following command:
 ```shell
    mvn test
  ```

### Technology Used:
* Java 19
* SpringBoot
* Maven
* Spring Security
* Spring Data JPA
* Docker
* Lombok
* CI/CD
* Junit & Mockito
* Git and GitLab
* Postman

### Contributing
We welcome contributions to the BookHub project. If you would like to contribute, please follow these steps: 
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Implement your changes and ensure that all tests pass.
4. Commit your changes and push them to your forked repository.
5. Submit a pull request to the main repository.

Please ensure that your code follows the existing coding style and conventions, and that you have added appropriate tests for your changes.

### License
The BookHub application is licensed under the [MIT License](LICENSE.md).

Feel free to reach out with any questions, feedback, or suggestions.