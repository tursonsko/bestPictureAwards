package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.model.dto.response.OmdbApiResponseDto;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import com.backbase.bestPictureAwards.util.Utils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FillUpDatabaseService {

    private final AcademyAwardRepository academyAwardRepository;
    private final ConfigProperties configProperties;
    private final RestTemplate restTemplateWithConnectionAndReadTimeout;
    private final Gson gsonWithPrettyPrinting;

    public FillUpDatabaseService(AcademyAwardRepository academyAwardRepository, ConfigProperties configProperties,
                                 RestTemplate restTemplateWithConnectionAndReadTimeout, Gson gsonWithPrettyPrinting) {
        this.academyAwardRepository = academyAwardRepository;
        this.configProperties = configProperties;
        this.restTemplateWithConnectionAndReadTimeout = restTemplateWithConnectionAndReadTimeout;
        this.gsonWithPrettyPrinting = gsonWithPrettyPrinting;
    }

    public List<AcademyAward> findAllBestPictureCategoryMovies() {
        return academyAwardRepository.findAcademyAwardByAwarded(configProperties.getCategoryBestPicture());
    }

    public void fillUpBestPicturesBoxOfficeValue(String apiKey) {

        List<String> bestPicturesTitles = findAllBestPictureCategoryMovies().stream()
                .map(AcademyAward::getNominee)
                .collect(Collectors.toList());

        List<OmdbApiResponseDto> apiResponseList = new ArrayList<>();
        Map<String, Integer> ombdResponseDtoMap = new HashMap<>();

        bestPicturesTitles.forEach(movieTitle -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.ALL));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            String urlTemplate = UriComponentsBuilder.fromHttpUrl(configProperties.getOmdbApiUrl())
                    .queryParam(configProperties.getOmdbApiTitleParamName(),
                            "{" + configProperties.getOmdbApiTitleParamName() + "}")
                    .queryParam(configProperties.getOmdbApiKeyParamName(),
                            "{" + configProperties.getOmdbApiKeyParamName() + "}")
                    .build()
                    .toUriString();
            Map<String, String> params = new HashMap<>();
            params.put(configProperties.getOmdbApiTitleParamName(), movieTitle);
            params.put(configProperties.getOmdbApiKeyParamName(), apiKey);
            ResponseEntity<OmdbApiResponseDto> response = null;
            try {
                response = restTemplateWithConnectionAndReadTimeout.exchange(urlTemplate, HttpMethod.GET, entity, OmdbApiResponseDto.class, params);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException();
            }
//            log.info(gsonWithPrettyPrinting.toJson(response.getBody()));

            String movieTitleToUpdate = response.getBody().getTitle();
            String boxOfficeValueToParse = response.getBody().getBoxOffice();
            Integer boxOfficeValueToFill = Utils.parseValueToNumeric(boxOfficeValueToParse);
            ombdResponseDtoMap.put(movieTitleToUpdate, boxOfficeValueToFill);
            ombdResponseDtoMap.entrySet().removeIf(curr -> curr.getKey() == null);

            apiResponseList.add(response.getBody());

        });
        apiResponseList.removeIf(omdbApiResponseDto -> omdbApiResponseDto.getTitle() == null);


//        Map<String, Integer> apiResponseMap = apiResponseList.stream()
//                .collect(Collectors.toMap(OmdbApiResponseDto::getTitle.concat(OmdbApiResponseDto::getYear),
//                        omdbApiResponseDto -> Utils.parseValueToNumeric(omdbApiResponseDto.getBoxOffice()),
//                        (key1, key2) -> key1));

        /*
         * lista obiektow (title, year, box office) -> foreach -> obiekt.title i obiekt.year i tym uderzam do bazy
         * -> jesli znajdzie to w tym filmie zapisac boxOffice, zebym mogl z bazy wyszukac liste po title i year
         * a potem zebym mogl zapisac w bazie boxOffice tego filmu ktory znajde w jsonie?
         *
         * Cleopatra z 1934 (N/A) i 1963(57,777,778) - czyli trzeba zapytac api na podstawie tytlu + yar
         * */

//        apiResponseList.forEach(System.out::println);
//        for (Map.Entry<String, Integer> entry : apiResponseMap.entrySet()) {
//            System.out.println(entry.getKey() + " -> " + entry.getValue());
//        }

        List<OmdbApiResponseDto> list = apiResponseList.stream().filter(omdbApiResponseDto ->
                omdbApiResponseDto.getTitle().equals("Cleopatra")).collect(Collectors.toList());
        log.info(String.valueOf(list.size()));

//        log.info(String.valueOf(ombdResponseDtoMap));

        //printowanie mapy responsow wszystkich
//        for (Map.Entry<String, Integer> entry : ombdResponseDtoMap.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
        List<String> movieTitles = new ArrayList<>(ombdResponseDtoMap.keySet()).stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        movieTitles.forEach(System.out::println);
//        List<Integer> movieBoxOffice = new ArrayList<>(ombdResponseDtoMap.values());
//        log.info(String.valueOf(movieTitles));
//        log.info(String.valueOf(movieBoxOffice));

        List<AcademyAward> foundMoviesByTitlesFromOmdbApi = academyAwardRepository
                .findAcademyAwardByNomineeInAndCategory(movieTitles, configProperties.getCategoryBestPicture());
        foundMoviesByTitlesFromOmdbApi.forEach(movie -> {
            String movieTitle = movie.getNominee();
            movie.setBoxOffice(ombdResponseDtoMap.get(movieTitle));
        });

        academyAwardRepository.saveAll(foundMoviesByTitlesFromOmdbApi);

//        foundMoviesByTitlesFromOmdbApi.forEach(System.out::println);
        log.info("koniec");

        System.out.println("-------------------------");
        //printy do sprawdzenia czego brakuje - 5 pol
        //mapa 479 sztuk
//        for (Map.Entry<String, Integer> entry : apiResponseMap.entrySet()) {
//            System.out.println(entry.getKey());
//        }
        System.out.println("-------------------------");
        System.out.println("-------------------------");

        apiResponseList.forEach(omdbApiResponseDto -> {
            System.out.println(omdbApiResponseDto.getTitle());
        });

        System.out.println("-------------------------");

        changeAllBoxOfficeNullValues();
    }

    //hack narazie tak ma byc jesli nie uda sie rozwiazac problemu
    private void changeAllBoxOfficeNullValues() {
        List<AcademyAward> listToUpdateNullBoxOfficeValues = findAllBestPictureCategoryMovies().stream()
                .filter(academyAward -> academyAward.getBoxOffice() == null).collect(Collectors.toList());
                listToUpdateNullBoxOfficeValues.forEach(academyAward -> academyAward.setBoxOffice(0));
        academyAwardRepository.saveAll(listToUpdateNullBoxOfficeValues);
    }
}
