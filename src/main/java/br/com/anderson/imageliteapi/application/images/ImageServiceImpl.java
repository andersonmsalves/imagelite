package br.com.anderson.imageliteapi.application.images;

import br.com.anderson.imageliteapi.domain.entity.Image;
import br.com.anderson.imageliteapi.domain.enums.ImageExtension;
import br.com.anderson.imageliteapi.domain.service.ImageService;
import br.com.anderson.imageliteapi.infra.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repository;

    @Override
    @Transactional
    public Image save(Image image) {
        return repository.save(image);
    }

    @Override
    public Optional<Image> findById(String id){
        return repository.findById(id);
    }

    @Override
    public List<Image> search(ImageExtension extension, String query) {
        return repository.findByExtensionsAndNameOrTagsLike(extension, query);
    }


}
