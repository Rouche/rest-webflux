package com.resolutech.restwebflux.bootstrap;

import com.resolutech.restwebflux.domain.Category;
import com.resolutech.restwebflux.domain.Vendor;
import com.resolutech.restwebflux.repositories.CategoryRepository;
import com.resolutech.restwebflux.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppBootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private VendorRepository vendorRepository;

    public AppBootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        if(vendorRepository.count().block() == 0) {
            loadVendors();
            loadCategories();
        }
    }

    private void loadCategories() {

        categoryRepository.save(Category.builder().name("One").build()).block();
        categoryRepository.save(Category.builder().name("Two").build()).block();
        categoryRepository.save(Category.builder().name("Three").build()).block();
        categoryRepository.save(Category.builder().name("Four").build()).block();

        log.debug("Category loaded: " + vendorRepository.count().block());
    }

    private void loadVendors() {

        vendorRepository.save(Vendor.builder().firstName("Bob").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Joe").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Chose").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Inoss").build()).block();

        log.debug("Vendor loaded: " + vendorRepository.count().block());
    }
}