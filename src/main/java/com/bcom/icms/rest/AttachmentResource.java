package com.bcom.icms.rest;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Type;
import com.bcom.icms.model.AttachmentDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.TypeRepository;
import com.bcom.icms.service.AttachmentService;
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
@RequestMapping(value = "/api/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class AttachmentResource {

    private final AttachmentService attachmentService;
    private final AttachmentAssembler attachmentAssembler;
    private final PagedResourcesAssembler<AttachmentDTO> pagedResourcesAssembler;
    private final ClientRepository clientRepository;
    private final TypeRepository typeRepository;

    public AttachmentResource(final AttachmentService attachmentService,
            final AttachmentAssembler attachmentAssembler,
            final PagedResourcesAssembler<AttachmentDTO> pagedResourcesAssembler,
            final ClientRepository clientRepository, final TypeRepository typeRepository) {
        this.attachmentService = attachmentService;
        this.attachmentAssembler = attachmentAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.clientRepository = clientRepository;
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
    public ResponseEntity<PagedModel<EntityModel<AttachmentDTO>>> getAllAttachments(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<AttachmentDTO> attachmentDTOs = attachmentService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(attachmentDTOs, attachmentAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AttachmentDTO>> getAttachment(
            @PathVariable(name = "id") final Long id) {
        final AttachmentDTO attachmentDTO = attachmentService.get(id);
        return ResponseEntity.ok(attachmentAssembler.toModel(attachmentDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createAttachment(
            @RequestBody @Valid final AttachmentDTO attachmentDTO) {
        final Long createdId = attachmentService.create(attachmentDTO);
        return new ResponseEntity<>(attachmentAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateAttachment(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AttachmentDTO attachmentDTO) {
        attachmentService.update(id, attachmentDTO);
        return ResponseEntity.ok(attachmentAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAttachment(@PathVariable(name = "id") final Long id) {
        attachmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientValues")
    public ResponseEntity<Map<Long, String>> getClientValues() {
        return ResponseEntity.ok(clientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Client::getId, Client::getClientName)));
    }

    @GetMapping("/typeValues")
    public ResponseEntity<Map<Long, String>> getTypeValues() {
        return ResponseEntity.ok(typeRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Type::getId, Type::getTypeCode)));
    }

}
