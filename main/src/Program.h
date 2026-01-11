#ifndef Program_h
#define Program_h
#include <Components.h>
#include <HardwareFassade.h>

static void execute_program(Components &comp)
{

  // проверка на отмену программы

  bool (*cancelCheck)() = comp.getCancelCheck();

  auto yield = [&]() -> bool { 
    if (cancelCheck()) return true;
    wait(100);
    if (cancelCheck()) return true;
    return false;
  };

  ////////////////////

  
  const int times = 3;

  comp.addPowder(powder_rotation_time_limit);
  
  for (int i = 0; i < times; i++)
  {
    if (yield()) return;
    comp.mix(3);
    if (yield()) return;
    comp.addWater(max_capacity / times);
  }
  
  if (yield()) return;
  
  comp.shitOut();

}

#endif