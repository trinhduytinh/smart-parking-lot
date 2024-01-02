#include <Wire.h>//Gọi thư viện i2c
#include <LiquidCrystal_I2C.h>//Thư viện LCD I2C
#include <ESP8266WiFi.h> 
#include <NTPClient.h> 
#include <Servo.h> 
//Frirebase Library
#include <FirebaseArduino.h>

#include <WiFiUdp.h> 
#define WIFI_SSID "hi"
#define WIFI_PASSWORD "tinh1234"
String time1= "89";
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", 19800,60000);
//Frirebase Declare
#define FIREBASE_HOST "diary1311-59147-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "MxowcWWzD5sGZqJOCDGhS4zroFaAey5hzJCTS9J4"

//NTPClient timeClient(ntpUDP, "pool.ntp.org", 19800,60000);
LiquidCrystal_I2C lcd(0x27, 16, 2);//Khai báo địa chỉ i2c và LCD 16x2
Servo myservo;                          // servo dưới dạng cổng
Servo myservos;                         // servo dưới dạng cổng


//int slot3 = D5;                 
int slot2 = D2;
int slot1 = D1;
int carEnter = D6;
int count = 0; 
int CLOSE_ANGLE = 90;  // Góc đóng của cánh tay động cơ servo
int OPEN_ANGLE = 0;   // Góc mở của cánh tay động cơ servo         
int  hh, mm, ss;
int pos;
int pos1;
int cong1, cong2;
String h, m,EntryTimeSlot1,ExitTimeSlot1, EntryTimeSlot2,ExitTimeSlot2, EntryTimeSlot3,ExitTimeSlot3;
boolean entrysensor, exitsensor,s1,s2,s3;

String kt;
int s1_occupied = false;
int s2_occupied = false;
int s3_occupied = false;

WiFiClient client;

void setup() {
  Serial.begin(115200);
  Serial.print("connecting");
  timeClient.begin(); 
  myservo.attach(D0);      // servo pin to D0
  //myservos.attach(D7);       // servo pin to D7
//  pinMode(carExited, INPUT);    // ir as input
  pinMode(carEnter, INPUT);     // ir as input
  pinMode(slot1, INPUT);
  pinMode(slot2, INPUT);
//  pinMode(slot3, INPUT);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Wire.begin(D3, D4);
  lcd.init();
  lcd.clear();
  lcd.backlight();
  //code lcd
  lcd.setCursor(0,0);//Chọn vị trí con trỏ . 0 đầu là chọn cột (từ 0 - 16)
  lcd.print("Chao quy khach!");
}

void loop() {


 timeClient.setTimeOffset(+7*60*60);
 timeClient.update();
 hh = timeClient.getHours();
 mm = timeClient.getMinutes();
 ss = timeClient.getSeconds();
 h= String(hh); 
 m= String(mm);
 h +" :" + m;
 
 s1 = digitalRead(slot1);
 s2 = digitalRead(slot2);

 entrysensor = digitalRead(carEnter);

 //kiem tra cong co xe
 Serial.println(entrysensor);
   if (entrysensor == 0){
    myservo.write(OPEN_ANGLE);
    
   }
    
    
   if(entrysensor == 1){
    myservo.write(CLOSE_ANGLE);
   }
    
   


//bai giu xe
if(s1 == 0 && s1_occupied == true)
{
  Firebase.setString("/baixe/slot3", "1");
        //Serial.println("Occupied1 ");
        EntryTimeSlot1 =  h +" :" + m;
//        Serial.print("EntryTimeSlot1");
//        Serial.print(EntryTimeSlot1);
        Firebase.pushString("/time/cong3/thoigianvao", EntryTimeSlot1);
        count += 1;
        Firebase.setInt("/cong/soluong", count);
        s1_occupied = false;
        if (Firebase.failed()) 
    {
 
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error()); 
      return;
  }
}
if(s1 == 1 && s1_occupied == false)
{
        Firebase.setString("/baixe/slot3", "0");
//        Serial.println("Occupied1 ");
        ExitTimeSlot1 =  h +" :" + m;
//        Serial.print("ExitTimeSlot1");
//        Serial.print(ExitTimeSlot1);
        Firebase.pushString("/time/cong3/thoigianra", ExitTimeSlot1);
        count -= 1;
        Firebase.setInt("/cong/soluong", count);
        s1_occupied = true;
        if (Firebase.failed()) 
    {
 
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error()); 
      return;
  }
}
if(s2 == 0 && s2_occupied == true)
{
  Firebase.setString("/baixe/slot4", "1");
//        Serial.println("Occupied2 ");
        EntryTimeSlot2 =  h +" :" + m;
//        Serial.print("EntryTimeSlot2");
//        Serial.print(EntryTimeSlot2);
        Firebase.pushString("/time/cong4/thoigianvao", EntryTimeSlot2);
        count += 1;
        Firebase.setInt("/cong/soluong", count);
        s2_occupied = false;
        if (Firebase.failed()) 
    {
 
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error()); 
      return;
  }
}
if(s2 == 1&& s2_occupied == false)
{
        Firebase.setString("/baixe/slot4", "0");
//        Serial.println("Occupied2 ");
        ExitTimeSlot2 =  h +" :" + m;
//        Serial.print("ExitTimeSlot2");
//        Serial.print(ExitTimeSlot2);
        Firebase.pushString("/time/cong4/thoigianra", ExitTimeSlot2);
        count -= 1;
        Firebase.setInt("/cong/soluong", count);
        s2_occupied = true;
        if (Firebase.failed()) 
    {
 
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error()); 
      return;
  }
}

//lay so luong xe

//int temp = Firebase.getInt("/cong/soluong");
//
  lcd.setCursor(2,1);
  lcd.print("Cam on quy khach");
delay(500);
}
