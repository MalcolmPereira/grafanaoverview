package com.malcolm.imageapi;

import com.malcolm.imageapi.model.ImageMetadata;
import com.malcolm.imageapi.model.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Tag(
        name = "Image API",
        description = "Image API allows to process image metadata for uploaded image "
)
@RequestMapping("/")
public interface IImageAPI {

    @Operation(
            summary 	= "Process image for image metadata. ",

            description = "Tenants will upload image and retrieve image metadata .",

            tags 		= { "Image API" }
    )
    @ApiResponses(
            value = {
                @ApiResponse(
                    description = "REST API invoked successfully. HTTP_OK (Status 200).",
                    responseCode = "200",
                    content = {
                        @Content(
                            mediaType = "application/json" ,
                            schema = @Schema(implementation = ImageMetadata.class)
                        ),
                        @Content(
                            mediaType = "application/xml" ,
                            schema = @Schema(implementation = ImageMetadata.class)
                        )
                    }
                ),
                @ApiResponse(
                    description = "REST API failed processing. HTTP_OK (Status 500).",
                    responseCode = "500",
                    content = {
                         @Content(
                             mediaType = "application/json" ,
                             schema = @Schema(implementation = StatusResponse.class)
                         ),
                         @Content(
                             mediaType = "application/xml" ,
                             schema = @Schema(implementation = StatusResponse.class)
                         )
                    }
                )
            }
    )
    @PostMapping(
            value= "/validate",
            consumes = { "multipart/form-data"},
            produces = { "application/json", "application/xml"}
    )
    ResponseEntity<ImageMetadata> validate(
            @Parameter(description = "Id of the tenant.")
            @RequestPart("tenantId") @NotNull String tenantId,
            @Parameter(description = "Image to be processed.")
            @RequestPart("image") @NotNull MultipartFile imageFile
    );
}
