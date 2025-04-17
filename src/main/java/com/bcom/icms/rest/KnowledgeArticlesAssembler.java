package com.bcom.icms.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.bcom.icms.model.KnowledgeArticlesDTO;
import com.bcom.icms.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class KnowledgeArticlesAssembler implements SimpleRepresentationModelAssembler<KnowledgeArticlesDTO> {

    @Override
    public void addLinks(final EntityModel<KnowledgeArticlesDTO> entityModel) {
        entityModel.add(linkTo(methodOn(KnowledgeArticlesResource.class).getKnowledgeArticles(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(KnowledgeArticlesResource.class).getAllKnowledgeArticless(null, null)).withRel(IanaLinkRelations.COLLECTION));
        entityModel.add(linkTo(methodOn(ClientResource.class).getClient(entityModel.getContent().getClient())).withRel("client"));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<KnowledgeArticlesDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(KnowledgeArticlesResource.class).getAllKnowledgeArticless(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(KnowledgeArticlesResource.class).getKnowledgeArticles(id)).withSelfRel());
        return simpleModel;
    }

}
