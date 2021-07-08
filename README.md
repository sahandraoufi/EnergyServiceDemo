# EnergyServiceDemo

## This project is developed by Sahand Raoufi

### EnergyService is a SpringBoot application written in Java and the main purpose is a demo using data provided in the link https://www.energy.gov/sites/prod/files/2020/12/f81/code-12-15-2020.json

### For testing the application clone the project in your local machine and the follow these steps:
* Open the Terminal and move to the project directory
* Run the comand ```mvn clean install```
* After the build is complete run the command ```java -jar target/energyservice-0.0.1-SNAPSHOT.jar```
* Now in your browser navigate to swagger-ui with the link http://localhost:8080/swagger-ui.html
* Under the energy-controller there are two endpoints provided, one for getting the result in json format and one for geeting downloadable csv file
* Open any of these endpoints and select sortField and sortType from dop-downs
* push the ```Try it out!``` button and you should be able to see the response
