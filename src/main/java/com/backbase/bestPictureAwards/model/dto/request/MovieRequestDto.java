package com.backbase.bestPictureAwards.model.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Parent class for both request DTO's
 */
@Getter
@Setter
public abstract class MovieRequestDto {

    private String movieTitle;
    private Integer year;
    private Integer rate;
}