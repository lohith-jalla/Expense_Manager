# Expense Tracker - API Documentation


### Author - Lohith Jalla
###  Any Queries or changes please mail to lohithjalla12@gmail.com

## 🚀 Backend Tech Stack (Microservices)

- **Language:** Java  
- **Framework:** Spring Boot (3.x)  
- **Architecture:** Microservices (User MS & Expense MS)  
- **Security:** Spring Security with JWT authentication  
- **Database:** MySQL / PostgreSQL  
- **Persistence:** Spring Data JPA (Hibernate under the hood)  
- **Documentation:** Swagger / Springdoc OpenAPI  
- **Scheduling:** Spring `@Scheduled` for limit alerts  
- **Email Service:** JavaMailSender (for sending monthly limit exceeded alerts)  
- **Build Tool:** Maven  
- **Testing:** Postman (for manual API testing)  

##
## User Microservice
### POST /user/setMail
**Summary:** setMail

**Parameters:**
- mail (query) - string
- Authorization (header) - string

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### POST /user/setLimit/{limit}

**Summary:** setLimit

**Parameters:**
- limit (path) - number
- Authorization (header) - string

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### POST /auth/register

**Summary:** Signup

**Parameters:**
- Request Body: Yes

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
##
### POST /auth/login

**Summary:** login

**Parameters:**
- Request Body: Yes

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden

##
### GET /user/{id}

**Summary:** getUser

**Parameters:**
- Authorization (header) - string
- id (path) - integer

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden

##
### GET /user/getHashedPass/{id}

**Summary:** getUserHashedPassword

**Parameters:**
- Authorization (header) - string
- id (path) - integer

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden

##
## Expense Microservice
### GET /expense/RExpense/{expenseId}

**Summary:** getRecurringExpenseById

**Parameters:**
- Authorization (header) - string
- expenseId (path) - integer

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### PUT /expense/RExpense/{expenseId}

**Summary:** updateRecurringExpense

**Parameters:**
- Authorization (header) - string
- expenseId (path) - integer
- Request Body: Yes

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### DELETE /expense/RExpense/{expenseId}

**Summary:** deleteExpense

**Parameters:**
- Authorization (header) - string
- X-User-password (header) - string
- expenseId (path) - integer

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### GET /expense

**Summary:** getExpenseByUserId

**Parameters:**
- page (query) - integer
- size (query) - integer
- Authorization (header) - string

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### POST /expense

**Summary:** createExpense

**Parameters:**
- Authorization (header) - string
- Request Body: Yes

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden

##
### GET /expense/RExpense/

**Summary:** getAllRecurringExpense

**Parameters:**
- page (header) - integer
- size (header) - integer
- Authorization (header) - string

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden

##
### POST /expense/RExpense/

**Summary:** addRecurringExpense

**Parameters:**
- Authorization (header) - string
- Request Body: Yes

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden

##
### GET /expense/{expenseId}

**Summary:** getExpenseById

**Parameters:**
- Authorization (header) - string
- expenseId (path) - integer

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### DELETE /expense/{expenseId}

**Summary:** deleteExpenseById

**Parameters:**
- Authorization (header) - string
- expenseId (path) - integer

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### PATCH /expense/{expenseId}

**Summary:** updateExpense

**Parameters:**
- Authorization (header) - string
- expenseId (path) - integer
- Request Body: Yes

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### GET /expense/summary/type

**Summary:** getSummaryByType

**Parameters:**
- Authorization (header) - string
- startDate (query) - string
- endDate (query) - string

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### GET /expense/search

**Summary:** searchExpenses

**Parameters:**
- query (query) - string
- page (query) - integer
- size (query) - integer
- Authorization (header) - string

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### GET /expense/progress

**Summary:** getMonthlyExpense

**Parameters:**
- period (query) - integer
- page (query) - integer
- size (query) - integer
- Authorization (header) - string

**Responses:**
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: NotFound

##
### GET /expense/monthly-summary

**Summary:** getMonthlySummary

**Parameters:**
- Authorization (header) - string
- year (query) - integer

**Responses:**
- 200: OK
##
