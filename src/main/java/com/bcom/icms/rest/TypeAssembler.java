package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.model.TypeDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class TypeAssembler implements SimpleRepresentationModelAssembler<TypeDTO> {

    @Override
    public void addLinks(final EntityModel<TypeDTO> entityModel) {
        entityModel.add(linkTo(methodOn(TypeResource.class).getType(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(TypeResource.class).getAllTypes(null, null)).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<TypeDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(TypeResource.class).getAllTypes(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(TypeResource.class).getType(id)).withSelfRel());
        return simpleModel;
    }

}
