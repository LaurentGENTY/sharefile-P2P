#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <ifaddrs.h>
#include <string.h>
#include <ctype.h>

#include "utils.h"

/**
 * Check the condition and if the case is false, display the message and terminate the program
 * @param condition test to be verified
 * @param prefix message to display if there is the error case
 */
void exit_if(int condition, const char *prefix)
{
    if (condition) {
	if ( errno != 0 ) {
	    perror(prefix);
	}
	else {
	    fprintf( stderr, "%s\n", prefix );
	}
        exit(1);
    }
}

/**
 * Retrieve the command and fill the command buffer given in parameter
 * @param buffer buffer filled with all the 1024 bytes buffer g=iven by the peer
 * @param command empty buffer which will be filled
 * @return command buffer filled with the command if a command is recognized, not filled otherwise
 */
void get_command(char *buffer, char *command)
{
    char *p, space = ' ';
    int i;

    /* Returns the pointer of the first occurence of the separator */
    p = strchr(buffer, space);

    /* If there is no space in the command */
    if (p == NULL)
    {
        fprintf(stderr,"No \"%c\" found.\n", space);
        return;
    }

    /* Position of the first itération */
    i = p - buffer;

    /* Get the command */
    strncpy(command, &buffer[0], i);
    command[i] = '\0';

    /* to lower */
    int j = 0;
    while(command[j]) {
        command[j] = tolower(command[j]);
        j++;
    }

    return;
}

int isNumeric (const char * s)
{
    if (s == NULL || *s == '\0' || isspace(*s))
      return 0;
    char * p;
    strtod (s, &p);
    return *p == '\0';
}

void _log (int fd, char * log_msg, char * err_msg) {
    exit_if ( write(fd, log_msg, strlen(log_msg)) == -1, err_msg );
}
