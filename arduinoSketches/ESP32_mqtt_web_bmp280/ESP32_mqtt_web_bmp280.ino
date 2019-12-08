/*********
  Rui Santos
  Complete project details at https://randomnerdtutorials.com
*********/

#include <WiFi.h>
#include <PubSubClient.h>
#include <Wire.h>
#include <Adafruit_BME280.h>
#include <Adafruit_Sensor.h>

// Durham height above sea level in meters
#define SEALEVELPRESSURE_HPA (981)
// SSID/Password
const char* ssid = "Parsons";
const char* password = "W3SCh8ll3ng3";
// MQTT Broker IP address
const char* mqtt_server = "10.3.141.1";
Adafruit_BME280 bme; // I2C

WiFiServer wifiServer(80);
WiFiClient wifiClient;
PubSubClient client(wifiClient);
long lastMsg = 0;
char msg[50];
int value = 0;
String local_ip;
// LED Pin
const int ledPin = 2;
String humidity_topic;
String temperature_topic;
byte mac[6];
String header;

void setup() {
  Serial.begin(115200);
  // Get MAC address
  WiFi.macAddress(mac);
  // Set topics up
  humidity_topic =  "sensor/esp32/bmp280/" +   mac2String(mac) + "/humidity";
  temperature_topic =  "sensor/esp32/bmp280/" + mac2String(mac) + "/temperature";
  if (!bme.begin(0x76)) {
    Serial.println("Could not find a valid BME280 sensor, check wiring!");
    while (1);
  }
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  pinMode(ledPin, OUTPUT);
}

void loop() {
  wifiClient = wifiServer.available();
  //  reconnect();
  //  client.loop();
  long now = millis();
  // Temperature in Celsius
  float temperature = bme.readTemperature();
  float humidity = bme.readHumidity();
  float pressure = bme.readPressure();
  float altitude = bme.readAltitude(SEALEVELPRESSURE_HPA);
  if (wifiClient) {
    // Serve web page
    servePage(wifiClient);
  }

    if (now - lastMsg > 5000) {
      lastMsg = now;
  
      char humString[8];
      dtostrf(humidity, 1, 2, humString);
      char tempString[8];
      dtostrf(temperature, 1, 2, tempString);
      publishMQTT(temperature_topic, tempString);
      publishMQTT(humidity_topic, humString);
    }
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      // Subscribe
      client.subscribe("esp32/output");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void setup_wifi() {
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);
  WiFi.config(IPAddress(10, 3, 141, 5), IPAddress(10, 3, 141, 1), IPAddress(255, 255, 255, 0));
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  local_ip = WiFi.localIP().toString();
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(local_ip);
  wifiServer.begin();
}

void callback(char* topic, byte * message, unsigned int length) {
  String messageTemp = byteArrayToString(message, length);
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  Serial.print(messageTemp);
  Serial.println();

  // If a message is received on the topic esp32/output, you check if the message is either "on" or "off".
  // Changes the output state according to the message
  if (String(topic) == "esp32/output") {
    Serial.print("Changing output to ");
    if (messageTemp == "on") {
      Serial.println("on");
      digitalWrite(ledPin, HIGH);
    }
    else if (messageTemp == "off") {
      Serial.println("off");
      digitalWrite(ledPin, LOW);
    }
  }
}

void publishMQTT(String topicString, char payload[8]) {
  unsigned int length = topicString.length() + 1;
  char topic[length];
  topicString.toCharArray(topic, length);
  Serial.print(topic);
  Serial.print(": ");
  Serial.println(payload);
  client.publish(topic, payload, true);
}

String mac2String(byte ar[]) {
  String s;
  for (byte i = 0; i < 6; ++i)  {
    char buf[3];
    sprintf(buf, "%2X", ar[i]);
    s += buf;
    if (i < 5) s += ':';
  }
  return s;
}

String byteArrayToString(byte * byteArray, unsigned int length) {
  String messageTemp;
  for (int i = 0; i < length; i++) {
    messageTemp += (char)byteArray[i];
  }
  return messageTemp;
}


void servePage(WiFiClient wifiClient) {
  Serial.println("New Client.");          // print a message out in the serial port
  String currentLine = "";                // make a String to hold incoming data from the client
  while (wifiClient.connected()) {            // loop while the client's connected
    if (wifiClient.available()) {             // if there's bytes to read from the client,
      char c = wifiClient.read();             // read a byte, then
      Serial.write(c);                    // print it out the serial monitor
      header += c;
      if (c == '\n') {                    // if the byte is a newline character
        // if the current line is blank, you got two newline characters in a row.
        // that's the end of the client HTTP request, so send a response:
        if (currentLine.length() == 0) {
          wifiClient.println("HTTP/1.1 200 OK");
          wifiClient.println("Content-type:text/html");
          wifiClient.println("Connection: close");
          wifiClient.println();

          // Display the HTML web page
          wifiClient.println("<!DOCTYPE html><html>");
          wifiClient.println("<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
          wifiClient.println("<link rel=\"icon\" href=\"data:,\">");
          // CSS to style the table
          wifiClient.println("<style>body { text-align: center; font-family: \"Trebuchet MS\", Arial;}");
          wifiClient.println("table { border-collapse: collapse; width:35%; margin-left:auto; margin-right:auto; }");
          wifiClient.println("th { padding: 12px; background-color: #0043af; color: white; }");
          wifiClient.println("tr { border: 1px solid #ddd; padding: 12px; }");
          wifiClient.println("tr:hover { background-color: #bcbcbc; }");
          wifiClient.println("td { border: none; padding: 12px; }");
          wifiClient.println(".sensor { color:white; font-weight: bold; background-color: #bcbcbc; padding: 1px; }");

          // Web Page Heading
          wifiClient.println("</style></head><body><h1>ESP32 with BME280</h1>");
          wifiClient.println("<table><tr><th>MEASUREMENT</th><th>VALUE</th></tr>");
          wifiClient.println("<tr><td>Temp. Celsius</td><td><span class=\"sensor\">");
          wifiClient.println(bme.readTemperature());
          wifiClient.println(" *C</span></td></tr>");
          wifiClient.println("<tr><td>Temp. Fahrenheit</td><td><span class=\"sensor\">");
          wifiClient.println(1.8 * bme.readTemperature() + 32);
          wifiClient.println(" *F</span></td></tr>");
          wifiClient.println("<tr><td>Pressure</td><td><span class=\"sensor\">");
          wifiClient.println(bme.readPressure() / 100.0F);
          wifiClient.println(" hPa</span></td></tr>");
          wifiClient.println("<tr><td>Approx. Altitude</td><td><span class=\"sensor\">");
          wifiClient.println(bme.readAltitude(SEALEVELPRESSURE_HPA));
          wifiClient.println(" m</span></td></tr>");
          wifiClient.println("<tr><td>Humidity</td><td><span class=\"sensor\">");
          wifiClient.println(bme.readHumidity());
          wifiClient.println(" %</span></td></tr>");
          wifiClient.println("</body></html>");

          // The HTTP response ends with another blank line
          wifiClient.println();
          header = "";
          // Close the connection
          wifiClient.stop();
          Serial.println("Client disconnected.");
          Serial.println("");
          break;
        } else { // if you got a newline, then clear currentLine
          currentLine = "";
        }
      } else if (c != '\r') {  // if you got anything else but a carriage return character,
        currentLine += c;      // add it to the end of the currentLine
      }
    }
  }
}
