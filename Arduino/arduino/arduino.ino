// these constants won't change:
const int knockSensor = A0; // the piezo is connected to analog pin 0
const int pir = 2;  

//piezo disk variables
const int threshold = 14;  // threshold value to decide when the detected sound is a knock or not

//pin for communication with wifi microcontroller
const int communicationPin = 11;

//pir variables
int val = 0; 
int pirState = LOW;  

// these variables will change:
int sensorReading = 0;      // variable to store the value read from the sensor pin

void setup() {
  pinMode(pir, INPUT);     // declare pir as input
  pinMode(communicationPin, OUTPUT);
  Serial.begin(9600);       // use the serial port
}

void loop() {
  // movement detector. code (PIR) below:

 val = digitalRead(pir);  // read pir value
  if (val == HIGH) {            // check if the input is HIGH
    if (pirState == LOW) {
      // we have just turned on
      digitalWrite(communicationPin, HIGH);
      delay(10000); // TEMPORARY!!
      Serial.println("Motion detected!");
      // We only want to print on the output change, not state
      pirState = HIGH;
    }
  } else {
    if (pirState == HIGH){
      // we have just turned of
      Serial.println("Motion ended!");
      // We only want to print on the output change, not state
      pirState = LOW;
    }
  }

  // motion detector. code (piezo disk) below:

  sensorReading = analogRead(knockSensor);
  Serial.println(sensorReading);

  if (pirState == LOW) { // if movement was detected previously, we don't need to check vibration
      // read the sensor and store it in the variable sensorReading:
      sensorReading = analogRead(knockSensor);
    
      // if the sensor reading is greater than the threshold:
      if (sensorReading > threshold) {
        digitalWrite(communicationPin, HIGH);
        delay(10000); // TEMPORARY!!
        Serial.println("Knock!");
      } else {
        digitalWrite(communicationPin, LOW);
      }
      
      delay(100);  // delay to avoid overloading the serial port buffer
  }
  
}

