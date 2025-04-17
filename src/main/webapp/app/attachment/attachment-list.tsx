import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useSearchParams } from 'react-router';
import { handleServerError, getListParams } from 'app/common/utils';
import { AttachmentDTO } from 'app/attachment/attachment-model';
import { Pagination } from 'app/common/list-helper/pagination';
import { ListModel } from 'app/common/hateoas';
import axios from 'axios';
import SearchFilter from 'app/common/list-helper/search-filter';
import Sorting from 'app/common/list-helper/sorting';
import useDocumentTitle from 'app/common/use-document-title';


export default function AttachmentList() {
  const { t } = useTranslation();
  useDocumentTitle(t('attachment.list.headline'));

  const [attachments, setAttachments] = useState<ListModel<AttachmentDTO>|undefined>(undefined);
  const navigate = useNavigate();
  const [searchParams, ] = useSearchParams();
  const listParams = getListParams();
  const sortOptions = {
    'id,ASC': t('attachment.list.sort.id,ASC'), 
    'fileName,ASC': t('attachment.list.sort.fileName,ASC'), 
    'filePath,ASC': t('attachment.list.sort.filePath,ASC')
  };

  const getAllAttachments = async () => {
    try {
      const response = await axios.get('/api/attachments?' + listParams);
      setAttachments(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/attachments/' + id);
      navigate('/attachments', {
            state: {
              msgInfo: t('attachment.delete.success')
            }
          });
      getAllAttachments();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllAttachments();
  }, [searchParams]);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('attachment.list.headline')}</h1>
      <div>
        <Link to="/attachments/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('attachment.list.createNew')}</Link>
      </div>
    </div>
    {((attachments?._embedded && attachments?.page?.totalElements !== 0) || searchParams.get('filter')) && (
    <div className="flex flex-wrap justify-between">
      <SearchFilter placeholder={t('attachment.list.filter')} />
      <Sorting sortOptions={sortOptions} />
    </div>
    )}
    {!attachments?._embedded || attachments?.page?.totalElements === 0 ? (
    <div>{t('attachment.list.empty')}</div>
    ) : (<>
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('attachment.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('attachment.fileName.label')}</th>
            <th scope="col" className="text-left p-2">{t('attachment.mimeType.label')}</th>
            <th scope="col" className="text-left p-2">{t('attachment.client.label')}</th>
            <th scope="col" className="text-left p-2">{t('attachment.type.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {attachments?._embedded?.['attachmentDTOList']?.map((attachment) => (
          <tr key={attachment.id} className="odd:bg-gray-100">
            <td className="p-2">{attachment.id}</td>
            <td className="p-2">{attachment.fileName}</td>
            <td className="p-2">{attachment.mimeType}</td>
            <td className="p-2">{attachment.client}</td>
            <td className="p-2">{attachment.type}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/attachments/edit/' + attachment.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('attachment.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(attachment.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('attachment.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    <Pagination page={attachments?.page} />
    </>)}
  </>);
}
