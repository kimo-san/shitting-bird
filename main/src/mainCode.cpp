#include <Pins.h>
#include <Components.h>
#include <Consts.h>
#include <HardwareFassade.h>
#include "Program.h"

static Pins pins { WATER_PIN, POWDER_PIN, MIXER_PIN, CREAM_OUT_PIN };
static Components comp(pins);

static String serial_text = "";
static void updateText()
{

  String newInput = serial.readChars();

  if (newInput != "" && newInput != serial_text) {
    serial_text = newInput;
    serial.println("-> Got text: " + serial_text);
  }

}

static bool listenCommands()
{

  updateText();

  bool isCancelled = serial_text.equalsIgnoreCase("c");
  if (isCancelled) {
    pins.cancelAll();
    serial.println((String)
      "-> Cancelled!" +
      "\n\t> current_ml = " + comp.getCurrentMl()
    );
  }
  
  return isCancelled;

}

static void runMainSketch()
{


  pins.setup();
  pins.cancelAll();
  comp.makeCancellable(listenCommands);
  serial.setup();
  serial.println("Ready to use!");


  String buff="";
  while (true)
  {
    
    updateText();

    if (serial_text.equalsIgnoreCase("d"))
    {
      serial.println("-> Executing!");
      execute_program(comp);
      serial.println("-> Execution finished.");
      serial_text = "";
    }
  
  }

}