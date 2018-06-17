#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

const char* ssid     = "SSID";
const char* password = "PASSWORD";

const char* host = "test966996.000webhostapp.com";

const int led1Pin = 13;
const int led2Pin = 14;
const int communicationWithArduinoPin = 16; // pin used to receive information from arduino (if =HIGH then arduino detected something... movement or vibration) 
const int buzzer = 12; //buzzer to arduino pin 12

boolean isAlarmOn;
boolean isPlaying;
boolean notified;

void setup() {
  Serial.begin(115200);
  pinMode(led1Pin, OUTPUT);
  pinMode(led2Pin, OUTPUT);
  pinMode(buzzer, OUTPUT); // Set buzzer - pin 12 as an output
  pinMode(communicationWithArduinoPin, INPUT);
  delay(100);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  isAlarmOn = false;
  isPlaying = false;
  notified = false;
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.print("Netmask: ");
  Serial.println(WiFi.subnetMask());
  Serial.print("Gateway: ");
  Serial.println(WiFi.gatewayIP());
}

int value = 0;

void loop() {
  delay(5000);
  ++value;

  Serial.print("connecting to ");
  Serial.println(host);
  
  // Use WiFiClient class to create TCP connections
  WiFiClient client;
  const int httpPort = 80;
  if (!client.connect(host, httpPort)) {
    Serial.println("connection failed");
    return;
  }
  
  // We now create a URI for the request
  String url = "/api/arduino.php";
  Serial.print("Requesting URL: ");
  Serial.println(url);
  
  // This will send the request to the server
  client.print(String("GET ") + url + " HTTP/1.1\r\n" +
               "Host: " + host + "\r\n" + 
               "Connection: close\r\n\r\n");
  delay(500);
  
  // Read all the lines of the reply from server and print them to Serial
  while(client.available()){
    String line = client.readStringUntil('\r');
    Serial.print(line);

    if (line.indexOf("led1=on") > 0) {
     digitalWrite(led1Pin, HIGH);
    } else if (line.indexOf("led1=off") > 0) {
      digitalWrite(led1Pin, LOW);
    }

    if (line.indexOf("led2=on") > 0) {
     digitalWrite(led2Pin, HIGH);
    } else if (line.indexOf("led2=off") > 0) {
      digitalWrite(led2Pin, LOW);
    }

    
   if (line.indexOf("alarme=on") > 0 && !isAlarmOn) {
      if (!isAlarmOn) {
        notified = false;  
      }
      isAlarmOn = true;
    } else if (line.indexOf("alarme=off") > 0 && isAlarmOn) {
      if (isAlarmOn) {
        notified = false;  
      }
      isAlarmOn = false;
      isPlaying = false;
    }
     
  }

  int arduinoValue = digitalRead(communicationWithArduinoPin);
  if (arduinoValue == HIGH) {
    Serial.println("GOT HIGH FROM ARDUINO");
    isPlaying = true;  
  }

  if(isPlaying)
  Serial.println("isPlaying=true");
  else Serial.println("isPlaying=false");

  if(isAlarmOn)
  Serial.println("isAlarmOn=true");
  else Serial.println("isAlarmOn=false");
  
  if (isPlaying && isAlarmOn) { // check if arduino detected something and alarm is activated
      if (!notified) {
        HTTPClient http;
        http.begin("http://test966996.000webhostapp.com/api/notifications.php?areaEntrada=1");
        http.addHeader("Content-Type", "application/x-www-form-urlencoded");
        http.POST("mtd=secure");
        http.writeToStream(&Serial);
        http.end();  
        notified = true;
      }
      
      playBipSound(3, 1000);
  }
  
  Serial.println();
  Serial.println("closing connection");
}

void playBipSound(int numBips, int freq) {
    int currentBips = 0;

    while (currentBips < numBips) {
      tone(buzzer, freq); // Send "freq" Hz sound signal...
      delay(500);        // ...for 500ms
      noTone(buzzer);     // Stop sound...
      delay(500);        // ...for 500ms
      currentBips++;
    };
}
