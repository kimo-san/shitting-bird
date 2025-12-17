#ifndef Program_h
#define Program_h
#include <Components.h>
#include <HardwareFassade.h>

static void execute_program(Components &comp)
{

  /// проверка на отмену программы

  bool (*cancelCheck)() = comp.getCancelCheck();

  auto yield = [&]() -> bool { 
    if (cancelCheck()) return true;
    wait(100);
    if (cancelCheck()) return true;
    return false;
  };

  /////

  if (yield()) return;
    
  comp.addPowder(500);

  if (yield()) return;

  comp.addWater(7);
    
  if (yield()) return;
    
  comp.addPowder(500);
    
  if (yield()) return;
    
  comp.mix(5);

  if (yield()) return;
    
  comp.addWater(7);

  if (yield()) return;
    
  comp.mix(10);

  if (yield()) return;

  comp.shitOut();

}

#endif