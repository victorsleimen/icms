import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { ClientDTO } from 'app/client/client-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function ClientList() {
  const { t } = useTranslation();
  useDocumentTitle(t('client.list.headline'));

  const [clients, setClients] = useState<ListModel<ClientDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('client.list.sort.id,ASC'), 
    'clientName,ASC': t('client.list.sort.clientName,ASC'), 
    'address,ASC': t('client.list.sort.address,ASC')
  };

  const getAllClients = async () => {
    try {
      const response = await axios.get('/api/clients?' + listParams);
      setClients(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/clients/' + id);
      navigate('/clients', {
            state: {
              msgInfo: t('client.delete.success')
            }
          });
      getAllClients();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/clients', {
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
    getAllClients();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('client.list.headline')}</h1>
      <div>
        <Link to="/clients/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('client.list.createNew')}</Link>
      </div>
    </div>
    {((clients?._embedded && clients?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('client.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!clients?._embedded || clients?.page?.totalElements === 0 ? (
    <div>{t('client.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('client.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('client.clientName.label')}</th>
            <th scope="col" className="text-left p-2">{t('client.address.label')}</th>
            <th scope="col" className="text-left p-2">{t('client.tel.label')}</th>
            <th scope="col" className="text-left p-2">{t('client.fax.label')}</th>
            <th scope="col" className="text-left p-2">{t('client.mobile.label')}</th>
            <th scope="col" className="text-left p-2">{t('client.email.label')}</th>
            <th scope="col" className="text-left p-2">{t('client.registrationNum.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {clients?._embedded?.['clientDTOList']?.map((client) => (
          <tr key={client.id} className="odd:bg-gray-100">
            <td className="p-2">{client.id}</td>
            <td className="p-2">{client.clientName}</td>
            <td className="p-2">{client.address}</td>
            <td className="p-2">{client.tel}</td>
            <td className="p-2">{client.fax}</td>
            <td className="p-2">{client.mobile}</td>
            <td className="p-2">{client.email}</td>
            <td className="p-2">{client.registrationNum}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/clients/edit/' + client.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('client.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(client.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('client.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={clients?.page} />
    </>)}
  </>);
}
