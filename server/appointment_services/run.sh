#!/bin/bash
echo Building the project with Maven...
mvn clean install && java -jar target/appointment_services-0.0.1-SNAPSHOT.jar