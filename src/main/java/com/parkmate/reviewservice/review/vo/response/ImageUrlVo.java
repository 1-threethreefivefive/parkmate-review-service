package com.parkmate.reviewservice.review.vo.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ImageUrlVo {

    private String imageUrl;

    private ImageUrlVo(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ImageUrlVo of(String imageUrl) {
        return new ImageUrlVo(imageUrl);
    }

    public static List<ImageUrlVo> from(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return List.of();
        }
        return urls.stream()
                .map(ImageUrlVo::of)
                .collect(Collectors.toList());
    }
}