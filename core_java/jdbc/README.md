# Introduction
The purpose of the JDBC application was to become familiarized with Java's API capabilities when connecting
a Java application to an RDBMS through the use of Java SQL libraries. This JDBC application creates a connection 
between a Java application and a PostgreSQL RDBMS to implement CRUD functionalities with JDBC API's. CRUD functionalities
allows the user to: create, read, update, and/or delete data. The application was designed to implement two DAO's (Data 
Access Object) from a sample database, one for customers and one for orders. The technologies used to create this application
are listed below:

* Docker
* Java
* PostgreSQL
* JDBC
* Intellij

# Implementaiton
## ER Diagram
![ Diagram ](./assets/er%20diagram.png "ER Diagram")

## Design Patterns
### DAO (Data Access Object) Pattern

* DAO's are one of the most common patterns when dealing with databases as they provide an abstraction layer
between the JDBC code and the business logic. When used as an interface, the input and output will be a `Data Transfer Object (DTO`
, which are encapsulated objects that provide a single domain of data. DAO's are useful as they allow for the user to have a
single instance of the database and to compute the joins using foreign keys.

### Repository Pattern

* The repository pattern focuses on a single table accesses per class, thus allowing joins to be performed in the code after
all the data has been selected from multiple tables. The repository pattern is useful when it is needed to scale a database horziontally,
resulting in a more distributed system.

# Test
This application was manually tested with a PostgreSQL instance through docker using the `JDBCExecutor` class. This class acted as 
the client, since all test were executed through the main method within the class. Using the `CustomerDAO` and `OrderDAO` classes, DTO 
(Data Transfer Object) were returned, and then their outputs were displayed on the console to compare results.
