package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.NotificationDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class NotificationAssembler implements SimpleRepresentationModelAssembler<NotificationDTO> {

    @Override
    public void addLinks(final EntityModel<NotificationDTO> entityModel) {
        entityModel.add(linkTo(methodOn(NotificationResource.class).getNotification(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(NotificationResource.class).getAllNotifications(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getClient())).withRel("client"));
        entityModel.add(linkTo(methodOn(UserResource.class).getUser(entityModel.getContent().getUser())).withRel("user"));
        entityModel.add(linkTo(methodOn(TicketResource.class).getTicket(entityModel.getContent().getTicket())).withRel("ticket"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<NotificationDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(NotificationResource.class).getAllNotifications(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(NotificationResource.class).getNotification(id)).withSelfRel());
        return simpleModel;
    }

}
