package com.example.project1.controller;

import com.dropbox.core.DbxException;
import com.example.project1.model.Image;
import com.example.project1.service.DropboxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class ImageController {

    private final DropboxService dropboxService;

    @Autowired
    public ImageController(DropboxService dropboxService) {
        this.dropboxService = dropboxService;
    }

    @GetMapping("/")
    public String listImages(Model model) {
        try {
            List<String> images = dropboxService.listImages();
            model.addAttribute("images", images);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return "image/listImages";
    }

    @GetMapping("/image/{imageName:.+}")
    public void getImage(@PathVariable String imageName, HttpServletResponse response) {
        try {
            byte[] imageData = dropboxService.getImage(imageName);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.getOutputStream().write(imageData);
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/uploadImage")
    public String showUploadForm(Image image, Model model) {
        model.addAttribute("image", image);
        return "image/uploadImage";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@Valid @ModelAttribute Image image, BindingResult result) {
        if (result.hasErrors()) {
            return "image/uploadImage";
        }
        try {
            String imageName = dropboxService.saveImage(image.getFile(), image.getImageName());
            return "redirect:/";
        } catch (IOException | DbxException e) {
            e.printStackTrace();
            return "image/uploadImage";
        }
    }
}
