package com.backbase.bestPictureAwards.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwardedMovieRequestDto extends MovieRequestDto {

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private Integer year;
}