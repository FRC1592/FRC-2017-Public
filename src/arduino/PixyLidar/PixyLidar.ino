#include <SPI.h>
#include <Wire.h>
#include <Adafruit_NeoPixel.h>
#include <Pixy.h>

// Constants
#define DEBUG
#define USE_LIDAR
#define USE_PIXY
#define USE_LEDS

#define PIN_LIDAR_TRIG 2
#define PIN_LIDAR_MON 3
#define PIN_LEDS 9
#define PIN_STATUS 8

#define GEAR
#define LED_TIMEOUT 100
#define NUM_LEDS 40 //112

#define X_CENTER ((PIXY_MAX_X - PIXY_MIN_X + 1)/2)       
#define Y_CENTER ((PIXY_MAX_Y - PIXY_MIN_Y + 1)/2)

int dist, errP = 0;

//Objects
#ifdef USE_PIXY
  Pixy pixy;
  uint16_t blocks;
#endif

#ifdef USE_LEDS
  Adafruit_NeoPixel leds = Adafruit_NeoPixel(NUM_LEDS, PIN_LEDS, NEO_GRB + NEO_KHZ800);
  char inChar[5];
  boolean recv = false;
  int cnt = 0;
#endif


void setup()
{
  #ifdef GEAR
    Wire.begin(0x65);
  #else
    Wire.begin(0x67);
  #endif
  Wire.onRequest(requestEvent);

  Serial.begin(9600);
  #ifdef DEBUG
    Serial.println("Starting...");
  #endif

  #ifdef USE_LIDAR
    pinMode(PIN_LIDAR_TRIG, OUTPUT);
    pinMode(PIN_LIDAR_MON, INPUT);
    
    digitalWrite(PIN_LIDAR_TRIG, LOW);
  #endif

  #ifdef USE_PIXY
    pixy.init();
    #ifdef DEBUG
      Serial.println("Pixy Started...");
    #endif
  #endif

  #ifdef USE_LEDS
    Serial.setTimeout(LED_TIMEOUT);
    
    leds.begin();
    leds.show();
  #endif
  
  pinMode(PIN_STATUS, OUTPUT);
}

void loop()
{
  #ifdef USE_LIDAR
      dist = getDistance(PIN_LIDAR_MON);
  #endif

  #ifdef USE_PIXY
    blocks = pixy.getBlocks();

    if (blocks)
    {
      #ifdef GEAR
        if (blocks > 1)
        {
          // centered calculation errP = (pixy.blocks[0].x + pixy.blocks[1].x) / 2;
          // offset only look at left block
          if (pixy.blocks[0].x < pixy.blocks[1].x)
            errP = pixy.blocks[0].x;
          else
            errP = pixy.blocks[1].x;
        }
        else if (blocks==1)
        {
          //TODO this means only one tape on gear is being seen,
          //not sure we want to set the errP to x (the center of the tape)
          errP = pixy.blocks[0].x;
        }
      #else
        errP = pixy.blocks[0].x;
      #endif
      
//      validErrP();
    } else {
      errP = -1;
    }
  #endif

  #ifdef USE_LEDS
    if(Serial.available()) {
      digitalWrite(PIN_STATUS, HIGH);
      char in = Serial.read();

      if(in == '\n') {
        #ifdef DEBUG
          Serial.println("Got nl");
        #endif
        recv = false;
        cnt = 0;
      }

      if(recv) {
        #ifdef DEBUG
          Serial.print(cnt);
          Serial.print(": ");
          Serial.println(in);
        #endif
        if(cnt < 4) {
          inChar[cnt] = in;
          cnt++;
        } else {
          int s = inChar[0];
          int e = inChar[1];
          int r = inChar[2];
          int g = inChar[3];
          int b = inChar[4];
          for(int i=s; i<e; i++) {
            leds.setPixelColor(i, leds.Color(r, g, b));
          }
          leds.show();
          cnt = 0;
          recv = false;
          digitalWrite(PIN_STATUS, LOW);
        }
      }
      
      if(in == 'l' && recv == false) {
        #ifdef DEBUG
          Serial.println("Got l");
        #endif
        recv = true;
      }
    }
  #endif
    
  #ifdef DEBUG
    Serial.print("Lidar distance: ");
    Serial.print(dist);
    Serial.print(" Pixy err: ");
    Serial.println(errP);
  #endif
}

int getDistance(int pinNumber)
{
  int pulseWidth;
  int Distance;
  
  // Count how long the pulse is high in microseconds
  pulseWidth = pulseIn(pinNumber, HIGH);
  
  // 10usec = 1 cm of distance, then convert to inches
  if (pulseWidth != 0)
  {
    Distance = pulseWidth / 10 / 2.54;
  }
  return Distance;
}

void validErrP()
{
    if (errP < PIXY_MIN_X)
        errP = PIXY_MIN_X;
    if (errP > PIXY_MAX_X)
        errP = PIXY_MAX_X;
}

void requestEvent() {
  digitalWrite(PIN_STATUS, HIGH);
  byte buffer[4];
  buffer[0]=highByte(errP);
  buffer[1]=lowByte(errP);
  buffer[2]=highByte(dist);
  buffer[3]=lowByte(dist);

  Wire.write(buffer,4);
  digitalWrite(PIN_STATUS, LOW);
}
