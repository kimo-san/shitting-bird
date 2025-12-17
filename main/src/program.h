#ifndef Program_h
#define Program_h
#include <Components.h>
#include <HardwareFassade.h>

static void execute_program(Components &comp)
{

  bool (*cancelCheck)() = comp.getCancelCheck();

  auto yield = [cancelCheck] { 
    if (cancelCheck()) return;
    wait(100);
    if (cancelCheck()) return;
  };

  for (int i = 0; i < 2; i++)
  {
    
    yield();

    comp.addWater(7);
    
    yield();
    
    comp.addPowder(0.5);
    
    yield();
    
    comp.mix(5);

    yield();
    
    comp.addWater(7);

  }

  yield();

  comp.shitOut();

}

#endif