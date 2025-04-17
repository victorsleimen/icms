import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { CommentDTO } from 'app/comment/comment-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    description: yup.string().emptyToNull().required(),
    client: yup.number().integer().emptyToNull().required(),
    user: yup.number().integer().emptyToNull().required()
  });
}

export default function CommentEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('comment.edit.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());
  const [userValues, setUserValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/comments/clientValues');
      setClientValues(clientValuesResponse.data);
      const userValuesResponse = await axios.get('/api/comments/userValues');
      setUserValues(userValuesResponse.data);
      const data = (await axios.get('/api/comments/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateComment = async (data: CommentDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/comments/' + currentId, data);
      navigate('/comments', {
            state: {
              msgSuccess: t('comment.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('comment.edit.headline')}</h1>
      <div>
        <Link to="/comments" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('comment.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateComment)} noValidate>
      <InputRow useFormResult={useFormResult} object="comment" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="comment" field="description" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="comment" field="client" required={true} type="select" options={clientValues} />
      <InputRow useFormResult={useFormResult} object="comment" field="user" required={true} type="select" options={userValues} />
      <input type="submit" value={t('comment.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
