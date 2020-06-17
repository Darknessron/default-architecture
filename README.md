
# demo-architecture
Demo architecture for microservices with **Spring Boot**


## Framework

[spring-cloud-starter-gateway](https://spring.io/projects/spring-cloud-gateway)
[spring-cloud-starter-netflix-eureka-server](https://cloud.spring.io/spring-cloud-netflix/reference/html/#spring-cloud-eureka-server)
[spring-cloud-starter-netflix-eureka-client](https://cloud.spring.io/spring-cloud-netflix/reference/html/#service-discovery-eureka-clients)
[spring-boot-starter-web RESTful](https://spring.io/guides/gs/rest-service/)
[spring-boot-starter-web WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
[spring-boot-starter-data-jpa](https://spring.io/projects/spring-data-jpa)

## Library / Technical
[AES  Encryption](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard)
[JWT](https://jwt.io/)

Due to the time limitation, I didn't complete all the functions.
Also, this system can be improved by decoupling the direct access to the DB  instead of using [RabbitMQ](https://www.rabbitmq.com/).
Which can ensure the data will not lose during the connection problem between services and the DB.

## Services Overall

![Overall](https://raw.githubusercontent.com/Darknessron/default-microservices-architecture/master/overall.jpg)

 - Login
 ![Login](https://raw.githubusercontent.com/Darknessron/default-microservices-architecture/master/login.jpg)
 
 - Submit requirement
 - ![Submit requirement](https://raw.githubusercontent.com/Darknessron/default-microservices-architecture/master/SubmitRequirement.jpg)

