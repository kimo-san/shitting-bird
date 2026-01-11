#include <Arduino.h>
#include <Consts.h>
#include <Components.h>
#include <Pins.h>

static void runEmptyTest() {
  
  pinMode(ALWAYS_ON_PIN, OUTPUT);
  digitalWrite(ALWAYS_ON_PIN, HIGH);
  
}

static void runCheckPinsTest(Components& comps)
{

  Pins pins = comps.getPins();

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
// 1. Измерить обьем пролитой воды за время исполнения этого теста в МИЛЛИЛИТРАХ.
// 2. Рассчитать за следующей формулой: (обьем воды) / 5000.
// 3. Внести результат вычисления в файл `Consts.h` на место переменной `pump_speed`.
static void runWaterFlowTest(Components& comps)
{
  
  Pins pins = comps.getPins();

  pinMode(pins.WTR, OUTPUT);
  digitalWrite(pins.WTR, HIGH);
  delay(5000);
  digitalWrite(pins.WTR, LOW);

}



#include "Program.h"

// Единичное проигрывание программы.
static void runProgramTest(Components& comps)
{

  Pins pins = comps.getPins();

  pins.setup();
  pins.cancelAll();
  Serial.println("Running program!");

  execute_program(comps);

}