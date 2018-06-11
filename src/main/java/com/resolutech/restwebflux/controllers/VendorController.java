package com.resolutech.restwebflux.controllers;

import com.resolutech.restwebflux.domain.Vendor;
import com.resolutech.restwebflux.repositories.VendorRepository;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    protected static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher) {
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {

        Mono<Vendor> vendorMono = vendorRepository.findById(id);
        Vendor vendorBd = vendorMono.block();

        boolean save = false;
        if (!StringUtils.equals(vendor.getFirstName(), vendorBd.getFirstName())) {
            vendorBd.setFirstName(vendor.getFirstName());
            save = true;
        }
        if (!StringUtils.equals(vendor.getLastName(), vendorBd.getLastName())) {
            vendorBd.setLastName(vendor.getLastName());
            save = true;
        }
        if(save) {
            vendorMono = vendorRepository.save(vendor);
        }

        return vendorMono;
    }
}
