package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.configuration.ConfigProperties;
import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AcademyAwardService {

    private final AcademyAwardRepository academyAwardRepository;
    private final ConfigProperties configProperties;

    public AcademyAwardService(AcademyAwardRepository academyAwardRepository, ConfigProperties configProperties) {
        this.academyAwardRepository = academyAwardRepository;
        this.configProperties = configProperties;
    }

    public AcademyAward findAcademyAwardById(Long id) {
        AcademyAward foundRecord =  academyAwardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("record not found"));
        log.info(foundRecord.toString());
        return foundRecord;
    }

    public List<AcademyAward> findAllAwardedAndBestPictureCatagory() {
        List<AcademyAward> academyAwards = academyAwardRepository.findAcademyAwardByAwardedAndCategory(AwardStatusEnum.valueOf(configProperties.getAwarded_type()), configProperties.getCategory());
        return academyAwards;
    }

    //todo implementacja
    //    metoda sprawdzajaca baze i omdb (
    //      potrzebuje uderzyc do bazy za pomocą tytułu (potestować jak baza czyta male/duze litery i jakies apostrofy, kropki itd.)
    //     jesli tytul filmu a bazie && w api && jest awarded:YES w bazie to zwrocic że jest (jakiś model najlepiej dto response)

    public void checkIfIsAwardedBestPicture() {

    }


    //todo metoda Uzupelniająca Kolumnę BoxOffice W Bazie Tylko Te Ktore Sa Best Picture && Awarded:YES
    public void metodaUzupelniającaKolumnęBoxOfficeWBazieTylkoTeKtoreSaBestPictureIAwardedYES() {

    }

    //todo metoda zwracajaca liste top10 ocenionych filmow (mozna zreobić osobną kolumnę z ocenami z imdb np, zebym mial sortowanie po ocenach moich i imdb osobno(?))
    // sortowanie ma byc po polu boxOffice więc najpierw muszę zasilic z OMDB API te kolumnę w bazie
    public void todo_1() {

    }
}
