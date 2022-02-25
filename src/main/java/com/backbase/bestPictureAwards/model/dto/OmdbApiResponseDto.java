package com.backbase.bestPictureAwards.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbApiResponseDto {

    @JsonProperty("Title")
    private String title;
    @JsonProperty("BoxOffice")
    private String boxOffice;
}
