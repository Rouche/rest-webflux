package com.resolutech.restwebflux.controllers;

import com.resolutech.restwebflux.domain.Category;
import com.resolutech.restwebflux.repositories.CategoryRepository;
import com.sun.deploy.util.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {

    protected static final String BASE_URL = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Category> categoryPublisher) {
        return categoryRepository.saveAll(categoryPublisher).then();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {

        Category categoryBd = categoryRepository.findById(id).block();

        if(!category.getName().equals(categoryBd.getName())) {
            categoryBd.setName(category.getName());
            return categoryRepository.save(categoryBd);
        }

        return Mono.just(categoryBd);
    }
}
