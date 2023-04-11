package project;

import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LED extends BasicCoapResource{
	private String state = "off";
	GpioController gpio;
	GpioPinDigitalOutput r_led;
	GpioPinDigitalOutput g_led;
	GpioPinDigitalInput pir;


	private LED(String path, String value, CoapMediaType mediaType) {
		super(path, value, mediaType);
	}

	public LED() {
		this("/led", "off", CoapMediaType.text_plain);
		gpio = GpioFactory.getInstance();
		r_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, PinState.LOW);
		g_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, PinState.LOW);
		pir = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00);
	}

	@Override
	public synchronized CoapData get(List<String> query, List<CoapMediaType> mediaTypesAccepted) {
		return get(mediaTypesAccepted);
	}
	
	@Override
	public synchronized CoapData get(List<CoapMediaType> mediaTypesAccepted) {
		return new CoapData(Encoder.StringToByte(this.state), CoapMediaType.text_plain);
	}

	@Override
	public synchronized boolean setValue(byte[] value) {
		this.state = Encoder.ByteToString(value);
		boolean PIR_result = false;
		PIR_result = pir.isHigh();
		
		if(PIR_result == true) {
			r_led.high();
			g_led.low();
		}
		else{
			r_led.low();
			g_led.high();
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean post(byte[] data, CoapMediaType type) {
		return this.setValue(data);
	}

	@Override
	public synchronized boolean put(byte[] data, CoapMediaType type) {
		return this.setValue(data);
	}

	@Override
	public synchronized String getResourceType() {
		return "Raspberry pi 4 LED";
	}

}