package com.resolutech.restwebflux.controllers;

import com.resolutech.restwebflux.domain.Category;
import com.resolutech.restwebflux.repositories.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
