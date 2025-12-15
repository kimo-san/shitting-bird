#include "Arduino.h"
#include <SoftwareSerial.h>
#include "HardwareFassade.h"
#include "Consts.h"

SoftwareSerial bluetooth(BLUETOOTH_RX_PIN, BLUETOOTH_TX_PIN);

bool SerialFassade::isButtonPressed()
{
  delay(timeout_BTN);
  return digitalRead(BUTTON_PIN) == HIGH;
};

void SerialFassade::setup()
{

  Serial.begin(9600);
  Serial.setTimeout(timeout_USB);

  bluetooth.begin(9600);
  bluetooth.setTimeout(timeout_BLU);

  println("AT+NAME" + BLUETOOTH_NAME);
  println("AT+PASS" + BLUETOOTH_PASS);

};

void SerialFassade::println(const String& msg)
{

  if (bluetooth.available() >= 0) {
    bluetooth.println(msg);
  }

  if (Serial.available() >= 0) {
    Serial.println(msg);
  }
};

void SerialFassade::print(const String& msg)
{

  if (bluetooth.available() >= 0) {
    bluetooth.print(msg);
  }

  if (Serial.available() >= 0) {
    Serial.print(msg);
  }
};

String SerialFassade::readChars()
{
  
  String text = "";
  
  long start = millis();
  while (millis() - start < timeout_BLU) {
    
    while (bluetooth.available() > 0) {
      char c = bluetooth.read();
      text += c;
    }
  }

  if (text == "") {
  
    if (Serial.available() > 0) {
      text = Serial.readString();
    } else {
      delay(timeout_USB);
    }
  }

  text.trim();
  
  return text;
};

void wait(int millis) { delay(millis); };

SerialFassade serial;