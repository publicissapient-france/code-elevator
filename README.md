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

    $ git clone git@github.com:xebia-france/code-elevator.git
    $ cd code-elevator
    $ mvn clean install
    $ mvn --file elevator-server/pom.xml jetty:run [-DADMIN_PASSWORD=secret]

Go to [http://localhost:8080](http://localhost:8080), subscribe to a session and start implementing your elevator
server.

If you run a public session you may modify admin password by starting server with system property `ADMIN_PASSWORD`. You
can also control max number of users per building which is three at first or kick out some subscribers when going to
[http://localhost:8080/#/administration](http://localhost:8080/#/administration).

## Running on a remote server

Don't want to install Java nor fill up your hard drive with jar files you can try
[Sebastian's online server](http://code-elevator.seblm.eu.cloudbees.net).

To deploy to a cloudbees instance (example) :

    $ mvn verify
    $ bees app:deploy --appid seblm/code-elevator --endPoint eu [--message "informational message"] [-P ADMIN_PASSWORD=secret] --type tomcat7 elevator-server/target/elevator-server-1.1-SNAPSHOT.war

## Running with Docker

There are multiple ways of making this kind of project running with Docker, but here's the one we chose: a first container will actually deal with the Maven build, and put the result of it (the generated WAR) in a volume. That volume will then be used by a Jetty container allowing to run the application.

The only addition in this project is the Dockerfile describing what the maven container is supposed to do. Please notice that with this configuration, it's not even mandatory for you to have Maven installed anywhere. You can trigger the build directly with Docker which will take care of retrieving the correct version of Maven and JDK.

The commands to run are:

- Build the Docker image for the Maven build: `docker build -t code_elevator .`
- Run a container based on the previously created image: `docker run --name code_elevator_container code_elevator`

At this point, the Maven build of code-elevator will be managed, and the result of the build will be available in a volume managed by the container. The only remaining thing is to execute a Jetty container allowing to run our application:

- `docker run -d -p 8080:8080 --volumes-from code_elevator_container jetty:9.2.9-jre8`

That's all! The server is now up and running in a Docker container!

## Export / Import users

When you redeploy the application, all users are lost in the operation. You can then save all users and re-import after redeployment.

- export


    $ curl --basic --user :secret --url http://localhost:8080/resources/players.csv > players.csv

Csv is user email, user login, server url and score:

    "foo@exemple.org","Foo","http://exemple.org:8081/elevator-participant/",4242
    "foo@exemple.org","Duplicate Foo","http://exemple.org:8081/elevator-participant/",0
    "bar@exemple.org","Bar","http://exemple.org",666

- import


    $ curl --basic --user :secret --url http://localhost:8080/resources/players.csv --form players=@players.csv > uploadResult.txt

Upload result is a json file where the key is the user email and the value is the import result. Import is successful when the password is defined:

    {
      "foo@exemple.org":["password","a game with player foo@exemple.org has already been added"],
      "bar@exemple.org":["password"]
    }
