import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { HistoryLogsDTO } from 'app/history-logs/history-logs-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    entityType: yup.string().emptyToNull(),
    changedDate: yup.string().emptyToNull().required(),
    action: yup.string().emptyToNull(),
    client: yup.number().integer().emptyToNull().required()
  });
}

export default function HistoryLogsAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('historyLogs.add.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/historyLogss/clientValues');
      setClientValues(clientValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createHistoryLogs = async (data: HistoryLogsDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/historyLogss', data);
      navigate('/historyLogss', {
            state: {
              msgSuccess: t('historyLogs.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('historyLogs.add.headline')}</h1>
      <div>
        <Link to="/historyLogss" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('historyLogs.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createHistoryLogs)} noValidate>
      <InputRow useFormResult={useFormResult} object="historyLogs" field="entityType" required={true} type="radio" options={{'TICKET': 'TICKET', 'KNOWLEDGE_ARTICLES': 'KNOWLEDGE_ARTICLES'}} />
      <InputRow useFormResult={useFormResult} object="historyLogs" field="changedDate" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="historyLogs" field="action" required={true} type="radio" options={{'INSERT': 'INSERT', 'UPDATE': 'UPDATE', 'DELETE': 'DELETE'}} />
      <InputRow useFormResult={useFormResult} object="historyLogs" field="client" required={true} type="select" options={clientValues} />
      <input type="submit" value={t('historyLogs.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
