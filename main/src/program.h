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
  const int mls = max_capacity;
  const int mss = powder_rotation_time_limit;

  comp.addPowder(mss);
  
  for (int i = 0; i < times; i++)
  {
    if (yield()) return;
    comp.mix(5);
    if (yield()) return;
    comp.addWater(mls / times);
  }
  
  if (yield()) return;

  comp.shitOut();

}

#endif