package com.malcolm.imageapi.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class ImageMetadata {
    @Getter
    @Setter
    private String mimetype;

    @Getter
    @Setter
    private long size;
}
