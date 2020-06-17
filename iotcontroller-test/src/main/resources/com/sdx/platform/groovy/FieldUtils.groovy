package com.sdx.platform.groovy

class FieldUtils {
	
	static Map buildTF(String pLabel, String pKey, Object defValue, boolean isNum) {
		def tfMap = [
			type 			: "textfield", 
			label 			: pLabel, 
			key 			: pKey, 
			defaultValue	: defValue != null ? defValue : "", 
			inputType		: isNum ? "number" : "text",
			input 			: true,
			clearOnHide		: true,
			"hidden"		: false,
			placeholder		: "Enter "+pLabel
			
		];
		return tfMap;
	}
	
	
	static Map buildDF(String pTitle, String pKey, Object defValue, boolean isNum) {
		def tfMap = [
			type 			: "textfield",
			inputType		: isNum ? "number" : "text",
			label 			: pTitle, 
			key 			: pKey,
			defaultValue			: defValue != null ? defValue : "",
			readonly		: "readonly",
			input 			: true,
			clearOnHide		: true,
			"hidden"		: false,
			placeholder		: "Enter "+pTitle
			
		];
		return tfMap;
	}
	
	
	static Map buildButton(String pLabel, String pKey, String pAction) {
		def tfMap = [
			type 			: 	"button",
			label 			: 	pLabel,
			action 			: 	pAction,
			theme			: 	"primary ",
			leftIcon		: 	"fa-plus"
			
		];
		return tfMap;
	}
	
	
	
	static Map buildPhoneField(String pLabel, String pKey, Object defValue) {
		def tfMap = [
			type 			: "textfield", 
			label 			: pLabel, 
			key 			: pKey, 
			input 			: true,
			inputMask		: "(999) 999-9999",
			defaultValue	: defValue != null ? defValue : "",
			clearOnHide		: true,
			"hidden"		: false,
			placeholder		: "Enter "+pLabel
			
		];
		return tfMap;
	}
	
}
