package com.bcom.icms.rest;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeResource {

    @GetMapping("/home")
    public RepresentationModel<?> index() {
        return RepresentationModel.of(null)
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserResource.class).getAllUsers(null, null)).withRel("users"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleResource.class).getAllRoles(null, null)).withRel("roles"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientResource.class).getAllClients(null, null)).withRel("clients"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TypeResource.class).getAllTypes(null, null)).withRel("types"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ModuleSatusOrderResource.class).getAllModuleSatusOrders(null, null)).withRel("moduleSatusOrders"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AllStatusResource.class).getAllAllStatuses(null, null)).withRel("allStatuses"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TicketResource.class).getAllTickets(null, null)).withRel("tickets"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SlaResource.class).getAllSlas(null, null)).withRel("slas"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PriorityMatrixResource.class).getAllPriorityMatrices(null, null)).withRel("priorityMatrixes"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AttachmentResource.class).getAllAttachments(null, null)).withRel("attachments"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentResource.class).getAllComments(null, null)).withRel("comments"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(NotificationResource.class).getAllNotifications(null, null)).withRel("notifications"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(KnowledgeArticlesResource.class).getAllKnowledgeArticless(null, null)).withRel("knowledgeArticleses"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistoryLogsResource.class).getAllHistoryLogss(null, null)).withRel("historyLogses"));
    }

}
