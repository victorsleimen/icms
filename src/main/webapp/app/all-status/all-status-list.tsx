import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { AllStatusDTO } from 'app/all-status/all-status-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function AllStatusList() {
  const { t } = useTranslation();
  useDocumentTitle(t('allStatus.list.headline'));

  const [allStatuses, setAllStatuses] = useState<ListModel<AllStatusDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('allStatus.list.sort.id,ASC'), 
    'name,ASC': t('allStatus.list.sort.name,ASC')
  };

  const getAllAllStatuses = async () => {
    try {
      const response = await axios.get('/api/allStatuses?' + listParams);
      setAllStatuses(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/allStatuses/' + id);
      navigate('/allStatuses', {
            state: {
              msgInfo: t('allStatus.delete.success')
            }
          });
      getAllAllStatuses();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/allStatuses', {
              state: {
                msgError: t(messageParts[0]!, { id: messageParts[1]! })
              }
            });
        return;
      }
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllAllStatuses();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('allStatus.list.headline')}</h1>
      <div>
        <Link to="/allStatuses/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('allStatus.list.createNew')}</Link>
      </div>
    </div>
    {((allStatuses?._embedded && allStatuses?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('allStatus.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!allStatuses?._embedded || allStatuses?.page?.totalElements === 0 ? (
    <div>{t('allStatus.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('allStatus.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('allStatus.name.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {allStatuses?._embedded?.['allStatusDTOList']?.map((allStatus) => (
          <tr key={allStatus.id} className="odd:bg-gray-100">
            <td className="p-2">{allStatus.id}</td>
            <td className="p-2">{allStatus.name}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/allStatuses/edit/' + allStatus.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('allStatus.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(allStatus.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('allStatus.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={allStatuses?.page} />
    </>)}
  </>);
}
