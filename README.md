NetworkAnalyzer-ClientServer
============================

This is part of the NetworkAnalyzer project but it something written in Java because it couldn't be done as easy in PHP.

This part of the application allows the application to run certain basic commands on the remote systems that it monitors
to collect data (like network traffic).

The application can run in either server or client mode depending on the given parameters

#### To run as a server you need to launch with argument:
* -mode server

###### Possible arguments:
* -port   The port you want the server to listen to

#### To run as a client:
* -mode client

###### Possible arguments:
* -command    The client basically relies on this command, so if you don't give a command it will do nothing. Example: -command "ifconfig eth0 down"
* -port       The port you want the client to connect to
* -address    By default this will be "127.0.0.1". This is the address of the server part of the application that listens for connections
* -v          Verbose. Prints some additional information besides the results of the command.
