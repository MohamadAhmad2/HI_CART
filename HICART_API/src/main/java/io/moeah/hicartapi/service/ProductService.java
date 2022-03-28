package io.moeah.hicartapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.moeah.hicartapi.exception.AlreadyExistsException;
import io.moeah.hicartapi.exception.NotFoundException;
import io.moeah.hicartapi.model.persistent.Product;
import io.moeah.hicartapi.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	public void saveProduct(Product product) {

		Optional<Product> existingProduct = productRepository.findByNameIgnoreCase(product.getName().trim());

		if (existingProduct.isPresent())
			throw new AlreadyExistsException("Product Name already exists.");

		productRepository.saveAndFlush(product);
	}

	public Product findProductById(Long productId) {
		return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product Not found"));
	}

	public void updateProduct(Long productId, Product product) {

		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundException("Product Not found"));

		if (product.getName() != null && !product.getName().trim().isEmpty()
				&& !product.getName().equalsIgnoreCase(existingProduct.getName())) {

			Optional<Product> productWithSameName = productRepository.findByNameIgnoreCase(product.getName().trim());
			if (productWithSameName.isPresent())
				throw new AlreadyExistsException("Product Name already exists.");
		}

		if (product.getName() != null && !product.getName().trim().isEmpty()
				&& !product.getName().equalsIgnoreCase(existingProduct.getName()))
			existingProduct.setName(product.getName());

		if (product.getPrice() != null && !product.getPrice().isNaN() && product.getPrice() >= 0.0)
			existingProduct.setPrice(product.getPrice());

		if (product.getQuantity() != null && product.getQuantity() >= 0.0)
			existingProduct.setQuantity(product.getQuantity());

		productRepository.saveAndFlush(existingProduct);
	}

	public void deleteProduct(Long productId) {

		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundException("Product Not found"));

		productRepository.delete(existingProduct);
	}

	public Boolean existsBySellerId(Long sellerId) {
		return productRepository.existsBySellerId(sellerId);
	}

}
