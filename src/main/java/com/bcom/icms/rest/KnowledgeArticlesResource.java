package com.bcom.icms.rest;

import com.bcom.icms.domain.Client;
import com.bcom.icms.model.KnowledgeArticlesDTO;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.service.KnowledgeArticlesService;
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
@RequestMapping(value = "/api/knowledgeArticless", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class KnowledgeArticlesResource {

    private final KnowledgeArticlesService knowledgeArticlesService;
    private final KnowledgeArticlesAssembler knowledgeArticlesAssembler;
    private final PagedResourcesAssembler<KnowledgeArticlesDTO> pagedResourcesAssembler;
    private final ClientRepository clientRepository;

    public KnowledgeArticlesResource(final KnowledgeArticlesService knowledgeArticlesService,
            final KnowledgeArticlesAssembler knowledgeArticlesAssembler,
            final PagedResourcesAssembler<KnowledgeArticlesDTO> pagedResourcesAssembler,
            final ClientRepository clientRepository) {
        this.knowledgeArticlesService = knowledgeArticlesService;
        this.knowledgeArticlesAssembler = knowledgeArticlesAssembler;
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
    public ResponseEntity<PagedModel<EntityModel<KnowledgeArticlesDTO>>> getAllKnowledgeArticless(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<KnowledgeArticlesDTO> knowledgeArticlesDTOs = knowledgeArticlesService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(knowledgeArticlesDTOs, knowledgeArticlesAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<KnowledgeArticlesDTO>> getKnowledgeArticles(
            @PathVariable(name = "id") final Long id) {
        final KnowledgeArticlesDTO knowledgeArticlesDTO = knowledgeArticlesService.get(id);
        return ResponseEntity.ok(knowledgeArticlesAssembler.toModel(knowledgeArticlesDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createKnowledgeArticles(
            @RequestBody @Valid final KnowledgeArticlesDTO knowledgeArticlesDTO) {
        final Long createdId = knowledgeArticlesService.create(knowledgeArticlesDTO);
        return new ResponseEntity<>(knowledgeArticlesAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateKnowledgeArticles(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KnowledgeArticlesDTO knowledgeArticlesDTO) {
        knowledgeArticlesService.update(id, knowledgeArticlesDTO);
        return ResponseEntity.ok(knowledgeArticlesAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteKnowledgeArticles(@PathVariable(name = "id") final Long id) {
        knowledgeArticlesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientValues")
    public ResponseEntity<Map<Long, String>> getClientValues() {
        return ResponseEntity.ok(clientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Client::getId, Client::getClientName)));
    }

}
