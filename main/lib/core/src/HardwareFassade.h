#ifndef HardwareFassade_h
#define HardwareFassade_h

#include <Arduino.h>
#include <Consts.h>

void wait(int millis);

class SerialFassade
{
  
  public:

    void setup();
    void println(const String& msg);
    void print(const String& msg);
    String readChars();


  private:
    int timeout_USB = 10;
    int timeout_BLU = listenSerialTimeOut - timeout_USB;

};

extern SerialFassade serial;

#endif