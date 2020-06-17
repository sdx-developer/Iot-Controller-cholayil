package com.sdx.platform.groovy.scripts

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;



import com.ghgande.j2mod.modbus.*;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.net.*;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.*;

public class Slave {
	
	
	/**
	 * @param args
	 */
	  static void main(String[] args) {
		/* The important instances of the classes mentioned before */
		ModbusSerialMaster serialMaster = null; // the connection
		
		
		/* Variables for storying the parameters */
		String portname = "COM3"; // the name of the serial port to be used  /// oUr Port Name might be Different
		int unitID = 2; // the unit identifier we will be talking to  /// Our Device ID
		int startingRegister = 12740; // the reference, where to start reading from  //Registry // reg2 machine // reg 1 machine status
		int registerCount = 5; // the count of the input registers to read
		Register[] slaveResponse = new Register[registerCount];

	
		while (true) {
		try {
			/* Setup the serial parameters */
			SerialParameters parameters = new SerialParameters();
			parameters.setPortName(portname);
			parameters.setBaudRate(19200); //Our Baud Rate Here ...Our Baud Rate is 19200
			parameters.setDatabits(8); // Even WE also Have same DataBits
			parameters.setParity(AbstractSerialConnection.EVEN_PARITY);
			parameters.setStopbits(AbstractSerialConnection.ONE_
				STOP_BIT);
			parameters.setEncoding(Modbus.SERIAL_ENCODING_RTU); // We also Use SAme RTU communication.
			parameters.setEcho(false);

			/* Open the connection */
			serialMaster = new ModbusSerialMaster(parameters);
			serialMaster.connect();

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		/* Read the first four holding registers */
		try {
			slaveResponse = serialMaster.readMultipleRegisters(unitID, startingRegister, registerCount);
			for (int i = 0; i < slaveResponse.length; i++) {
				System.out.println("reg" + i + " = " + slaveResponse[i]);
			}
		} catch (ModbusException e) {
			e.printStackTrace();
		}
		/* Close the connection */
		serialMaster.disconnect();

	} // main
	}
}
