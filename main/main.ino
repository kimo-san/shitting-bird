#include "Pins.h"
#include "Components.h"
#include "Program.h"
#include "Consts.h"
#include "HardwareFassade.h"

Pins pins { WATER_PIN, POWDER_PIN, MIXER_PIN, CREAM_OUT_PIN };
Components comp(pins);

String serial_text = "";
void updateText()
{

  String newInput = serial.readChars();

  if (newInput != "" && newInput != serial_text) {
    serial_text = newInput;
    serial.println("-> Got text: " + serial_text);
  }

}

bool listenCommands()
{

  updateText();

  bool isCancelled = serial_text.equalsIgnoreCase("c") || serial.isButtonPressed();
  if (isCancelled) {
    pins.cancelAll();
    serial.println((String)
      "-> Cancelled!" +
      "\n\t> current_ml = " + comp.getCurrentMl()
    );
  }
  
  return isCancelled;

}


void setup()
{

  pins.setup();
  pins.cancelAll();
  comp.makeCancellable(listenCommands);
  serial.setup();
  serial.println("Ready to use!");

}

String buff="";
void loop()
{
  
  updateText();

  if (serial_text.equalsIgnoreCase("d") || serial.isButtonPressed())
  {
    serial.println("-> Executing!");
    execute_program(comp, listenCommands);
    serial.println("-> Execution finished.");
    serial_text = "";
  }

}
