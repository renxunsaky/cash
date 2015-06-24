package com.surpassun.cash.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.surpassun.cash.domain.Product;
import com.surpassun.cash.repository.ProductRepository;

@Service
public class ProductService {
	
	private final Logger log = LoggerFactory.getLogger(ProductService.class);

	@Inject
	private ProductRepository productRepository;
	
	public Product getProductByBarcode(String code) {
		log.info("Get product by barcode: {}", code);
		return productRepository.findByCode(code);
	}
}
