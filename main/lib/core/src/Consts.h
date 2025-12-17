#ifndef Consts_h
#define Consts_h
#include <Arduino.h>

// PINS
const int WATER_PIN = 2; // Насос для чистой воды
const int POWDER_PIN = 3; // Вращающийся мотор для струса порошка
const int MIXER_PIN = 4; // Вращающийся мотор для перемешивания
const int CREAM_OUT_PIN = 5; // Насос для выкачивания сливок

const int BLUETOOTH_RX_PIN = 8; // Подключить к TX пину на блютуз модуле
const int BLUETOOTH_TX_PIN = 9; // Подключить к RX пину на блютуз модуле

// CONNECTIVITY
const String BLUETOOTH_NAME = "Kimo's shitter";
const String BLUETOOTH_PASS = "7216";

// MEASURES
const double pump_speed = 0.004; // Скорость насоса: 40ml/s
const int max_capacity = 20; // Вмещаемость стакана: 20ml
const int rotation_duration = 1000; // Время на один полный оборот моторчика

// UTIL
const int cancellationTimeOut = 500; // Максимальное время ожидания отмены операции
const int listenSerialTimeOut = 500; // Сколько времени будет занимать прослушка сериалов

#endif
