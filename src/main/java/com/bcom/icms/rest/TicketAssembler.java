package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.SimpleValue;
import com.bcom.icms.model.TicketDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class TicketAssembler implements SimpleRepresentationModelAssembler<TicketDTO> {

    @Override
    public void addLinks(final EntityModel<TicketDTO> entityModel) {
        entityModel.add(linkTo(methodOn(TicketResource.class).getTicket(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(TicketResource.class).getAllTickets(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getClient())).withRel("client"));
        entityModel.add(linkTo(methodOn(SlaResource.class).getSla(entityModel.getContent().getSla())).withRel("sla"));
        entityModel.add(linkTo(methodOn(UserResource.class).getUser(entityModel.getContent().getAssignee())).withRel("assignee"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<TicketDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(TicketResource.class).getAllTickets(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(TicketResource.class).getTicket(id)).withSelfRel());
        return simpleModel;
    }

}
