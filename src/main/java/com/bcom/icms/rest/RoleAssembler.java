package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.RoleDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class RoleAssembler implements SimpleRepresentationModelAssembler<RoleDTO> {

    @Override
    public void addLinks(final EntityModel<RoleDTO> entityModel) {
        entityModel.add(linkTo(methodOn(RoleResource.class).getRole(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(RoleResource.class).getAllRoles(null, null)).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<RoleDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(RoleResource.class).getAllRoles(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(RoleResource.class).getRole(id)).withSelfRel());
        return simpleModel;
    }

}
