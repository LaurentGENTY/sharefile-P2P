/**
 * @file port_table.c
 * */
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "port_table.h"

/**
 * Hash function in order to find index where to store elements
 * @param key unique key of the file
 *
 * @return hashed index value : where to store the file
 * */
int hash_value_port(char* key){
    int hash = 0;
    for (int i = 0; i < strlen(key); i++)
        hash = 31 * hash + key[i];
    return abs(hash%PORT_TABLE_LENGTH);
}

int port__add(char* IP, int port){
    int index = hash_value_port(IP);
    struct peer *p;
    pthread_mutex_lock(&port_mutex_table[index]);
    SLIST_FOREACH(p,&port_table[index],next_peer)
        if(!strcmp(p->IP,IP)){
            pthread_mutex_unlock(&port_mutex_table[index]);
            return 1;
        }
    struct peer *f = malloc(sizeof(struct peer));
    f->IP = malloc(sizeof(char) * 1024);
    strcpy(f->IP,IP);
    f->port = port;
    SLIST_INSERT_HEAD(&port_table[index],f,next_peer);
    pthread_mutex_unlock(&port_mutex_table[index]);
    return 1;
}

int port__search(char *IP){
    int index = hash_value_port(IP);
    struct peer *p;
    pthread_mutex_lock(&port_mutex_table[index]);
    SLIST_FOREACH(p,&port_table[index],next_peer){
        if(!strcmp(p->IP,IP)){
            pthread_mutex_unlock(&port_mutex_table[index]);
            return p->port;
        }
    }
    pthread_mutex_unlock(&port_mutex_table[index]);
    return -1;
}

void port__table_init(){
    for(int i = 0; i<PORT_TABLE_LENGTH;i++){
        SLIST_INIT(&port_table[i]);
        pthread_mutex_init(&port_mutex_table[i],NULL);
    }
}

void port__print(){
    for(int i = 0; i<PORT_TABLE_LENGTH;i++){
        pthread_mutex_lock(&port_mutex_table[i]);
        struct peer *p;
        SLIST_FOREACH(p,&port_table[i],next_peer)
            printf("\ni:%d | ip=%s:port=%d",i,p->IP,p->port);
        pthread_mutex_unlock(&port_mutex_table[i]);
    }
    printf("\n");
}

void port__table_end(){
    for(int i = 0; i<PORT_TABLE_LENGTH; i++){
        struct peer *p;
        SLIST_FOREACH(p,&port_table[i],next_peer){
            free(p->IP);
            free(p);
        }
        pthread_mutex_destroy(&port_mutex_table[i]);
    }
}
