# Memories
A terminal note-taking application.

# What is it?
This a note application designed mainly for GNU/Linux.
This application can do the following things:
- You can quickly view all the note that you have taken.
- You can take note and store it on a local MySQL database without having to create a file.
- You can copy a note on MySQL database to file, edit it and update the content.
- You can run bash command on the application just like interacting with a terminal.

# How to use
## How to run
You can run the *start.sh* in *"./out/start/sh"*
```markdown
bash out/start.sh
```
If you have different configuration for MySQL connection, then adjust the file.

## How to create a file
To create a file, please run
```bash
edit -n newfile.md new
```
*__Pleas note!__*
When editing a file, you'll have to following its format or otherwise the app cannot parse the content.
```markdown
# Meta information
## ID
300525489
## Theme
Hackathon
## Create On
2023-09-13

# Content
## Note
Prepare for the Semi-Finals
## Description
Congratulations! You've Advanced to the Semi-Finals of the Hackathon.
Each time has 15 minutes: {5 for presentation, 10 for debate and QAs with the judges}
## Links
- AI
- Recycling
- LLM
```
## How to copy a note to a file
First, you'll have to view the notes
```bash
view -a
```
After that, you can select a note using its index number (start from 0) and copy it to a file.
```bash
edit -n select_2.md -s 2 copy
```

## How to persist a file to a record on MySQL DB
Assume that you have edit _mynote.md_ file, you can do just that by:
```bash
edit -n mynote.md save
```

# Use case diagram
Here is an overview of the app
![Use Case diagram]("./assests/use_case.png")
