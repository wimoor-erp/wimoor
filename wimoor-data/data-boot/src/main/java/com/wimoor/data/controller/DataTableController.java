package com.wimoor.data.controller;


import com.wimoor.data.service.IDataMoveService;
import com.wimoor.data.service.IDataTableMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataTableController {
    @Autowired
    IDataTableMoveService iDataTableMoveService;

    @GetMapping("/move")
    public void remote(){
        iDataTableMoveService.moveSettlement();
    }

}