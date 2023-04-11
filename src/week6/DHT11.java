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
        int laststate = Gpio.HIGH; // signal ���� ��ȭ�� �˱� ����, ���� ���¸� ���.
        int j = 0; // ������ Bit�� index counter
        float h = -99; // ����
        float c = -99; // ���� �µ�
        float f = -99; // ȭ�� �µ�
        
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
        // Integral RH, Decimal RH, Interal T, Decimal C
        
        // 1. DHT11 �������� start signal ����.
        Gpio.pinMode(pin, Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.LOW);
        Gpio.delay(18);

        // 2. Pull-up -> ���Ÿ��� ��ȯ -> ������ ���� ���
        Gpio.digitalWrite(pin, Gpio.HIGH);
        Gpio.pinMode(pin, Gpio.INPUT);
        
        // 3. ������ ���信 ���� ����
        for (int i = 0; i < MAXTIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(pin) == laststate) { // GPIO pin ���°� �ٲ�� ����
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
            
            // ������ bit ������ ����
            if (i >= 4 && i % 2 == 0) { // ù 3���� bit�� ����, laststate �� low���� high �� �ٲ𶧸� �� ����
            	// data ����
                dht11_dat[j / 8] <<= 1; // 0 bit 
                if (counter > 16) {
                    dht11_dat[j / 8] |= 1; // 1 bit
                }
                j++;
            }
        }
        
        // üũ�� Ȯ��
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

