package com.skillip.skillip_backend.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.skillip.skillip_backend.common.dtos.UserDTO;
import com.skillip.skillip_backend.common.exceptions.UserNotFoundException;
import com.skillip.skillip_backend.models.User;
import com.skillip.skillip_backend.repositories.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String ALLOWED_CONTENT_TYPES = "image/jpeg,image/png,image/gif";

    @Autowired
    private final UserRepository userRepository;  // Field injection

    @Autowired
    private final AmazonS3 amazonS3;

    @Value("${application.bucket.name}")
    private String bucketName;

    public UserService(UserRepository userRepository, AmazonS3 amazonS3) {
        this.userRepository = userRepository;
        this.amazonS3 = amazonS3;
    }

    public UserDTO createUser(@NotNull User user){
        if(user.getUsername() == null || user.getUsername().trim().isEmpty()){
            user.setUsername(user.getEmail());
        }
        user = userRepository.save(user);
        return new UserDTO(user.getUsername(), user.getEmail(), user.getProfileImageUrl(), null);
    }

    public User getUser(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        user.setPassword(null);
        return user;
    }

    public User createTalent(@NotNull User user) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if(optionalUser.isPresent()){
            return userRepository.save(user);
        }
        else{
            throw new UserNotFoundException(user.getEmail());
        }
    }

    private void validateFile(MultipartFile file) {
        
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds 5MB limit");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("File must be an image (JPEG, PNG, or GIF)");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("File must have a valid extension");
        }
    }

    public ResponseEntity<Map<String, Object>> uploadProfileImage(String email, MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());

        try {
            // Validate file
            validateFile(file);
            
            // Get user
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(email));

            if(user.getProfileImageUrl() != null){
                // Delete the old profile image from S3
                String oldProfileImageUrl = user.getProfileImageUrl();
                String oldFileName = oldProfileImageUrl.substring(oldProfileImageUrl.indexOf("profile-images/"));
                System.out.println("Deleting old profile image: " + oldFileName);

                try{
                    amazonS3.deleteObject(bucketName, oldFileName);
                }
                catch(AmazonS3Exception e){
                    logger.warn("Failed to delete old profile image: {}", oldFileName, e);
                }
            }
                
            // Generate unique filename
            String fileName = UUID.randomUUID().toString();
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fullFileName = "profile-images/" + fileName + fileExtension;
            
            // Set metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.setCacheControl("public, max-age=31536000"); // Cache for 1 year
            
            // Upload to S3
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,
                fullFileName,
                file.getInputStream(),
                metadata
            );

            // Here we are saving the file to S3, but S3 have Block public access (bucket settings) property
            // enabled so we cannot access the file directly from S3
            // We need to generate a pre-signed URL to access the file
            // Set the expiration time for the pre-signed URL
            amazonS3.putObject(putObjectRequest);
            
            // Generate URL
            String fileUrl = amazonS3.getUrl(bucketName, fullFileName).toString();
            
            // Update user's profile image URL
            user.setProfileImageUrl(fileUrl);

            userRepository.save(user);
            
            // logger.info("Profile image uploaded successfully for user: {}", email);
            response.put("message", "Profile image uploaded successfully");
            response.put("status", HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", email, e);
            response.put("message", "User not found");
            response.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            logger.error("Failed to upload profile image for user: {}", email, e);
            response.put("message", "Failed to upload profile image");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid file for user: {}", email, e);
            response.put("message", "Invalid file");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (AmazonServiceException e) {
            logger.error("S3 error while uploading profile image for user: {}", email, e);
            response.put("message", "Failed to upload to S3");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Unexpected error while uploading profile image for user: {}", email, e);
            response.put("message", "An unexpected error occurred");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}