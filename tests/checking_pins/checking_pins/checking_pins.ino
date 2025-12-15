
/*

  Сюда вписываются пины отдельных устройств, на которые
  будет последовательно подаваться ток с контроллера
  
  Ожидаемое поведение: все детали работают последовательно.

*/

const int UNIVERSAL_PIN = 7; // Универсальный пин, всегда дает ток
const int size = 4;
const int pins[size] = { 2, 3, 4, 5 };

const int WATER_PIN = pins[0]; // Насос для чистой воды
const int POWDER_PIN = pins[1]; // Вращающийся мотор для струса порошка
const int MIXER_PIN = pins[2]; // Вращающийся мотор для перемешивания
const int CREAM_OUT_PIN = pins[3]; // Насос для выкачивания сливок


void setup() {

  Serial.begin(9600);
  
  pinMode(WATER_PIN, OUTPUT);
  pinMode(POWDER_PIN, OUTPUT);
  pinMode(MIXER_PIN, OUTPUT);
  pinMode(CREAM_OUT_PIN, OUTPUT);
  pinMode(UNIVERSAL_PIN, OUTPUT);

  digitalWrite(UNIVERSAL_PIN, HIGH);
  for (int i = 0; i < size; i++) {
    digitalWrite(pins[i], LOW);
  }
  
  Serial.println("Start work...");

}

void loop() {
  for (int i = 0; i < size; i++) {
    
    delay(1000);

    digitalWrite(pins[i], HIGH);
    Serial.print("Working pin: "); Serial.println(pins[i]);

    delay(2000);
    digitalWrite(pins[i], LOW);

  }
}
