# registration-app

## What it does
The app fulfills the purpose of a simple site, where you can register, login, logout and update fields such as name and password. Also has a simple CAPTCHA in order to prevent attacks from bots.

## Backend Technologies - Java, MySQL, JUnit, Mockito

### Java
Through specially designed Java classes we connect to the MySQL Database and update and fetch data from it.

### My SQL
It is saved in a locally supported database with fields for a unique id, email, two names, dates of entry creation and last update and a password that must be hashed before entered into the database as a security measure. 

### JUnit, Mockito
Used for running tests for all methods in the Java app

## Frontend Technologies - HTML, JSP, Tomcat

### HTML, JSP
Connected with Java through Servlets (specially designed classes that we use for requests on the web part to trigger methods in Java). JSP is used on the /register page in order to utilize the CAPTCHA.

### Tomcat
Used to start the server and run it.

## How I made it and what I learned
Previously I had only made some backend Java applications and studied some SQL (writing queries) and HTML (most of the basic syntax). But it was a first time connecting them into one connected fullstack application. Of course, I needed assistance via AI and online forums in order to set up the technologies to work together and learn the syntax of the communicators between them. The following list is the full extent to which I have used such assistance :
- Servlet syntax
- web.xml file syntax and some copypasta
- jsp syntax for register.jsp in order to include the captcha
- pom.xml syntax and copypasta
- a couple of specific JUnit tests of methods with dependencies that were harder to test
