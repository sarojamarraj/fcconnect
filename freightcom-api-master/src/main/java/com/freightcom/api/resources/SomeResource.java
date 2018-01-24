package com.freightcom.api.resources;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import com.freightcom.api.model.Customer;

public class SomeResource extends Resource<Customer> {
	
	public static final String INDEX = "index";
	public static final String INDICES = "indices";
	public static final String INDICES_PATH = "/indices";

	public SomeResource(Customer content, Link... links) {
		super(content, links);
	}
}