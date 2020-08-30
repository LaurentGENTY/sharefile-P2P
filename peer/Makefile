SRC=src/
BLD=build/
TST=tst/


FLAGS=-d $(BLD) -cp $(BLD) #-Xlint:unchecked -Xlint:deprecation


.PHONY: all build tests clean

all: build

## Building java into .class

build:
	javac $(FLAGS) $(SRC)*.java

doxygen:
	doxygen dox


tests:
	javac $(FLAGS) $(SRC)*.java $(TST)*.java


clean:
	rm -rf $(SRC)*.class *~ \#*#
	rm -rf $(BLD)*