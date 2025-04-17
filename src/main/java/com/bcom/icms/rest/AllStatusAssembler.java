package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.AllStatusDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class AllStatusAssembler implements SimpleRepresentationModelAssembler<AllStatusDTO> {

    @Override
    public void addLinks(final EntityModel<AllStatusDTO> entityModel) {
        entityModel.add(linkTo(methodOn(AllStatusResource.class).getAllStatus(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(AllStatusResource.class).getAllAllStatuses(null, null)).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<AllStatusDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(AllStatusResource.class).getAllAllStatuses(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(AllStatusResource.class).getAllStatus(id)).withSelfRel());
        return simpleModel;
    }

}
