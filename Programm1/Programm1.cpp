#include <iostream>
#include <string>
#include <mutex>
#include <condition_variable>
#include <algorithm>
#include <vector>
#include <thread>
#include <cctype>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <cstring>

const int RECONNECT_DELAY_MS = 5000; // Задержка между попытками подключения в миллисекундах

using namespace std;
const int PORT = 8080;

class Buffer {
private:
    string buffer;
    bool newDataAvailable = false;
    bool exitFlag = false;
    mutex mtx;
    condition_variable cv;

public:
    void set(const string& data) {
        unique_lock<mutex> lock(mtx);
        buffer = data;
        newDataAvailable = true;
        cout << "Buffer: " << buffer << endl;
        cout << endl;
        cv.notify_one(); // Уведомляем один поток, что данные доступны
    }   

    string get() {
        unique_lock<mutex> lock(mtx);
        cv.wait(lock, [this] { return newDataAvailable || exitFlag; });
        if (exitFlag && !newDataAvailable) return ""; // Если установлен флаг завершения и нет данных, возвращаем пустую строку
        // Сохраняем данные из буфера и очищаем буфер
        string data = buffer;
        buffer.clear(); // Очистка буфера
        newDataAvailable = false;
        cout << "Buffer: " << buffer << endl;
        return data;
    }

    void setExitFlag() {
        unique_lock<mutex> lock(mtx);
        exitFlag = true;
        cv.notify_all(); // Уведомляем все потоки, чтобы они могли завершиться
    }

    bool getExitFlag() {
        unique_lock<mutex> lock(mtx);
        return exitFlag;
    }
};

void thread_1(const string& message, Buffer& bufferObj) {
    cout << "Received message: " << message << endl;
    if (message.length() < 64) {

        for (char c : message) {
            if (!isdigit(c)) {
                cout << "The message consists of more than just numbers!" << endl;
                bufferObj.setExitFlag(); // Устанавливаем флаг завершения работы
                return;
            }
        }

        vector<int> digits;
        for (char c : message) {
           digits.push_back(c - '0');
        }

        sort(digits.rbegin(), digits.rend());

        string result;
        for (int digit : digits) {
            if (digit % 2 == 0) {
                result += "KB";
            } else {
                result += to_string(digit);
            }
        }

        cout << "Result string: " << result << endl;
        bufferObj.set(result);
        
    }else{
        cout << "Received message > 64 characters" << endl;
        bufferObj.setExitFlag(); // Устанавливаем флаг завершения работы
        return;
    }
}

void thread_2(Buffer& bufferObj) {
    while (true) {
        string line = bufferObj.get();
        if (line.empty() && bufferObj.getExitFlag()){
            cout << "line.empty(): " << line.empty() << " bufferObj.getExitFlag(): " <<  bufferObj.getExitFlag() << endl;
            break; // Прерываем цикл, если получена пустая строка и установлен флаг завершения
        }

        cout << "Result line: " << line << endl;
        if (!line.empty()) {
            int sum = 0;
            for (char c : line) {
                if (isdigit(c)) {
                    sum += (c - '0');
                }
            }

            cout << "Sum of numeric values: " << sum << endl;
           
            int sock = -1;
            struct sockaddr_in servAddr;
            char buffer[1024] = {0};

            while (true) {
                // Создание сокета
                sock = socket(AF_INET, SOCK_STREAM, 0);
                if (sock == -1) {
                    perror("Socket creation failed");
                    this_thread::sleep_for(chrono::milliseconds(RECONNECT_DELAY_MS));
                    continue; // Повторить попытку
                }

                servAddr.sin_family = AF_INET;
                servAddr.sin_port = htons(PORT);
                servAddr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Используем inet_addr

                // Подключение к серверу
                if (connect(sock, (struct sockaddr*)&servAddr, sizeof(servAddr)) == -1) {
                    perror("Connect failed");
                    close(sock);
                    this_thread::sleep_for(chrono::milliseconds(RECONNECT_DELAY_MS));
                    continue; // Повторить попытку
                }

                // Отправка данных
                snprintf(buffer, sizeof(buffer), "%d", sum);
                send(sock, buffer, strlen(buffer), 0);
                cout << "Sum sent: " << buffer << endl;

                // Закрытие сокета
                close(sock);
                break; // Успешное завершение цикла
            }
        }
    }
}

int main() {
    setlocale(LC_ALL, "ru");
    Buffer bufferObj;
    string msg;
    cout << "Enter a string of numbers (max. 64 characters) or 'exit' to exit: ";
    thread t2(thread_2, ref(bufferObj)); // Запускаем второй поток
    while (true) {
        //cout << "Enter a string of numbers (max. 64 characters) or 'exit' to exit: ";
        cin >> msg;
        cout << endl;

        if (msg == "exit") {
            bufferObj.setExitFlag();
            break;
        }
        thread t1(thread_1, msg, ref(bufferObj)); // Обрабатываем ввод пользователя
        t1.join();

        if(bufferObj.getExitFlag())break;
    }
    t2.join(); // Ждем завершения второго потока

    return 0;
}
