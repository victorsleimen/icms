package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.PriorityMatrixDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class PriorityMatrixAssembler implements SimpleRepresentationModelAssembler<PriorityMatrixDTO> {

    @Override
    public void addLinks(final EntityModel<PriorityMatrixDTO> entityModel) {
        entityModel.add(linkTo(methodOn(PriorityMatrixResource.class).getPriorityMatrix(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(PriorityMatrixResource.class).getAllPriorityMatrices(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(TypeResource.class).getType(entityModel.getContent().getType())).withRel("type"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<PriorityMatrixDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(PriorityMatrixResource.class).getAllPriorityMatrices(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(PriorityMatrixResource.class).getPriorityMatrix(id)).withSelfRel());
        return simpleModel;
    }

}
