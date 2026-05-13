package com.msAuth.application.utils;


import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GenericUtils {
    public <T> T applyIfChanged(T oldValue, T newValue) {
        if (newValue == null) return oldValue;
        if (newValue instanceof String && ((String) newValue).isBlank()) return oldValue;
        return newValue.equals(oldValue) ? oldValue : newValue;
    }
    public static <T> Specification<T> search(String term, Class<T> entityClass) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) return cb.conjunction();

            String pattern = "%" + term.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            for (Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.getType() == String.class) {
                        predicates.add(cb.like(cb.lower(root.get(field.getName())), pattern));
                    } else if (field.getType().isEnum()) {
                        predicates.add(cb.like(cb.lower(root.get(field.getName()).as(String.class)), pattern));
                    } else if (field.getType() == UUID.class) {
                        predicates.add(cb.like(root.get(field.getName()).as(String.class), "%" + term + "%"));
                    }
                } catch (Exception ignored) {}
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}