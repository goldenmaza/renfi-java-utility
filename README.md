# Renfi (Java version) &middot; [![GitHub license](https://img.shields.io/badge/license-ISC-blue.svg)](https://github.com/goldenmaza/renfi-java-utility/blob/master/LICENSE.md)

* [Purpose](##purpose)
* [File descriptions](#file-descriptions)
  + [RenfiUtility](#renfiutility)
  + [Constants](#constants)
  + [HelpGuideUtil](#helpguideutil)
  + [FileHandlingManager](#filehandlingmanager-file-handling-features)
  + [DataHandlingManager](#datahandlingmanager-data-handling-features)
  + [HistoryHandlingManager](#historyhandlingmanager)
  + [FileProcessingUtil](#fileprocessingutil)
  + [ImageProcessingUtil](#imageprocessingutil)
  + [VideoProcessingUtil](#videoprocessingutil)

## Purpose
The purpose with this project was to increase my knowledge of file manipulation in Java 8. I had to use a custom library
made by [drewnoakes](https://github.com/drewnoakes) to fetch Metadata from multimedia files (such as creation date etc).

The project contains the following source files: RenfiUtility.java, Constants.java, HelpGuideUtil.java,
FileProcessingUtil.java, ImageProcessingUtil.java, VideoProcessingUtil.java, DataHandlingManager.java,
FileHandlingManager.java and HistoryHandlingManager.java.

The project also contains the following test files: TODO.

## File descriptions
The following section details what each file does.

### RenfiUtility
This is the main class where argument verification happens as well as the user will be prompted on the possible flow
of the application. Loading of files will be performed by this class before the main method calls the managers for
either performing the file handling or the data handling features mentioned below.

### Constants
A central location of all logging output, available flags, extensions and more.

### HelpGuideUtil
The code for the printout of the HelpGuide can be found here.

### FileHandlingManager (file handling features)
This manager deals with calling the appropriate branch for the FileHandling flow. Meaning either: Compare, Crop,
Convert, Detect or Source.

### DataHandlingManager (data handling features)
This manager deals with calling the appropriate branch for the DataHandling flow. Meaning either: Java, Origin
or List. The work processed here will be used by the manager stated below.

### HistoryHandlingManager
This manager deals with working with the history result of what was prepared by the DataHandlingManager class.

### FileProcessingUtil
This file contains most of the features in this application:

a) This utility handles the processing of actually renaming the files, both do and undo.
b) This file also contains the code for manipulating and analyzing files like comparing, converting, cropping and
detecting black borders. It is also here were we can find the small logic of gathering file names and placing them
all in a txt file.
c) Finally, the code for creating directories and files are also here. 

This is an abstract class which the other two utility classes extends.

### ImageProcessingUtil
This utility prepares the conversion history for images currently supported. Mr. Noakes's library is used here to
gather image metadata for determining the creation date.

### VideoProcessingUtil
This utility prepares the conversion history for videos currently supported. Mr. Noakes's library is used here to
gather video metadata for determining the creation date.
