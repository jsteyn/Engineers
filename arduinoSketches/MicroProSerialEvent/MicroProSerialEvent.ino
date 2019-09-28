#include <dht.h>
#include <Servo.h>

#define DHT11_PIN 3

String inputString = "";         // a String to hold incoming data
bool stringComplete = false;  // whether the string is complete
const int output5 = 5;
const int output4 = 4;
const int motor = 9;
dht DHT;
Servo myservo;
int pos = 0;
boolean open = true;

void setup() {
  // initialize serial:
  Serial.begin(9600);
  myservo.attach(motor);
  pinMode(output5, OUTPUT);
  pinMode(output4, OUTPUT);
  Serial.println("Enter ? for help");
  // reserve 200 bytes for the inputString:
  inputString.reserve(200);
  open = closeDoor(open);
}

void loop() {
  // print the string when a newline arrives:
  if (stringComplete) {
    // clear the string:
    stringComplete = false;
    if (inputString.charAt(0) == '?') {
      Serial.println("1 - Ligths (1) on");
      Serial.println("2 - Ligths (2) on");
      Serial.println("3 - Ligths (1) off");
      Serial.println("4 - Ligths (2) off");
      Serial.println("5 - Temperature");
      Serial.println("6 - Humidity");
      Serial.println("7 - Door open");
      Serial.println("8 - Door closing");
    }
    if (inputString.charAt(0) == '1') {
      //      Serial.println("Switch on 1"); // pin 5
      digitalWrite(output5, HIGH);
    }
    if (inputString.charAt(0) == '2') {
      //      Serial.println("Switch on 2"); // pin 4
      digitalWrite(output4, HIGH);
    }
    if (inputString.charAt(0) == '3') {
      //      Serial.println("Switch off 1");
      digitalWrite(output5, LOW);
    }
    if (inputString.charAt(0) == '4') {
      //      Serial.println("Switch off 2");
      digitalWrite(output4, LOW);
    }
    if (inputString.charAt(0) == '5') {
      int chk = DHT.read11(DHT11_PIN);
      Serial.print(DHT.temperature);
      Serial.println(" ºC");
    }
    if (inputString.charAt(0) == '6') {
      int chk = DHT.read11(DHT11_PIN);
      Serial.print(DHT.humidity);
      Serial.println("%");
    }
    if (inputString.charAt(0) == '8') {
      if (open) {
        open = closeDoor(open);
      }
    }
    if (inputString.charAt(0) == '7') {
      if (!open) {
        open = openDoor(open);
      }
    }
  }
  inputString = "";

  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag so the main loop can
    // do something about it:
    if (inChar == '\n') {
      stringComplete = true;
    }
  }
}

boolean closeDoor(boolean open) {
  Serial.println("Door closing ...");
  for (pos = 0; pos <= 90; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  return false;
}

boolean openDoor(boolean open) {
  Serial.println("Door opening ...");
  for (pos = 90; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  return true;
}
