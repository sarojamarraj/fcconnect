package com.freightcom.api.controllers;


import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.model.Customer;
import com.freightcom.api.resources.SomeResource;

@RestController
@ExposesResourceFor(Customer.class)
@RequestMapping(value="/indices", produces={"application/xml", "application/json"})
public class SomeController {

	@RequestMapping(method=GET)
	public PagedResources<SomeResource> getSeveral(
			@RequestParam(value="exchange", required=false) String exchangeId,
			@RequestParam(value="market", required=false) String marketId,
			@PageableDefault(size=10, page=0, sort={"previousClose"}, direction=Direction.DESC) Pageable pageable){
		return null;
	}
}
