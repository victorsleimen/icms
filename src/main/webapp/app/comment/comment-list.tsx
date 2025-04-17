import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { CommentDTO } from 'app/comment/comment-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function CommentList() {
  const { t } = useTranslation();
  useDocumentTitle(t('comment.list.headline'));

  const [comments, setComments] = useState<ListModel<CommentDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('comment.list.sort.id,ASC'), 
    'description,ASC': t('comment.list.sort.description,ASC')
  };

  const getAllComments = async () => {
    try {
      const response = await axios.get('/api/comments?' + listParams);
      setComments(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/comments/' + id);
      navigate('/comments', {
            state: {
              msgInfo: t('comment.delete.success')
            }
          });
      getAllComments();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllComments();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('comment.list.headline')}</h1>
      <div>
        <Link to="/comments/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('comment.list.createNew')}</Link>
      </div>
    </div>
    {((comments?._embedded && comments?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('comment.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!comments?._embedded || comments?.page?.totalElements === 0 ? (
    <div>{t('comment.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('comment.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('comment.client.label')}</th>
            <th scope="col" className="text-left p-2">{t('comment.user.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {comments?._embedded?.['commentDTOList']?.map((comment) => (
          <tr key={comment.id} className="odd:bg-gray-100">
            <td className="p-2">{comment.id}</td>
            <td className="p-2">{comment.client}</td>
            <td className="p-2">{comment.user}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/comments/edit/' + comment.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('comment.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(comment.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('comment.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={comments?.page} />
    </>)}
  </>);
}
