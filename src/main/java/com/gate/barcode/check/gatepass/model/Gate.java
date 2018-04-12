package com.gate.barcode.check.gatepass.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <<This is the entity to create the gate information>>
 * @author Munal
 * @version 1.0.0
 * @since , 11 Apr 2018
 */
@Entity
@Table(name = "gate")
public class Gate implements Serializable{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String gateName;
	private Long ticketCheckor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGateName() {
		return gateName;
	}

	public void setGateName(String gateName) {
		this.gateName = gateName;
	}

	public Long getTicketCheckor() {
		return ticketCheckor;
	}

	public void setTicketCheckor(Long ticketCheckor) {
		this.ticketCheckor = ticketCheckor;
	}
	
}
