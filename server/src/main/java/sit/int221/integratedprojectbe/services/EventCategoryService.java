package sit.int221.integratedprojectbe.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.EditEventCategoryDTO;
import sit.int221.integratedprojectbe.entities.EventCategory;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.repositories.EventCategoryRepository;
import sit.int221.integratedprojectbe.utils.ListMapper;

import java.util.List;

@Service
public class EventCategoryService {
    @Autowired
    private EventCategoryRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public List<EventCategory> getAll() {
        return repository.findAllByOrderByCategoryIdDesc();
    }

    public EventCategory getCategoryById(Integer categoryId) {
        EventCategory category = repository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Category ID %d doesn't exist.", categoryId)
                ));

        return  modelMapper.map(category,EventCategory.class);
    }

    public EventCategory editCategory(Integer categoryId, EditEventCategoryDTO updateCategory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        EventCategory category = repository.findById(categoryId)
                .map(existingCategory -> mapCategory(existingCategory, updateCategory))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Category ID %d doesn't exist.", categoryId)
                ));


        if (updateCategory.getCategoryName() != null && repository.existsByCategoryNameAndCategoryIdNot(updateCategory.getCategoryName(), categoryId)
        ) {

            FieldError error = new FieldError(
                    "editEventCategoryDTO",
                    "categoryName",
                    "Category name is already exist.");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        return repository.saveAndFlush(category);
    }

    private EventCategory mapCategory(EventCategory existingCategory, EditEventCategoryDTO updateCategory) {
        if (updateCategory.getCategoryName() != null) {
            existingCategory.setCategoryName(updateCategory.getCategoryName().trim());
        }
        existingCategory.setEventDuration(updateCategory.getEventDuration());
        if (updateCategory.getEventCategoryDesc() != null) {
            existingCategory.setEventCategoryDesc(updateCategory.getEventCategoryDesc().trim());
        }
        return  existingCategory;
    }


}
