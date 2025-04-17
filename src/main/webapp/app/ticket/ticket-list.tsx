import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { TicketDTO } from 'app/ticket/ticket-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function TicketList() {
  const { t } = useTranslation();
  useDocumentTitle(t('ticket.list.headline'));

  const [tickets, setTickets] = useState<ListModel<TicketDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('ticket.list.sort.id,ASC'), 
    'code,ASC': t('ticket.list.sort.code,ASC'), 
    'name,ASC': t('ticket.list.sort.name,ASC')
  };

  const getAllTickets = async () => {
    try {
      const response = await axios.get('/api/tickets?' + listParams);
      setTickets(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/tickets/' + id);
      navigate('/tickets', {
            state: {
              msgInfo: t('ticket.delete.success')
            }
          });
      getAllTickets();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/tickets', {
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
    getAllTickets();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('ticket.list.headline')}</h1>
      <div>
        <Link to="/tickets/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('ticket.list.createNew')}</Link>
      </div>
    </div>
    {((tickets?._embedded && tickets?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('ticket.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!tickets?._embedded || tickets?.page?.totalElements === 0 ? (
    <div>{t('ticket.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('ticket.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('ticket.code.label')}</th>
            <th scope="col" className="text-left p-2">{t('ticket.name.label')}</th>
            <th scope="col" className="text-left p-2">{t('ticket.description.label')}</th>
            <th scope="col" className="text-left p-2">{t('ticket.openDate.label')}</th>
            <th scope="col" className="text-left p-2">{t('ticket.ticketType.label')}</th>
            <th scope="col" className="text-left p-2">{t('ticket.owner.label')}</th>
            <th scope="col" className="text-left p-2">{t('ticket.status.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {tickets?._embedded?.['ticketDTOList']?.map((ticket) => (
          <tr key={ticket.id} className="odd:bg-gray-100">
            <td className="p-2">{ticket.id}</td>
            <td className="p-2">{ticket.code}</td>
            <td className="p-2">{ticket.name}</td>
            <td className="p-2">{ticket.description}</td>
            <td className="p-2">{ticket.openDate}</td>
            <td className="p-2">{ticket.ticketType}</td>
            <td className="p-2">{ticket.owner}</td>
            <td className="p-2">{ticket.status}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/tickets/edit/' + ticket.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('ticket.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(ticket.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('ticket.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={tickets?.page} />
    </>)}
  </>);
}
