import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { RoleDTO } from 'app/role/role-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(255).required()
  });
}

export default function RoleEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('role.edit.headline'));

  const navigate = useNavigate();
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const getMessage = (key: string) => {
    const messages: Record<string, string> = {
      ROLE_NAME_UNIQUE: t('exists.role.name')
    };
    return messages[key];
  };

  const prepareForm = async () => {
    try {
      const data = (await axios.get('/api/roles/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateRole = async (data: RoleDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/roles/' + currentId, data);
      navigate('/roles', {
            state: {
              msgSuccess: t('role.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t, getMessage);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('role.edit.headline')}</h1>
      <div>
        <Link to="/roles" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('role.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateRole)} noValidate>
      <InputRow useFormResult={useFormResult} object="role" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="role" field="name" required={true} />
      <input type="submit" value={t('role.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
