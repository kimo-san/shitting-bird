#include "Components.h"
#include "Arduino.h" 
#include "Pins.h"


static const int rotation_duration = 1000;
static bool noncancellable() { return false; }

Components::Components(Pins pins)
  :pins(pins), cancel_check(noncancellable)
{
  // empty block
}

void Components::makeCancellable(bool(*cancel_check)())
{
  this -> cancel_check = cancel_check;
}

void Components::addPowder(int rotation_times)
{
  pins.cancelAll();
  int pin = pins.PDR;

  digitalWrite(pin, HIGH);

  for (int i = 0; i < rotation_times; i++)
  {
    if (cancel_check()) return;
    delay(rotation_duration);
  }

  pins.cancelAll();
}

void Components::addWater(int duration_millis)
{
  pins.cancelAll();
  int pin = pins.WTR;

  int part_duration = 100 - Serial.getTimeout();
  int parts = duration_millis / part_duration;

  digitalWrite(pin, HIGH);
  for (int i = 0; i < parts; i++) {
    if (cancel_check()) return;
    delay(part_duration);
  }

  pins.cancelAll();
}

void Components::mix(int rotation_times)
{
  pins.cancelAll();
  int pin = pins.MXR;

  digitalWrite(pin, HIGH);
  
  for (int i = 0; i < rotation_times; i++)
  {
    if (cancel_check()) return;
    delay(rotation_duration);
  }

  pins.cancelAll();
}


