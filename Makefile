run: compile
	java -cp .:evernote-api-1.25.1.jar Main

compile:
	javac -cp .:evernote-api-1.25.1.jar *.java
