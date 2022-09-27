package sit.int221.integratedprojectbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.integratedprojectbe.entities.EventCategory;

import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
    public List<EventCategory> findAllByOrderByCategoryIdDesc();

    public  boolean existsByCategoryNameAndCategoryIdNot(String categoryName, Integer categoryId);

}