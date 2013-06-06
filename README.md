Code Elevator
=============

This project is a live coding contest.

Rules
-----

The goal of the game is to implement an elevator engine. Participants have to subscribe with a login, an email (in order
to display a linked [gravatar](http://www.gravatar.com)) and a server url. Then HTTP GET requests will be send to this
server :

### events (just respond HTTP 200 return code)

- `/call?atFloor=[0-5]&to=[UP|DOWN]`
- `/go?floorToGo=[0-5]`
- `/userHasEntered`
- `/userHasExited`
- `/reset`

### response

- `/nextCommand` : body of the request must contains `NOTHING`, `UP`, `DOWN`, `OPEN` or `CLOSE`

Prerequisites
-------------

Here is what you need to build and run a code elevator session :

- JDK 1.8
- maven

Steps
-----

These steps are described for a Mac Os X machine but can adapted to any operating system.

If you don't have jdk8 installed on your machine get `jdk-8-ea-bin-b92-macosx-x86_64-30_may_2013.dmg` from
[jdk8.java.net](http://jdk8.java.net/download.html). Double-click on downloaded dmg file and follow instructions.

    $ git clone git@github.com:xebia-france/code-elevator.git
    $ cd code-elevator
    $ export JAVA_HOME=`/usr/libexec/java_home -version 1.8`
    $ mvn clean install
    $ cd elevator-server
    $ mvn jetty:run

And then go to [http://localhost:8080](http://localhost:8080), subscribe to a session and start implementing your
participant server.