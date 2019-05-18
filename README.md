# Prudential-WeatherApp
This project based on a weather service that will retrieve daily information for number of cities in the world.
# Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
# Prerequisites
JDK 8 

Maven 3.5.2

If you are using Windows OS then set environment variable 
PROPERTY_NAME = Root directory of Project interface. (ex : PROPERTY_NAME : D:\weatherapp )

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

mvn clean compile

# Running the tests


# Run Instructions

mvn clean install

If you are using MAC / Linux OS then 

cd Root directory of Project interface. ( ex : cd /Users/raja/Documents/WORKSPACE/weatherapp ) Then run below command

java -jar -Dapple.awt.UIElement="true" {Root directory of Project Interface}/target/weatherapp-0.0.1-SNAPSHOT.jar -h

(ex : java -jar -Dapple.awt.UIElement="true" /Users/raja/Documents/WORKSPACE/weatherapp/target/weatherapp-0.0.1-SNAPSHOT.jar -h)



