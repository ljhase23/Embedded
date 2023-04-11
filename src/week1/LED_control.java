package week1;

import com.pi4j.io.gpio.*;

public class LED_control {
	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();
		GpioPinDigitalOutput r_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, PinState.LOW);
		GpioPinDigitalOutput g_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, PinState.LOW);
		GpioPinDigitalOutput b_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
		
		try {
			r_led.high();
			Thread.sleep(1000);
			g_led.high();
			Thread.sleep(1000);
			b_led.high();
			Thread.sleep(1000);
			
			r_led.low();
			Thread.sleep(1000);
			g_led.low();
			Thread.sleep(1000);
			b_led.low();
			Thread.sleep(1000);
		}
		catch (Exception e) {
			
		}
		
	}

}
