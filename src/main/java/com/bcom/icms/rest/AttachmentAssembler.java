package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.AttachmentDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class AttachmentAssembler implements SimpleRepresentationModelAssembler<AttachmentDTO> {

    @Override
    public void addLinks(final EntityModel<AttachmentDTO> entityModel) {
        entityModel.add(linkTo(methodOn(AttachmentResource.class).getAttachment(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(AttachmentResource.class).getAllAttachments(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getClient())).withRel("client"));
        entityModel.add(linkTo(methodOn(TypeResource.class).getType(entityModel.getContent().getType())).withRel("type"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<AttachmentDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(AttachmentResource.class).getAllAttachments(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(AttachmentResource.class).getAttachment(id)).withSelfRel());
        return simpleModel;
    }

}
