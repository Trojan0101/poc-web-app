# poc Web App

A Java Spring-Boot based web application that incorporates authentication and authorization using Auth0 and cluster marking using google maps.

## Tech Stack:

Backend: Java, Spring Boot, Hibernate
Database: PostgreSQL
Frontend: HTML, CSS, Javascript
Authentication and Authorization: Auth0 Service
Cluster Marking API: Google Javascript Maps API
Tools: Intellij Idea, VS-Code

## Pre-requisite:

### Auth0:

- Regular Web Application in Auth0:

    - Create a new application with type **Regular Web Application**.
    - Copy the **domain, client id, and client secret**. Replace the values in the application.properties file and wherever necessary.
    - Add Allowed Callback URLs: http<span></span>://localhost:8080/callback
    - Add Allowed Logout URLs: http<span></span>://localhost:8080

- Machine to Machine Application in Auth0:

    - Create a new application with type **Machine to Machine Application**.
    - Copy the **domain, client id, and client secret**. Replace the values in the application.properties file and wherever necessary.

- Username-Password-Authentication Database in Auth0:

    - In the custom database section, enable the **Use my own database** option.
    - Modify all the action scripts. Actions scripts are in **poc-db-action-scripts** folder.
    - Add encrypted value for **configuration.POSTGRES_URL**. This will contain the connection string for postgresql connection.

- Universal Login in Auth0:

    - Change from **New** to **Classic** in settings tab.
    - In the login tab, enable the **Customize Login Page** option.
    - Edit the HTML code using the code given in **poc-auth0-login-html** folder.

- GeoLocation Rule:

    - Add a rule named **GeoLocation**, and paste the code from **poc-geoip-script** folder.

### Database:

- Modify the properties for database in the **application.properties** file.
