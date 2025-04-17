import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { SlaDTO } from 'app/sla/sla-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(100).required(),
    description: yup.string().emptyToNull(),
    responseTime: yup.number().integer().emptyToNull().required(),
    resolutionTime: yup.number().integer().emptyToNull().required(),
    client: yup.number().integer().emptyToNull().required()
  });
}

export default function SlaAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('sla.add.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/slas/clientValues');
      setClientValues(clientValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createSla = async (data: SlaDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/slas', data);
      navigate('/slas', {
            state: {
              msgSuccess: t('sla.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('sla.add.headline')}</h1>
      <div>
        <Link to="/slas" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('sla.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createSla)} noValidate>
      <InputRow useFormResult={useFormResult} object="sla" field="name" required={true} />
      <InputRow useFormResult={useFormResult} object="sla" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="sla" field="responseTime" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="sla" field="resolutionTime" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="sla" field="client" required={true} type="select" options={clientValues} />
      <input type="submit" value={t('sla.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
