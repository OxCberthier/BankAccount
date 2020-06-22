# Bank account kata

## Building the project
`mvn clean install` in the root project directory

## Running Spring Boot Application
Application is initialize with:
* 1 Client (Id=10)
* 1 Account (Id=100)

`mvn spring-boot:run -Dspring-boot.run.profiles=dev` in the root project directory


## Testing User Story

Request to generate JWT:
`curl --location --request POST "http://localhost:8080/auth/signin" --header "Content-Type: application/json" --data-raw "{\"email\": \"cberthier@oxiane.com\",\"password\": \"Password\"}"`

**US 1:**
 
**IN ORDER TO** save money

**AS A** bank client

**I WANT TO** make a deposit in my account 

Request to add 100 on account with id 100:
 
`curl --location --request POST "http://localhost:8080/accounts/operations" --header "Content-Type: application/json" --header "Authorization: Bearer ${JWT}"  --data-raw "{\"accountId\":100,\"amount\":100,\"operationType\":\"DEPOSIT\"}"`

**US 2:**

**IN ORDER TO** retrieve some or all of my savings

**AS A** bank client

**I WANT TO** make a withdrawal from my account

Request to retrieve 10 on account with id 100:
 
`curl --location --request POST "http://localhost:8080/accounts/operations" --header "Content-Type: application/json" --header "Authorization: Bearer ${JWT}" --data-raw "{\"accountId\":100,\"amount\":10,\"operationType\":\"WITHDRAWAL\"}"`

**US 3:**

**IN ORDER TO** check my operations

**AS A** bank client

**I WANT TO** see the history (operation, date, amount, balance) of my operations

Request to retrieve 4 last operation account:
 
`curl --location --request GET "http://localhost:8080/accounts/operations?accountId=100&page=0&size=4&sort=date,desc" --header "Authorization: Bearer ${JWT}"`
