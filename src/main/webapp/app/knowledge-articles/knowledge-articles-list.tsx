import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { KnowledgeArticlesDTO } from 'app/knowledge-articles/knowledge-articles-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function KnowledgeArticlesList() {
  const { t } = useTranslation();
  useDocumentTitle(t('knowledgeArticles.list.headline'));

  const [knowledgeArticleses, setKnowledgeArticleses] = useState<ListModel<KnowledgeArticlesDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('knowledgeArticles.list.sort.id,ASC'), 
    'title,ASC': t('knowledgeArticles.list.sort.title,ASC'), 
    'content,ASC': t('knowledgeArticles.list.sort.content,ASC')
  };

  const getAllKnowledgeArticleses = async () => {
    try {
      const response = await axios.get('/api/knowledgeArticless?' + listParams);
      setKnowledgeArticleses(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/knowledgeArticless/' + id);
      navigate('/knowledgeArticless', {
            state: {
              msgInfo: t('knowledgeArticles.delete.success')
            }
          });
      getAllKnowledgeArticleses();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllKnowledgeArticleses();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('knowledgeArticles.list.headline')}</h1>
      <div>
        <Link to="/knowledgeArticless/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('knowledgeArticles.list.createNew')}</Link>
      </div>
    </div>
    {((knowledgeArticleses?._embedded && knowledgeArticleses?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('knowledgeArticles.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!knowledgeArticleses?._embedded || knowledgeArticleses?.page?.totalElements === 0 ? (
    <div>{t('knowledgeArticles.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('knowledgeArticles.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('knowledgeArticles.title.label')}</th>
            <th scope="col" className="text-left p-2">{t('knowledgeArticles.status.label')}</th>
            <th scope="col" className="text-left p-2">{t('knowledgeArticles.client.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {knowledgeArticleses?._embedded?.['knowledgeArticlesDTOList']?.map((knowledgeArticles) => (
          <tr key={knowledgeArticles.id} className="odd:bg-gray-100">
            <td className="p-2">{knowledgeArticles.id}</td>
            <td className="p-2">{knowledgeArticles.title}</td>
            <td className="p-2">{knowledgeArticles.status}</td>
            <td className="p-2">{knowledgeArticles.client}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/knowledgeArticless/edit/' + knowledgeArticles.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('knowledgeArticles.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(knowledgeArticles.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('knowledgeArticles.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={knowledgeArticleses?.page} />
    </>)}
  </>);
}
