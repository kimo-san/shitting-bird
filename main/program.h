#include "Components.h"

static const int delay_time = 100;

void program(
  Components &comp,
  bool (*cancelCheck)()
  )
{

  for (int i = 0; i < 4; i++)
  {

    if (cancelCheck()) return;
    delay(delay_time);
    if (cancelCheck()) return;
    
    comp.addWater(7);
    
    if (cancelCheck()) return;
    delay(delay_time);
    if (cancelCheck()) return;
    
    comp.addPowder(1);
    
    if (cancelCheck()) return;
    delay(delay_time);
    if (cancelCheck()) return;
    
    comp.mix(5);
    
  }

  if (cancelCheck()) return;

  comp.shitOut();

}
