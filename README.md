# Poc Web App:

A Java Spring-Boot based web application that incorporates authentication and authorization using Auth0 and cluster marking using google maps.

## Tech Stack:

- Backend: Java, Spring Boot, Hibernate
- Database: PostgreSQL
- Frontend: HTML, CSS, Javascript
- Authentication and Authorization: Auth0 Service
- Cluster Marking API: Google Maps Javascript API
- Tools: Intellij Idea, VS-Code

## Pre-Requisite:

### Auth0:

- Regular Web Application:

    - Create a new application with type **Regular Web Application**.
    - Copy the **domain, client id, and client secret**. Replace the values in the **application.properties** file and wherever necessary.
    - Add Allowed Callback URLs: http<span></span>://localhost:8080/callback
    - Add Allowed Logout URLs: http<span></span>://localhost:8080

- Machine to Machine Application:

    - Create a new application with type **Machine to Machine Application**.
    - Copy the **domain, client id, and client secret**. Replace the values in the **application.properties** file and wherever necessary.

- Username-Password-Authentication Database:

    - Create a new database.
    - In the custom database section, enable the **Use my own database** option.
    - Modify all the action scripts using the scripts in **poc-db-action-scripts** folder.
    - Add encrypted value for **configuration.POSTGRES_URL**. This will contain the connection string for postgresql connection.

- Universal Login:

    - Change from **New** to **Classic** in settings tab.
    - In the login tab, enable the **Customize Login Page** option.
    - Replace the HTML code using the code from the file in **poc-auth0-login-html** folder.

- GeoLocation Rule:

    - Add a rule named **GeoLocation**, and paste the code from the file in **poc-geoip-script** folder.

### Database:

- Start postgresql in local or in cloud, and create a database.
- Modify the properties for database in the **application.properties** file.

### Google Maps:

- Create an **API Credential** in Google Cloud Platform.
- Copy the API key and replace it in the **dashboard.html** file.
- Enable the **Maps Javascript API**.

### End-Points Usage Overview:

| Controller | End-Point | Triggered |
|:-----------|:---------:|----------:|
| AuthController | /login | When the login button in index pages is clicked. |
| AuthController | /callback | When the user successfully logs in, the endpoint redirects the user to dashboard page. |
| CommentsController | /showComments | When a location marker is clicked, shows the comments made to that marker. |
| CommentsController | /getLatLng | When a comment in ViewComments page is clicked, the latitude and longitude to which the comment is made is fetched. |
| FileUploadController | /addComments | When a comment is added by the user using the ‘Add Comment’ button. |
| FileUploadController | /getCommentsPicture | When a user clicks a marker, fetches the picture added to the comments made to that location. |
| HomeController | /dashboard | Shows the dashboard page. |
| HomeController | /viewComments | Shows the user comments page. |
| LogoutController | - | When the user logs out from the app. |

### NOTE:

application.yml file for the spring boot application is not included in the repo. Create or modify your application.yml file in **resources** folder by following the below template.

```
spring:
    jpa:
        properties:
            hibernate:
                dialect: org.hibernate.dialect.PostgreSQLDialect
        hibernate:
            ddl-auto: update
    datasource:
        password: <YOUR POSTGRESQL PASSWORD>
        driver-class-name: org.postgresql.Driver
        username: <YOUR POSTGRESQL USERNAME>
        url: <YOUR POSTGRESQL CONNECTION STRING>
com:
    auth0:
        managementApi:
            clientSecret: <YOUR AUTH0 MACHINE TO MACHINE APPLICATION CLIENT SECRET>
            grantType: client_credentials
            clientId: <YOUR AUTH0 MACHINE TO MACHINE APPLICATION CLIENTID>
        domain: <YOUR AUTH0 DOMAIN>
        clientSecret: <YOUR AUTH0 REGULAR WEB APPLICATION CLIENT SECRET>
        clientId: <YOUR AUTH0 REGULAR WEB APPLICATION CLIENTID>

---
spring:
    servlet:
        multipart:
            max-file-size: 101MB
            max-request-size: 101MB

server:
    port: '8080'
```
