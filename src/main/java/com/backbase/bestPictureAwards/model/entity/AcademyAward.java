package com.backbase.bestPictureAwards.model.entity;

import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "academy_awards_temp")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AcademyAward {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

//    private Integer boxValue;
    //todo to pole bedzie dzialac ale najpierw parser stringa na inta, narazie wylaczone
    //todo zamiana z jsona kwoty na Integera
/*    String boxOffice = "$70,099,045";
    Integer boxOffice1 = Integer.valueOf(boxOffice.replaceAll("[^0-9]", ""));*/

    //todo jesli w requestcie w tytule filmu bedzie spacja to zamieniam na znak '+'
/*    String movieTitle = "Kill Bill 2";
    String movieTitleParsedToPathVariable = movieTitle.replaceAll("\\s+","+");*/


    /*taski
     1) Wniosek powinien wskazywać, czy film zdobył Oscara za „Najlepszy film”, biorąc pod uwagę
        tytuł filmu oparty na tym API i załączonym pliku CSV, który zawiera zwycięzców z 1927 do 2010.

     2) Powinno również umożliwiać użytkownikom ocenianie filmów

     3) dostarczanie ich listy 10 najwyżej ocenianych filmów uporządkowanych według wartości kasowej.
     */

//todo do dokumentacji sqlka
/*    CREATE TABLE academy_awards (
            id bigint NOT NULL AUTO_INCREMENT,
            year VARCHAR(20) NOT NULL,
    category VARCHAR(255) NOT NULL,
    nominee VARCHAR(1000) NOT NULL,
    additional_info VARCHAR(1000) NOT NULL,
    awarded VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);*/
}
