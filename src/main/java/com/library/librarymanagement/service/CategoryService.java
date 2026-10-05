package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.CategoryRequest;
import com.library.librarymanagement.entity.Category;
import com.library.librarymanagement.exception.ResourceNotFoundException;
import com.library.librarymanagement.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );
    }

    public Category addCategory(CategoryRequest request) {

        Category category = new Category();

        category.setName(request.getName());

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest request) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        existingCategory.setName(request.getName());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        categoryRepository.delete(existingCategory);
    }
}