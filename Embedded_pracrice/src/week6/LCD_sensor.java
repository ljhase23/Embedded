package week6;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class LCD_sensor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		I2CDevice device = null;
		I2CLCD lcd = null;
		DHT11 dht = new DHT11();
		
		try {
			I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
			device = bus.getDevice(0x27);
			lcd = new I2CLCD(device);
			lcd.init();
			lcd.backlight(true);;
		}catch(Exception e) {
			System.out.println(e);
		}
		while(true) {
			try {
				float[]sensing_data = dht.getTemperature(15);
				if(sensing_data[0] != -99) {
					lcd.clear();
					lcd.display_string("Humi : " + sensing_data[0], 1);
					lcd.display_string("Temperature : " + sensing_data[1], 2);
				}
				Thread.sleep(2000);
			}catch(Exception e) {
				System.out.println(e);
			}

			
		}

	}

}
