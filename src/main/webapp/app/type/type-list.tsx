import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { TypeDTO } from 'app/type/type-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function TypeList() {
  const { t } = useTranslation();
  useDocumentTitle(t('type.list.headline'));

  const [types, setTypes] = useState<ListModel<TypeDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('type.list.sort.id,ASC'), 
    'typeCode,ASC': t('type.list.sort.typeCode,ASC'), 
    'typeName,ASC': t('type.list.sort.typeName,ASC')
  };

  const getAllTypes = async () => {
    try {
      const response = await axios.get('/api/types?' + listParams);
      setTypes(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/types/' + id);
      navigate('/types', {
            state: {
              msgInfo: t('type.delete.success')
            }
          });
      getAllTypes();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/types', {
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
    getAllTypes();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('type.list.headline')}</h1>
      <div>
        <Link to="/types/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('type.list.createNew')}</Link>
      </div>
    </div>
    {((types?._embedded && types?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('type.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!types?._embedded || types?.page?.totalElements === 0 ? (
    <div>{t('type.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('type.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('type.typeCode.label')}</th>
            <th scope="col" className="text-left p-2">{t('type.typeName.label')}</th>
            <th scope="col" className="text-left p-2">{t('type.sequenceNum.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {types?._embedded?.['typeDTOList']?.map((type) => (
          <tr key={type.id} className="odd:bg-gray-100">
            <td className="p-2">{type.id}</td>
            <td className="p-2">{type.typeCode}</td>
            <td className="p-2">{type.typeName}</td>
            <td className="p-2">{type.sequenceNum}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/types/edit/' + type.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('type.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(type.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('type.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={types?.page} />
    </>)}
  </>);
}
