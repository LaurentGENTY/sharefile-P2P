/**
 * @file port_table.h
 * Port table structures and methods in order to store PORT owned by the IP
 */
#ifndef PORT_TABLE
#define PORT_TABLE
#include <pthread.h>
#include "queue.h"

/**
* @def PORT_TABLE_LENGTH port_table.h
*/
#define PORT_TABLE_LENGTH 10000

/**
* @struct peer port_table.h
*/
struct peer {
    char* IP; /** IP adress of the peer of the file */
    int port; /** port of the peer of the file */
    SLIST_ENTRY(peer) next_peer; /** next_peer for queue bsd lib*/
};

/**
* @def port_table port_table.h
*/
SLIST_HEAD(,peer) port_table[PORT_TABLE_LENGTH];

/**
* @def port_mutex_table port_table.h
*/
pthread_mutex_t port_mutex_table[PORT_TABLE_LENGTH];

/**
 * Add a peer in the port_table depending of the IP
 * @param IP key of the port_table
 * @param port port of the peer
 *
 * @return an integer 1 if it's a success and 0 otherwise
* */
int port__add(char* IP, int port);

/**
 * Search a peer in the port_table depending of the IP
 * @param IP key of the port table
 *
 * @return the port found and -1 otherwise
 * */
int port__search(char* IP);

/**
 * Initialize port_table
 * @return port_table variable initialized
 * */
void port__table_init();

/**
 * Print all the files in port_table (used in debug)
 * */
void port__print();

/**
 * Free all table memory by freeing each elements inside
 * @return port_table freeed
 * */
void port__table_end();

#endif
