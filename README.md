# Bank account kata

## Building the project
`mvn clean install` in the root project directory

## Running Spring Boot Application
`mvn spring-boot:run` in the root project directory


## Testing User Story
 **US 1**:
 
**IN ORDER TO** save money
**AS A** bank client
**I WANT TO** make a deposit in my account 

Request to add 100 on account with id 10:
 
`curl --location --request POST "http://localhost:8080/accounts/operations" --header "Content-Type: application/json" --data-raw "{\"accountId\":10,\"amount\":100,\"operationType\":\"DEPOSIT\"}"`