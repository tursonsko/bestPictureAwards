package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.exception.WrongRateException;
import com.backbase.bestPictureAwards.model.dto.request.MovieRequestDto;
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

/**
 * Service used to accomplish 3 challenge tasks for Backbase
 */
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

    /**
     * Method returns from database movie which has category "Best Picture".
     * Movie is find by title and by year of production.
     *
     * @param dto MovieRequestDto - DTO used to REST requests
     * @return AcademyAward single entity object representation from academy_awards table
     * @throws AcademyAwardNotFoundException
     */
    public AcademyAward findAcademyAwardByNomineeAndYearLikeAndCategory(MovieRequestDto dto) throws AcademyAwardNotFoundException {
        return academyAwardRepository.findAcademyAwardByNomineeAndYearLikeAndCategory(
                        dto.getMovieTitle(), String.valueOf(dto.getYear()), configProperties.getCategoryBestPicture())
                .orElseThrow(() -> new AcademyAwardNotFoundException("Movie \"" + dto.getMovieTitle() + "\" not found in database "));
    }

    /**
     * Method to fin all movies from academy_award table with category "Best Picture"
     *
     * @return List<AcademyAward>
     * @throws AcademyAwardNotFoundException
     */
    public List<AcademyAward> findAllBestPictureCategoryMovies() throws AcademyAwardNotFoundException {
        List<AcademyAward> allBestPiocturesMovies = academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture());
        if (allBestPiocturesMovies.size() == 0) {
            throw new AcademyAwardNotFoundException("Movies not found in database ");
        }
        return allBestPiocturesMovies;
    }

    //::::DONE::::sprawdzanie czy wygral czy nie oscara - done

    /**
     * Method to check if selected movie was awarded with Oscar
     * @param dto MovieRequestDto
     * @return AwardedMovieResponseDto
     * @throws AcademyAwardNotFoundException
     */
    public AwardedMovieResponseDto checkIfIsAwardedBestPicture(MovieRequestDto dto) throws AcademyAwardNotFoundException {
        AcademyAward movie = findAcademyAwardByNomineeAndYearLikeAndCategory(dto);
        return new AwardedMovieResponseDto(movie);
    }

    //::::DONE::::top 10 po ratingu a potem po box office -

    /**
     * Method used to get top-10 rated movies from academy_award table with category "Best Picture"
     * ordered by "Box Office" value
     * @return List<TopTenMoviesResponseDto>
     * @throws AcademyAwardNotFoundException
     */
    public List<TopTenMoviesResponseDto> findTenTopRatedMoviesSortedByBoxOfficeValue() throws AcademyAwardNotFoundException {
        List<AcademyAward> allMBestPictureMoviesList = findAllBestPictureCategoryMovies();
        return allMBestPictureMoviesList.stream()
                .map(TopTenMoviesResponseDto::new)
                .sorted(Comparator.comparing(TopTenMoviesResponseDto::getRating, Comparator.reverseOrder()))
                .limit(10)
                .sorted(Comparator.comparing(TopTenMoviesResponseDto::getBoxOffice, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    //::::DONE::::srednia - done

    /**
     * Method allows user to give rate for selected movie from academy_award table with category "Best Picture"
     * @param dto MovieRequestDto
     * @return RatedMovieResponseDto
     * @throws AcademyAwardNotFoundException
     * @throws WrongRateException
     */
    public RatedMovieResponseDto giveRateForNomineeToBestPictureMovie(MovieRequestDto dto)
            throws AcademyAwardNotFoundException, WrongRateException {
        AcademyAward movie = findAcademyAwardByNomineeAndYearLikeAndCategory(dto);
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

    /**
     * Helper method for giveRateForNomineeToBestPictureMovie() to calculate average
     * rating based on past and new on get from user.
     * @param ratingTotalSum Long
     * @param providedRate Integer
     * @param votesNumber Long
     * @return Double (new average value of rating)
     * @throws WrongRateException
     */
    private Double calculateMovieRating(Long ratingTotalSum, Integer providedRate, Long votesNumber)
            throws WrongRateException {
        if (providedRate <= 10 && providedRate >= 1) {
            if (votesNumber < 0) {
                log.info("Votes Number in database database is less than " + votesNumber);
                throw new WrongRateException("Sorry, something went wrong, don't worry, this bug has been reported :)");
            }
            return votesNumber == 0
                    ? Double.valueOf(providedRate)
                    : (double) (ratingTotalSum + providedRate) / (votesNumber + 1L);
        } else {
            throw new WrongRateException("You cannot provide rate " + providedRate
                    + ". The scale is between 1-10");
        }
    }
}