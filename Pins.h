#ifndef Pins_h
#define Pins_h

class Pins {

  public:

    int WTR; // water
    int PDR; // powder
    int MXR; // mixer

    Pins(
      int WTR,
      int PDR,
      int MXR
    );

    void setup();
    void cancelAll();
    
};


#endif