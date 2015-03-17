This project was created as a way for me to store personal code, including scripts, utilities, and applications.

# Introduction #

This project includes an assortment of useful scripts, Java, C/C++, and C# classes, applications, etc.

# Details #
Short descriptions of all the utilities in this repository follow.

## Scripts ##

---


### Perl ###
#### mcp ####
A handy little utility that allows multiple files to be copied or moved, with search/replace functionality.
E.g. Suppose you have the following files:
  * _main.cpp_
  * _class\_a.cpp_
  * _class\_b.cpp_
The following command:
  * `mcp cpp cpp_orig *.cpp`
copies the all files in the search list (`*.cpp`), and replaces all instances of "cpp" with "cpp\_orig".

### Python ###
#### classes ####
This script just shows how to do simple classes and inheritance in Python.

#### euler ####
This is a ProjectEuler solver written in Python.

#### mcp ####
This is the same multi-copy/move utility described above but written in Python. (Which makes it much better.)

#### utils ####
A rather small collection of handy Python utility modules.

### Shell ###

#### svndiff ####
A useful shell script that uses Meld to diff source files that are under subversion's control. Includes flags for diffing with the HEAD, BASE, or a particular rev.

## Java ##

---


### [JavaUtils](JavaUtils.md) ###
This project represents the core Java utilities, classes, etc. that I use to build other apps from. This project includes things like:
  * useful Swing widgets
  * tasking/threading widgets
  * gaming logic
  * useful functions

### [Blue](Blue.md) ###
A simple desktop game that is based on the popular Flash game, Red. This was more an exercise in gaming logic and simple 2D physics collision logic than anything else.

### [HexEditor](HexEditor.md) ###
A relatively full-featured hex editor.

### [Meteors](Meteors.md) ###
Another simple desktop game where the objective is to shoot meteors before they crash down on your city.

### [ProjectEuler](ProjectEuler.md) ###
A ProjectEuler solver written in Java.

### [ChatterBox](ChatterBox.md) ###
A simple desktop IM client that works via multicast.

### [StreamView](StreamView.md) ###
A binary stream viewer that works with files.

### [BigThree](BigThree.md) ###
A simple Android application that keeps track of progress made in weightlifting exercises.

## C# ##

---


### [ChatterBox](ChatterBoxCSharp.md) ###
A simple desktop IM client that works via multicast.

### [CsUtils](CsUtils.md) ###
A collection of C# utility methods and classes.

### [CsvEditor](CsvEditor.md) ###
A simple .csv file editor.

### [StreamView](StreamViewCSharp.md) ###
A binary stream viewer that works with files.