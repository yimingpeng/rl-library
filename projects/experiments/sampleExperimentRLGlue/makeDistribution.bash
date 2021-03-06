#!/bin/bash


#Make the distribution for the RL-Glue Sample experiment
#This relies on a fairly general script that will work for most Java environments and agents.
#Requirements
#	this should be done with code that is in a subversion checkout
#	you should have a build.xml file that follows the "rl-library build system"
#	I'll add more as I think of it.

#Maybe this should be done in ANT.  Not quite sure.

#Set these variables for this specific project
PROJECTNAME=SampleExperimentRLGlue-Java
SYSTEMPATH=../../../system
WIKIPAGENAME='Sample_Experiment_RL-Glue_(Java)'
SVNPASSWORDFILE=~/rl-library-svn-password
WIKIPASSWORDFILE=~/rl-library-wiki-password
PROJECTTYPE=Experiment
LANGUAGE=Java


#Get all of the build functions
source $SYSTEMPATH/common/scripts/build-java-experiment.sh

#Sets up the most of the paths and creates the distribution directory
#Copies the default set of jars and stuff to the appropriate directories.
#After you call this, you should copy whatever other files you need to $DISTDIR
javaDistributionInit

#
#  Extra files you want to distribute go here
#


#This will go into $DISTDIR, build the project, remove the build directory, back out
#tar and gzip the directory, and then delete the directory.
javaDistributionBuildJarAndGzip

if [ -z "$SKIPUPLOAD" ]
then
	javaDistributionUploadFile
else
	echo "   Skipping upload."
fi


javaDistributionUpdateWiki

#Delete $DISTDIR and $DISTFILENAME
javaDistributionCleanup
