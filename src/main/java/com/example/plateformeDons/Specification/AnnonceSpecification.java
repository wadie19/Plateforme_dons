package com.example.plateformeDons.Specification;

import com.example.plateformeDons.models.Annonce;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AnnonceSpecification {
    public static Specification<Annonce> withFilters(String zone, String etat, String motCle) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (zone != null && !zone.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("zoneGeographique"), zone));
            }

            if (etat != null && !etat.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("etat"), etat));
            }

            if (motCle != null && !motCle.isEmpty()) {
                predicates.add(criteriaBuilder.isMember(motCle, root.get("motCles")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
