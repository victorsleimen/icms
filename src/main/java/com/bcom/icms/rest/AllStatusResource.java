package com.bcom.icms.rest;

import com.bcom.icms.model.AllStatusDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.service.AllStatusService;
import com.bcom.icms.util.ReferencedException;
import com.bcom.icms.util.ReferencedWarning;
import com.bcom.icms.util.UserRoles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/api/allStatuses", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class AllStatusResource {

    private final AllStatusService allStatusService;
    private final AllStatusAssembler allStatusAssembler;
    private final PagedResourcesAssembler<AllStatusDTO> pagedResourcesAssembler;

    public AllStatusResource(final AllStatusService allStatusService,
            final AllStatusAssembler allStatusAssembler,
            final PagedResourcesAssembler<AllStatusDTO> pagedResourcesAssembler) {
        this.allStatusService = allStatusService;
        this.allStatusAssembler = allStatusAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
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
    public ResponseEntity<PagedModel<EntityModel<AllStatusDTO>>> getAllAllStatuses(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<AllStatusDTO> allStatusDTOs = allStatusService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(allStatusDTOs, allStatusAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AllStatusDTO>> getAllStatus(
            @PathVariable(name = "id") final Long id) {
        final AllStatusDTO allStatusDTO = allStatusService.get(id);
        return ResponseEntity.ok(allStatusAssembler.toModel(allStatusDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createAllStatus(
            @RequestBody @Valid final AllStatusDTO allStatusDTO) {
        final Long createdId = allStatusService.create(allStatusDTO);
        return new ResponseEntity<>(allStatusAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateAllStatus(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AllStatusDTO allStatusDTO) {
        allStatusService.update(id, allStatusDTO);
        return ResponseEntity.ok(allStatusAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAllStatus(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = allStatusService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        allStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
