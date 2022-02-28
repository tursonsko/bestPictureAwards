package com.backbase.bestPictureAwards.model.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class MovieRequestDto {

    private String movieTitle;
    private Integer year;
    private Integer rate;

    public MovieRequestDto(String movieTitle, Integer year) {
        this.movieTitle = movieTitle;
        this.year = year;
    }

    public MovieRequestDto(String movieTitle, Integer year, Integer rate) {
        this.movieTitle = movieTitle;
        this.year = year;
        this.rate = rate;
    }
}