import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { PriorityMatrixDTO } from 'app/priority-matrix/priority-matrix-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    urgency: yup.number().integer().emptyToNull().required(),
    impact: yup.number().integer().emptyToNull().required(),
    priority: yup.string().emptyToNull(),
    type: yup.number().integer().emptyToNull().required()
  });
}

export default function PriorityMatrixEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('priorityMatrix.edit.headline'));

  const navigate = useNavigate();
  const [typeValues, setTypeValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const typeValuesResponse = await axios.get('/api/priorityMatrices/typeValues');
      setTypeValues(typeValuesResponse.data);
      const data = (await axios.get('/api/priorityMatrices/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updatePriorityMatrix = async (data: PriorityMatrixDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/priorityMatrices/' + currentId, data);
      navigate('/priorityMatrices', {
            state: {
              msgSuccess: t('priorityMatrix.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('priorityMatrix.edit.headline')}</h1>
      <div>
        <Link to="/priorityMatrices" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('priorityMatrix.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updatePriorityMatrix)} noValidate>
      <InputRow useFormResult={useFormResult} object="priorityMatrix" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="priorityMatrix" field="urgency" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="priorityMatrix" field="impact" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="priorityMatrix" field="priority" required={true} type="select" options={{'LOW': 'LOW', 'MEDIUM': 'MEDIUM', 'HIGH': 'HIGH', 'CRITICAL': 'CRITICAL'}} />
      <InputRow useFormResult={useFormResult} object="priorityMatrix" field="type" required={true} type="select" options={typeValues} />
      <input type="submit" value={t('priorityMatrix.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
