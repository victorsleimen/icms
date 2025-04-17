package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.ModuleSatusOrderDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class ModuleSatusOrderAssembler implements SimpleRepresentationModelAssembler<ModuleSatusOrderDTO> {

    @Override
    public void addLinks(final EntityModel<ModuleSatusOrderDTO> entityModel) {
        entityModel.add(linkTo(methodOn(ModuleSatusOrderResource.class).getModuleSatusOrder(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(ModuleSatusOrderResource.class).getAllModuleSatusOrders(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(TypeResource.class).getType(entityModel.getContent().getType())).withRel("type"));
        entityModel.add(linkTo(methodOn(AllStatusResource.class).getAllStatus(entityModel.getContent().getStatus())).withRel("status"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<ModuleSatusOrderDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(ModuleSatusOrderResource.class).getAllModuleSatusOrders(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(ModuleSatusOrderResource.class).getModuleSatusOrder(id)).withSelfRel());
        return simpleModel;
    }

}
