# Running the code
Clone the repo and build locally. You will need installed:
 - Java 17
 - Maven

Clone the repo by copying the url from GitHub, and using the following commands:

`git clone <repo_url>`  
`cd <project_folder>`

When in the project folder, run this command using Maven:

`./mvnw clean verify`

If successful, you should be able to run the app using the following command:
```bash
java -jar target/tokenizerApp*.jar
```
and access the app by making POST requests to the respective endpoints 

`http://localhost:3000/tokenize`

`http://localhost:3000/detokenize`

The app uses H2 as an in-memory data store, and can be accessed via:

[http://localhost:3000/h2-console/](http://localhost:3000/h2-console/)

(username: sa, no password, JDBC URL: jdbc:h2:mem:testdb)

# Walkthrough

When the app is running, the user can make a POST request to `http://localhost:3000/tokenize` with an array of strings in an accountNumbers object. An example curl request is:

```bash
curl -X POST http://localhost:3000/tokenize \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumbers": [
      "4111-1111-1111-1112",
      "4444-3333-2222-1113",
      "4444-1111-2222-3334"
    ]
  }'
```

The user will receive a response containing the tokenized response numbers. If the user makes a POST request to `http://localhost:3000/detokenize` with any token values stored in the database, they will receive a response with the corresponding raw account numbers. An example curl request for this endpoint is:

```bash
curl -X POST http://localhost:3000/detokenize \
  -H "Content-Type: application/json" \
  -d '{
    "tokenizedAccountNumbers": [
      "token1",
      "token2",
      "token3"
    ]
  }'
```
Note that errors will be thrown if the user tries to POST existing account numbers to the `/tokenise` endpoint, or sends tokens that don’t exist to the `/detokenise` endpoint. An BAD_REQUEST code will be received in the response, with a basic error message as the body. 





