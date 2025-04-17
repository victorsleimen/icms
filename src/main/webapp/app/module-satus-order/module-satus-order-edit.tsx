import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { ModuleSatusOrderDTO } from 'app/module-satus-order/module-satus-order-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    moduleName: yup.string().emptyToNull().max(75).required(),
    level: yup.number().integer().emptyToNull().required(),
    order: yup.number().integer().emptyToNull().required(),
    type: yup.number().integer().emptyToNull().required(),
    status: yup.number().integer().emptyToNull().required()
  });
}

export default function ModuleSatusOrderEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('moduleSatusOrder.edit.headline'));

  const navigate = useNavigate();
  const [typeValues, setTypeValues] = useState<Map<number,string>>(new Map());
  const [statusValues, setStatusValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const typeValuesResponse = await axios.get('/api/moduleSatusOrders/typeValues');
      setTypeValues(typeValuesResponse.data);
      const statusValuesResponse = await axios.get('/api/moduleSatusOrders/statusValues');
      setStatusValues(statusValuesResponse.data);
      const data = (await axios.get('/api/moduleSatusOrders/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateModuleSatusOrder = async (data: ModuleSatusOrderDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/moduleSatusOrders/' + currentId, data);
      navigate('/moduleSatusOrders', {
            state: {
              msgSuccess: t('moduleSatusOrder.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('moduleSatusOrder.edit.headline')}</h1>
      <div>
        <Link to="/moduleSatusOrders" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('moduleSatusOrder.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateModuleSatusOrder)} noValidate>
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="moduleName" required={true} />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="level" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="order" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="type" required={true} type="select" options={typeValues} />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="status" required={true} type="select" options={statusValues} />
      <input type="submit" value={t('moduleSatusOrder.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
