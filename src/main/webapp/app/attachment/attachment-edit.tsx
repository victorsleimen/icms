import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
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

export default function AttachmentEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('attachment.edit.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());
  const [typeValues, setTypeValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/attachments/clientValues');
      setClientValues(clientValuesResponse.data);
      const typeValuesResponse = await axios.get('/api/attachments/typeValues');
      setTypeValues(typeValuesResponse.data);
      const data = (await axios.get('/api/attachments/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateAttachment = async (data: AttachmentDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/attachments/' + currentId, data);
      navigate('/attachments', {
            state: {
              msgSuccess: t('attachment.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('attachment.edit.headline')}</h1>
      <div>
        <Link to="/attachments" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('attachment.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateAttachment)} noValidate>
      <InputRow useFormResult={useFormResult} object="attachment" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="attachment" field="fileName" required={true} />
      <InputRow useFormResult={useFormResult} object="attachment" field="filePath" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="attachment" field="mimeType" />
      <InputRow useFormResult={useFormResult} object="attachment" field="client" required={true} type="select" options={clientValues} />
      <InputRow useFormResult={useFormResult} object="attachment" field="type" required={true} type="select" options={typeValues} />
      <input type="submit" value={t('attachment.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
