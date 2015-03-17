sudo docker run --name code_elevator_container code_elevator
#replace 9000 by whatever port you want to expose ie 80
sudo docker run -d -p 9000:8080 --volumes-from code_elevator_container jetty:9.2.9-jre8
