package br.com.anderson.imageliteapi.application.images;

import br.com.anderson.imageliteapi.domain.entity.Image;
import br.com.anderson.imageliteapi.domain.enums.ImageExtension;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class ImageMapper {

    public Image mapToImage(MultipartFile file, String name, List<String> tags) throws IOException {
        return Image.builder()
                .name(name)
                .tags(String.join(",",tags)) // [tag1, tag2] -> "tag1, tag2"
                .size(file.getSize())
                .extension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())))
                .file(file.getBytes()) // Transforma em array de bytes.
                .build();
    }

    public ImageDTO imageToDTO(Image image, String url) {
        return ImageDTO.builder()
                .name(image.getName())
                .size(image.getSize())
                .url(url)
                .extension(image.getExtension().name())
                .uploadDate(image.getUpdateDate().toLocalDate())
                .build();
    }
}
