package com.resolutech.restwebflux.controllers;

import com.resolutech.restwebflux.domain.Category;
import com.resolutech.restwebflux.domain.Vendor;
import com.resolutech.restwebflux.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

    @Test
    public void testCreate() {

        Vendor vendor = Vendor.builder().firstName("VendorToSave").build();

        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(vendor));

        Mono<Vendor> vendorMonoToSave = Mono.just(vendor);

        webTestClient.post()
                .uri(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(vendorMonoToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {

        Vendor vendor = Vendor.builder().firstName("VendorToSave").build();
        Mono<Vendor> vendorMonoToUpdate = Mono.just(vendor);

        BDDMockito.given(vendorRepository.save(vendor))
                .willReturn(vendorMonoToUpdate);


        webTestClient.put()
                .uri(VendorController.BASE_URL + "/myId")
                .contentType(MediaType.APPLICATION_JSON)
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);
    }

    @Test
    public void testPatch() {

        Mono<Vendor> catMono = Mono.just(Vendor.builder().firstName("VendorToUpdate").build());

        Vendor cat = Vendor.builder().firstName("New name").build();
        Mono<Vendor> catUpdatedMono = Mono.just(cat);

        given(vendorRepository.findById(anyString()))
                .willReturn(catMono);

        given(vendorRepository.save(cat))
                .willReturn(catUpdatedMono);

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/myId")
                .contentType(MediaType.APPLICATION_JSON)
                .body(catUpdatedMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);

        verify(vendorRepository).save(cat);
    }

    @Test
    public void testPatchNoChanges() {

        Vendor vendor = Vendor.builder().firstName("New name").build();
        Mono<Vendor> vendorMono = Mono.just(vendor);

        given(vendorRepository.findById(anyString()))
                .willReturn(vendorMono);

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/myId")
                .contentType(MediaType.APPLICATION_JSON)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);

        verify(vendorRepository, never()).save(vendor);
    }
}