package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AttachmentDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String fileName;

    @NotNull
    private String filePath;

    @Size(max = 100)
    private String mimeType;

    @NotNull
    private Long client;

    @NotNull
    private Long type;

}
