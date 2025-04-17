package com.bcom.icms.rest;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Sla;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.model.TicketDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.SlaRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.service.TicketService;
import com.bcom.icms.util.CustomCollectors;
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
@RequestMapping(value = "/api/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class TicketResource {

    private final TicketService ticketService;
    private final TicketAssembler ticketAssembler;
    private final PagedResourcesAssembler<TicketDTO> pagedResourcesAssembler;
    private final ClientRepository clientRepository;
    private final SlaRepository slaRepository;
    private final UserRepository userRepository;

    public TicketResource(final TicketService ticketService, final TicketAssembler ticketAssembler,
            final PagedResourcesAssembler<TicketDTO> pagedResourcesAssembler,
            final ClientRepository clientRepository, final SlaRepository slaRepository,
            final UserRepository userRepository) {
        this.ticketService = ticketService;
        this.ticketAssembler = ticketAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.clientRepository = clientRepository;
        this.slaRepository = slaRepository;
        this.userRepository = userRepository;
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
    public ResponseEntity<PagedModel<EntityModel<TicketDTO>>> getAllTickets(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<TicketDTO> ticketDTOs = ticketService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(ticketDTOs, ticketAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TicketDTO>> getTicket(
            @PathVariable(name = "id") final Long id) {
        final TicketDTO ticketDTO = ticketService.get(id);
        return ResponseEntity.ok(ticketAssembler.toModel(ticketDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createTicket(
            @RequestBody @Valid final TicketDTO ticketDTO) {
        final Long createdId = ticketService.create(ticketDTO);
        return new ResponseEntity<>(ticketAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateTicket(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TicketDTO ticketDTO) {
        ticketService.update(id, ticketDTO);
        return ResponseEntity.ok(ticketAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTicket(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = ticketService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientValues")
    public ResponseEntity<Map<Long, String>> getClientValues() {
        return ResponseEntity.ok(clientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Client::getId, Client::getClientName)));
    }

    @GetMapping("/slaValues")
    public ResponseEntity<Map<Long, String>> getSlaValues() {
        return ResponseEntity.ok(slaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Sla::getId, Sla::getName)));
    }

    @GetMapping("/assigneeValues")
    public ResponseEntity<Map<Long, String>> getAssigneeValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

}
