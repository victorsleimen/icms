import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { SlaDTO } from 'app/sla/sla-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function SlaList() {
  const { t } = useTranslation();
  useDocumentTitle(t('sla.list.headline'));

  const [slas, setSlas] = useState<ListModel<SlaDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('sla.list.sort.id,ASC'), 
    'name,ASC': t('sla.list.sort.name,ASC'), 
    'description,ASC': t('sla.list.sort.description,ASC')
  };

  const getAllSlas = async () => {
    try {
      const response = await axios.get('/api/slas?' + listParams);
      setSlas(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/slas/' + id);
      navigate('/slas', {
            state: {
              msgInfo: t('sla.delete.success')
            }
          });
      getAllSlas();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/slas', {
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
    getAllSlas();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('sla.list.headline')}</h1>
      <div>
        <Link to="/slas/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('sla.list.createNew')}</Link>
      </div>
    </div>
    {((slas?._embedded && slas?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('sla.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!slas?._embedded || slas?.page?.totalElements === 0 ? (
    <div>{t('sla.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('sla.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('sla.name.label')}</th>
            <th scope="col" className="text-left p-2">{t('sla.responseTime.label')}</th>
            <th scope="col" className="text-left p-2">{t('sla.resolutionTime.label')}</th>
            <th scope="col" className="text-left p-2">{t('sla.client.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {slas?._embedded?.['slaDTOList']?.map((sla) => (
          <tr key={sla.id} className="odd:bg-gray-100">
            <td className="p-2">{sla.id}</td>
            <td className="p-2">{sla.name}</td>
            <td className="p-2">{sla.responseTime}</td>
            <td className="p-2">{sla.resolutionTime}</td>
            <td className="p-2">{sla.client}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/slas/edit/' + sla.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('sla.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(sla.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('sla.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={slas?.page} />
    </>)}
  </>);
}
