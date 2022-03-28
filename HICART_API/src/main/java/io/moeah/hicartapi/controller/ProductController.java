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

import io.moeah.hicartapi.model.persistent.Product;
import io.moeah.hicartapi.service.ProductService;
import io.moeah.hicartapi.service.SellerService;

@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {

	private final ProductService productService;
	private final SellerService sellerService;

	@Autowired
	public ProductController(ProductService productService, SellerService sellerService) {
		this.productService = productService;
		this.sellerService = sellerService;
	}

	@GetMapping("/all")
	public Page<Product> getAllProducts(Pageable pageable) {
		return productService.findAll(pageable);
	}

	@GetMapping("{productId}")
	public Product getProductById(@PathVariable("productId") Long productId) {
		return productService.findProductById(productId);
	}

	@PostMapping
	public void saveProduct(@Valid @RequestBody Product product) {

		if (product.getSeller() != null)
			sellerService.findSellerById(product.getId());

		productService.saveProduct(product);
	}

	@PutMapping("{productId}")
	public void updateProduct(@PathVariable("productId") Long productId, @RequestBody Product product) {
		productService.updateProduct(productId, product);
	}

	@DeleteMapping("{productId}")
	public void deleteProduct(@PathVariable("productId") Long productId) {
		productService.deleteProduct(productId);
	}
}
