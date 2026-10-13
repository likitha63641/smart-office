package com.smartoffice.service;

import com.smartoffice.dto.CategoryRequest;
import com.smartoffice.model.Category;
import com.smartoffice.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    public CategoryService(CategoryRepository repository) { this.repository=repository; }
    public List<Category> active() { return repository.findByActiveTrueOrderByNameAsc(); }
    public List<Category> all() { return repository.findAll(); }
    public Category create(CategoryRequest request) {
        if (repository.existsByNameIgnoreCase(request.getName())) throw new IllegalStateException("Category already exists");
        Category c = new Category(request.getName().trim()); c.setActive(request.isActive()); return repository.save(c);
    }
    public Category update(Long id, CategoryRequest request) {
        Category c = repository.findById(id).orElseThrow(() -> new IllegalStateException("Category not found"));
        c.setName(request.getName().trim()); c.setActive(request.isActive()); return repository.save(c);
    }
    public void delete(Long id) { repository.deleteById(id); }
}
