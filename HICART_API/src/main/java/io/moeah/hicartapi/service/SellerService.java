package io.moeah.hicartapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.moeah.hicartapi.exception.AlreadyExistsException;
import io.moeah.hicartapi.exception.NotFoundException;
import io.moeah.hicartapi.model.persistent.Seller;
import io.moeah.hicartapi.repository.SellerRepository;

@Service
public class SellerService {

	private final SellerRepository sellerRepository;

	@Autowired
	public SellerService(SellerRepository sellerRepository) {
		this.sellerRepository = sellerRepository;
	}

	public Page<Seller> findAll(Pageable pageable) {
		return sellerRepository.findAll(pageable);
	}

	public Seller findSellerById(Long sellerId) {
		return sellerRepository.findById(sellerId).orElseThrow(() -> new NotFoundException("Seller Not found"));
	}

	public void saveSeller(Seller seller) {

		Optional<Seller> existingSeller = sellerRepository.findByNameIgnoreCase(seller.getName().trim());

		if (existingSeller.isPresent())
			throw new AlreadyExistsException("Seller Name already exists");

		sellerRepository.saveAndFlush(seller);
	}

	public void updateSeller(Long sellerId, Seller seller) {

		Seller existingSeller = sellerRepository.findById(sellerId)
				.orElseThrow(() -> new NotFoundException("Seller Not found"));

		if (seller.getName() != null && !seller.getName().trim().isEmpty()
				&& !existingSeller.getName().equalsIgnoreCase(seller.getName().trim())) {

			Optional<Seller> sellerWithSameName = sellerRepository.findByNameIgnoreCase(seller.getName().trim());
			if (sellerWithSameName.isPresent())
				throw new AlreadyExistsException("Seller Name already exists.");
		}

		if (seller.getName() != null && !seller.getName().trim().isEmpty())
			existingSeller.setName(seller.getName());

		sellerRepository.saveAndFlush(existingSeller);
	}

	public void deleteSeller(Long sellerId) {
		sellerRepository.deleteById(sellerId);
	}
}
