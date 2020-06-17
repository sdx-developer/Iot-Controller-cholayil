package com.sdx.platform.util.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "machinemetricsfailures")
@Component
public class MachineMetricsFailures {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int transid;
	public String payload;
	public String payloadtype;
	public String trandtm;
	public String streamtype;
	public String statusflag;
	public String failurereason;
	public int retrycount;
	
	

	public int getTransId() {
		return transid;
	}
	public void setTransId(int transId) {
		transid = transId;
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
	public String getFailurereason() {
		return failurereason;
	}
	public void setFailurereason(String failurereason) {
		this.failurereason = failurereason;
	}
	public int getRetrycount() {
		return retrycount;
	}
	public void setRetrycount(int retrycount) {
		this.retrycount = retrycount;
	}
	

	
}
