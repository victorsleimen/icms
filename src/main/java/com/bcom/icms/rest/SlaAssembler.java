package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.model.SlaDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class SlaAssembler implements SimpleRepresentationModelAssembler<SlaDTO> {

    @Override
    public void addLinks(final EntityModel<SlaDTO> entityModel) {
        entityModel.add(linkTo(methodOn(SlaResource.class).getSla(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(SlaResource.class).getAllSlas(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getClient())).withRel("client"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<SlaDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(SlaResource.class).getAllSlas(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(SlaResource.class).getSla(id)).withSelfRel());
        return simpleModel;
    }

}
