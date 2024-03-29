package org.example.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.example.clients.RestClient;
import org.example.dto.RequestDto;
import org.example.entity.PostEntity;
import org.example.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class AppService {
    @Value("${url}")
    private String url;

    @Autowired
    RestClient restClient;
    
    @Autowired
    PostRepository postRepository;

    public Map<String, ?> serve(RequestDto requestDto) {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<Object> restFuture = CompletableFuture.supplyAsync(() -> {
            try {
                log.info("API_URL: " + url);  
                return restClient.getData(url);
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        });

        CompletableFuture<PostEntity> createPostFuture = CompletableFuture.supplyAsync(() -> {
            PostEntity postEntity = new PostEntity(requestDto.getPostName(), requestDto.getPostContents());
            return postRepository.save(postEntity);
        });
        CompletableFuture.allOf(createPostFuture, restFuture).join();
        try {
            response.put("rest_outbound", restFuture.get());
        } catch (Exception e) {
            log.error("Error in restFuture", e);
            e.printStackTrace();
            response.put("rest_outbound", e.getMessage());
        }
        try {
            response.put("dp_post", createPostFuture.get());
        } catch (Exception e) {
            log.error("Error in createPostFuture", e);
            e.printStackTrace();
            response.put("dp_post", e.getMessage());
        }
        return response;
    }
}
