package com.backbase.bestPictureAwards.model.entity;

import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    //pola z .csv
    private String year;
    private String category;
    private String nominee;
    private String additionalInfo;
    @Enumerated(EnumType.STRING)
    private AwardStatusEnum awarded;

    //todo pole do ocen filmow, logika -> zliczane i wyciagana srednia
    private Double rating;
    private Long votesNumber;

    //todo jesli w requestcie w tytule filmu bedzie spacja to zamieniam na znak '+'


    /*taski
     1) Wniosek powinien wskazywać, czy film zdobył Oscara za „Najlepszy film”, biorąc pod uwagę
        tytuł filmu oparty na tym API i załączonym pliku CSV, który zawiera zwycięzców z 1927 do 2010.

     2) Powinno również umożliwiać użytkownikom ocenianie filmów

     3) dostarczanie ich listy 10 najwyżej ocenianych filmów uporządkowanych według wartości kasowej.
     */
}
