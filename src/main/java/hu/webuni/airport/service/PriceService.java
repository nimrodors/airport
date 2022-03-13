package hu.webuni.airport.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
	
	private DiscountService discountService; 
	
	public PriceService(DiscountService discountService) {
		super();
		this.discountService = discountService;
	}

	//ebben számolom ki, hogy mennyi a végső ár
	public int getFinalPrice(int price) {
		return (int) (price / 100.0 *(100 - discountService.getDiscountPercent(price)));
	}
}
