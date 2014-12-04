##################################################################
#          Project Convenience Makefile Wrapper for Maven2       #
##################################################################

# This makefile is just a convenience wrapper for the Maven2
# program. The actual building rules for this project may
# be found in the Maven2 "pom.xml" file located in this folder.

######################### DEFINITIONS ############################

# Define the commandline invocation of Maven2 if necessary:
ifeq ($(MVN),)
    MVN  := mvn
endif

######################## BUILD TARGETS ###########################

.PHONY: all package compile check test doc docs javadoc clean help

all: 
	@ $(MVN) $(MVNFLAGS) package

package: 
	@ $(MVN) $(MVNFLAGS) package

compile: 
	@ $(MVN) $(MVNFLAGS) compile

check:
	@ $(MVN) $(MVNFLAGS) test

test:
	@ $(MVN) $(MVNFLAGS) test

run:
	@ echo
	@ java -cp bin/csce450GProject-0.1.jar edu.louisiana.cacs.Main data/sample.txt

testreport:
	@ cat bin/surefire-reports/*.txt

clean:
	@- rm -rf ./bin/*
	@- rm -rf ./build/*

distclean: clean ;

help:
	@ echo "Usage   :  make <target>"
	@ echo "Targets :"
	@ echo "   all ........... Builds the project."
	@ echo "   package ....... Archives all *.class files."
	@ echo "   compile ....... Compiles all Java files."
	@ echo "   check ......... Builds and runs all unit tests."
	@ echo "   test .......... Builds and runs all unit tests."
	@ echo "   testreport .... Displays all the test report"
	@ echo "   clean ......... Removes build products."
	@ echo "   run ........... Runs the project."
	@ echo "   help .......... Prints this help message."
