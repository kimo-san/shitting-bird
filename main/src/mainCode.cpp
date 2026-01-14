#ifndef MAIN_H
#define MAIN_H

#include <Pins.h>
#include <Components.h>
#include <Consts.h>
#include <HardwareFassade.h>
#include "CommandParser.h"
#include "Listener.h"

static Pins pins { WATER_PIN, POWDER_PIN, MIXER_PIN, CREAM_OUT_PIN, ALWAYS_ON_PIN };
static Components comp(pins);
static Listener listener(pins);
static CommandParser parser(comp);

static bool listenCheck() { return listener.listenCommands(); }

static void runMainSketch()
{

  pins.setup();
  pins.cancelAll();
  comp.makeCancellable(listenCheck);
  serial.setup();
  serial.println("Ready to use!");

  while (true)
  {
    
    listener.updateText();
    String buffer = listener.getText();

    if (listener.gotText())
    {
      serial.println("-> Parsing!");
      parser.parse(buffer);
      listener.reset();
    }
  }

}

#endif