import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { NotificationDTO } from 'app/notification/notification-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function NotificationList() {
  const { t } = useTranslation();
  useDocumentTitle(t('notification.list.headline'));

  const [notifications, setNotifications] = useState<ListModel<NotificationDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('notification.list.sort.id,ASC'), 
    'notifType,ASC': t('notification.list.sort.notifType,ASC'), 
    'message,ASC': t('notification.list.sort.message,ASC')
  };

  const getAllNotifications = async () => {
    try {
      const response = await axios.get('/api/notifications?' + listParams);
      setNotifications(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/notifications/' + id);
      navigate('/notifications', {
            state: {
              msgInfo: t('notification.delete.success')
            }
          });
      getAllNotifications();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllNotifications();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('notification.list.headline')}</h1>
      <div>
        <Link to="/notifications/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('notification.list.createNew')}</Link>
      </div>
    </div>
    {((notifications?._embedded && notifications?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('notification.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!notifications?._embedded || notifications?.page?.totalElements === 0 ? (
    <div>{t('notification.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('notification.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('notification.notifType.label')}</th>
            <th scope="col" className="text-left p-2">{t('notification.isRead.label')}</th>
            <th scope="col" className="text-left p-2">{t('notification.client.label')}</th>
            <th scope="col" className="text-left p-2">{t('notification.user.label')}</th>
            <th scope="col" className="text-left p-2">{t('notification.ticket.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {notifications?._embedded?.['notificationDTOList']?.map((notification) => (
          <tr key={notification.id} className="odd:bg-gray-100">
            <td className="p-2">{notification.id}</td>
            <td className="p-2">{notification.notifType}</td>
            <td className="p-2">{notification.isRead?.toString()}</td>
            <td className="p-2">{notification.client}</td>
            <td className="p-2">{notification.user}</td>
            <td className="p-2">{notification.ticket}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/notifications/edit/' + notification.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('notification.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(notification.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('notification.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={notifications?.page} />
    </>)}
  </>);
}
