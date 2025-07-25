package br.com.anderson.imageliteapi.application.images;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data // Getter and setters
@Builder
public class ImageDTO {

    private String url;
    private String name;
    private String extension;
    private Long size;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate uploadDate;
}
