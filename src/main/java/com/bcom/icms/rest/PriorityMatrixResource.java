package com.bcom.icms.rest;

import com.bcom.icms.domain.Type;
import com.bcom.icms.model.PriorityMatrixDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.repos.TypeRepository;
import com.bcom.icms.service.PriorityMatrixService;
import com.bcom.icms.util.CustomCollectors;
import com.bcom.icms.util.UserRoles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/priorityMatrices", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class PriorityMatrixResource {

    private final PriorityMatrixService priorityMatrixService;
    private final PriorityMatrixAssembler priorityMatrixAssembler;
    private final PagedResourcesAssembler<PriorityMatrixDTO> pagedResourcesAssembler;
    private final TypeRepository typeRepository;

    public PriorityMatrixResource(final PriorityMatrixService priorityMatrixService,
            final PriorityMatrixAssembler priorityMatrixAssembler,
            final PagedResourcesAssembler<PriorityMatrixDTO> pagedResourcesAssembler,
            final TypeRepository typeRepository) {
        this.priorityMatrixService = priorityMatrixService;
        this.priorityMatrixAssembler = priorityMatrixAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.typeRepository = typeRepository;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PriorityMatrixDTO>>> getAllPriorityMatrices(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<PriorityMatrixDTO> priorityMatrixDTOs = priorityMatrixService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(priorityMatrixDTOs, priorityMatrixAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PriorityMatrixDTO>> getPriorityMatrix(
            @PathVariable(name = "id") final Long id) {
        final PriorityMatrixDTO priorityMatrixDTO = priorityMatrixService.get(id);
        return ResponseEntity.ok(priorityMatrixAssembler.toModel(priorityMatrixDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createPriorityMatrix(
            @RequestBody @Valid final PriorityMatrixDTO priorityMatrixDTO) {
        final Long createdId = priorityMatrixService.create(priorityMatrixDTO);
        return new ResponseEntity<>(priorityMatrixAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updatePriorityMatrix(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PriorityMatrixDTO priorityMatrixDTO) {
        priorityMatrixService.update(id, priorityMatrixDTO);
        return ResponseEntity.ok(priorityMatrixAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePriorityMatrix(@PathVariable(name = "id") final Long id) {
        priorityMatrixService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/typeValues")
    public ResponseEntity<Map<Long, String>> getTypeValues() {
        return ResponseEntity.ok(typeRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Type::getId, Type::getTypeCode)));
    }

}
