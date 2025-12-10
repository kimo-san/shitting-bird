#ifndef Consts_h
#define Consts_h

// PINS
const int WATER_PIN = 0; // Насос для чистой воды
const int POWDER_PIN = 3; // Вращающийся мотор для струса порошка
const int MIXER_PIN = 2; // Вращающийся мотор для перемешивания
const int CREAM_OUT_PIN = 1; // Насос для выкачивания сливок

// MEASURES
const int rotation_duration = 1000; // Один полный оборот моторчика
const double pump_speed = 0.004; // Скорость насоса: 40 ml/s
const int max_capacity = 20; // Вмещаемость стакана: 20ml

// UTIL
const int listenTimeOut_USB = 50;

#endif