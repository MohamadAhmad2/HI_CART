package io.moeah.hicartapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.moeah.hicartapi.exception.SellerHasProductsException;
import io.moeah.hicartapi.model.persistent.Seller;
import io.moeah.hicartapi.service.ProductService;
import io.moeah.hicartapi.service.SellerService;

@RestController
@RequestMapping(path = "api/v1/sellers")
public class SellerController {

	private final ProductService productService;
	private final SellerService sellerService;

	@Autowired
	public SellerController(ProductService productService, SellerService sellerService) {
		this.productService = productService;
		this.sellerService = sellerService;
	}

	@GetMapping("/all")
	public Page<Seller> getAllSellers(Pageable pageable) {
		return sellerService.findAll(pageable);
	}

	@GetMapping("{sellerId}")
	public Seller getSellerById(@PathVariable("sellerId") Long sellerId) {
		return sellerService.findSellerById(sellerId);
	}

	@PostMapping
	public void saveSeller(@Valid @RequestBody Seller seller) {
		sellerService.saveSeller(seller);
	}

	@PutMapping("{sellerId}")
	public void updateSeller(@PathVariable("sellerId") Long sellerId, @RequestBody Seller seller) {
		sellerService.updateSeller(sellerId, seller);
	}

	@DeleteMapping("{sellerId}")
	public void deleteSeller(@PathVariable("sellerId") Long sellerId) {

		// Validate if seller exists
		sellerService.findSellerById(sellerId);
		// Validate if products exists on this seller
		if (productService.existsBySellerId(sellerId))
			throw new SellerHasProductsException();

		sellerService.deleteSeller(sellerId);
	}

}
