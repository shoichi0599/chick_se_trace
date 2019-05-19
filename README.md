# Chick Se Trace
A web application for a blog.

## Technical Specification
- Framework: Spring Boot, Thymeleaf
- Language: Kotlin, JavaScript (jQuery), HTML, CSS
- DB: MySQL, Redis (for cache)

## How to build
```
cd {PATH_OF_REPOSITORY}
./gradlew build
```

## How to start the application
```
bash
nohup java -jar /usr/local/product/chick_se_trace/chick_se_trace.jar --spring.profiles.active={pro | local} >/dev/null 2>&1 &
```
 

