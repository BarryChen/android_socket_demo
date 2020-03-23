
#include <sys/socket.h>
#include <sys/un.h>
#include <stddef.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <cutils/sockets.h>
#include <pthread.h>
#define PATH "com.7sate.localsocket"

void * connectThread(void *arg);
int main(int argc, char *argv[])
{
    int ret ;
    int serverID = socket_local_server(PATH, ANDROID_SOCKET_NAMESPACE_ABSTRACT, SOCK_STREAM);
    if(serverID < 0){
        printf("socket_local_server failed :%d\n",serverID);
        return serverID;
    }
    int socketID;
    pthread_t tid;
    while((socketID= accept(serverID, NULL, NULL)) >=0){
        ret = pthread_create(&tid, NULL, connectThread, (void *)&socketID); 
        //connectThread(&socketID); 
        printf("server: while\n");
        if(ret != 0){
            printf("error create thread:%s\n",strerror(ret));
            exit(1);
        }
    }
    return ret;

}

void * connectThread(void *arg)
{
    int ret;
    int socketID = *(int *)arg;
    if(socketID < 0){
        printf("socketID is %d\n",socketID);
        return NULL;
    }
    char buf2[512] = {0};
    ret = read(socketID, buf2, sizeof(buf2));
    if(ret < 0){
        printf("recived failed\n");
        return NULL;
    }
    printf("c server recived: %s\n",buf2);
    char buffer[]  = {"this message from c server "};
    ret = write(socketID, buffer, strlen(buffer));
    if(ret < 0){
        printf("write failed\n");
        return NULL;
    }
    close(socketID);
    return NULL;

}