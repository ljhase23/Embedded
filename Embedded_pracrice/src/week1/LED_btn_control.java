package week1;
import com.pi4j.io.gpio.*;

public class LED_btn_control {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GpioController gpio = GpioFactory.getInstance();
		GpioPinDigitalOutput r_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, PinState.LOW);
		GpioPinDigitalOutput g_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, PinState.LOW);
		GpioPinDigitalOutput b_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
		GpioPinDigitalInput btn = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);
		
boolean buttonPressed = false;
		
		while(true) {
			buttonPressed = btn.isHigh();
			if(buttonPressed == true) {
				r_led.high();
				g_led.high();
				b_led.high();
			}
			else {
				r_led.low();
				g_led.low();
				b_led.low();
			}
		}

	}

}
