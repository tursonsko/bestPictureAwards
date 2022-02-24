package com.backbase.bestPictureAwards.controller;

import com.backbase.bestPictureAwards.service.AcademyAwardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AcademyAwardController {

    private final AcademyAwardService academyAwardService;

    public AcademyAwardController(AcademyAwardService academyAwardService) {
        this.academyAwardService = academyAwardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOneBrokerSettingsById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(academyAwardService.findAcademyAwardById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
