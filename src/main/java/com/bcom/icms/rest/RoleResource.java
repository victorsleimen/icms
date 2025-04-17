package com.bcom.icms.rest;

import com.bcom.icms.model.RoleDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.service.RoleService;
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
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class RoleResource {

    private final RoleService roleService;
    private final RoleAssembler roleAssembler;
    private final PagedResourcesAssembler<RoleDTO> pagedResourcesAssembler;

    public RoleResource(final RoleService roleService, final RoleAssembler roleAssembler,
            final PagedResourcesAssembler<RoleDTO> pagedResourcesAssembler) {
        this.roleService = roleService;
        this.roleAssembler = roleAssembler;
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
    public ResponseEntity<PagedModel<EntityModel<RoleDTO>>> getAllRoles(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<RoleDTO> roleDTOs = roleService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(roleDTOs, roleAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RoleDTO>> getRole(@PathVariable(name = "id") final Long id) {
        final RoleDTO roleDTO = roleService.get(id);
        return ResponseEntity.ok(roleAssembler.toModel(roleDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createRole(
            @RequestBody @Valid final RoleDTO roleDTO) {
        final Long createdId = roleService.create(roleDTO);
        return new ResponseEntity<>(roleAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateRole(
            @PathVariable(name = "id") final Long id, @RequestBody @Valid final RoleDTO roleDTO) {
        roleService.update(id, roleDTO);
        return ResponseEntity.ok(roleAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "id") final Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
