package com.bcom.icms.rest;

import com.bcom.icms.domain.AllStatus;
import com.bcom.icms.domain.Type;
import com.bcom.icms.model.ModuleSatusOrderDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.repos.AllStatusRepository;
import com.bcom.icms.repos.TypeRepository;
import com.bcom.icms.service.ModuleSatusOrderService;
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
@RequestMapping(value = "/api/moduleSatusOrders", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class ModuleSatusOrderResource {

    private final ModuleSatusOrderService moduleSatusOrderService;
    private final ModuleSatusOrderAssembler moduleSatusOrderAssembler;
    private final PagedResourcesAssembler<ModuleSatusOrderDTO> pagedResourcesAssembler;
    private final TypeRepository typeRepository;
    private final AllStatusRepository allStatusRepository;

    public ModuleSatusOrderResource(final ModuleSatusOrderService moduleSatusOrderService,
            final ModuleSatusOrderAssembler moduleSatusOrderAssembler,
            final PagedResourcesAssembler<ModuleSatusOrderDTO> pagedResourcesAssembler,
            final TypeRepository typeRepository, final AllStatusRepository allStatusRepository) {
        this.moduleSatusOrderService = moduleSatusOrderService;
        this.moduleSatusOrderAssembler = moduleSatusOrderAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.typeRepository = typeRepository;
        this.allStatusRepository = allStatusRepository;
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
    public ResponseEntity<PagedModel<EntityModel<ModuleSatusOrderDTO>>> getAllModuleSatusOrders(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<ModuleSatusOrderDTO> moduleSatusOrderDTOs = moduleSatusOrderService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(moduleSatusOrderDTOs, moduleSatusOrderAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ModuleSatusOrderDTO>> getModuleSatusOrder(
            @PathVariable(name = "id") final Long id) {
        final ModuleSatusOrderDTO moduleSatusOrderDTO = moduleSatusOrderService.get(id);
        return ResponseEntity.ok(moduleSatusOrderAssembler.toModel(moduleSatusOrderDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createModuleSatusOrder(
            @RequestBody @Valid final ModuleSatusOrderDTO moduleSatusOrderDTO) {
        final Long createdId = moduleSatusOrderService.create(moduleSatusOrderDTO);
        return new ResponseEntity<>(moduleSatusOrderAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateModuleSatusOrder(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ModuleSatusOrderDTO moduleSatusOrderDTO) {
        moduleSatusOrderService.update(id, moduleSatusOrderDTO);
        return ResponseEntity.ok(moduleSatusOrderAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteModuleSatusOrder(@PathVariable(name = "id") final Long id) {
        moduleSatusOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/typeValues")
    public ResponseEntity<Map<Long, String>> getTypeValues() {
        return ResponseEntity.ok(typeRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Type::getId, Type::getTypeCode)));
    }

    @GetMapping("/statusValues")
    public ResponseEntity<Map<Long, String>> getStatusValues() {
        return ResponseEntity.ok(allStatusRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(AllStatus::getId, AllStatus::getName)));
    }

}
