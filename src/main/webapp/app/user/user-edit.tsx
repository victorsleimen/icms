import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { UserDTO } from 'app/user/user-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    username: yup.string().emptyToNull().max(100).required(),
    password: yup.string().emptyToNull().max(100),
    firstName: yup.string().emptyToNull().max(100),
    lastName: yup.string().emptyToNull().max(100),
    email: yup.string().emptyToNull().max(125),
    doe: yup.string().emptyToNull(),
    timeZoneId: yup.string().emptyToNull().max(125),
    firstLogin: yup.bool(),
    isUtc: yup.bool(),
    isActive: yup.bool(),
    loggedUser: yup.string().emptyToNull().max(100),
    roles: yup.array(yup.number().required()).emptyToNull().json(),
    client: yup.number().integer().emptyToNull().required()
  });
}

export default function UserEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('user.edit.headline'));

  const navigate = useNavigate();
  const [rolesValues, setRolesValues] = useState<Map<number,string>>(new Map());
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const getMessage = (key: string) => {
    const messages: Record<string, string> = {
      USER_USERNAME_UNIQUE: t('exists.user.username')
    };
    return messages[key];
  };

  const prepareForm = async () => {
    try {
      const rolesValuesResponse = await axios.get('/api/users/rolesValues');
      setRolesValues(rolesValuesResponse.data);
      const clientValuesResponse = await axios.get('/api/users/clientValues');
      setClientValues(clientValuesResponse.data);
      const data = (await axios.get('/api/users/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateUser = async (data: UserDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/users/' + currentId, data);
      navigate('/users', {
            state: {
              msgSuccess: t('user.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t, getMessage);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('user.edit.headline')}</h1>
      <div>
        <Link to="/users" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('user.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateUser)} noValidate>
      <input type="submit" value={t('user.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6 mb-5" />
      <InputRow useFormResult={useFormResult} object="user" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="user" field="username" required={true} />
      <InputRow useFormResult={useFormResult} object="user" field="password" />
      <InputRow useFormResult={useFormResult} object="user" field="firstName" />
      <InputRow useFormResult={useFormResult} object="user" field="lastName" />
      <InputRow useFormResult={useFormResult} object="user" field="email" />
      <InputRow useFormResult={useFormResult} object="user" field="doe" type="datepicker" />
      <InputRow useFormResult={useFormResult} object="user" field="timeZoneId" />
      <InputRow useFormResult={useFormResult} object="user" field="firstLogin" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="user" field="isUtc" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="user" field="isActive" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="user" field="loggedUser" />
      <InputRow useFormResult={useFormResult} object="user" field="roles" type="multiselect" options={rolesValues} />
      <InputRow useFormResult={useFormResult} object="user" field="client" required={true} type="select" options={clientValues} />
      <input type="submit" value={t('user.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
