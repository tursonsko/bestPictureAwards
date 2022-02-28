package com.backbase.bestPictureAwards.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AwardedMovieRequestDto extends MovieRequestDto {

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private Integer year;

    public AwardedMovieRequestDto(String movieTitle, Integer year) {
        super(movieTitle, year);
    }
}