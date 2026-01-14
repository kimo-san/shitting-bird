#ifndef LISTENER_H
#define LISTENER_H

#include <HardwareFassade.h>
#include <Pins.h>

class Listener
{

private:
    String serial_text;
    Pins& pins;
public:
    Listener(Pins& pins);
    void updateText();
    bool gotText();
    String getText();
    bool listenCommands();
    void reset();


};

#endif