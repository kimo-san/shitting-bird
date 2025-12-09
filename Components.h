#ifndef Components_h
#define Components_h

#include "Pins.h"

class Components
{

  public:

    Components(Pins pins);

    void addWater(int duration_millis);
    void addPowder(int rotation_times);
    void mix(int rotation_times);

    void makeCancellable(bool(*cancel_check)());


  private:
    Pins pins;
    bool(*cancel_check)();
};


#endif