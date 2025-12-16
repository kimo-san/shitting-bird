
#include <Arduino.h>
#include "mainCode.cpp"
#include "tests.h"

void setup() {


    /* MAIN PROGRAM */

    //runMainSketch();


    /* TESTS */
    
    //runCheckPinsTest(pins);
    //runWaterFlowTest();
    runProgramTest(comp);
    
}

void loop() { }