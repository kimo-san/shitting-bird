#ifndef Pins_h
#define Pins_h

struct Pins {

  const int WTR; // water
  const int PDR; // powder
  const int MXR; // mixer
  const int CRM; // cream

  void setup();
  void cancelAll();

};

void turnOn(const int pin);

#endif