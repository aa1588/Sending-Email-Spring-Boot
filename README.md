
# Spring Boot Email

The goal of this project is to expose a few rest endpoints for sending an email using `spring-boot-starter-mail` ( internally uses `JavaMailSender` interface) for different email body types. This application covers sending a simple email with


```bash
                      1. plain text as email body, 
                      2. HTML content as email body,
                      3. a fixed template(text-file),
                      4. file attachments (MultiPartFile). 
```

Also, covers Unit testing with `Junit and Mockito`.
## Tech Stack

**Tools**: `Docker, Jib, Taskfile, MailHog`

We will be using MailHog as an email-testing tool that works with a fake SMTP server underneath.




## Installation

Install Docker Desktop, Taskfile

- https://docs.docker.com/desktop/
- https://taskfile.dev/installation/




## Run Local Machine

1. Run the Unit tests

```bash
  mvn test
```

2. Start the MailHog server
```bash
  docker compose -f deployment/docker-compose/infra.yml up -d
```

Alternatively, if you've installed `Taskfile`, Instead of `steps 1,2`, simply run

```bash
  task test
  task start_infra
```

This will start the MailHog server in a docker container, you can RUN the app from the IDE and access it at `http://localhost:8083/`.


## Run Inside a Docker Environment

1. Run the Unit tests

```bash
  mvn test
```

2. Compile + Create docker Image + Push to dockerHub

```bash
  mvn compile jib:build -DskipTests
```

3. Start the MailHog server + App
```bash
  docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/app.yml up -d
```

Alternatively, if you've installed `Taskfile`, Instead of `steps 1,2,3`, simply run

```bash
  task test
  task start
```

This will start the MailHog server and the application in a docker container, you can access the application at `http://localhost:8080/`.
## API Reference

#### Send an email with plain text

```http
  GET /api/simple?to=amrit_dev@gmail.com&subject=my subject&text=my content
```

#### Send an email with HTML body

```http
  GET /api/html?to=amrit_dev@gmail.com&subject=my subject&html=Hi <h3>AMRIT</h3><br>Find my <b>content</b>
```

| Parameter | Type     | Description                     |
|:----------|:---------|:--------------------------------|
| `to`      | `string` | **Required**. To:               |
| `subject` | `string` | **Required**. Subject:          |
| `html`    | `string` | **Required**. Email body (html) |



#### Send an email using text Template

```http
  GET api/template?to=amrit_dev@gmail.com&subject=my subject&name=AMRIT
```

| Parameter | Type     | Description                                           |
|:----------|:---------|:------------------------------------------------------|
| `to`      | `string` | **Required**. To:                                     |
| `subject` | `string` | **Required**. Subject:                                |
| `name`    | `string` | **Required**. Name of person you're sending email to. |


#### Send an email with attachment

```http
  POST /api/attachment
```

| Parameter | Type              | Description                                    |
|:----------|:------------------|:-----------------------------------------------|
| `to`      | `string`          | **Required**. To:                              |
| `cc`      | `string[]`        | **Optional**. cc:                              |
| `bcc`     | `string[]`        | **Optional**. bcc:                             |
| `text`    | `string`          | **Required**. Email body                       |
| `subject` | `string`          | **Required**. Email subject                    |
| `files`   | `MultipartFile[]` | **Optional**. File attachment(`zero or more`). |