SRC=src/
BLD=build/
TST=tests/
DOC=html/
LAT=latex/

FLAGS= -Wall

.PHONY: all build tests clean doxygen 

all: build

build: dir tracker #doxygen

doxygen:
	doxygen dox
	
tests: $(BLD)utils.o $(BLD)thpool.o $(BLD)hash_table.o
	cc $(FLAGS) -o $(BLD)$@ $(TST)test_hash_table.c $^ -pthread

clean:
	rm -rf $(SRC)*.o *~ \#*#
	rm -rf $(BLD)*
	if [ -d $(BLD) ]; then rmdir $(BLD); fi
	rm -rf $(DOC)*
	if [ -d $(DOC) ]; then rmdir $(DOC); fi
	rm -rf $(LAT)*
	if [ -d $(LAT) ]; then rmdir $(LAT); fi

tracker: $(BLD)utils.o $(BLD)thpool.o $(BLD)hash_table.o $(BLD)port_table.o
	@if [ -z $(N_THREADS) ]; then \
		cc $(FLAGS) -g3 -o $(BLD)$@ $(SRC)$@.c $^ -pthread; \
	else \
		cc $(FLAGS) -g3 -o $(BLD)$@ $(SRC)$@.c $^ -pthread -DN_THREADS=$(N_THREADS); \
	fi; \

$(BLD)utils.o:
		cc $(FLAGS) -o $@ -c $(SRC)utils.c

$(BLD)thpool.o:
		cc $(FLAGS) -o $@ -c $(SRC)thpool.c -D THPOOL_DEBUG -pthread

$(BLD)hash_table.o:
		cc $(FLAGS) -o $@ -c $(SRC)hash_table.c

$(BLD)port_table.o:
		cc $(FLAGS) -o $@ -c $(SRC)port_table.c

dir:
	mkdir -p $(BLD)