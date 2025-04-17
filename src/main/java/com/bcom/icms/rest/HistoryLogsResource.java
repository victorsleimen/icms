package com.bcom.icms.rest;

import com.bcom.icms.domain.Client;
import com.bcom.icms.model.HistoryLogsDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.service.HistoryLogsService;
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
@RequestMapping(value = "/api/historyLogss", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class HistoryLogsResource {

    private final HistoryLogsService historyLogsService;
    private final HistoryLogsAssembler historyLogsAssembler;
    private final PagedResourcesAssembler<HistoryLogsDTO> pagedResourcesAssembler;
    private final ClientRepository clientRepository;

    public HistoryLogsResource(final HistoryLogsService historyLogsService,
            final HistoryLogsAssembler historyLogsAssembler,
            final PagedResourcesAssembler<HistoryLogsDTO> pagedResourcesAssembler,
            final ClientRepository clientRepository) {
        this.historyLogsService = historyLogsService;
        this.historyLogsAssembler = historyLogsAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.clientRepository = clientRepository;
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
    public ResponseEntity<PagedModel<EntityModel<HistoryLogsDTO>>> getAllHistoryLogss(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<HistoryLogsDTO> historyLogsDTOs = historyLogsService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(historyLogsDTOs, historyLogsAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<HistoryLogsDTO>> getHistoryLogs(
            @PathVariable(name = "id") final Long id) {
        final HistoryLogsDTO historyLogsDTO = historyLogsService.get(id);
        return ResponseEntity.ok(historyLogsAssembler.toModel(historyLogsDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createHistoryLogs(
            @RequestBody @Valid final HistoryLogsDTO historyLogsDTO) {
        final Long createdId = historyLogsService.create(historyLogsDTO);
        return new ResponseEntity<>(historyLogsAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateHistoryLogs(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final HistoryLogsDTO historyLogsDTO) {
        historyLogsService.update(id, historyLogsDTO);
        return ResponseEntity.ok(historyLogsAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteHistoryLogs(@PathVariable(name = "id") final Long id) {
        historyLogsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientValues")
    public ResponseEntity<Map<Long, String>> getClientValues() {
        return ResponseEntity.ok(clientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Client::getId, Client::getClientName)));
    }

}
