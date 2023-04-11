package week6;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

public class DHT11 {
    private static final int MAXTIMINGS  = 85;
    private final int[] dht11_dat   = { 0, 0, 0, 0, 0 };

    public DHT11() {
        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        GpioUtil.export(3, GpioUtil.DIRECTION_OUT);
    }

    public float[] getTemperature(final int pin) {
        int laststate = Gpio.HIGH; // signal 상태 변화를 알기 위해, 기존 상태를 기억.
        int j = 0; // 수신한 Bit의 index counter
        float h = -99; // 습도
        float c = -99; // 섭씨 온도
        float f = -99; // 화씨 온도
        
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
        // Integral RH, Decimal RH, Interal T, Decimal C
        
        // 1. DHT11 센서에게 start signal 전달.
        Gpio.pinMode(pin, Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.LOW);
        Gpio.delay(18);

        // 2. Pull-up -> 수신모드로 전환 -> 센서의 응답 대기
        Gpio.digitalWrite(pin, Gpio.HIGH);
        Gpio.pinMode(pin, Gpio.INPUT);
        
        // 3. 센서의 응답에 따른 동작
        for (int i = 0; i < MAXTIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(pin) == laststate) { // GPIO pin 상태가 바뀌면 진행
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }
            laststate = Gpio.digitalRead(pin);
            if (counter == 255) {
                break;
            }
            
            // 각각의 bit 데이터 저장
            if (i >= 4 && i % 2 == 0) { // 첫 3개의 bit는 무시, laststate 가 low에서 high 로 바뀔때만 값 저장
            	// data 저장
                dht11_dat[j / 8] <<= 1; // 0 bit 
                if (counter > 16) {
                    dht11_dat[j / 8] |= 1; // 1 bit
                }
                j++;
            }
        }
        
        // 체크섬 확인
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last
        // byte
        if (j >= 40 && checkParity()) {
            h = (float) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if (h > 100) {
                h = dht11_dat[0]; // for DHT11
            }
            c = (float) (((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if (c > 125) {
                c = dht11_dat[2]; // for DHT11
            }
            if ((dht11_dat[2] & 0x80) != 0) {
                c = -c;
            }
            f = c * 1.8f + 32;
            System.out.println("Humidity = " + h + " Temperature = " + c + "(" + f + "f)");
        }
        else {
            System.out.println("Data not good, skip");
        }
        float [] result = {h,c,f};
        return result;
    }

    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }
}

