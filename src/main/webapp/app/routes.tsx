import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from "./app";
import Home from './home/home';
import UserList from './user/user-list';
import UserAdd from './user/user-add';
import UserEdit from './user/user-edit';
import RoleList from './role/role-list';
import RoleAdd from './role/role-add';
import RoleEdit from './role/role-edit';
import ClientList from './client/client-list';
import ClientAdd from './client/client-add';
import ClientEdit from './client/client-edit';
import TypeList from './type/type-list';
import TypeAdd from './type/type-add';
import TypeEdit from './type/type-edit';
import ModuleSatusOrderList from './module-satus-order/module-satus-order-list';
import ModuleSatusOrderAdd from './module-satus-order/module-satus-order-add';
import ModuleSatusOrderEdit from './module-satus-order/module-satus-order-edit';
import AllStatusList from './all-status/all-status-list';
import AllStatusAdd from './all-status/all-status-add';
import AllStatusEdit from './all-status/all-status-edit';
import TicketList from './ticket/ticket-list';
import TicketAdd from './ticket/ticket-add';
import TicketEdit from './ticket/ticket-edit';
import SlaList from './sla/sla-list';
import SlaAdd from './sla/sla-add';
import SlaEdit from './sla/sla-edit';
import PriorityMatrixList from './priority-matrix/priority-matrix-list';
import PriorityMatrixAdd from './priority-matrix/priority-matrix-add';
import PriorityMatrixEdit from './priority-matrix/priority-matrix-edit';
import AttachmentList from './attachment/attachment-list';
import AttachmentAdd from './attachment/attachment-add';
import AttachmentEdit from './attachment/attachment-edit';
import CommentList from './comment/comment-list';
import CommentAdd from './comment/comment-add';
import CommentEdit from './comment/comment-edit';
import NotificationList from './notification/notification-list';
import NotificationAdd from './notification/notification-add';
import NotificationEdit from './notification/notification-edit';
import KnowledgeArticlesList from './knowledge-articles/knowledge-articles-list';
import KnowledgeArticlesAdd from './knowledge-articles/knowledge-articles-add';
import KnowledgeArticlesEdit from './knowledge-articles/knowledge-articles-edit';
import HistoryLogsList from './history-logs/history-logs-list';
import HistoryLogsAdd from './history-logs/history-logs-add';
import HistoryLogsEdit from './history-logs/history-logs-edit';
import Error from './error/error';
import { SUPERADMIN, USER } from 'app/security/authentication-provider';


export default function AppRoutes() {
  const router = createBrowserRouter([
    {
      element: <App />,
      children: [
        { path: '', element: <Home /> , handle: { roles: [SUPERADMIN] } },
        { path: 'users', element: <UserList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'users/add', element: <UserAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'users/edit/:id', element: <UserEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'roles', element: <RoleList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'roles/add', element: <RoleAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'roles/edit/:id', element: <RoleEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'clients', element: <ClientList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'clients/add', element: <ClientAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'clients/edit/:id', element: <ClientEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'types', element: <TypeList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'types/add', element: <TypeAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'types/edit/:id', element: <TypeEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'moduleSatusOrders', element: <ModuleSatusOrderList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'moduleSatusOrders/add', element: <ModuleSatusOrderAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'moduleSatusOrders/edit/:id', element: <ModuleSatusOrderEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'allStatuses', element: <AllStatusList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'allStatuses/add', element: <AllStatusAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'allStatuses/edit/:id', element: <AllStatusEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'tickets', element: <TicketList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'tickets/add', element: <TicketAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'tickets/edit/:id', element: <TicketEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'slas', element: <SlaList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'slas/add', element: <SlaAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'slas/edit/:id', element: <SlaEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'priorityMatrices', element: <PriorityMatrixList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'priorityMatrices/add', element: <PriorityMatrixAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'priorityMatrices/edit/:id', element: <PriorityMatrixEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'attachments', element: <AttachmentList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'attachments/add', element: <AttachmentAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'attachments/edit/:id', element: <AttachmentEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'comments', element: <CommentList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'comments/add', element: <CommentAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'comments/edit/:id', element: <CommentEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'notifications', element: <NotificationList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'notifications/add', element: <NotificationAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'notifications/edit/:id', element: <NotificationEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'knowledgeArticless', element: <KnowledgeArticlesList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'knowledgeArticless/add', element: <KnowledgeArticlesAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'knowledgeArticless/edit/:id', element: <KnowledgeArticlesEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'historyLogss', element: <HistoryLogsList /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'historyLogss/add', element: <HistoryLogsAdd /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'historyLogss/edit/:id', element: <HistoryLogsEdit /> , handle: { roles: [SUPERADMIN, USER] } },
        { path: 'error', element: <Error /> },
        { path: '*', element: <Error /> }
      ]
    }
  ]);

  return (
    <RouterProvider router={router} />
  );
}
