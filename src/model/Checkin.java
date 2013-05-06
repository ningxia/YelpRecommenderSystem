package model;
import java.util.HashMap;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;


public class Checkin {
	@Id
	private ObjectId id;
	
	private String business_id;
	
	private String type;
	
	private HashMap<String, Integer> checkin_info;

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, Integer> getCheckin_info() {
		return checkin_info;
	}

	public void setCheckin_info(HashMap<String, Integer> checkin_info) {
		this.checkin_info = checkin_info;
	}
	
	
}
