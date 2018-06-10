package com.resolutech.restwebflux.controllers;

import com.resolutech.restwebflux.domain.Category;
import com.resolutech.restwebflux.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);

        categoryController = new CategoryController(categoryRepository);

        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void testList() {

        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().name("Mouhaha").build(),
                        Category.builder().name("Zzzzzzzzz").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void testGetById() {
        BDDMockito.given(categoryRepository.findById("ONE"))
                .willReturn(Mono.just(Category.builder().name("Mouhaha").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/ONE")
                .exchange()
                .expectBody(Category.class);
    }
}