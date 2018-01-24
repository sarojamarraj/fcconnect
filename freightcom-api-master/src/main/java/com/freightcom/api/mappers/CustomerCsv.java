package com.freightcom.api.mappers;

import com.freightcom.api.model.Customer;

public class CustomerCsv {
	private final Customer customer;

	public CustomerCsv(Customer customer) {
		this.customer = customer;
	}

	public Long getId() {
		return customer.getId();
	}

	public String getName() {
		return customer.getName();
	}
}
