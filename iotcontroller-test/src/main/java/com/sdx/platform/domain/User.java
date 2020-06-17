package com.sdx.platform.domain;

/*import org.springframework.data.annotation.Id;*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	
	/*@Id*/
	private String _id;
	private String guid;
	
	private boolean active = false;
	
	private String picture;
	private int age = 0;
	
	private String role;
	private String duty;
	
	private String fName;
	private String lName;
	private String callout;
	
	private String gender;
	private String company;
	
	private String email;
	private String phone;
	
	private int[] departments;

}
	