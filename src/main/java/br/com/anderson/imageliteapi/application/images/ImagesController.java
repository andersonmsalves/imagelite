package br.com.anderson.imageliteapi.application.images;

import br.com.anderson.imageliteapi.domain.entity.Image;
import br.com.anderson.imageliteapi.domain.enums.ImageExtension;
import br.com.anderson.imageliteapi.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/images")
@Slf4j
@RequiredArgsConstructor
public class ImagesController {

    //private final Logger logger = LoggerFactory.getLogger(ImagesController.class);
    private final ImageService service;
    private final ImageMapper mapper;

//    public ImagesController(ImageService service) {
//        this.service = service;
//    }

    @PostMapping
    public ResponseEntity save(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam(value = "tags", required = false)List<String> tags
            ) throws IOException {

        log.info("Imagem recebida: name: {}, size: {}", file.getOriginalFilename(), file.getSize());
        log.info("Nome definido para a imagem: {}", name);
        log.info("Tags: {}", tags);
        log.info("Content Type: {}", file.getContentType()); // image/png

        var mediaType = MediaType.valueOf(file.getContentType());
        log.info("Media Type: {}", mediaType);

        var image = mapper.mapToImage(file, name, tags);
        service.save(image);
        URI imageUri = buildImageURL(image);

        return ResponseEntity.created(imageUri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String imagemId) {
        var possibleImage = service.findById(imagemId);
        if(possibleImage.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var image = possibleImage.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename=\"" + image.getFileName() + "\"", image.getFileName());

        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ImageDTO>> search(
            @RequestParam(value="extension", required = false, defaultValue = "")  String extension,
            @RequestParam(value="query", required = false, defaultValue = "") String query){

        var result = service.search(ImageExtension.getMediaType(extension), query);

        var images = result.stream().map(image->{
            var url = buildImageURL(image).toString();
            return mapper.imageToDTO(image, url);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(images);
    }

    // localhost:8080/v1/images/{id-image}
    private URI buildImageURL(Image image) {
        String imagePath = "/" + image.getId();
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                                          .path(imagePath)
                                          .build()
                                          .toUri();
    }
}
