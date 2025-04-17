import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { AllStatusDTO } from 'app/all-status/all-status-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(180).required()
  });
}

export default function AllStatusAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('allStatus.add.headline'));

  const navigate = useNavigate();

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const createAllStatus = async (data: AllStatusDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/allStatuses', data);
      navigate('/allStatuses', {
            state: {
              msgSuccess: t('allStatus.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('allStatus.add.headline')}</h1>
      <div>
        <Link to="/allStatuses" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('allStatus.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createAllStatus)} noValidate>
      <InputRow useFormResult={useFormResult} object="allStatus" field="name" required={true} />
      <input type="submit" value={t('allStatus.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
