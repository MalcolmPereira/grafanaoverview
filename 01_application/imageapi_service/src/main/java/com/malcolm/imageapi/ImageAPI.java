package com.malcolm.imageapi;

import com.malcolm.imageapi.model.ImageMetadata;
import com.malcolm.imageapi.model.StatusResponse;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;

@RestController
public class ImageAPI implements IImageAPI {

    private final HttpServletRequest requestContext;

    private static final Logger LOGGER = LogManager.getLogger(ImageAPI.class);

    private static final String TENANT_ID = "TENANT_ID";

    private static final String X_REAL_IP = "X-Real-IP";

    public ImageAPI(HttpServletRequest requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    @Timed
    public ResponseEntity<ImageMetadata> validate(@RequestPart("tenantId") @NotNull String tenantId, @RequestPart("image") @NotNull MultipartFile imageFile) {
        try{
            ThreadContext.put(TENANT_ID, tenantId);

            //Get Remote Host
            String remoteHost = requestContext.getRemoteHost();
            if(requestContext.getHeader(X_REAL_IP)!= null){
                remoteHost = requestContext.getHeader(X_REAL_IP);
            }

            LOGGER.debug("START Processing Image");
            LOGGER.info("Processing Image from HOST: "+remoteHost);
            LOGGER.info("Processing Image for TENANT_ID: "+tenantId);

            if (imageFile == null) {
                LOGGER.warn("Processing Invalid Image: ");
                throw new Exception("Invalid file upload data");
            }

            ImageInfo imageInfo;
            try (BufferedInputStream buffStream = new BufferedInputStream(imageFile.getInputStream())) {
                imageInfo = Imaging.getImageInfo(buffStream, "");
                if (imageInfo.getMimeType() == null || imageInfo.getMimeType().isBlank()) {
                    LOGGER.error("ImageCheckService Invalid Image mimeType: " + imageInfo.getMimeType());
                    throw new Exception("Unsupported file mime type");
                }
            }catch(ImageReadException imageReadException){
                throw new Exception("Unsupported file mime type");
            }
            LOGGER.debug("Image Metadata mimeType: " + imageInfo.getMimeType());
            LOGGER.debug("Image Metadata size: " + imageFile.getSize());
            ImageMetadata metadata = new ImageMetadata();
            metadata.setMimetype(imageInfo.getMimeType());
            metadata.setSize(imageFile.getSize());
            LOGGER.debug("END Processing Image");
            return new ResponseEntity<>(metadata, HttpStatus.OK);

        }catch(Exception err){
            LOGGER.error("Error processing image validation",err);
            throw new RuntimeException("Error processing image",err);
        }finally {
            ThreadContext.remove(TENANT_ID);
        }
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<StatusResponse> handleException(RuntimeException err) {
        StatusResponse response = new StatusResponse();
        response.setError("Error processing Image");
        response.setMessage(err.getCause().getMessage());
        response.setStatusCode(500);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
