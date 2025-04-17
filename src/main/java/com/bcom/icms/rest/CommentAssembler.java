package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.CommentDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class CommentAssembler implements SimpleRepresentationModelAssembler<CommentDTO> {

    @Override
    public void addLinks(final EntityModel<CommentDTO> entityModel) {
        entityModel.add(linkTo(methodOn(CommentResource.class).getComment(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(CommentResource.class).getAllComments(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getClient())).withRel("client"));
        entityModel.add(linkTo(methodOn(UserResource.class).getUser(entityModel.getContent().getUser())).withRel("user"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<CommentDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(CommentResource.class).getAllComments(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(CommentResource.class).getComment(id)).withSelfRel());
        return simpleModel;
    }

}
