# citrinechallenge


The server.js file handles all serverside intilization and computation 
(i.e running a jar file and then placing outputs in JSON format).
The Citrine.java is the java source code that performs all the requisite computation to convert
an input string into SI units and determines a multiplication factor.
SIConverter1.7.jar is the jar file compiled from Citrine.java that is compatible with Java 7 since
Heroku servers are only running Java 7.

NOTE SIConverter1.7.jar is not included since it cannot be sent over email.
