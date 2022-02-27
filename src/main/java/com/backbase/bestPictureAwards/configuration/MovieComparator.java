/*
package com.backbase.bestPictureAwards.configuration;

import com.backbase.bestPictureAwards.model.dto.response.TopTenMoviesResponseDto;

import java.util.Comparator;

public class MovieComparator implements Comparator<TopTenMoviesResponseDto> {
    @Override
    public int compare(TopTenMoviesResponseDto o1, TopTenMoviesResponseDto o2) {
        int value1 = o1.getRating().compareTo(o2.getRating());
        if (value1 == 0) {
            int value2 = o1.faculty.compareTo(o2.faculty);
            if (value2 == 0) {
                return o1.building.compareTo(o2.building);
            } else {
                return value2;
            }
        }
        return value1;
    }
}
*/
