## Rest Error Handler Starter

### Purpose
The purpose of this starter is to provide the common functionality for error handling for the rest service calls to most microservices which return the error response in the below fashion.
```
{
  "errors": {
    "root_cause": [
      {
        "code": "string",
        "reason": "string"
      }
    ],
    "code": "string",
    "reason": "string",
    "timestamp": 0
  }
}
```
### Starter Usage
Include the following dependency in the pom file.
For EAF 4.5.8/Spring Boot 1.x.x use resterrorhandler version 1.x.x
For EAF 5/Spring Boot 2.x.x use resterrorhandler version 2.x.x
```
<dependency>
	<groupId>com.home.rest.errorhandler.starter</groupId>
	<artifactId>spring-boot-starter-resterrorhandler</artifactId>
	<version>1.x.x</version>
</dependency>
```
Throw the exception of type Runtime formulating the Error bean :
```
Error error = ErrorUtil.populateError(HttpStatus.NOT_FOUND, "No record found for ", null);
throw new NoDataFoundException(error);
```