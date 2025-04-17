import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
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

export default function ModuleSatusOrderAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('moduleSatusOrder.add.headline'));

  const navigate = useNavigate();
  const [typeValues, setTypeValues] = useState<Map<number,string>>(new Map());
  const [statusValues, setStatusValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const typeValuesResponse = await axios.get('/api/moduleSatusOrders/typeValues');
      setTypeValues(typeValuesResponse.data);
      const statusValuesResponse = await axios.get('/api/moduleSatusOrders/statusValues');
      setStatusValues(statusValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createModuleSatusOrder = async (data: ModuleSatusOrderDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/moduleSatusOrders', data);
      navigate('/moduleSatusOrders', {
            state: {
              msgSuccess: t('moduleSatusOrder.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('moduleSatusOrder.add.headline')}</h1>
      <div>
        <Link to="/moduleSatusOrders" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('moduleSatusOrder.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createModuleSatusOrder)} noValidate>
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="moduleName" required={true} />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="level" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="order" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="type" required={true} type="select" options={typeValues} />
      <InputRow useFormResult={useFormResult} object="moduleSatusOrder" field="status" required={true} type="select" options={statusValues} />
      <input type="submit" value={t('moduleSatusOrder.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
