#ifndef Components_h
#define Components_h

#include <Pins.h>

class Components
{

  private:
    Pins& pins;
    bool(*cancel_check)();
    double current_ml;
    double current_cream_time;


  public:

    Components(Pins& pins);

    // Перекачивает воду в центральную емкость.
    // Параметр: обьем воды, который выливается за раз.
    void addWater(int ml_to_add); 

    // Скидывает пудру в центральную емкость.
    // Параметр: время прокручивания мотора.
    void addPowder(int time); 

    // Перемешивает компоненты в центральной
    // емкости при помощи ротируещегося мотора.
    // Параметр: количество оборотов мотора.
    void mix(int rotation_times);

    // Викачивает все содержимое из центральной емкости.
    void shitOut();

    // Назначает промежуточную комманду, которая
    // может остановить исполнение комманды.
    void makeCancellable(bool(*cancel_check)());
    // Возвращает текущий обьем жидкости в центральной емкости.
    double getCurrentMl();
    // Возвращает текущий указатель на функцию
    // проверки на остановление программы.
    bool(*getCancelCheck())() { return cancel_check; }
    // Возвращает пины.
    Pins getPins() { return pins; };

};


#endif
