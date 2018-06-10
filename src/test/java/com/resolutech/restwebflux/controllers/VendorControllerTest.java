package com.resolutech.restwebflux.controllers;

import com.resolutech.restwebflux.domain.Vendor;
import com.resolutech.restwebflux.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);

        vendorController = new VendorController(vendorRepository);

        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void testList() {

        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Mouhaha").build(),
                        Vendor.builder().firstName("Zzzzzzzzz").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void testGetById() {
        BDDMockito.given(vendorRepository.findById("ONE"))
                .willReturn(Mono.just(Vendor.builder().firstName("Mouhaha").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL + "/ONE")
                .exchange()
                .expectBody(Vendor.class);
    }
}