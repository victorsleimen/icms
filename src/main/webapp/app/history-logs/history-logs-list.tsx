import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { HistoryLogsDTO } from 'app/history-logs/history-logs-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function HistoryLogsList() {
  const { t } = useTranslation();
  useDocumentTitle(t('historyLogs.list.headline'));

  const [historyLogses, setHistoryLogses] = useState<ListModel<HistoryLogsDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('historyLogs.list.sort.id,ASC'), 
    'entityType,ASC': t('historyLogs.list.sort.entityType,ASC'), 
    'changedDate,ASC': t('historyLogs.list.sort.changedDate,ASC')
  };

  const getAllHistoryLogses = async () => {
    try {
      const response = await axios.get('/api/historyLogss?' + listParams);
      setHistoryLogses(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/historyLogss/' + id);
      navigate('/historyLogss', {
            state: {
              msgInfo: t('historyLogs.delete.success')
            }
          });
      getAllHistoryLogses();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllHistoryLogses();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('historyLogs.list.headline')}</h1>
      <div>
        <Link to="/historyLogss/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('historyLogs.list.createNew')}</Link>
      </div>
    </div>
    {((historyLogses?._embedded && historyLogses?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('historyLogs.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!historyLogses?._embedded || historyLogses?.page?.totalElements === 0 ? (
    <div>{t('historyLogs.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('historyLogs.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('historyLogs.entityType.label')}</th>
            <th scope="col" className="text-left p-2">{t('historyLogs.action.label')}</th>
            <th scope="col" className="text-left p-2">{t('historyLogs.client.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {historyLogses?._embedded?.['historyLogsDTOList']?.map((historyLogs) => (
          <tr key={historyLogs.id} className="odd:bg-gray-100">
            <td className="p-2">{historyLogs.id}</td>
            <td className="p-2">{historyLogs.entityType}</td>
            <td className="p-2">{historyLogs.action}</td>
            <td className="p-2">{historyLogs.client}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/historyLogss/edit/' + historyLogs.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('historyLogs.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(historyLogs.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('historyLogs.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={historyLogses?.page} />
    </>)}
  </>);
}
