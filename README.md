# Renfi (Java version) &middot; [![GitHub license](https://img.shields.io/badge/license-ISC-blue.svg)](https://github.com/goldenmaza/renfi-java-utility/blob/master/LICENSE.md)

* [Purpose](##purpose)
* [File descriptions](#file-descriptions)
  + [RenfiUtility](#renfiutility)
  + [Constants](#constants)
  + [HelpGuideUtil](#helpguideutil)
  + [FileProcessingUtil](#fileprocessingutil)
  + [ImageProcessingUtil](#imageprocessingutil)
  + [VideoProcessingUtil](#videoprocessingutil)

## Purpose
The purpose with this project was to increase my knowledge of file manipulation in Java 8. I had to use a custom library
made by [drewnoakes](https://github.com/drewnoakes) to fetch Metadata from multimedia files (such as creation date etc).

The project contains the following source files: RenfiUtility.java, Constants.java, FileProcessingUtil.java,
ImageProcessingUtil.java and VideoProcessingUtil.java.

The project also contains the following test files: TODO.

## File descriptions
The following section details what each file does.

### RenfiUtility
This is the main class where the main method calls the other classes for preparing the history and eventually perform
the renaming/undo of file names. It is also here we the small logic of gathering file names and placing them all in a
txt file.

### Constants
A central location of all logging output, available flags, extensions and more.

### HelpGuideUtil
The code for printout of the HelpGuide can be found here.

### FileProcessingUtil
This utility handles the processing of actually renaming the files. This is an abstract class which the other two
utility classes extends.

### ImageProcessingUtil
This utility prepares the conversion history for images currently supported. Mr. Noakes's library is used here to gather
image metadata for determining the creation date.

### VideoProcessingUtil
This utility prepares the conversion history for videos currently supported. Mr. Noakes's library is used here to gather
video metadata for determining the creation date.
