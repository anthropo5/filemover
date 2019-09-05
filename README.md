
## note
application is not ready. I'm aware there is a lot of bad code in here. I have learnt basics of java on this project. I abandoned it to learn new things related to web development.

## technologies
- Java 8
- Maven
- Logback
- Sneakyaml
- JUnit 4
- Apache Common CLI
- git

# filemover
Application moves files from main-folder to sub-folder depending on file extension.
You have to define main-folder, sub-folders and extensions in "config.yml".
Personally I use it to segregating files in download folder.

## config.yml
In first line you have to specify folder where application will be working i.e.  
`main folder: F:/Path/to/folder/download`

In next lines you have to specify folder name and extensions of files which be moved to it i.e.  
If you don't specify a path directory would be created in `main folder`.
```
directories:
   - name: doc
     path: '/home/kusy/git/filemover/_testfolder2'
     extensions: txt pdf xlsx pptx doc docx
   - name: img
     extensions: jpg jpeg png gif
 ```

`config.yml` contains simple example. You can use it. Remember to redit main-folder path.


## TODO
### In future
- [ ] Add more tests with JUnit
- [ ] Use constructor from sneakyaml

### In far future
- [ ] Add MainFolder class -> allows user to manage few folders
- [ ] Move files form the lowest to the biggest size
- [ ] Maybe move big files in new thread - what - observable pattern to progress bar? user input would be blocked anyway.
- [ ] Progress bar to each file
- [ ] Spearate View and Contoller - currently View = Controller + View
- [ ] Create optional default folder - all files with not recognized extensions would be moved to "default" folder
- [ ] Optional flag if directories should be moved to default folder
- [ ] User can specify which extensions wouldn't be copied to "default" folder

### Completed
- [X] Use project manager - maven (or gradle in future)
- [X] Add Logback
- [X] Change simple Logger to Logback
- [X] CRUD operations on directories
- [X] Specify destination folder path for extension e.g. in config.txt: `F:\From\main_folder\to\diffrent\location: jpg`
- [X] Add some tests with JUnit 4 (Directory.class)
- [X] Create basic CLi with Apache Common CLI




