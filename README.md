# Code Elevator

This project is a live coding contest. [More info here](http://xebia-france.github.io/code-elevator).

## Rules

The goal of the game is to implement an elevator engine. Participants have to subscribe with a login, an email (in order
to display a linked [gravatar](http://www.gravatar.com)) and a server url. Then HTTP GET requests will be send to this
server :

### events (just respond HTTP 200 return code)

- `/call?atFloor=[0-5]&to=[UP|DOWN]`
- `/go?floorToGo=[0-5]`
- `/userHasEntered`
- `/userHasExited`
- `/reset?cause=information+message`

### response

- `/nextCommand` : body of the request must contains `NOTHING`, `UP`, `DOWN`, `OPEN` or `CLOSE`

## Running the server locally

### Prerequisites

Here is what you need to build and run a code elevator session :

- JDK 1.8
- maven 3.x

### Steps

## Simple start with Docker

    $ git clone git@github.com:xebia-france/code-elevator.git
    $ cd code-elevator
    $ ./build.sh
    $ ./run.sh

## Building yourself with maven

    $ git clone git@github.com:xebia-france/code-elevator.git
    $ cd code-elevator
    $ mvn clean install
    $ mvn --file elevator-server/pom.xml jetty:run [-DADMIN_PASSWORD=secret]
    
Note : if you need to change the port because your port 8080 is already used, you can start it instead with :

    $ mvn --file elevator-server/pom.xml jetty:run -Djetty.port=8086 [-DADMIN_PASSWORD=secret] 

Go to [http://localhost:8080](http://localhost:8080), subscribe to a session and start implementing your elevator
server.

If you run a public session you may modify admin password by starting server with system property `ADMIN_PASSWORD`. You
can also control max number of users per building which is three at first or kick out some subscribers when going to
[http://localhost:8080/#/administration](http://localhost:8080/#/administration).

## Export / Import users

When you redeploy the application, all users are lost in the operation. You can then save all users and re-import after
redeployment.

- export


    $ curl --basic --user :secret --url http://localhost:8080/resources/players.csv > players.csv

Csv is user email, user login, server url and score:

    "foo@exemple.org","Foo","http://exemple.org:8081/elevator-participant/",4242
    "foo@exemple.org","Duplicate Foo","http://exemple.org:8081/elevator-participant/",0
    "bar@exemple.org","Bar","http://exemple.org",666

- import


    $ curl --basic --user :secret --url http://localhost:8080/resources/players.csv --form players=@players.csv > uploadResult.txt

Upload result is a json file where the key is the user email and the value is the import result. Import is successful
when the password is defined:

    {
      "foo@exemple.org":["password","a game with player foo@exemple.org has already been added"],
      "bar@exemple.org":["password"]
    }
