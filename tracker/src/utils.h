/** 
 * @file utils.h
 * */

#ifndef UTILS_H
#define UTILS_H

/**
 * exit printing error prefixed by `prefix` if `condition` is true (non zero)
 */

/* default values for a file's fields in the hash table */
#define DEFAULT_FILE_NAME "-1"
#define DEFAULT_IP_ADDR "-1"
#define DEFAULT_PORT -1
#define DEFAULT_FILE_SIZE -1

/* various usefull stings to send to the socket */
#define OK "> ok"
#define NOK "> nok"
#define END "END"


void exit_if(int condition, const char *prefix);

void usage_commands(int socket);

void print_ip(unsigned int ip);
void show_local_ip(int portno);
void get_command(char *buffer, char *command);

int isNumeric (const char * s);


void _log (int fd, char * log_msg, char * err_msg);


#endif