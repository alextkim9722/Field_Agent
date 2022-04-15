
# Field Agent API Assessment Plan

_TODO_ Add time estimates to each of the top-level tasks

## Security Clearance CRUD

### HTTP Requests

#### Find All Security Clearances

**Request**

```
GET http://localhost:8080/api/security/clearance HTTP/1.1
```

**Responses**

* Success: 200 OK

#### Find a Security Clearance By Its Identifier

**Request**

```
GET http://localhost:8080/api/security/clearance/1 HTTP/1.1
```

**Responses**

* Success: 200 OK
* Failure (cannot be found): 404 Not Found

#### Add a Security Clearance

**Request**

```
POST http://localhost:8080/api/security/clearance HTTP/1.1
Content-Type: application/json

{
  "name": "Test"
}
```

**Responses**

* Success: 201 Created
* Failure (validation errors): 400 Bad Request

#### Update an Existing Security Clearance

**Request**

```
PUT http://localhost:8080/api/security/clearance/1 HTTP/1.1
Content-Type: application/json

{
  "securityClearanceId": 1,
  "name": "Top Secret Updated"
}
```

**Responses**

* Success: 204 No Content
* Failure (validation errors): 400 Bad Request
* Failure (route path ID and request body ID don't match): 409 Conflict

#### Delete a Security Clearance

**Request**

```
DELETE http://localhost:8080/api/security/clearance/1 HTTP/1.1
```

**Responses**

* Success: 204 No Content
* Failure (cannot be found): 404 Not Found
* Failure (is in use validation error): 400 Bad Request

### Model

_Nothing to do... the `SecurityClearance` model exists and appears to be good to go._

### [ ] Controller (1.5 hours)

* `SecurityClearanceController`
  * findAll()
    * returns a list of clearances, calls the SecurityClearanceService method findAll()
    * uses base URL defined in the @RequestMapping annotation for the class ("/api/security/clearance")
    
  * findById()
    * uses placeholder for path ("/{clearanceId}")
    * returns a SecurityClearance, takes `@ PathVariable clearanceId` as a parameter,
    calls the SecurityClearanceService method findById(clearanceId)
    
  * add()
    * uses base URL
    * returns a ResponseEntity<Object>, takes `@RequestBody SecurityClearance clearance` as a parameter,
    calls the SecurityClearanceService method add(clearance) 
    * Stores the result of service method call
      * returns 201 Created and SecurityClearance object if successful
      * returns 400 Bad Request if unsuccessful 
      
  * update()
    * uses placeholder for path ("/{clearanceId}")
    * returns a ResponseEntity<Object>, takes `@PathVariable int clearanceId` 
    and `@RequestBody SecurityClearance clearance` as parameters,
    calls the SecurityClearanceService method update(clearance)
    * Stores the result of service method call
      * returns 204 No Content if successful
      * returns 409 Conflict if id in URL path and id in request body do not match
      * returns 400 Bad Request if validation does not pass
      
  * deleteById()
    * uses placeholder for path ("/{clearanceId}")
    * returns a ResponseEntity<Object>, takes `@PathVariable int clearanceId` as a parameter,
    calls the SecurityClearanceService method deleteById(clearanceId)
    * returns 204 No Content if successful
    * returns 404 Not Found if SecurityClearance was not found with the id inputted
    * returns 400 Bad Request if the SecurityClearance is referenced in the agency_agent table

_Refer to the `AgentController` class and use that as an example for the methods to add and implementation details to follow._

### [ ] Domain (2.0 hours)

* `SecurityClearanceService`
  * deleteById(int clearanceId)
    * include one of the sql strings below, check if the ResultSet is null
      * If null, the repository method deleteById can be called 
      * If not null, return error message "You cannot delete a clearance that is referenced in another table"
  * findAll()
  * add()
    * include some sort of validation (a separate validation method)
  * update()
    * include some sort of validation (a separate validation method)
  * findById()

* `SecurityClearanceServiceTest`
  * shouldFind()
  * shouldAdd()
  * shouldNotAddWhenMissingName()
  * shouldNotAddWhenDuplicateName()
  * shouldDelete()
  * shouldNotDeleteIfReferenced()
  * shouldUpdate()
  * shouldNotUpdateIfNonExistentClearance()


_Refer to the `AgentService` class and use that as an example for the methods to add and implementation details to follow._

> **Decide how you're going to not touch the database when testing... use mocking or test double.**

#### Domain Rules

* Security clearance name is required
* Name cannot be duplicated
  * Retrieve existing security clearances and check to see if the security clearance to add/update is in the list

#### Deleting

**Can the delete succeed?** We need to know if the security clearance is being referenced.

_One idea... get all of the data from the agency_agent table for the security clearance ID that you're about to delete..._

```sql
select * from agency_agent where security_clearance_id = 1;
```

_Another idea... get row count from the agency_agent table for the security clearance ID that you're about to delete..._

```sql
select count(*) from agency_agent where security_clearance_id = 1;
```

### [ ] Data (3.5 hours)

* `SecurityClearanceMapper` (this class exists)
* `SecurityClearanceJdbcTemplateRepository` (this class exists but is incomplete)
  * findAll()
  * add()
  * update()
  * deleteById()
  * addAgents() 
    * Map agencies and agency/agent-specific fields to the SecurityClearance
* `SecurityClearanceRepository` (this interface exists but is incomplete)
  * add contracts for findAll(), add(), update(), and deleteById()
* `SecurityClearanceJdbcTemplateRepositoryTest` (this test class exists but is incomplete)
  * shouldFindAll()
  * shouldAdd()
  * shouldUpdate()
  * shouldDelete()

_Refer to the `AgentJdbcTemplateRepository` class and use that as an example for the methods to add and implementation details to follow._

> **Important: To support testing, update the `set_known_good_state()` stored procedure as needed.**
> Currently, there is an insert statement for the security_clearance table that is not part of 
> the set_known_good_state() procedure. This must be added before I run tests. 

## Aliases CRUD

### HTTP Requests

#### Fetch an Individual Agent with Aliases Attached

**Request**

```
GET http://localhost:8080/api/agent/2 HTTP/1.1
```

**Responses**

* Success: 200 OK

> **Review the code paths through the app for retrieving an agent... need to update to include aliases**

#### Add an Alias

**Request**

```
POST http://localhost:8080/api/alias HTTP/1.1
Content-Type: application/json

{
  "agentId": 1,
  "name": "Test"
}
```

**Responses**

* Success: 201 Created
* Failure (validation errors): 400 Bad Request

#### Update an Existing Alias

**Request**

```
PUT http://localhost:8080/api/alias/1 HTTP/1.1
Content-Type: application/json

{
  "aliasId": 1,
  "agentId": 1,
  "name": "Test",
  "persona": "Something"
}
```

**Responses**

* Success: 204 No Content
* Failure (validation errors): 400 Bad Request
* Failure (route path ID and request body ID don't match): 409 Conflict

#### Delete an Alias

**Request**

```
DELETE http://localhost:8080/api/alias/1 HTTP/1.1
```

**Responses**

* Success: 204 No Content
* Failure (cannot be found): 404 Not Found

### [ ] Model (0.5 hours)

* `Alias`
  * int aliasId
  * String name
  * String persona
  * int agentId (one-to-many between agent and alias)

### [ ] Controller (1.0 hour)

* `AliasController`
  * findAll()
  * findById()
  * add()
  * update()
  * deleteById()

_Refer to the `AgentController` class and use that as an example for the methods to add and implementation details to follow._

### [ ] Domain (2.0 hours)

* `AliasService`
* `AliasServiceTest`

_Refer to the `AgentService` class and use that as an example for the methods to add and implementation details to follow._

> **Decide how you're going to not touch the database when testing... use mocking or test double.**

#### Domain Rules

* Name is required
* Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.
  * Retrieve existing aliases and check to see if the alias to add/update is in the list
    * Can update repository findById method to include addAliases method 
    in order to attach aliases to an agent in a similar way that an
    agent's agencies are attached 
      * need a new Mapper for Alias
  * If it's a duplicate alias name, then require the persona.
    * check for duplicate name in the add() method
      * If it is duplicate, then require persona
      * If not, then don't require persona 

### [ ] Data (4.0 hours)

* `AliasMapper` (class)
* `AliasJdbcTemplateRepository` (class)
  * findAll()
  * findById()
  * add()
  * update()
  * deleteById()
* `AliasRepository` (interface)
* `AliasJdbcTemplateRepositoryTest` (test class)
  * shouldFindAll()
  * shouldFindById()
  * shouldAdd()
  * shouldUpdate()
  * shouldDelete()

_Refer to the `AgentJdbcTemplateRepository` class and use that as an example for the methods to add and implementation details to follow._

> **To support testing, update the `set_known_good_state()` stored procedure as needed.**
> Add insert statement for the alias table to the test database schema. 

## [ ] Global Error Handling (3.0 hours)

* Determine the most precise exception for **data integrity** failures and handle it with a specific data integrity message.
  * Specific exceptions to handle:
    * For unchecked exceptions (more specific):
      * IllegalArgumentException
      * BadRequestException (need to create a class for this that extends RunTimeException)
      * NotFoundException (need to create a class for this that extends RunTimeException)
* For all other exceptions, create a general "sorry, not sorry" response that doesn't share exception details.
  * For unchecked exceptions:
    * DataAccessException
  * For checked exceptions (most general to most specific):
    * Exception

_Refer back to the "Spring Profiles, Error Handling, and CORS" lesson in the LMS._

> **Research the most specific exception to handle data integrity errors**

---

## Test Plan

### Security Clearance

* [ ] GET all security clearances
* [ ] GET a security clearance by ID
* [ ] For GET return a 404 if security clearance is not found
* [ ] POST a security clearance
* [ ] For POST return a 400 if the security clearance fails one of the domain rules
  * [ ] Security clearance name is required
  * [ ] Name cannot be duplicated
* [ ] PUT an existing security clearance
* [ ] For PUT return a 400 if the security clearance fails one of the domain rules
* [ ] DELETE a security clearance that is not in use by ID
* [ ] For DELETE return a 404 if the security clearance is not found
* [ ] For DELETE return a 400 if the security clearance is in use 

### Alias

* [ ] GET an agent record with aliases attached
* [ ] POST an alias
* [ ] For POST return a 400 if the alias fails one of the domain rules
  * [ ] Name is required
  * [ ] Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.
* [ ] PUT an alias
* [ ] For PUT return a 400 if the alias fails one of the domain rules
* [ ] DELETE an alias by ID
* [ ] For DELETE Return a 404 if the alias is not found

### Global Error Handling

* [ ] Return a specific data integrity error message for data integrity issues
* [ ] Return a general error message for issues other than data integrity
