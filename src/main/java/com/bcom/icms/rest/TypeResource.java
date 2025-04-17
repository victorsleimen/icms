package com.bcom.icms.rest;

import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.model.TypeDTO;
import com.bcom.icms.service.TypeService;
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
@RequestMapping(value = "/api/types", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class TypeResource {

    private final TypeService typeService;
    private final TypeAssembler typeAssembler;
    private final PagedResourcesAssembler<TypeDTO> pagedResourcesAssembler;

    public TypeResource(final TypeService typeService, final TypeAssembler typeAssembler,
            final PagedResourcesAssembler<TypeDTO> pagedResourcesAssembler) {
        this.typeService = typeService;
        this.typeAssembler = typeAssembler;
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
    public ResponseEntity<PagedModel<EntityModel<TypeDTO>>> getAllTypes(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<TypeDTO> typeDTOs = typeService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(typeDTOs, typeAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TypeDTO>> getType(@PathVariable(name = "id") final Long id) {
        final TypeDTO typeDTO = typeService.get(id);
        return ResponseEntity.ok(typeAssembler.toModel(typeDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createType(
            @RequestBody @Valid final TypeDTO typeDTO) {
        final Long createdId = typeService.create(typeDTO);
        return new ResponseEntity<>(typeAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateType(
            @PathVariable(name = "id") final Long id, @RequestBody @Valid final TypeDTO typeDTO) {
        typeService.update(id, typeDTO);
        return ResponseEntity.ok(typeAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteType(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = typeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        typeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
