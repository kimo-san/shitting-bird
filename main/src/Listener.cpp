#include "Listener.h"


Listener::Listener(Pins& pins):
pins(pins),
serial_text("")
{
}

void Listener::updateText()
{

  String newInput = serial.readChars();
  newInput.trim();

  if (newInput != "" && newInput != serial_text) {
    serial_text = newInput;
    serial.println("-> Got text: " + serial_text);
  }

}

bool Listener::gotText()
{
    return serial_text != "";
}

String Listener::getText()
{
    return serial_text;
}

bool Listener::listenCommands()
{

  updateText();

  bool isCancelled = serial_text.equalsIgnoreCase(CANCEL_CMD);
  if (isCancelled)
  {
    pins.cancelAll();
    serial.println((String)
      "-> Cancelled!"
    );
  }
  
  return isCancelled;

}

void Listener::reset()
{
  serial_text = "";
}