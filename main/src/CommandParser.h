#ifndef CommandParser_H
#define CommandParser_H

#include <Arduino.h>
#include <HardwareFassade.h>
#include "Program.h"
#include "Scripts.h"
#include "Consts.h"

class CommandParser
{
private:
    Components& components;
public:
    CommandParser(Components& components);
    void parse(String &command);
};

inline void CommandParser::parse(String &command)
{

    if (command.equalsIgnoreCase(EXECUTE_CMD))
    {
        serial.println("-> Executing main program");
        execute_program(components);
    }
    else
    if (command.equalsIgnoreCase(WATER_TEST_CMD))
    {
        serial.println("-> Executing waterflow test");
        runWaterTest(components);
    }
    else
    if (command.equalsIgnoreCase(PIN_TEST_CMD))
    {
        serial.println("-> Executing pin test");
        runCheckPinsTest(components);
    }
    else
    if (command.equalsIgnoreCase(CREAM_TEST_CMD))
    {
        serial.println("-> Executing cream-out test");
        runCreamoutTest(components);
    }
    else
    if (command.equalsIgnoreCase(MIX_TEST_CMD)) 
    {
        serial.println("-> Executing mix test");
        runMixTest(components);
    }
    else
    if (command.equalsIgnoreCase(CLEAN_CMD))
    {
        serial.println("-> Cleaning device");
        runCleaning(components);
    }
    else
    {
        serial.println("-> Command unknown");
    }
    
    serial.println("-> Execution finished.");
}

inline CommandParser::CommandParser(Components &components):
components(components)
{
}

#endif