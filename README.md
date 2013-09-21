# Code Elevator


This is a slightly modified version of Sebastian original code-elevator, we have changed :
* A script to run the server (`./run.sh`)
* The admin login/password is hardcoded to admin/admin
* The default number of potential passenger is bumped from 0 to 3


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

- JDK 1.7
- maven 3.x

### Steps

    $ git clone https://github.com/jeanlaurent/code-elevator.git
    $ cd code-elevator
    $ ./run.sh
    
If you can't run shell script on your machine, just type in :

    $ mvn clean install
    $ mvn --file elevator-server/pom.xml jetty:run
    

Go to [http://localhost:8080](http://localhost:8080), subscribe to a session and start implementing your elevator
server.

If you want to increase the number of people at any given time, go to [http://localhost:8080/#/administration](http://localhost:8080/#/administration), log in with user
`admin` and password `admin`, and adjust (add or remove) the number of people taking the elevator.
If you put 0 person, no one will call the elevator.


## Running on a remote server

Don't want to install Java nor fill up your hard drive with jar files you can try [Sebastian's online server](http://code-elevator.seblm.cloudbees.net/#/)
