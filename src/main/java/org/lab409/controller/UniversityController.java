package org.lab409.controller;

import org.lab409.entity.MajorEntity;
import org.lab409.entity.ResponseMessage;
import org.lab409.entity.UniversityEntity;
import org.lab409.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @RequestMapping(path = "register/university", method = RequestMethod.GET)
    public ResponseMessage universityController() {

        List<UniversityEntity> universities = universityService.getUniversities();
        return new ResponseMessage<>(universities).success();
    }

    @RequestMapping(path = "register/university/{universityID}",method = RequestMethod.GET)
    public ResponseMessage majorByUniversityController(@PathVariable("universityID") Integer universityID) {
        List<MajorEntity> majors = universityService.getMajorByUniversity(universityID);
        return new ResponseMessage<>(majors).success();
    }
}
