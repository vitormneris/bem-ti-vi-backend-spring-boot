package com.bemtivi.bemtivi.factories;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageFactory {

    public MultipartFile createMultipartFile() {
        return new MockMultipartFile(
                "file",
                "arquivo_de_teste.jpg",
                "image/jpeg",
                new byte[] {}
        );
    }

}
