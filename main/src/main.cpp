#include <Arduino.h>
#include "mainCode.cpp"
#include "tests.h"


void setup() {

    //
    // Нужный режим - раскомментировать
    // Одновременно может быть загружен лишь один режим.
    //
    
    //runMainSketch();
    //runCheckPinsTest(comp);
    //runWaterFlowTest(comp);
    //runEmptyTest();
    runProgramTest(comp);
    
}

void loop() { /* This block will be never reached. */ }