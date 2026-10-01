package com.godev.nailManagement.common.controllers;

import com.godev.nailManagement.common.dtos.ProcedureRequest;
import com.godev.nailManagement.common.dtos.ProcedureResponse;
import com.godev.nailManagement.domain.procedure.ProcedureService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/procedure")
public class ProcedureController {

    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @GetMapping
    public List<ProcedureResponse> findAll() {
        return procedureService.findAll();
    }

    @GetMapping(params = "id")
    public ProcedureResponse findById(@RequestParam UUID id) {
        return procedureService.findById(id);
    }

     
    @PostMapping
    public ProcedureResponse save(@RequestBody ProcedureRequest request) {
        return procedureService.save(request);
    }

     
    @DeleteMapping
    public void deleteById(@RequestParam UUID id) {
        procedureService.deleteById(id);
    }

     
    @PutMapping
    public ProcedureResponse put(@RequestBody ProcedureRequest request) {
        return procedureService.put(request);
    }

     
    @PatchMapping
    public ProcedureResponse patch(@RequestParam UUID id, @RequestBody ProcedureRequest request) {
        return procedureService.patch(id, request);
    }

}
