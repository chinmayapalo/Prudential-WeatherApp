# Prudential-WeatherApp
This project based on a weather service that will retrieve daily information for number of cities in the world.
# Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
# Prerequisites
JDK 8 

Maven 3.5.2


If you are using Windows OS then set environment variable 
PROPERTY_NAME = Root directory of Project interface. (ex : PROPERTY_NAME : D:\weatherapp )

We are configuring PROPERTY_NAME variable into a java property for accessing different configuration file path during run time.

For MAC / Linux OS user No need to set any environment variable. 

# Installing

DOwnload & Install JDK 8 . Configure the JAVA_HOME environment variable.

Visit Maven official website, download the Maven zip file, for example : apache-maven-3.5.2-bin.zip.

Unzip it to a folder. In this article, we are using c:\opt\apache-maven-3.5.2

Add a MAVEN_HOME system variables, and point it to the Maven folder.

Test the installation :

java -version

mvn -version

echo $PROPERTY_NAME


# Build Instructions

change current directory to Root directory of Project interface where pom.xml file exist. 

cd (Root directory of Project interface). ( ex : cd /Users/raja/Documents/WORKSPACE/weatherapp ) then run below command.

mvn clean compile.

# Running the tests

There are 4 Test Case scenarios for this project.

 Test Case 01:

Test Case for OS dependent System properties initialization.

 Test Case 02:

Test Case whether CityInformation File exist or not.

 Test Case 03

Testing the Access Authorization Status Code. ( URL Accesstoken authorization Test )

 Test Case 04

Testing the Response Header Media Type. (MIME Type in Response Header).

For executing test cases use below command.

change current directory to Root directory of Project interface folder where pom.xml file exist. 

cd (Root directory of Project interface). ( ex : cd /Users/raja/Documents/WORKSPACE/weatherapp ) then run below command.

mvn clean test.

# Run Instructions

mvn clean install

cd (Root directory of Project interface). ( ex : cd /Users/raja/Documents/WORKSPACE/weatherapp ) Then run below command

java -jar -Dapple.awt.UIElement="true" {Root directory of Project Interface}/target/weatherapp-0.0.1-SNAPSHOT.jar -h

(ex : java -jar -Dapple.awt.UIElement="true" /Users/raja/Documents/WORKSPACE/weatherapp/target/weatherapp-0.0.1-SNAPSHOT.jar -h)

# Built With

Maven - Dependency Management

Junit - Unit Test

# Versioning

We use GIT for versioning. 

# Authors

Chinmaya Palo
