/**
 * @file hash_table.h
 * Hash table structures and methods in order to store FILES owned by peers
 */
#ifndef HASH_TABLE
#define HASH_TABLE
#include <pthread.h>
#include "queue.h"

/**
* @def HASH_TABLE_LENGTH hash_table.h
*/
#define HASH_TABLE_LENGTH 10000

/**
* @struct owner hash_table.h
*/
struct seeder {
    char* IP; /** IP adress of the seeder of the file */
    int port; /** port of the seeder of the file */
    SLIST_ENTRY(seeder) next_seeder; /** next_seeder for queue bsd lib*/
};

/**
* @struct leecher hash_table.h
*/
struct leecher {
    char* IP; /** IP adress of the leecher of the file */
    int port; /** port of the leecher of the file */
    LIST_ENTRY(leecher) next_leecher; /** next_leecher for queue bsd lib*/
};

/**
 * @struct file hash_table.h
 */
struct file {
        char* key; /** pseudo-unique key of the file */
        char* name; /** filename */
        int length; /** total length of the file */
        int piecesize; /** size of the piece */

        int nb_seeders; /** total number of seeders */
        int nb_leechers; /** total number of leechers */
        SLIST_HEAD(,seeder) seeders; /** list of seeder of the file */
        LIST_HEAD(,leecher) leechers; /** list of leecher of the file */

        SLIST_ENTRY(file) next_file; /** next_file for queue bsd lib */
};

/**
* \var hash_table hash_table.h
*/
SLIST_HEAD(,file) hash_table[HASH_TABLE_LENGTH];

/**
* \var mutex_table hash_table.h
*/
pthread_mutex_t mutex_table[HASH_TABLE_LENGTH];

/**
 * Convert integer into string
 * @param val is the integer that need transformation
 * @param base is the base of the value
 *
 * @return the string corresponding the integer val
 * */
char* itoa(int val, int base);

/**
 * Initialize hash_table
 * @return hash_table variable initialized
 * */
void hash__table_init();

/**
 * Add a file in the hash_table with its key, its criterions and its owner
 * @param key unique key linked to the file
 * @param IP IP of the owner
 * @param port port where the owner listen to other peers
 * @param name filename
 * @param length total length of the file
 * @param piecesize size of each segment which will be downloaded between peers
 *
 * @return 1 if the file has been added correctly, 0 otherwise
 * */
int hash__add_seeder(char* key,char* IP, int port,char* name, int length, int piecesize);

/**
 * Add a leecher in the file with the IP and port
 * @param key the file with the key key
 * @param IP IP of the leecher
 * @param port port where the leecher listen to other peer
 *
 * @return 1 if the file has been added correctly, 0 otherwise
 * */
int hash__add_leecher(char* key, char* IP, int port);

/**
 * Search a file in the hash_table depending of the key but also on different possible
 * criterions such as the total size with (with different comparators)
 * @param key key in order to search for the file
 * @param f file which will be returned depending on the search
 *
 * @return 1 and the file in f if the file has been found, 0 and f NULL otherwise
 * */
struct file* hash__search(char* key);

/**
 * Search some files in the hash_table with the name name and size
 * criterions such as the total size with (with different comparators)
 * @param comp compare the size with the size of the file (comp is <,= or >)
 * @param name it's the name wanted
 * @param size it's the size we want more or less
 * @param files_found it's the data wanted
 *
 * @return an integer that correspoond of the number of file
 * */
int hash__getfiles(char* name, char compn, int size, char* files_found);

/**
 * Print all the files in hash_table (used in debug)
 * */
void hash__print();

/**
 * Print all the peers in hash_table
 * */
void hash__peer_print();

/**
 * Free all table memory by freeing each elements inside
 * @return hash_table freeed
 * */
void hash__table_end();

#endif
