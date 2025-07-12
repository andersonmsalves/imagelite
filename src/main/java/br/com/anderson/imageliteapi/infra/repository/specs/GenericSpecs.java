package br.com.anderson.imageliteapi.infra.repository.specs;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecs<T> {

    private GenericSpecs(){}

    public static <T> Specification<T> conjunction() {
        return (root, q, cb) -> cb.conjunction();
    }
}
