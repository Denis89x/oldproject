package com.example.project1.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class Image {

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(max = 255, message = "Размер не больше 255")
    private String imageName;
    private MultipartFile file;
}
