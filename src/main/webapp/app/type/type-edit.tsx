import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { TypeDTO } from 'app/type/type-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    typeCode: yup.string().emptyToNull().max(3).required(),
    typeName: yup.string().emptyToNull().max(100).required(),
    sequenceNum: yup.number().integer().emptyToNull().required()
  });
}

export default function TypeEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('type.edit.headline'));

  const navigate = useNavigate();
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const data = (await axios.get('/api/types/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateType = async (data: TypeDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/types/' + currentId, data);
      navigate('/types', {
            state: {
              msgSuccess: t('type.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('type.edit.headline')}</h1>
      <div>
        <Link to="/types" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('type.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateType)} noValidate>
      <InputRow useFormResult={useFormResult} object="type" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="type" field="typeCode" required={true} />
      <InputRow useFormResult={useFormResult} object="type" field="typeName" required={true} />
      <InputRow useFormResult={useFormResult} object="type" field="sequenceNum" required={true} type="number" />
      <input type="submit" value={t('type.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
