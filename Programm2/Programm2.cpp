#include <algorithm>
#include <iostream>
#include <string>
#include <cctype>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <cstring>

using namespace std;

const int PORT = 8080;

bool isNumeric(const string& str) {
    return !str.empty() && all_of(str.begin(), str.end(), ::isdigit);
}

void handleClient(int clientSock) {
    char buffer[1024] = {0};
    int bytesRead = recv(clientSock, buffer, sizeof(buffer) - 1, 0);

    if (bytesRead > 0) {
        buffer[bytesRead] = '\0';
        string receivedData(buffer);

        cout << "Received data: " << receivedData << endl;

        // Проверка, что данные являются числовыми
        if (isNumeric(receivedData)) {
            if (receivedData.length() > 2) {
                int sum = stoi(receivedData);
                if (sum % 32 == 0) {
                    cout << "Received value is multiple of 32." << endl;
                } else {
                    cout << "Error: Received value is not multiple of 32." << endl;
                }
            } else {
                cout << "Error: Received value length is less than 2 characters." << endl;
            }
        } else {
            cout << "Error: Received data is not numeric." << endl;
        }
    } else if (bytesRead == 0) {
        cout << "Client disconnected." << endl;
    } else {
        cout << "Error receiving data." << endl;
    }
}

int main() {
    int serverSock = -1, clientSock;
    struct sockaddr_in servAddr, clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);

    // Создание сокета
    serverSock = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSock == -1) {
        perror("Socket creation failed");
        return 1;
    }

    servAddr.sin_family = AF_INET;
    servAddr.sin_addr.s_addr = INADDR_ANY;
    servAddr.sin_port = htons(PORT);

    // Привязка сокета
    if (bind(serverSock, (struct sockaddr*)&servAddr, sizeof(servAddr)) == -1) {
        perror("Bind failed");
        close(serverSock);
        return 1;
    }

    // Прослушивание входящих подключений
    if (listen(serverSock, SOMAXCONN) == -1) {
        perror("Listen failed");
        close(serverSock);
        return 1;
    }

    cout << "Waiting for connections..." << endl;

    // Ожидание подключений и обработка данных
    while (true) {
        clientSock = accept(serverSock, (struct sockaddr*)&clientAddr, &clientAddrLen);
        if (clientSock == -1) {
            perror("Accept failed");
            close(serverSock);
            return 1;
        }

        cout << "Client connected." << endl;
        handleClient(clientSock);
        close(clientSock);
    }

    close(serverSock);
    return 0;
}
