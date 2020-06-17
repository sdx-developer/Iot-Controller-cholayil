package com.sdx.platform.groovy

import groovy.json.JsonBuilder

public trait Basics {
	
	def convertToJSON() {
		println("THIS "+new JsonBuilder(this).toPrettyString())
	}
}
