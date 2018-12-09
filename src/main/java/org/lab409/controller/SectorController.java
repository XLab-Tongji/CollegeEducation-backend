package org.lab409.controller;

import org.lab409.entity.ResponseMessage;
import org.lab409.entity.Sector;
import org.lab409.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jiao on 2018/12/9.
 */

@RestController
public class SectorController {

    @Autowired
    SectorService sectorService;

    //get all sectors
    @RequestMapping(value = "sector/get",method = RequestMethod.GET)
    public ResponseMessage getSectors(){
        List<Sector> sectors=sectorService.getSectors();
        return new ResponseMessage<>(sectors).success();
    }
}
