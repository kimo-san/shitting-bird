
#include <Arduino.h>
#include "mainCode.cpp"
#include "tests.h"


void setup() {

    //
    // Нужный режим - раскомментировать
    // Одновременно может быть загружен лишь один режим.
    //

    /*
        Главная программа
    */

    //runMainSketch();


    /*
        Тесты
    */
    
    //runCheckPinsTest(comp);
    //runWaterFlowTest(comp);
    runProgramTest(comp);
    
}

void loop() { }