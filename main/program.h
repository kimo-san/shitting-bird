#include "Components.h"
#include "HardwareFassade.h"

static const int delay_time = 100;

void execute_program(
  Components &comp,
  bool (*cancelCheck)()
  )
{

  for (int i = 0; i < 4; i++)
  {

    if (cancelCheck()) return;
    wait(delay_time);
    if (cancelCheck()) return;
    
    comp.addWater(7);
    
    if (cancelCheck()) return;
    wait(delay_time);
    if (cancelCheck()) return;
    
    comp.addPowder(1);
    
    if (cancelCheck()) return;
    wait(delay_time);
    if (cancelCheck()) return;
    
    comp.mix(5);
    
  }

  if (cancelCheck()) return;

  comp.shitOut();

}
