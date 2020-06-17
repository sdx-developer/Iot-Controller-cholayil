package com.sdx.platform.groovy

import groovy.json.JsonBuilder
import groovy.json.JsonOutput;

class JUIFieldUtils {
	
	static Map buildTF(String pTitle, String pKey, Object defValue, boolean isNum, String pDesc) {
		def tfMap = [
			type 			: isNum ? "number" : "text", 
			title 			: pTitle, 
			key 			: pKey, 
			value			: defValue != null ? defValue : "",
			description		: pDesc
		];
		return tfMap;
	}
	
	
	static Map buildTFRO(String pTitle, String pKey, Object defValue, boolean isNum, String pDesc) {
		def tfMap = [
			type 			: isNum ? "number" : "text",
			title 			: pTitle,
			key 			: pKey,
			readonly		: "readonly",
			value			: defValue != null ? defValue : "",
			description		: pDesc
		];
		return tfMap;
	}
	
	static Map buildNF(String pTitle, String pKey, Object defValue, int min, int max, String pDesc) {
		def tfMap = [
			type 			: "number",
			title 			: pTitle,
			key 			: pKey,
			min				: 	min,
			max				: 	max,
			value			: defValue != null ? defValue : "",
			description		: pDesc
		];
		return tfMap;
	}
	

	
	static Map buildSelectBox(String pTitle, String pKey, String value, String[] items) {
		def tfMap = [
			type 			: 	"select",
			title 			: 	pTitle,
			key 			: 	pKey,
			value			: 	value,
			items			: 	items
			
		];
		return tfMap;
	}
	
	static Map buildCheckBox(String pTitle, String pKey, boolean value, String pDesc) {
		def tfMap = [
			type 			: 	"checkbox",
			title 			: 	pTitle,
			key 			: 	pKey,
			value			: 	value,
			description		: 	pDesc
		];
		return tfMap;
	}
	
	
	static Map buildSwitch(String pTitle, String pKey, boolean value, String pDesc) {
		def tfMap = [
			type 			: 	"switch",
			title 			: 	pTitle,
			key 			: 	pKey,
			value			: 	value,
			description		: 	pDesc
		];
		return tfMap;
	}
	
	static Map buildDatePicker	(String pTitle, String pKey, String value, String pDesc) {
		def tfMap = [
			type 			: 	"switch",
			title 			: 	pTitle,
			key 			: 	pKey,
			value			: 	value,
			description		: 	pDesc
		];
		return tfMap;
	}
	
	static Map buildRangeBuilder(String pTitle, String pKey, int value, int max, int min, 
								boolean isStep, int step, boolean isReadOnly, String pDesc) {
		def tfMap = [
			type 			: 	"range",
			title 			: 	pTitle,
			key 			: 	pKey,
			value			: 	value,
			min				: 	min,
			max				: 	max,
			step			: 	isStep ? step : 1,
			description		: 	pDesc
		];
		return tfMap;
	}
	
	
	static Map buildGroup(String pTitle, String pDesc) {
		def tfMap = [
			type 			: "group", 
			title 			: pTitle, 
			description		: pDesc
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
	
	
}
