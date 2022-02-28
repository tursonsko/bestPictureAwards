package com.backbase.bestPictureAwards.model.dto.response;

import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AwardedMovieResponseDto {

    private static final String MOVIE_AWARDED_INFO = "An Oscar-winning movie";
    private static final String MOVIE_NOT_AWARDED_INFO = "The movie did not win an Oscar";

    @JsonProperty("movieTitle")
    private String movieTitle;
    @JsonProperty("year")
    private String year;
    @JsonProperty("category")
    private String category;
    @JsonProperty("awarded")
    private String awarded;

    public AwardedMovieResponseDto(AcademyAward academyAward) {
        this.movieTitle = academyAward.getNominee();
        this.year = academyAward.getYear();
        this.category = academyAward.getCategory();
        this.awarded = academyAward.getAwarded().equals(AwardStatusEnum.YES)
                ? MOVIE_AWARDED_INFO
                : MOVIE_NOT_AWARDED_INFO;
    }
}