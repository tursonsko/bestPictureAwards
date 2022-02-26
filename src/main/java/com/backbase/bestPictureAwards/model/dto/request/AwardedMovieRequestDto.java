package com.backbase.bestPictureAwards.model.dto.request;

import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AwardedMovieRequestDto {

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private Integer year;

}
