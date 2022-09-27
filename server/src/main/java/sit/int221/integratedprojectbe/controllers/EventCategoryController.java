package sit.int221.integratedprojectbe.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sit.int221.integratedprojectbe.dtos.EditEventCategoryDTO;
import sit.int221.integratedprojectbe.entities.EventCategory;
import sit.int221.integratedprojectbe.services.EventCategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class EventCategoryController {
    @Autowired
    private EventCategoryService eventCategoryService;

    @GetMapping("")
    public List<EventCategory> getEventCategories() {
        return eventCategoryService.getAll();
    }

    @GetMapping("/{categoryId}")
    public EventCategory getCategoryById(@PathVariable Integer categoryId) {
        return eventCategoryService.getCategoryById(categoryId);
    }

    @PutMapping("/{categoryId}")
    public EventCategory update(@Valid @RequestBody EditEventCategoryDTO editCategory,
                                BindingResult bindingResult, @PathVariable Integer categoryId) {
        return eventCategoryService.editCategory(categoryId, editCategory, bindingResult);
    }
}
