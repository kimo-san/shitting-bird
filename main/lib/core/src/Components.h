#ifndef Components_h
#define Components_h

#include <Pins.h>

class Components
{

  private:
    Pins& pins;
    bool(*cancel_check)();
    double current_ml;


  public:

    Components(Pins& pins);

    void addWater(int ml_to_add);
    void addPowder(int rotation_times);
    void mix(int rotation_times);
    void shitOut();

    void makeCancellable(bool(*cancel_check)());
    double getCurrentMl();

    bool(*getCancelCheck())() { return cancel_check; }
    Pins getPins() { return pins; };

};


#endif
