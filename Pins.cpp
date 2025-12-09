#include "Pins.h"
#include "Arduino.h" 

Pins::Pins(
  int WTR, int PDR, int MXR
): WTR(WTR), PDR(PDR), MXR(MXR)
{
  // empty block
}

void Pins::setup()
{

  pinMode(WTR, OUTPUT);
  pinMode(MXR, OUTPUT);
  pinMode(PDR, OUTPUT);

}

void Pins::cancelAll()
{

  digitalWrite(WTR, LOW);
  digitalWrite(MXR, LOW);
  digitalWrite(PDR, LOW);

}