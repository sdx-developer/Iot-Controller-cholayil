package com.sdx.platform.util.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;


@Entity
@Table(name = "machinemetrics")
@Component
public class MachineMetrics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transid;
	public int getTransid() {
		return transid;
	}

	public void setTransid(int transid) {
		this.transid = transid;
	}

	private String payload;
	private String payloadtype;
	private String trandtm;
	private String streamtype;
	private String statusflag; 
	private String status;

	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPayloadtype() {
		return payloadtype;
	}

	public void setPayloadtype(String payloadtype) {
		this.payloadtype = payloadtype;
	}

	public String getTrandtm() {
		return trandtm;
	}

	public void setTrandtm(String trandtm) {
		this.trandtm = trandtm;
	}

	public String getStreamtype() {
		return streamtype;
	}

	public void setStreamtype(String streamtype) {
		this.streamtype = streamtype;
	}

	public String getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(String statusflag) {
		this.statusflag = statusflag;
	}

	
	

	

}
