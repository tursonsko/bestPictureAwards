package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.model.dto.request.AwardedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.request.RatedMovieRequestDto;
import com.backbase.bestPictureAwards.model.dto.response.AwardedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.RatedMovieResponseDto;
import com.backbase.bestPictureAwards.model.dto.response.TopTenMoviesResponseDto;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import com.backbase.bestPictureAwards.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AcademyAwardService {

    private static final Integer DIGITS_PLACES = 1;

    private final AcademyAwardRepository academyAwardRepository;
    private final ConfigProperties configProperties;

    public AcademyAwardService(AcademyAwardRepository academyAwardRepository, ConfigProperties configProperties) {
        this.academyAwardRepository = academyAwardRepository;
        this.configProperties = configProperties;
    }

    public AcademyAward findAcademyAwardById(Long id) {
        AcademyAward foundRecord = academyAwardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("record not found"));
        return foundRecord;
    }

    public List<AcademyAward> findAllAwardedAndBestPictureCatagory() {
        return academyAwardRepository.findAcademyAwardByAwardedAndCategory(
                AwardStatusEnum.valueOf(configProperties.getAwardedTypeYes()), configProperties.getCategoryBestPicture()
        );
    }

    public List<AcademyAward> findAllBestPictureCategoryMovies() {
        return academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture());
    }

    //::::DONE::::sprawdzanie czy wygral czy nie oscara - done
    public AwardedMovieResponseDto checkIfIsAwardedBestPicture(AwardedMovieRequestDto dto) throws AcademyAwardNotFoundException {
        AcademyAward movie = academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(
                dto.getMovieTitle(), String.valueOf(dto.getYear()), configProperties.getCategoryBestPicture())
                .orElseThrow(() -> new AcademyAwardNotFoundException("Movie \"" + dto.getMovieTitle() + "\" not found in database "));
        return new AwardedMovieResponseDto(movie);
    }

    //::::DONE::::top 10 po ratingu a potem po box office -
    public List<TopTenMoviesResponseDto> findTenTopRatedMoviesSortedByBoxOfficeValue() {
        return findAllBestPictureCategoryMovies().stream()
                .map(TopTenMoviesResponseDto::new)
                .sorted(Comparator.comparing(TopTenMoviesResponseDto::getRating, Comparator.reverseOrder())
                        .thenComparing(TopTenMoviesResponseDto::getBoxOffice, Comparator.reverseOrder()))
                .limit(10)
                .sorted(Comparator.comparing(TopTenMoviesResponseDto::getBoxOffice, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    //::::DONE::::srednia - done
    public RatedMovieResponseDto giveRateForNomineeToBestPictureMovie(RatedMovieRequestDto dto) throws AcademyAwardNotFoundException {
        AcademyAward movie = academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(
                dto.getMovieTitle(), String.valueOf(dto.getYear()), configProperties.getCategoryBestPicture())
                .orElseThrow(() -> new AcademyAwardNotFoundException("Movie \"" + dto.getMovieTitle() + "\" not found in database "));
        Long ratingTotalSum = movie.getRatingTotalSum();
        Integer providedRate = dto.getRate();
        Long votesNumber = movie.getVotesNumber();
        Double newRating = Utils.round(calculateMovieRating(ratingTotalSum, providedRate, votesNumber), DIGITS_PLACES);
        movie.setRating(newRating);
        movie.setRatingTotalSum(ratingTotalSum + providedRate);
        movie.setVotesNumber(votesNumber + 1L);
        academyAwardRepository.save(movie);
        return new RatedMovieResponseDto(movie);
    }

    private Double calculateMovieRating(Long ratingTotalSum, Integer providedRate, Long votesNumber) {
        if (providedRate <= 10 && providedRate >= 1) {
            if (votesNumber < 0) {
                throw new RuntimeException("nie moze byc liczba ocen mmiejsza niz 0");
            }
            return votesNumber == 0
                    ? Double.valueOf(providedRate)
                    : (double) (ratingTotalSum + providedRate) / (votesNumber + 1L);
        } else {
            throw new RuntimeException("wartosci tylko pomiedzy 1 a 10");
        }
    }
}