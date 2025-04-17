import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { ClientDTO } from 'app/client/client-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    clientName: yup.string().emptyToNull().max(255).required(),
    address: yup.string().emptyToNull().max(255).required(),
    tel: yup.string().emptyToNull().max(20),
    fax: yup.string().emptyToNull().max(20),
    mobile: yup.string().emptyToNull().max(20),
    email: yup.string().emptyToNull().max(125),
    registrationNum: yup.string().emptyToNull().max(50),
    webURL: yup.string().emptyToNull().max(125).required(),
    isBcom: yup.bool()
  });
}

export default function ClientEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('client.edit.headline'));

  const navigate = useNavigate();
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const data = (await axios.get('/api/clients/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateClient = async (data: ClientDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/clients/' + currentId, data);
      navigate('/clients', {
            state: {
              msgSuccess: t('client.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('client.edit.headline')}</h1>
      <div>
        <Link to="/clients" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('client.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateClient)} noValidate>
      <InputRow useFormResult={useFormResult} object="client" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="client" field="clientName" required={true} />
      <InputRow useFormResult={useFormResult} object="client" field="address" required={true} />
      <InputRow useFormResult={useFormResult} object="client" field="tel" />
      <InputRow useFormResult={useFormResult} object="client" field="fax" />
      <InputRow useFormResult={useFormResult} object="client" field="mobile" />
      <InputRow useFormResult={useFormResult} object="client" field="email" />
      <InputRow useFormResult={useFormResult} object="client" field="registrationNum" />
      <InputRow useFormResult={useFormResult} object="client" field="webURL" required={true} />
      <InputRow useFormResult={useFormResult} object="client" field="isBcom" type="checkbox" />
      <input type="submit" value={t('client.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
