#ifndef HardwareFassade_h
#define HardwareFassade_h

#include "Arduino.h"
#include "Consts.h"

void wait(int millis);

class SerialFassade
{

  private:

    int timeout_USB = 10;
    int timeout_BTN = 100;
    int timeout_BLU = listenSerialTimeOut - timeout_USB - timeout_BTN;

  public:

    void setup();
    bool isButtonPressed();
    void println(const String& msg);
    void print(const String& msg);
    String readChars();

};

extern SerialFassade serial;

#endif