package model;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class User {
	
	@Id
	private ObjectId id;
	
	private String user_id;
	
	private String name;
	
	private String type;
	
	private double average_stars;
	
	private int review_count;
	
	public User(String user_id, String name, String type) {
		this.user_id = user_id;
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public double getAverage_stars() {
		return average_stars;
	}

	public void setAverage_stars(double average_stars) {
		this.average_stars = average_stars;
	}

	public int getReview_count() {
		return review_count;
	}

	public void setReview_count(int review_count) {
		this.review_count = review_count;
	}
	

}
