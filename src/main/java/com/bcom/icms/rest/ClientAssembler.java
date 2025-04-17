package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.ClientDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class ClientAssembler implements SimpleRepresentationModelAssembler<ClientDTO> {

    @Override
    public void addLinks(final EntityModel<ClientDTO> entityModel) {
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(ClientResource.class).getAllClients(null, null)).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<ClientDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(ClientResource.class).getAllClients(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(ClientResource.class).getClient(id)).withSelfRel());
        return simpleModel;
    }

}
