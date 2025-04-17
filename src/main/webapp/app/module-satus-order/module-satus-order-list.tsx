import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { ModuleSatusOrderDTO } from 'app/module-satus-order/module-satus-order-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function ModuleSatusOrderList() {
  const { t } = useTranslation();
  useDocumentTitle(t('moduleSatusOrder.list.headline'));

  const [moduleSatusOrders, setModuleSatusOrders] = useState<ListModel<ModuleSatusOrderDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('moduleSatusOrder.list.sort.id,ASC'), 
    'moduleName,ASC': t('moduleSatusOrder.list.sort.moduleName,ASC'), 
    'level,ASC': t('moduleSatusOrder.list.sort.level,ASC')
  };

  const getAllModuleSatusOrders = async () => {
    try {
      const response = await axios.get('/api/moduleSatusOrders?' + listParams);
      setModuleSatusOrders(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/moduleSatusOrders/' + id);
      navigate('/moduleSatusOrders', {
            state: {
              msgInfo: t('moduleSatusOrder.delete.success')
            }
          });
      getAllModuleSatusOrders();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllModuleSatusOrders();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('moduleSatusOrder.list.headline')}</h1>
      <div>
        <Link to="/moduleSatusOrders/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('moduleSatusOrder.list.createNew')}</Link>
      </div>
    </div>
    {((moduleSatusOrders?._embedded && moduleSatusOrders?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('moduleSatusOrder.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!moduleSatusOrders?._embedded || moduleSatusOrders?.page?.totalElements === 0 ? (
    <div>{t('moduleSatusOrder.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('moduleSatusOrder.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('moduleSatusOrder.moduleName.label')}</th>
            <th scope="col" className="text-left p-2">{t('moduleSatusOrder.level.label')}</th>
            <th scope="col" className="text-left p-2">{t('moduleSatusOrder.order.label')}</th>
            <th scope="col" className="text-left p-2">{t('moduleSatusOrder.type.label')}</th>
            <th scope="col" className="text-left p-2">{t('moduleSatusOrder.status.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {moduleSatusOrders?._embedded?.['moduleSatusOrderDTOList']?.map((moduleSatusOrder) => (
          <tr key={moduleSatusOrder.id} className="odd:bg-gray-100">
            <td className="p-2">{moduleSatusOrder.id}</td>
            <td className="p-2">{moduleSatusOrder.moduleName}</td>
            <td className="p-2">{moduleSatusOrder.level}</td>
            <td className="p-2">{moduleSatusOrder.order}</td>
            <td className="p-2">{moduleSatusOrder.type}</td>
            <td className="p-2">{moduleSatusOrder.status}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/moduleSatusOrders/edit/' + moduleSatusOrder.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('moduleSatusOrder.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(moduleSatusOrder.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('moduleSatusOrder.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={moduleSatusOrders?.page} />
    </>)}
  </>);
}
