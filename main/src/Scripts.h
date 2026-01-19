#ifndef SCRIPTS_h
#define SCRIPTS_h

#include <Arduino.h>
#include <Consts.h>
#include <Components.h>
#include <Pins.h>

static void runCreamoutTest(Components& components)
{
  components.shitOut();
}

static void runMixTest(Components& components)
{
  components.mix(3);
}

static void runWaterTest(Components& components)
{
  components.addWater(max_capacity);
}

static void runCleaning(Components& components)
{
  int times = 5;

  while (times > 0)
  {
    times--;
    components.addWater(max_capacity);
    components.mix(2);
    components.shitOut();
  };

}

static void runCheckPinsTest(Components& components)
{

  Pins pins = components.getPins();

  Serial.begin(9600);
  pins.setup();
  pins.cancelAll();
  
  int arr[4] { pins.WTR, pins.PDR, pins.MXR, pins.CRM };
  Serial.println("Start work...");
  for (int i = 0; i < 4; i++) {
    delay(1000);
    digitalWrite(arr[i], HIGH);
    Serial.print("Working pin: ");
    Serial.println((String) arr[i]);
    delay(2000);
    digitalWrite(arr[i], LOW);
  }
}


// При помощи этого скрипта измерялась скорость насоса.
// 1. Измерить обьем пролитой воды за время исполнения этого теста в МИЛЛИЛИТРАХ.
// 2. Рассчитать за следующей формулой: (обьем воды) / значение переменной time.
// 3. Внести результат вычисления в файл `Consts.h` на место переменной `pump_speed`.
static void runWaterCalibrationTest(Components& components, int time = 2000)
{
  
  Pins pins = components.getPins();

  pinMode(pins.WTR, OUTPUT);
  digitalWrite(pins.WTR, HIGH);
  delay(time);
  digitalWrite(pins.WTR, LOW);

}

#endif