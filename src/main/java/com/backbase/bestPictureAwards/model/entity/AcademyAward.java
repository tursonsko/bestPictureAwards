package com.backbase.bestPictureAwards.model.entity;

import com.backbase.bestPictureAwards.enums.AwardStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "academy_awards")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AcademyAward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String year;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String category;

    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String nominee;

    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String additionalInfo;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private AwardStatusEnum awarded;

    @Column(nullable = false, columnDefinition = "double default 0")
    private Double rating;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long ratingTotalSum;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long votesNumber;

    @Column
    private Integer boxOffice;
}