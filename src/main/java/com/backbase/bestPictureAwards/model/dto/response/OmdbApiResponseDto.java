package com.backbase.bestPictureAwards.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO used to receive via REST data from Omdb Api
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbApiResponseDto {

    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("BoxOffice")
    private String boxOffice;
}