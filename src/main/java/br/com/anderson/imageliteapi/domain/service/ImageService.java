package br.com.anderson.imageliteapi.domain.service;

import br.com.anderson.imageliteapi.domain.entity.Image;
import br.com.anderson.imageliteapi.domain.enums.ImageExtension;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    Image save(Image image);
    Optional<Image> findById(String id);
    List<Image> search(ImageExtension extension, String query);
}
