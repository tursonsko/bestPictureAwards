package com.backbase.bestPictureAwards.service;

import com.backbase.bestPictureAwards.model.entity.AcademyAward;
import com.backbase.bestPictureAwards.repository.AcademyAwardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AcademyAwardService {

    private final AcademyAwardRepository academyAwardRepository;

    public AcademyAwardService(AcademyAwardRepository academyAwardRepository) {
        this.academyAwardRepository = academyAwardRepository;
    }

    public AcademyAward findAcademyAwardById(Long id) {
        AcademyAward foundRecord =  academyAwardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("record not found"));
        log.info(foundRecord.toString());
        return foundRecord;
    }
}
