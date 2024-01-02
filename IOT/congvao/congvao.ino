
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
String  h,m,EntryTimeSlot1,ExitTimeSlot1, EntryTimeSlot2,ExitTimeSlot2, EntryTimeSlot3,ExitTimeSlot3;
boolean entrysensor, exitsensor,s1,s2,s3;

String kt;
int s1_occupied = false;
int s2_occupied = false;
int s3_occupied = false;
String formattedDate;
String dayStamp;
String timeStamp;
WiFiClient client;

void setup() {
  Serial.begin(115200);
  Serial.print("connecting");
  timeClient.begin(); 
  myservo.attach(D0);      // servo pin to D0
  pinMode(carEnter, INPUT);     // ir as input
  pinMode(slot1, INPUT);
  pinMode(slot2, INPUT);
  
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

String a = "";
 timeClient.setTimeOffset(+7*60*60);
 timeClient.update();
formattedDate = timeClient.getFormattedDate();
// Extract date
int splitT = formattedDate.indexOf("T");
dayStamp = formattedDate.substring(0, splitT);
// Extract time
timeStamp = formattedDate.substring(splitT+1, formattedDate.length()-1);
 h= String(dayStamp); 
 m= String(timeStamp);
 //h +" - " + m;


 
 s1 = digitalRead(slot1);
 s2 = digitalRead(slot2);
 entrysensor = digitalRead(carEnter);

 //kiem tra cong co xe
 //Serial.println(entrysensor);
   if (entrysensor == 0) {
    //gui du lieu qua mt
   Firebase.setString("/flags/check", "true");
   //delay(3000);
   //neu kt bien hop le mo cua
   kt =  Firebase.getString("/flags/open");
   if(kt == String("true")){
    myservo.write(OPEN_ANGLE);
    }
    // Lấy giá trị
  Serial.print("cong vao: ");
  cong1 = Firebase.getInt("/cong/congvao");
  Serial.println(Firebase.getInt("/cong/congvao"));
   if (cong1 == 1) {                     // nếu cao thì đếm và gửi dữ liệu
   myservo.write(OPEN_ANGLE);

   }
   String temp1 = Firebase.getString("/baixe/slot1");
String temp2 = Firebase.getString("/baixe/slot2");
String temp3 = Firebase.getString("/baixe/slot3");
String temp4 = Firebase.getString("/baixe/slot4");
if(temp1 == String("0")){
  Serial.println(a);
  a +=1;
}
if(temp2 == String("0"))
      a +=2;
if(temp3 == String("0"))
  a +=3;
if(temp4 == String("0"))
  a +=4;
  Serial.println(a);
  lcd.setCursor(0,1);
  lcd.print("Trong:" + a);
  delay(2000);


//      lcd.setCursor(3,1);
//      lcd.print("Tình trạng: " + String(temp) + "/4");

   }
   if(entrysensor == 1)
   {
     lcd.setCursor(0,1);
    lcd.print("NHOM 8      ");
    delay(2000);
    myservo.write(CLOSE_ANGLE);
    Firebase.setString("/flags/check", "false");
    Firebase.setString("/flags/open", "false");
    Firebase.setInt("/cong/congvao", 0);
   }
   


//bai giu xe
if(s1 == 0 && s1_occupied == true)
{
  Firebase.setString("/baixe/slot1", "1");
        EntryTimeSlot1 =  h +" - " + m;
        Firebase.pushString("/time/cong1/thoigianvao", EntryTimeSlot1);
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
        Firebase.setString("/baixe/slot1", "0");
        ExitTimeSlot1 =  h +" - " + m;
        Firebase.pushString("/time/cong1/thoigianra", ExitTimeSlot1);
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
  Firebase.setString("/baixe/slot2", "1");
        EntryTimeSlot2 =  h +" - " + m;
        Firebase.pushString("/time/cong2/thoigianvao", EntryTimeSlot2);
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
        Firebase.setString("/baixe/slot2", "0");
        ExitTimeSlot2 =  h +" - " + m;
        Firebase.pushString("/time/cong2/thoigianra", ExitTimeSlot2);
        s2_occupied = true;
        if (Firebase.failed()) 
    {
 
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error()); 
      return;
  }
}

//lay so luong xe
//String temp1 = Firebase.getString("/baixe/slot1");
//String temp2 = Firebase.getString("/baixe/slot2");
//String temp3 = Firebase.getString("/baixe/slot3");
//String temp4 = Firebase.getString("/baixe/slot4");
//if(temp1 == String("0")){
//  Serial.println(a);
//  a +=1;
//}
//if(temp2 == String("0"))
//      a +=2;
//if(temp3 == String("0"))
//  a +=3;
//if(temp4 == String("0"))
//  a +=4;
//  Serial.println(a);
//  lcd.setCursor(0,1);
//  lcd.print("Trong:" + a);
delay(500);
}
