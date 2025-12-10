#include "Arduino.h"
#include "Pins.h"
#include "Components.h"
#include "Program.h"
#include "Consts.h"

Pins pins { WATER_PIN, POWDER_PIN, MIXER_PIN, CREAM_OUT_PIN };
Components comp(pins);

String serial_text = "";
void updateText()
{
  String newInput = Serial.readStringUntil('\n');
  newInput.trim();
  if (newInput != "" && newInput != serial_text) {
    serial_text = newInput;
    Serial.println("-> Got text: " + serial_text);
  }
  serial_text.trim();
}

bool listenCommands()
{

  updateText();

  bool isCancelled = serial_text.equalsIgnoreCase("c");
  if (isCancelled) {
    pins.cancelAll();
    Serial.println((String)
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
  Serial.begin(9600);
  Serial.setTimeout(listenTimeOut_USB);
  Serial.println("Ready to use!");

}

void loop()
{

  updateText();

  if (serial_text.equalsIgnoreCase("d"))
  {
    Serial.println("-> Executing!");
    program(comp, listenCommands);
    Serial.println("-> Execution finished.");
    serial_text = "";
  }
}
