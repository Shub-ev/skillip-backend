package com.skillip.skillip_backend.services;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.skillip.skillip_backend.common.dtos.LoginRequestDTO;
import com.skillip.skillip_backend.common.dtos.UserDTO;
import com.skillip.skillip_backend.common.exceptions.IncorrectPasswordException;
import com.skillip.skillip_backend.common.exceptions.UserNotFoundException;
import com.skillip.skillip_backend.models.User;
import com.skillip.skillip_backend.repositories.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;
    private final String bucketName;

    // set the expiration time for the presigned URL 
    private final Long expirationTimeInMilis = 1000L * 60 * 60 * 24 * 7; // 7 days

    // Constructor injection is preferred over field injection for better
    // testability and immutability
    // and to avoid issues with circular dependencies.
    public AuthService(UserRepository userRepository, AmazonS3 amazonS3,
            @Value("${application.bucket.name}") String bucketName) {
        this.userRepository = userRepository;
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    // separate method to generate persigned URL's
    public String generatePresignedURL(User user) {
        // Get user profile image URL from the database
        // and extract the file address from the URL
        String profileImageUrl = user.getProfileImageUrl();
        profileImageUrl = profileImageUrl.substring(profileImageUrl.indexOf("profile-images/"));
        
        // S3 have Block public access (bucket settings) property enabled,
        // so we cannot directly use the URL to access the image
        // we have to generate a "pre-signed URL" to access the image
        String preSignedURL;
        try {
            preSignedURL = amazonS3.generatePresignedUrl(
                    bucketName,
                    // here we don't have to pass complete url instread we have to pass S3 address
                    profileImageUrl,
                    new Date(System.currentTimeMillis() + expirationTimeInMilis)).toString();
        } catch (AmazonServiceException e) {
            // Handle the exception if the presigned URL generation fails
            System.err.println("Error generating presigned URL: " + e.getMessage());
            throw new RuntimeException("Error generating presigned URL", e);
        }

        return preSignedURL;
    }

    public UserDTO loginUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException(loginRequestDTO.getEmail()));

        if (user.getProfileImageUrl() == null || user.getProfileImageUrl().isEmpty()) {
            return new UserDTO(user.getUsername(), user.getEmail(), null, null);
        }

        String presignedUrl = generatePresignedURL(user);
        // Check if the password is correct
        // ***** Note: In a real-world application, you should hash the password and
        // compare the hashes *****
        if (user.getPassword().equals(loginRequestDTO.getPassword())) {
            return new UserDTO(user.getUsername(), user.getEmail(), presignedUrl,
                    new Date(System.currentTimeMillis() + expirationTimeInMilis).toString());
        } else {
            throw new IncorrectPasswordException("Incorrect Password!");
        }

    }
}
