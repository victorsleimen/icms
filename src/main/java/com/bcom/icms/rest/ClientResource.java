package com.bcom.icms.rest;

import com.bcom.icms.model.ClientDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.service.ClientService;
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
@RequestMapping(value = "/api/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class ClientResource {

    private final ClientService clientService;
    private final ClientAssembler clientAssembler;
    private final PagedResourcesAssembler<ClientDTO> pagedResourcesAssembler;

    public ClientResource(final ClientService clientService, final ClientAssembler clientAssembler,
            final PagedResourcesAssembler<ClientDTO> pagedResourcesAssembler) {
        this.clientService = clientService;
        this.clientAssembler = clientAssembler;
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
    public ResponseEntity<PagedModel<EntityModel<ClientDTO>>> getAllClients(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<ClientDTO> clientDTOs = clientService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(clientDTOs, clientAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClientDTO>> getClient(
            @PathVariable(name = "id") final Long id) {
        final ClientDTO clientDTO = clientService.get(id);
        return ResponseEntity.ok(clientAssembler.toModel(clientDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createClient(
            @RequestBody @Valid final ClientDTO clientDTO) {
        final Long createdId = clientService.create(clientDTO);
        return new ResponseEntity<>(clientAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateClient(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ClientDTO clientDTO) {
        clientService.update(id, clientDTO);
        return ResponseEntity.ok(clientAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteClient(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = clientService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
