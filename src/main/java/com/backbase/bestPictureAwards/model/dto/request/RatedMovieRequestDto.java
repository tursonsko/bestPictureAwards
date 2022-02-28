package com.backbase.bestPictureAwards.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RatedMovieRequestDto extends MovieRequestDto {

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("rate")
    private Integer rate;

    public RatedMovieRequestDto(String movieTitle, Integer year, Integer rate) {
        super(movieTitle, year, rate);
    }
}