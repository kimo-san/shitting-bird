
#include <Arduino.h>
#include "mainCode.cpp"


void setup() {

    //
    // Нужный режим - раскомментировать
    // Одновременно может быть загружен лишь один режим.
    //
    
    runMainSketch();
    //runCheckPinsTest(comp);
    //runWaterinTest(comp);
    //... any other funktion from `tests.h`
    
}

void loop() { /* This block will be never reached. */ }