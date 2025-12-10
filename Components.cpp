#include "Arduino.h"
#include "Components.h"
#include "Pins.h"
#include "Consts.h"

static int check_time() { return max(1, 100 - Serial.getTimeout()); }
static bool noncancellable() { return false; }

Components::Components(Pins& pins)
  :pins(pins), cancel_check(noncancellable), current_ml(0.0)
{
  // empty block
}

double Components::getCurrentMl() { return current_ml; }

void Components::makeCancellable(bool(*cancel_check)())
{
  this -> cancel_check = cancel_check;
}

void Components::addWater(int ml_to_add)
{
  pins.cancelAll();

  double estimated_ml = current_ml + ml_to_add;
  
  int ms_per_part = check_time();
  double ml_per_part = ms_per_part * pump_speed;

  Serial.println((String)
    "\nAdding water:" +
    "\n\t> max_capacity: " + max_capacity +
    "\n\t> current_ml: " + current_ml +
    "\n\t> estimated_ml: " + estimated_ml +
    "\n\t> ms_per_part: " + ms_per_part +
    "\n\t> ml_per_part: " + ml_per_part
  );

  turnOn(pins.WTR);

  while (
    estimated_ml > current_ml &&
    current_ml < max_capacity
    ) {
    current_ml += ml_per_part;
    if (cancel_check()) return;
    delay(ms_per_part);
  }

  if (current_ml > max_capacity) {
    Serial.println((String)
      "\n\t-> max_capacity was reached!"
    );
  }

  pins.cancelAll();
}

void Components::addPowder(int rotation_times)
{
  pins.cancelAll();

  int ms_per_part = check_time();

  int estimated_ms = rotation_times * rotation_duration;
  int current_ms = 0;

  Serial.println((String)
    "\nAdding powder:" +
    "\n\t> rotation_times: " + rotation_times +
    "\n\t> estimated_ms: " + estimated_ms +
    "\n\t> ms_per_part: " + ms_per_part
  );

  turnOn(pins.PDR);

  while (estimated_ms > current_ms)
  {
    if (cancel_check()) return;
    delay(ms_per_part);
    current_ms += ms_per_part;
  }

  pins.cancelAll();
}

void Components::mix(int rotation_times)
{
  pins.cancelAll();

  int ms_per_part = check_time();

  int estimated_ms = rotation_times * rotation_duration;
  int current_ms = 0;
  
  Serial.println((String)
    "\nMixing:" +
    "\n\t> rotation_times: " + rotation_times +
    "\n\t> estimated_ms: " + estimated_ms +
    "\n\t> ms_per_part: " + ms_per_part
  );

  turnOn(pins.MXR);

  while (estimated_ms > current_ms)
  {
    if (cancel_check()) return;
    current_ms += ms_per_part;
    delay(ms_per_part);
  }

  pins.cancelAll();
}

void Components::shitOut() {
  // todo
}


