#include "Arduino.h"
#include "Pins.h"

void Pins::setup()
{

  pinMode(WTR, OUTPUT);
  pinMode(MXR, OUTPUT);
  pinMode(PDR, OUTPUT);
  pinMode(CRM, OUTPUT);

}

void turnOn(int pin) {
  digitalWrite(pin, HIGH);
}

void Pins::cancelAll()
{

  digitalWrite(WTR, LOW);
  digitalWrite(MXR, LOW);
  digitalWrite(PDR, LOW);
  digitalWrite(CRM, LOW);

}
