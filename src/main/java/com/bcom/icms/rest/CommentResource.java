package com.bcom.icms.rest;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.User;
import com.bcom.icms.model.CommentDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.UserRepository;
import com.bcom.icms.service.CommentService;
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
@RequestMapping(value = "/api/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class CommentResource {

    private final CommentService commentService;
    private final CommentAssembler commentAssembler;
    private final PagedResourcesAssembler<CommentDTO> pagedResourcesAssembler;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public CommentResource(final CommentService commentService,
            final CommentAssembler commentAssembler,
            final PagedResourcesAssembler<CommentDTO> pagedResourcesAssembler,
            final ClientRepository clientRepository, final UserRepository userRepository) {
        this.commentService = commentService;
        this.commentAssembler = commentAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.clientRepository = clientRepository;
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
    public ResponseEntity<PagedModel<EntityModel<CommentDTO>>> getAllComments(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<CommentDTO> commentDTOs = commentService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(commentDTOs, commentAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CommentDTO>> getComment(
            @PathVariable(name = "id") final Long id) {
        final CommentDTO commentDTO = commentService.get(id);
        return ResponseEntity.ok(commentAssembler.toModel(commentDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createComment(
            @RequestBody @Valid final CommentDTO commentDTO) {
        final Long createdId = commentService.create(commentDTO);
        return new ResponseEntity<>(commentAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateComment(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CommentDTO commentDTO) {
        commentService.update(id, commentDTO);
        return ResponseEntity.ok(commentAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "id") final Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientValues")
    public ResponseEntity<Map<Long, String>> getClientValues() {
        return ResponseEntity.ok(clientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Client::getId, Client::getClientName)));
    }

    @GetMapping("/userValues")
    public ResponseEntity<Map<Long, String>> getUserValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

}
