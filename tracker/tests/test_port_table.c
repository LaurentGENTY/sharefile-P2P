#include <assert.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "../src/port_table.h"

void test_port__search(){
    printf("Test de port__search:\n");
    port__table_init();
    int err = port__add("125.125.125.125",900);
    assert(err == 1);
    assert(port__search("125.125.125.125") == 900);
    port__table_end();
    printf("SUCCESS\n");
}

void test_port__add(){
    printf("Test de port__add:\n");
    port__table_init();
    int err = port__add("255.255.255.255",1000);
    assert(err == 1);
    err = port__add("255.255.255.255",800);
    assert(err == 1);
    assert(port__search("255.255.255.255") == 1000);
    err = port__add("1.1.1.1",500);
    assert(port__search("1.1.1.1") == 500);
    port__table_end();
    printf("SUCCESS\n");
}

void test_port__print(){
    printf("Test de port__print:\n");
    port__table_init();
    int err = port__add("8.8.8.8",8888);
    assert(err == 1);
    err = port__add("2.2.2.2",2222);
    assert(err == 1);
    port__print();
    port__table_end();
    printf("SUCCESS\n");
}

//Exemple d'utilisation de la port_table
int main(int argc, char const *argv[]) {
    test_port__search();
    test_port__add();
    test_port__print();
    return 0;
}
