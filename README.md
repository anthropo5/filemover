# filemover
Application moves files from main-folder to sub-folder depending on file extension.
You have to define main-folder, sub-folders and extensions in "config.txt".
Personally I use it to segregating files in download folder.

## config.txt
In first line you have to specify folder where application will be working i.e.  
`main folder: F:/Path/to/folder/download`

In next lines you have to specify folder name and extensions of files which be moved to it i.e.  
`folder_name: ext1 ext2 ext3`  
`images: jpeg jpg png tiff giff`

`config.txt` contains simple example. You can use it.

## _testfolder
Contains empty files with diffrent extensions. Allows you to test aplication. Remember to specify path in config.txt


## TODO
- [ ] Add MainFolder class -> allows user to manage few folders
- [ ] Specify destination folder path for extension e.g. in config.txt: `F:\From\main_folder\to\diffrent\location: jpg`
- [ ] Create optional default folder - all files with not recognized extensions would be moved to "default" folder
- [ ] Optional flag if directories should be moved to default folder
- [ ] User can specify which extensions wouldn't be copied to "default" folder
- [ ] Move files form the biggest to the lowest size
- [ ] Use project manager - maven (or gradle in future)
- [ ] Add log4j
- [ ] Spearate View and Contoller - currently View = Controller + View
