#include "Components.h"

void program(
  Components comp,
  bool (*cancelCheck)()
)
{

  for (int i = 0; i < 4; i++)
  {
    if (cancelCheck()) return;

    comp.addWater(1000);
    
    if (cancelCheck()) return;
    
    delay(500);
    
    if (cancelCheck()) return;
    
    comp.addPowder(1);
    
    if (cancelCheck()) return;
    
    delay(500);
    
    if (cancelCheck()) return;
    
    comp.mix(5);
  }
} 