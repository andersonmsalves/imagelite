package br.com.anderson.imageliteapi.infra.repository;

import br.com.anderson.imageliteapi.domain.entity.Image;
import br.com.anderson.imageliteapi.domain.enums.ImageExtension;
import br.com.anderson.imageliteapi.infra.repository.specs.GenericSpecs;
import br.com.anderson.imageliteapi.infra.repository.specs.ImageSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {

    /*
    SELECT *
    FROM IMAGE
    WHERE 1 = 1 // Sempre true para criar queries dinâmicas.
    AND EXTENSION = 'PNG'
    AND ( NAME LIKE 'Name' OR TAGS LIKE 'QUERY' )
     */

    default List<Image> findByExtensionsAndNameOrTagsLike(ImageExtension extension, String query) {

        Specification<Image> spec = GenericSpecs.conjunction();

        if(extension != null) {
            spec = spec.and(ImageSpecs.extensionEqual(extension));
        }

        if (StringUtils.hasText(query)) {
            Specification<Image> nameLike = ImageSpecs.nameLike(query);
            Specification<Image> tagsLike = ImageSpecs.tagsLike(query);
            spec = spec.and(nameLike.or(tagsLike));
        }
        return findAll(spec);
    }
}
