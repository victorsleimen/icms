package com.bcom.icms.rest;

import com.bcom.icms.domain.Client;
import com.bcom.icms.domain.Role;
import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.model.UserDTO;
import com.bcom.icms.repos.ClientRepository;
import com.bcom.icms.repos.RoleRepository;
import com.bcom.icms.service.UserService;
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
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRoles.SUPERADMIN + "', '" + UserRoles.USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class UserResource {

    private final UserService userService;
    private final UserAssembler userAssembler;
    private final PagedResourcesAssembler<UserDTO> pagedResourcesAssembler;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;

    public UserResource(final UserService userService, final UserAssembler userAssembler,
            final PagedResourcesAssembler<UserDTO> pagedResourcesAssembler,
            final RoleRepository roleRepository, final ClientRepository clientRepository) {
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.roleRepository = roleRepository;
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
    public ResponseEntity<PagedModel<EntityModel<UserDTO>>> getAllUsers(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<UserDTO> userDTOs = userService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(userDTOs, userAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUser(@PathVariable(name = "id") final Long id) {
        final UserDTO userDTO = userService.get(id);
        return ResponseEntity.ok(userAssembler.toModel(userDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createUser(
            @RequestBody @Valid final UserDTO userDTO) {
        final Long createdId = userService.create(userDTO);
        return new ResponseEntity<>(userAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateUser(
            @PathVariable(name = "id") final Long id, @RequestBody @Valid final UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok(userAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = userService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rolesValues")
    public ResponseEntity<Map<Long, String>> getRolesValues() {
        return ResponseEntity.ok(roleRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Role::getId, Role::getName)));
    }

    @GetMapping("/clientValues")
    public ResponseEntity<Map<Long, String>> getClientValues() {
        return ResponseEntity.ok(clientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Client::getId, Client::getClientName)));
    }

}
