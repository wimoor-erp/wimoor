package com.wimoor.data.controller;


import com.wimoor.data.service.IDataMoveService;
import com.wimoor.data.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DataController {
    @Autowired
    IDataMoveService iDataMoveService;

    @GetMapping("/query")
    public void remote(String database){
        iDataMoveService.move(database,null );
    }

}