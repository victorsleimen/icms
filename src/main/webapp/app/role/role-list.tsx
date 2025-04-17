import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { RoleDTO } from 'app/role/role-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function RoleList() {
  const { t } = useTranslation();
  useDocumentTitle(t('role.list.headline'));

  const [roles, setRoles] = useState<ListModel<RoleDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('role.list.sort.id,ASC'), 
    'name,ASC': t('role.list.sort.name,ASC')
  };

  const getAllRoles = async () => {
    try {
      const response = await axios.get('/api/roles?' + listParams);
      setRoles(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/roles/' + id);
      navigate('/roles', {
            state: {
              msgInfo: t('role.delete.success')
            }
          });
      getAllRoles();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllRoles();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('role.list.headline')}</h1>
      <div>
        <Link to="/roles/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('role.list.createNew')}</Link>
      </div>
    </div>
    {((roles?._embedded && roles?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('role.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!roles?._embedded || roles?.page?.totalElements === 0 ? (
    <div>{t('role.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('role.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('role.name.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {roles?._embedded?.['roleDTOList']?.map((role) => (
          <tr key={role.id} className="odd:bg-gray-100">
            <td className="p-2">{role.id}</td>
            <td className="p-2">{role.name}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/roles/edit/' + role.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('role.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(role.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('role.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={roles?.page} />
    </>)}
  </>);
}
