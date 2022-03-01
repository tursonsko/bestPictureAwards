package com.backbase.bestPictureAwards.model.dto.response;

import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO to return Top-10 rated movies
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopTenMoviesResponseDto {

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private String year;
    @JsonProperty("rating")
    private Double rating;
    @JsonProperty("boxOffice")
    private Integer boxOffice;

    public TopTenMoviesResponseDto(AcademyAward academyAward) {
        this.movieTitle = academyAward.getNominee();
        this.year = academyAward.getYear();
        this.boxOffice = academyAward.getBoxOffice();
        this.rating = academyAward.getRating();
    }
}