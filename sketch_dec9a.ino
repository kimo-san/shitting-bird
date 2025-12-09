#include "Arduino.h" 
#include "Pins.h"
#include "Components.h"
#include "Program.h"

Pins pins = Pins(8, 11, 9);
Components comp = Components(pins);

String serial_text = "";
void updateText()
{
  String newInput = Serial.readStringUntil('\n');
  if (newInput != "") serial_text = newInput;
  serial_text.trim();
}

bool stopExecution()
{

  updateText();
  
  Serial.println("-> current text: " + serial_text);

  bool result = serial_text.equalsIgnoreCase("c");
  if (result) {
    pins.cancelAll();
    Serial.println("-> Stopped!");
  }
  return result;

}


void setup()
{

  pins.setup();
  comp.makeCancellable(stopExecution);
  Serial.begin(9600);
  Serial.setTimeout(10);

}

void loop()
{

  updateText();
  Serial.println(serial_text);

  if (serial_text.equalsIgnoreCase("d"))
  {
    Serial.println("-> Executing!");
    program(comp, stopExecution);
    Serial.println("-> Execution finished.");
    serial_text = "";
  }
}
