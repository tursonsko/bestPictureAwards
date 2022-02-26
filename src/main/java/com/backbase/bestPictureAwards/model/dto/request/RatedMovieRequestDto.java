package com.backbase.bestPictureAwards.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RatedMovieRequestDto {

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("rate")
    private Integer rate;
}
