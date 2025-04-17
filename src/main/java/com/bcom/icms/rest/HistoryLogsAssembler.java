package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.HistoryLogsDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class HistoryLogsAssembler implements SimpleRepresentationModelAssembler<HistoryLogsDTO> {

    @Override
    public void addLinks(final EntityModel<HistoryLogsDTO> entityModel) {
        entityModel.add(linkTo(methodOn(HistoryLogsResource.class).getHistoryLogs(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(HistoryLogsResource.class).getAllHistoryLogss(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getClient())).withRel("client"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<HistoryLogsDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(HistoryLogsResource.class).getAllHistoryLogss(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(HistoryLogsResource.class).getHistoryLogs(id)).withSelfRel());
        return simpleModel;
    }

}
