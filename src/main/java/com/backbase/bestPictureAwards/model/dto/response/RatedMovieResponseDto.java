package com.backbase.bestPictureAwards.model.dto.response;

import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatedMovieResponseDto {

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private String year;
    @JsonProperty("category")
    private String category;
    @JsonProperty("rating")
    private Double rating;
    @JsonProperty("votesNumber")
    private Long votesNumber;

    public RatedMovieResponseDto(AcademyAward academyAward) {
        this.movieTitle = academyAward.getNominee();
        this.year = academyAward.getYear();
        this.category = academyAward.getCategory();
        this.rating = academyAward.getRating();
        this.votesNumber = academyAward.getVotesNumber();
    }

}
