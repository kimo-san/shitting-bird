# Shitter Communicator (Android)

![App logo](app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp)

Приложение-компаньон для птичьей машины. Позволяет запускать и отслеживать режимы через блютуз.

## Фичи
- Сканированние устройств поблизости
- Подключение через BLE (Bluetooth Low Energy)
- Удобные кнопки для запуска комманд
- Чтение сообщений от устройства

## Добавление своих комманд
Для добавления своего нового режима в приложение:
1. Добавьте новый режим в `/domain/Commands.kt`.
2. Запишите комманду для контроллера в функцию, лежащую в `/data/CommandMapper.kt`.
3. По желанию, дополните маппер `/presentation/DisplayCommandName.kt` для более корректного отображения названия комманды во фронтэнде.
> Искать в `android_app/app/src/main/java/com/qymoy/ShitterCommunicator/`

## Авторство
> Используемая фотография [этого прекрасного щегола](app/src/main/res/drawable/bird_by_holger_uwe_schmitt.jpg) была снята Holger Uwe Schmitt под лицензией CC BY-SA 4.0 и опубликована на сайте de.wikipedia.org.