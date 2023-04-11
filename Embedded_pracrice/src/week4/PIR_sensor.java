package week4;

import com.pi4j.io.gpio.*;
import com.pi4j.component.light.impl.GpioDimmableLightComponent;
public class PIR_sensor {
	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();
		GpioPinDigitalOutput r_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23,PinState.LOW);
		GpioPinDigitalInput pir = gpio.provisionDigitalInputPin(RaspiPin.GPIO_20);
		
		boolean PIR_result = false;
		
		while(true) {
			PIR_result = pir.isHigh();
			if(PIR_result == true) {
				System.out.println("Detected!");
				r_led.high();
			}
			else {
				System.out.println("Not Detected!");
				r_led.low();
			}
		}
	}

}
