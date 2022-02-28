package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.model.dto.response.OmdbApiResponseDto;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import com.backbase.bestPictureAwards.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public List<AcademyAward> findAllBestPictureCategoryMovies() throws AcademyAwardNotFoundException {
        List<AcademyAward> allBestPiocturesMovies = academyAwardRepository
                .findAcademyAwardByAwarded(configProperties.getCategoryBestPicture());
        if (allBestPiocturesMovies.size() == 0) {
            throw new AcademyAwardNotFoundException("Movies not found in database ");
        }
        return allBestPiocturesMovies;
    }

    public void fillUpBestPicturesBoxOfficeValue(String apiKey) throws AcademyAwardNotFoundException {
        List<String> bestPicturesTitles = findAllBestPictureCategoryMovies().stream()
                .map(AcademyAward::getNominee)
                .collect(Collectors.toList());
        Map<String, Integer> ombdResponseDtoMap = getAllBoxOfficeValuesFromOmdbApi(bestPicturesTitles, apiKey);
        List<String> movieTitles = new ArrayList<>(ombdResponseDtoMap.keySet()).stream().map(String::toLowerCase)
                .collect(Collectors.toList());
        updateAllBoxOfficeValue(movieTitles, ombdResponseDtoMap);
    }

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
        ombdResponseDtoMap.entrySet().removeIf(omdbResponseDto -> omdbResponseDto.getKey() == null);
        return ombdResponseDtoMap;
    }

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

    private void changeAllBoxOfficeNullValues() throws AcademyAwardNotFoundException {
        List<AcademyAward> listToUpdateNullBoxOfficeValues = findAllBestPictureCategoryMovies().stream()
                .filter(academyAward -> academyAward.getBoxOffice() == null).collect(Collectors.toList());
        listToUpdateNullBoxOfficeValues.forEach(academyAward -> academyAward.setBoxOffice(0));
        academyAwardRepository.saveAll(listToUpdateNullBoxOfficeValues);
    }
}