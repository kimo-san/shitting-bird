#include <Arduino.h>
#include <Consts.h>
#include <Pins.h>




static void runCheckPinsTest(Pins pins)
{

  Serial.begin(9600);
  pins.setup();
  pins.cancelAll();
  
  int arr[4] { pins.WTR, pins.PDR, pins.MXR, pins.CRM };
  Serial.println("Start work...");

  while (true)
  {
    for (int i = 0; i < 4; i++) {

        delay(1000);
        digitalWrite(arr[i], HIGH);
        Serial.print("Working pin: "); Serial.println(arr[i]);
        delay(2000);
        digitalWrite(arr[i], LOW);
    }
  }
}



// При помощи этого скрипта измерялась скорость насоса.
static void runWaterFlowTest(Pins pins)
{

    pinMode(pins.WTR, OUTPUT);
    digitalWrite(pins.WTR, HIGH);
    delay(5000);
    digitalWrite(pins.WTR, LOW);

}



#include "program.h"
#include <Components.h>
static void runProgramTest(Components comps)
{

  Pins pins = comps.getPins();

  pins.setup();
  pins.cancelAll();
  Serial.println("Running program!");

  execute_program(comps);

}