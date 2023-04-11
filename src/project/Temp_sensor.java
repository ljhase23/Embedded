package project;

import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;
import week6.DHT11;

public class Temp_sensor extends BasicCoapResource{

	private boolean value; 
	
	private Temp_sensor(String path, String value, CoapMediaType mediaType) {
		super(path, value, mediaType);
		public static boolean PIR_sensor = false;
	}

	public Temp_sensor() {
		this("/temperature", "0", CoapMediaType.text_plain);
	}
	
	@Override
	public synchronized CoapData get(List<CoapMediaType> mediaTypesAccepted) {
		this.value = Float.toString(sensing_data[1]);
		return new CoapData(Encoder.StringToByte(this.value), CoapMediaType.text_plain);
	}

	
	@Override
	public synchronized boolean setValue(byte[] value) {
		this.value = Encoder.ByteToString(value);
		return true;
	}
	public synchronized void optional_changed() {
		//온도값
		//온도 값 변했으면, Observe Response
		//안 변했으면, do nothing
		float[] sensing_data = dht.getTemperature(15);
		boolean PIR_sensor = false;
		
		if(PIR_sensor == true) {
			System.out.println("temperature is not changed.");
		}
		else if(PIR_sensor == false) {
			System.out.println("failed to read temp data.");
		}
		else {
			this.value = PIR_sensor;
			this.changed(this.value);
		}
		
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
		return "Raspberry pi 4 Temperature Sensor";
	}
}
