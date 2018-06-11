package com.resolutech.restwebflux.controllers;

import com.resolutech.restwebflux.domain.Category;
import com.resolutech.restwebflux.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
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

        given(categoryRepository.findAll())
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
        given(categoryRepository.findById("ONE"))
                .willReturn(Mono.just(Category.builder().name("Mouhaha").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/ONE")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void testCreate() {

        Category cat = Category.builder().name("CategoryToSave").build();

        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(cat));

        Mono<Category> catToSave = Mono.just(cat);

        webTestClient.post()
                .uri(CategoryController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {

        Category cat = Category.builder().name("CategoryToUpdate").build();
        Mono<Category> catMono = Mono.just(cat);

        given(categoryRepository.save(cat))
                .willReturn(catMono);

        webTestClient.put()
                .uri(CategoryController.BASE_URL + "/myId")
                .contentType(MediaType.APPLICATION_JSON)
                .body(catMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);
    }

    @Test
    public void testPatch() {

        Mono<Category> catMono = Mono.just(Category.builder().name("CategoryToUpdate").build());

        Category cat = Category.builder().name("New name").build();
        Mono<Category> catUpdatedMono = Mono.just(cat);

        given(categoryRepository.findById(anyString()))
                .willReturn(catMono);

        given(categoryRepository.save(cat))
                .willReturn(catUpdatedMono);

        webTestClient.patch()
                .uri(CategoryController.BASE_URL + "/myId")
                .contentType(MediaType.APPLICATION_JSON)
                .body(catUpdatedMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);

        verify(categoryRepository).save(cat);
    }

    @Test
    public void testPatchNoChanges() {

        Category cat = Category.builder().name("New name").build();
        Mono<Category> categoryMono = Mono.just(cat);

        given(categoryRepository.findById(anyString()))
                .willReturn(categoryMono);

        webTestClient.patch()
                .uri(CategoryController.BASE_URL + "/myId")
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);

        verify(categoryRepository, never()).save(cat);
    }
}