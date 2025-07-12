package br.com.anderson.imageliteapi.infra.repository.specs;

import br.com.anderson.imageliteapi.domain.entity.Image;
import br.com.anderson.imageliteapi.domain.enums.ImageExtension;
import org.springframework.data.jpa.domain.Specification;

public class ImageSpecs {

    private ImageSpecs(){}

    public static Specification<Image> extensionEqual(ImageExtension extension) {
        return (root, q, cb) -> cb.equal(root.get("extension"), extension);
    }

    public static Specification<Image> nameLike(String query) {
        return (root, q, cb) -> cb.like(cb.lower(root.get("name")), "%" + query.toLowerCase() + "%");
    }

    public static Specification<Image> tagsLike(String query) {
        return (root, q, cb) -> cb.like(cb.lower(root.get("tags")), "%" + query.toLowerCase() + "%");
    }
}
