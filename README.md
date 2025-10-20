# Endorra #

Endorra is AI-Powered Microservice Application developed for CSCI318

## Set-Up ##

### Apache Kafka Setup ###
This project uses Apache Kafka as a event messaging platform. For the project to run correctly, Kafka will need to be set-up first

#### Linux & Mac OS ####
Download a binary package of Apache Kafka (this exmaple used version 3.7.0) from https://kafka.apache.org/downloads and unzip it. In the Terminal, cd to the unzip folder, and start Kafka with the following commands (each in a separate Terminal session):
```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
```bash
./bin/kafka-server-start.sh ./config/server.properties
```

#### Windows ####
Download a binary package of Apache Kafka (this exmaple used version 3.7.0) from https://kafka.apache.org/downloads and unzip it to `C:/kafka`. Then use the follwoing commands in seperate CMD windows:
```bat
C:\kafka\bin\windows\zookeeper-server-start.bat C:\kafka\config\zookeeper.properties
```
```bat
C:\kafka\bin\windows\kafka-server-start.bat C:\kafka\config\server.properties
```

### AI Agent ###
This project includes an AI Agent for customer support. For the agent to run an API key must be provided. This example uses Google Gemini.

1. Goto https://aistudio.google.com/api-keys
2. Select `Create API key`
3. Follow the instructions & copy the API key
4. Create an `.env` at the root of the project
5. Enter the API key as follows:
   ```.env
   GOOGLE_AI_GEMINI_API_KEY=[API_KEY]
   ```

## Running ##
1. Build & start the Spring Boot microservices. This is done from the root of each module
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   > On Windows, use mvnw.cmd instead of ./mvnw
2. Verify all services are running. Each microservice is allocated its own port

## Interacting with Services ##
Since no default admin account exists, you can create a user via HTTP requests. For example, using cURL:
```bash
curl -X POST http://localhost:8080/users/register \
-H "Content-Type: application/json" \
-d '{
  "email": "admin@email.com",
  "address": "address",
  "password": "password",
  "role": "ADMIN"
  }'
```

### Logging in ###
```bash
curl -X POST http://localhost:8080/users/login \
-H "Content-Type: application/json" \
-d '{
  "email": "admin@email.com",
  "password": "password",
  }'
```
The response will include an authenticated token. Use the token in the `Authorization` header for future requests.

### Create a Product ###
```bash
curl -X POST http://localhost:8081/products \
-H "Authorization: Bearer [YOUR_TOKEN]" \
-H "Content-Type: application/json" \
-d '{
  "name": "Toaster",
  "description": "White Toaster",
  "price": 149.99,
  "stock": 5
  }'
```

### Create a Cart ###
```bash
curl -X POST http://localhost:8082/carts \
-H "Authorization: Bearer [YOUR_TOKEN]"
```

### Add Item to Cart ###
```bash
curl -X POST http://localhost:8082/carts/[CART_ID]/add/[PRODUCT_ID] \
-H "Authorization: Bearer [YOUR_TOKEN]"
```

### View Cart ###
```bash
curl -X GET http://localhost:8082/carts/[CART_ID] \
-H "Authorization: Bearer [YOUR_TOKEN]"
```

### Purchase Cart ###
```bash
curl -X POST http://localhost:8082/carts/[CART_ID]/purchase \
-H "Authorization: Bearer [YOUR_TOKEN]"
```

### Talk to AI ###
```bash
curl -X POST http://localhost:8083/customerSupportAgent \
-H "Authorization: Bearer [YOUR_TOKEN]" \
-H "Content-Type: application/json" \
-d '{
  "sessionId": [SESSION_ID]
  "userMessage": "Give me more information about the Toaster"
}'

