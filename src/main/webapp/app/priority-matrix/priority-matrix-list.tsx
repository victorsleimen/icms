import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { PriorityMatrixDTO } from 'app/priority-matrix/priority-matrix-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function PriorityMatrixList() {
  const { t } = useTranslation();
  useDocumentTitle(t('priorityMatrix.list.headline'));

  const [priorityMatrixes, setPriorityMatrixes] = useState<ListModel<PriorityMatrixDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('priorityMatrix.list.sort.id,ASC'), 
    'urgency,ASC': t('priorityMatrix.list.sort.urgency,ASC'), 
    'impact,ASC': t('priorityMatrix.list.sort.impact,ASC')
  };

  const getAllPriorityMatrixes = async () => {
    try {
      const response = await axios.get('/api/priorityMatrices?' + listParams);
      setPriorityMatrixes(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/priorityMatrices/' + id);
      navigate('/priorityMatrices', {
            state: {
              msgInfo: t('priorityMatrix.delete.success')
            }
          });
      getAllPriorityMatrixes();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllPriorityMatrixes();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('priorityMatrix.list.headline')}</h1>
      <div>
        <Link to="/priorityMatrices/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('priorityMatrix.list.createNew')}</Link>
      </div>
    </div>
    {((priorityMatrixes?._embedded && priorityMatrixes?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('priorityMatrix.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!priorityMatrixes?._embedded || priorityMatrixes?.page?.totalElements === 0 ? (
    <div>{t('priorityMatrix.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('priorityMatrix.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('priorityMatrix.urgency.label')}</th>
            <th scope="col" className="text-left p-2">{t('priorityMatrix.impact.label')}</th>
            <th scope="col" className="text-left p-2">{t('priorityMatrix.priority.label')}</th>
            <th scope="col" className="text-left p-2">{t('priorityMatrix.type.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {priorityMatrixes?._embedded?.['priorityMatrixDTOList']?.map((priorityMatrix) => (
          <tr key={priorityMatrix.id} className="odd:bg-gray-100">
            <td className="p-2">{priorityMatrix.id}</td>
            <td className="p-2">{priorityMatrix.urgency}</td>
            <td className="p-2">{priorityMatrix.impact}</td>
            <td className="p-2">{priorityMatrix.priority}</td>
            <td className="p-2">{priorityMatrix.type}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/priorityMatrices/edit/' + priorityMatrix.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('priorityMatrix.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(priorityMatrix.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('priorityMatrix.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={priorityMatrixes?.page} />
    </>)}
  </>);
}
