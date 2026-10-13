package com.smartoffice.controller;

import com.smartoffice.dto.CategoryRequest;
import com.smartoffice.model.Category;
import com.smartoffice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;
    public CategoryController(CategoryService service) { this.service=service; }
    @GetMapping public List<Category> active() { return service.active(); }
    @GetMapping("/all") public List<Category> all() { return service.all(); }
    @PostMapping public Category create(@Valid @RequestBody CategoryRequest request) { return service.create(request); }
    @PutMapping("/{id}") public Category update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
