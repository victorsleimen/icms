import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { KnowledgeArticlesDTO } from 'app/knowledge-articles/knowledge-articles-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    title: yup.string().emptyToNull().max(255).required(),
    content: yup.string().emptyToNull(),
    status: yup.string().emptyToNull(),
    client: yup.number().integer().emptyToNull().required()
  });
}

export default function KnowledgeArticlesAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('knowledgeArticles.add.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/knowledgeArticless/clientValues');
      setClientValues(clientValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createKnowledgeArticles = async (data: KnowledgeArticlesDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/knowledgeArticless', data);
      navigate('/knowledgeArticless', {
            state: {
              msgSuccess: t('knowledgeArticles.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('knowledgeArticles.add.headline')}</h1>
      <div>
        <Link to="/knowledgeArticless" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('knowledgeArticles.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createKnowledgeArticles)} noValidate>
      <InputRow useFormResult={useFormResult} object="knowledgeArticles" field="title" required={true} />
      <InputRow useFormResult={useFormResult} object="knowledgeArticles" field="content" type="textarea" />
      <InputRow useFormResult={useFormResult} object="knowledgeArticles" field="status" required={true} type="radio" options={{'DRAFT': 'DRAFT', 'PUBLISHED': 'PUBLISHED'}} />
      <InputRow useFormResult={useFormResult} object="knowledgeArticles" field="client" required={true} type="select" options={clientValues} />
      <input type="submit" value={t('knowledgeArticles.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
