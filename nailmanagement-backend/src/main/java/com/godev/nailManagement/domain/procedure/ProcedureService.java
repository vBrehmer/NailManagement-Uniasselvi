package com.godev.nailManagement.domain.procedure;

import com.godev.nailManagement.common.dtos.ProcedureResponse;
import com.godev.nailManagement.common.dtos.ProcedureRequest;
import com.godev.nailManagement.common.exceptions.ProcedureNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    public ProcedureService(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }
    public List<ProcedureResponse> findAll() {
        return procedureRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProcedureResponse findById(UUID id) {
        return toResponse(procedureRepository.findById(id).orElseThrow(() -> new ProcedureNotFoundException("Procedure not found!")));
    }

    public ProcedureResponse save(ProcedureRequest request) {
        Procedure proc = new Procedure();
        proc.setName(request.name());
        proc.setValue(request.value());

        return toResponse(procedureRepository.save(proc));
    }

    public ProcedureResponse put(ProcedureRequest request) {
        Procedure proc = procedureRepository.findByNameIgnoreCase(request.name()).orElseThrow(() -> new ProcedureNotFoundException("Procedure not found!"));
        proc.setName(request.name());
        proc.setValue(request.value());
        return toResponse(procedureRepository.save(proc));
    }

    public ProcedureResponse patch(UUID id, ProcedureRequest request) {
        validatePatch(request);

        Procedure p = procedureRepository.findById(id).orElseThrow(() -> new ProcedureNotFoundException("Procedure not found!"));
        if (request.value() != null && request.value() > p.getValue()) {
            p.setValue(request.value());
        }
        if (request.name()!= null && !request.name().isBlank()) {
            p.setName(request.name());
        }
        return toResponse(procedureRepository.save(p));
    }

    public void deleteById(UUID id) {
        if (procedureRepository.existsById(id)) {
            procedureRepository.deleteById(id);
        } else {
            throw new ProcedureNotFoundException("Procedure not found!");
        }
    }

    private ProcedureResponse toResponse (Procedure proc) {
        return new ProcedureResponse(
                proc.getId(),
                proc.getName(),
                proc.getValue()
        );
    }


    private void validatePatch(ProcedureRequest request) {
        if (request.name() != null && request.name().isBlank()) {
            throw new IllegalArgumentException("'username' not informed or blank!");
        }
        if (request.value() != null && request.value() < 0) {
            throw new IllegalArgumentException("'value' not informed or negative!");
        }
    }

}
