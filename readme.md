Super basic readme
============
* proto directory contains .proto files that describes the messages a responses client and server will use to communicate
* gRPC automatically creates java classes based on these files.

run below commands to generate java classes
------------
    mvn clean install


if on Windows and maven commands from terminal don't work
You can get intelliJ to do this by clicking:
1. run.
2. Edit configurations.
3. Select the + in top left corner.
4. Under command line type clean install.
5. Run clean install command.