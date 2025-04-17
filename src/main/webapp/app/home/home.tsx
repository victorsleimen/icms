import React from 'react';
import { Link } from 'react-router';
import { Trans, useTranslation } from 'react-i18next';
import useDocumentTitle from 'app/common/use-document-title';
import './home.scss';


export default function Home() {
  const { t } = useTranslation();
  useDocumentTitle(t('home.index.headline'));

  return (<>
    <h1 className="grow text-3xl md:text-4xl font-medium mb-8">{t('home.index.headline')}</h1>
    <p className="mb-4"><Trans i18nKey="home.index.text" components={{ a: <a />, strong: <strong /> }} /></p>
    <p className="mb-12">
      <span>{t('home.index.swagger.text')}</span>
      <span> </span>
      <a href={process.env.API_PATH + '/swagger-ui.html'} target="_blank" className="underline">{t('home.index.swagger.link')}</a>.
    </p>
    <div className="md:w-2/5 mb-12">
      <h4 className="text-2xl font-medium mb-4">{t('home.index.exploreEntities')}</h4>
      <div className="flex flex-col border border-gray-300 rounded">
        <Link to="/users" className="w-full border-gray-300 hover:bg-gray-100 border-b rounded-t px-4 py-2">{t('user.list.headline')}</Link>
        <Link to="/roles" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('role.list.headline')}</Link>
        <Link to="/clients" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('client.list.headline')}</Link>
        <Link to="/types" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('type.list.headline')}</Link>
        <Link to="/moduleSatusOrders" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('moduleSatusOrder.list.headline')}</Link>
        <Link to="/allStatuses" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('allStatus.list.headline')}</Link>
        <Link to="/tickets" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('ticket.list.headline')}</Link>
        <Link to="/slas" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('sla.list.headline')}</Link>
        <Link to="/priorityMatrices" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('priorityMatrix.list.headline')}</Link>
        <Link to="/attachments" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('attachment.list.headline')}</Link>
        <Link to="/comments" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('comment.list.headline')}</Link>
        <Link to="/notifications" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('notification.list.headline')}</Link>
        <Link to="/knowledgeArticless" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('knowledgeArticles.list.headline')}</Link>
        <Link to="/historyLogss" className="w-full border-gray-300 hover:bg-gray-100 rounded-b px-4 py-2">{t('historyLogs.list.headline')}</Link>
      </div>
    </div>
  </>);
}
