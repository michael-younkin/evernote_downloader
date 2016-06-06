I wrote this tool to help me move all my old notes out of Evernote.

I don't use Evernote anymore because it doesn't make me more productive, but I still have a bunch of content in it from many years ago.
This Python script uses the Evernote API to download all notes, images, and other documents from Evernote.

If it isn't a huge pain, I'll also convert the notes from their xml representation to regular HTML.
Though it may actually be more useful to just store it in a sqlite database...

# Requirements

Tested with Java 8.

# Usage

Generate a dev token for Evernote's production servers on their developer website.
(This is much easier than setting up OAuth).
Then put this in a single file named `token.txt` next to the `main.java` file.
Then run `make`, which will automatically compile and run the program.
