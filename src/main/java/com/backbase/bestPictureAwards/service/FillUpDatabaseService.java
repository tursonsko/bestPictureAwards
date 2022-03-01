package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.model.dto.response.OmdbApiResponseDto;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import com.backbase.bestPictureAwards.util.Utils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This service is implementation of auto filling academy_awards table with Box Office value,
 * which allow user to request all Best Picture movies from table to Omdb Api and get as much as possible
 * Box Office values.
 */
@Service
public class FillUpDatabaseService {

    private final AcademyAwardRepository academyAwardRepository;
    private final ConfigProperties configProperties;
    private final RestTemplate restTemplateWithConnectionAndReadTimeout;
    private final String urlTemplate;
    private final HttpEntity<?> httpEntity;

    public FillUpDatabaseService(AcademyAwardRepository academyAwardRepository, ConfigProperties configProperties,
             RestTemplate restTemplateWithConnectionAndReadTimeout, String urlTemplate, HttpEntity<?> httpEntity) {
        this.academyAwardRepository = academyAwardRepository;
        this.configProperties = configProperties;
        this.restTemplateWithConnectionAndReadTimeout = restTemplateWithConnectionAndReadTimeout;
        this.urlTemplate = urlTemplate;
        this.httpEntity = httpEntity;
    }

    /**
     * Main method which is responsible for find all Best Picture category movies ("nominee" column)
     * from "academy_awards" table and serach all of titles in Omdb API to fill up values in box_office column
     * It use findAllBestPictureCategoryMovies() method which find all movies and map this list to list containing
     * only move titles used in getAllBoxOfficeValuesFromOmdbApi() to get Box Office values for each movie title.
     * Method works only for "Best Picture" category movies.
     *
     * @param apiKey String - Omdb (external service) api key
     * @throws AcademyAwardNotFoundException
     */
    public void fillUpBestPicturesBoxOfficeValue(String apiKey) throws AcademyAwardNotFoundException {
        List<String> bestPicturesTitles = findAllBestPictureCategoryMovies().stream()
                .map(AcademyAward::getNominee)
                .collect(Collectors.toList());
        Map<String, Integer> ombdResponseDtoMap = getAllBoxOfficeValuesFromOmdbApi(bestPicturesTitles, apiKey);
        List<String> movieTitles = new ArrayList<>(ombdResponseDtoMap.keySet()).stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        updateAllBoxOfficeValue(movieTitles, ombdResponseDtoMap);
    }

    /**
     * Method used to find all "Best Picture" category movies from academy_award table.
     *
     * @return List<AcademyAward> - list af all "Best Picture" category movies
     * @throws AcademyAwardNotFoundException - if no requested records in table (list size is equal 0)
     */
    public List<AcademyAward> findAllBestPictureCategoryMovies() throws AcademyAwardNotFoundException {
        List<AcademyAward> allBestPiocturesMovies = academyAwardRepository
                .findAcademyAwardByAwarded(configProperties.getCategoryBestPicture());
        if (allBestPiocturesMovies.size() == 0) {
            throw new AcademyAwardNotFoundException("Movies not found in database ");
        }
        return allBestPiocturesMovies;
    }

    /**
     * Method responsible for get all pairs "Movie Title" - "Box Office" from Omdb Api related on titles from Databaase
     *
     * @param bestPicturesTitles List<String> -
     * @param apiKey String - Omdb (external service) api key
     * @return Map<String, Integer> - all found by movie title box_office values grouped in Map -
     * (Key(String) - Movie Title; Value(Integer) - Box Office value of movie)
     */
    private Map<String, Integer> getAllBoxOfficeValuesFromOmdbApi(List<String> bestPicturesTitles, String apiKey) {
        Map<String, Integer> ombdResponseDtoMap = new HashMap<>();
        bestPicturesTitles.forEach(movieTitle -> {
            Map<String, String> params = new HashMap<>();
            params.put(configProperties.getOmdbApiTitleParamName(), movieTitle);
            params.put(configProperties.getOmdbApiKeyParamName(), apiKey);
            ResponseEntity<OmdbApiResponseDto> response = restTemplateWithConnectionAndReadTimeout
                    .exchange(urlTemplate, HttpMethod.GET, httpEntity, OmdbApiResponseDto.class, params);
            if (response.getBody() != null || response.getStatusCode().is2xxSuccessful()) {
                String movieTitleToUpdate = response.getBody().getTitle();
                Integer boxOfficeValueToUpdate = Utils.parseValueToNumeric(response.getBody().getBoxOffice());
                ombdResponseDtoMap.put(movieTitleToUpdate, boxOfficeValueToUpdate);
            }
        });
        removeNullKeysFromOmdbResponseMap(ombdResponseDtoMap);
        return ombdResponseDtoMap;
    }

    /**
     * This method is responsible for "cleaning" the api response map from null keys if it will find any of it
     *
     * @param ombdResponseDtoMap Map<String, Integer>
     */
    private void removeNullKeysFromOmdbResponseMap(Map<String, Integer> ombdResponseDtoMap) {
        ombdResponseDtoMap.entrySet().removeIf(omdbResponseDto -> omdbResponseDto.getKey() == null);
    }

    /**
     * This method is responsible for pre-final operation
     * (final is in method changeAllBoxOfficeNullValues() - below this one)
     * of this service. It finds all movies matched
     * with movie titles list from Omdb Api.
     *
     * @param movieTitles List<String> - movie Titles from Omdb Api - this list come from Map ombdResponseDtoMap
     * @param ombdResponseDtoMap Map<String, Integer> - all found by movie title box_office values grouped in Map structure,
     *                           method uses only Key (Box Office values) from this map
     * @throws AcademyAwardNotFoundException
     */
    private void updateAllBoxOfficeValue(List<String> movieTitles, Map<String, Integer> ombdResponseDtoMap)
            throws AcademyAwardNotFoundException {
        List<AcademyAward> foundMoviesByTitlesFromOmdbApi = academyAwardRepository
                .findAcademyAwardByNomineeInAndCategory(movieTitles, configProperties.getCategoryBestPicture());
        foundMoviesByTitlesFromOmdbApi.forEach(movie -> {
            String movieTitle = movie.getNominee();
            movie.setBoxOffice(ombdResponseDtoMap.get(movieTitle));
        });
        academyAwardRepository.saveAll(foundMoviesByTitlesFromOmdbApi);
        changeAllBoxOfficeNullValues();
    }

    /**
     * This is hack method (workaround), cause this service does not put only proper values into database.
     * To avoid null in Box Office column, this method is provided to replace all nulls
     * (for exmaple because of not found title in Ombd Api) to value "0" (Integer)
     *
     * @throws AcademyAwardNotFoundException
     */
    private void changeAllBoxOfficeNullValues() throws AcademyAwardNotFoundException {
        List<AcademyAward> listToUpdateNullBoxOfficeValues = findAllBestPictureCategoryMovies().stream()
                .filter(academyAward -> academyAward.getBoxOffice() == null).collect(Collectors.toList());
        listToUpdateNullBoxOfficeValues.forEach(academyAward -> academyAward.setBoxOffice(0));
        academyAwardRepository.saveAll(listToUpdateNullBoxOfficeValues);
    }
}