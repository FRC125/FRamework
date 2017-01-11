#!/bin/bash

# This script downloads the latest (known) file of the WPI C++ plugin.
# The .jar file (really a .zip) contains a java.zip that has the
# include/ and lib/ folders necessary to build/link an executable FRC robot.

# The resulting wpilib/include/ folder should be used as a g++ -I statement
# The wpilib/lib folder should be used as a g++ -L statement

# TODO: script determining the latest .jar file based on the date string
# rather than a hard coded file name.
# The latest version can be determined from
# http://first.wpi.edu/FRC/roborio/release/eclipse/site.xml
version="2017.1.1"

source wpilib/version.txt
if [ ! "$version" = "$downloaded_version" ] ; then
	wget --quiet -O wpijava.zip http://first.wpi.edu/FRC/roborio/release/eclipse/plugins/edu.wpi.first.wpilib.plugins.java_$version.jar
	unzip wpijava.zip resources/java.zip
	mkdir wpilib
	mv resources/java.zip ./
	rm -rf resources
	unzip -d wpilib/ java.zip

	rm -rf java.zip
	rm -rf wpijava.zip
	echo "downloaded_version=$version" > wpilib/version.txt
else
	echo "Already at latest version"
fi

echo "Version = $version"
