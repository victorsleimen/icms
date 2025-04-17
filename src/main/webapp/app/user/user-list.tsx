import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { UserDTO } from 'app/user/user-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function UserList() {
  const { t } = useTranslation();
  useDocumentTitle(t('user.list.headline'));

  const [users, setUsers] = useState<ListModel<UserDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('user.list.sort.id,ASC'), 
    'username,ASC': t('user.list.sort.username,ASC'), 
    'password,ASC': t('user.list.sort.password,ASC')
  };

  const getAllUsers = async () => {
    try {
      const response = await axios.get('/api/users?' + listParams);
      setUsers(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/users/' + id);
      navigate('/users', {
            state: {
              msgInfo: t('user.delete.success')
            }
          });
      getAllUsers();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/users', {
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
    getAllUsers();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('user.list.headline')}</h1>
      <div>
        <Link to="/users/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('user.list.createNew')}</Link>
      </div>
    </div>
    {((users?._embedded && users?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('user.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!users?._embedded || users?.page?.totalElements === 0 ? (
    <div>{t('user.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('user.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('user.username.label')}</th>
            <th scope="col" className="text-left p-2">{t('user.password.label')}</th>
            <th scope="col" className="text-left p-2">{t('user.firstName.label')}</th>
            <th scope="col" className="text-left p-2">{t('user.lastName.label')}</th>
            <th scope="col" className="text-left p-2">{t('user.email.label')}</th>
            <th scope="col" className="text-left p-2">{t('user.doe.label')}</th>
            <th scope="col" className="text-left p-2">{t('user.timeZoneId.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {users?._embedded?.['userDTOList']?.map((user) => (
          <tr key={user.id} className="odd:bg-gray-100">
            <td className="p-2">{user.id}</td>
            <td className="p-2">{user.username}</td>
            <td className="p-2">{user.password}</td>
            <td className="p-2">{user.firstName}</td>
            <td className="p-2">{user.lastName}</td>
            <td className="p-2">{user.email}</td>
            <td className="p-2">{user.doe}</td>
            <td className="p-2">{user.timeZoneId}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/users/edit/' + user.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('user.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(user.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('user.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={users?.page} />
    </>)}
  </>);
}
