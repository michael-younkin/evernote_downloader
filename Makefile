run: compile
	rm -rf notes_backup
	java -cp .:evernote-api-1.25.1.jar Main notes_backup

compile:
	javac -cp .:evernote-api-1.25.1.jar *.java
