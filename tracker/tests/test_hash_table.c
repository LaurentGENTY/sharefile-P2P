#include <assert.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "../src/hash_table.h"


void verif(struct file *f,char* key, char* IP, int port, char* name, int length, int piecesize){
    assert(strcmp(f->key,key) == 0);
    assert(strcmp(f->name,name) == 0);
    assert(f->length == length);
    assert(f->piecesize == piecesize);

    int n = 0;

    struct seeder* seed;
    SLIST_FOREACH(seed,&f->seeders,next_seeder){
        if(strcmp(seed->IP,IP) == 0)
            if(seed->port == port)
                n = 1;
    }
    assert(n);
}

void test_hash__search(){
    printf("Test de hash__search:\n");
    hash__table_init();
    struct file* f;
    //On ajoute un élément
    hash__add_seeder("LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    f = hash__search("LROTPbestdlesGAY4EVER");
    assert(strcmp(f->key,"LROTPbestdlesGAY4EVER") == 0);
    hash__table_end();
    printf("SUCCESS\n");
}

void test_hash__add_seeder(){
    printf("Test de hash__add_seeder:\n");
    hash__table_init();
    struct file* f;
    //On ajoute un élément
    hash__add_seeder("LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    f = hash__search("LROTPbestdlesGAY4EVER");
    verif(f,"LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    //On modifie cet élément en lui ajoutant un port avec même IP
    hash__add_seeder("LROTPbestdlesGAY4EVER","255.255.255.255",380,"Le delegue",2054,3);
    f = hash__search("LROTPbestdlesGAY4EVER");
    verif(f,"LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    verif(f,"LROTPbestdlesGAY4EVER","255.255.255.255",380,"Le delegue",2054,3);

    //On ajoute un autre élément avec une autre clef
    hash__add_seeder("3OGenstil","225.250.225.230",210,"Un delegue",1028,1);
    f = hash__search("3OGenstil");
    verif(f,"3OGenstil","225.250.225.230",210,"Un delegue",1028,1);
    hash__table_end();
    printf("SUCCESS\n");
}

void test_hash__add_leecher(){
    printf("Test de hash__add_leecher:\n");
    hash__table_init();
    //On ajoute trois élément
    hash__add_seeder("LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    hash__add_seeder("3OGenstil","225.250.225.230",210,"Un delegue",1028,1);
    hash__add_seeder("TT","5.5.5.5",1000,"Un delegue",10,4);

    assert(hash__add_leecher("LROTPbestdlesGAY4EVER","125.125.125.125",8080));
    assert(hash__add_leecher("3OGenstil","125.125.125.125",8080));
    assert(hash__add_leecher("TT","125.125.125.125",8080));

    hash__table_end();
    printf("SUCCESS\n");
}

void test_hash__print(){
    printf("Test de hash__print:");
    hash__table_init();
    hash__add_seeder("LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    hash__add_seeder("3OGenstil","225.250.225.230",210,"Un delegue",1028,1);
    hash__print();
    hash__table_end();
    printf("SUCCESS\n");
}

void test_hash__peer_print(){
    printf("Test de hash__peer_print:\n");
    hash__table_init();
    //On ajoute 3 éléments qui ont le fichier
    hash__add_seeder("LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    hash__add_seeder("3OGenstil","255.255.255.255",255,"Un delegue",1028,1);
    hash__add_seeder("TT","5.5.5.5",1000,"Un delegue",10,4);
    //On ajoute 3 éléments qui téléchargent le fichier
    hash__add_leecher("LROTPbestdlesGAY4EVER","5.5.5.5",1000);
    hash__add_leecher("3OGenstil","255.225.240.230",8000);
    hash__add_leecher("TT","255.255.255.255",3000);
    hash__peer_print();
    hash__table_end();
    printf("SUCCESS\n");
}

void test_hash__getfiles(){
    printf("Test de hash__getfiles:\n");
    hash__table_init();
    hash__add_seeder("LROTPbestdlesGAY4EVER","255.255.255.255",255,"Le delegue",2054,3);
    hash__add_seeder("3OGenstil","225.250.225.230",210,"Un delegue",1028,1);
    hash__add_seeder("JAzzBusquet","125.125.125.125",1000,"Gobert",3000,2);
    hash__add_seeder("TT","5.5.5.5",1000,"Un delegue",10,4);
    char* err = malloc(150*sizeof(char));
    int nb = 0;
    nb = hash__getfiles("-1",'>',2000,err);
    assert(!strcmp(err,"Le delegue 2054 3 LROTPbestdlesGAY4EVER Gobert 3000 2 JAzzBusquet") && nb == 2);
    *err = '\0';
    nb = hash__getfiles("-1",'<',2000,err);
    assert(!strcmp(err,"Un delegue 10 4 TT Un delegue 1028 1 3OGenstil") && nb == 2);
    *err = '\0';
    nb = hash__getfiles("-1",'=',2054,err);
    assert(!strcmp(err,"Le delegue 2054 3 LROTPbestdlesGAY4EVER") && nb == 1);
    *err = '\0';
    nb = hash__getfiles("Gobert",'=',-1,err);
    assert(!strcmp(err,"Gobert 3000 2 JAzzBusquet") && nb == 1);
    *err = '\0';
    nb = hash__getfiles("Le delegue",'>',2000,err);
    assert(!strcmp(err,"Le delegue 2054 3 LROTPbestdlesGAY4EVER") && nb == 1);
    *err = '\0';
    nb = hash__getfiles("-1",'=',-1,err);
    assert(!strcmp(err,"") && !nb);
    *err = '\0';
    nb = hash__getfiles("-1",'>',3000,err);
    assert(!strcmp(err,"") && !nb);
    free(err);
    hash__table_end();
    printf("SUCCESS\n");
}

//Exemple d'utilisation de la hash_table
int main(int argc, char const *argv[]) {
    test_hash__search();
    test_hash__add_seeder();
    test_hash__add_leecher();
    test_hash__print();
    test_hash__peer_print();
    test_hash__getfiles();
    return 0;
}
