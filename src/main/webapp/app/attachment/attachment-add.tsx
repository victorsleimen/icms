import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { AttachmentDTO } from 'app/attachment/attachment-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    fileName: yup.string().emptyToNull().max(255).required(),
    filePath: yup.string().emptyToNull().required(),
    mimeType: yup.string().emptyToNull().max(100),
    client: yup.number().integer().emptyToNull().required(),
    type: yup.number().integer().emptyToNull().required()
  });
}

export default function AttachmentAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('attachment.add.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());
  const [typeValues, setTypeValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/attachments/clientValues');
      setClientValues(clientValuesResponse.data);
      const typeValuesResponse = await axios.get('/api/attachments/typeValues');
      setTypeValues(typeValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createAttachment = async (data: AttachmentDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/attachments', data);
      navigate('/attachments', {
            state: {
              msgSuccess: t('attachment.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('attachment.add.headline')}</h1>
      <div>
        <Link to="/attachments" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('attachment.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createAttachment)} noValidate>
      <InputRow useFormResult={useFormResult} object="attachment" field="fileName" required={true} />
      <InputRow useFormResult={useFormResult} object="attachment" field="filePath" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="attachment" field="mimeType" />
      <InputRow useFormResult={useFormResult} object="attachment" field="client" required={true} type="select" options={clientValues} />
      <InputRow useFormResult={useFormResult} object="attachment" field="type" required={true} type="select" options={typeValues} />
      <input type="submit" value={t('attachment.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
