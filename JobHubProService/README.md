<h2>Job Hub Pro Service</h2>
	This provides back-end service for the request received from its client. <br/>
	
<h4>Getting Started:</h4>
	To clone project locally,
	
	git clone https://github.com/ranjithramasamy/SpringBoot.git
	
<h4>Build and Run:</h4>
	
	cd JobHubProService	
	mvn clean install
	java -jar target/JobHubProService-0.0.1-SNAPSHOT.jar

Base url to access all resources available in this service,
	
	http://localhost:8080/v1/api/
	
<h5>If you want to run this application as a docker container then please use below commands to make it work, </h5>
   To build the docker image:
    
    docker build -t job-hub-pro-backend:prod .
    
   To bring up the docker container:
        
    docker-compose up
   
   To bring down the docker container:
           
    docker-compose down