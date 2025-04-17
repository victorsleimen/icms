import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { NotificationDTO } from 'app/notification/notification-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    notifType: yup.string().emptyToNull(),
    message: yup.string().emptyToNull().required(),
    isRead: yup.bool(),
    client: yup.number().integer().emptyToNull().required(),
    user: yup.number().integer().emptyToNull().required(),
    ticket: yup.number().integer().emptyToNull().required()
  });
}

export default function NotificationEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('notification.edit.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());
  const [userValues, setUserValues] = useState<Map<number,string>>(new Map());
  const [ticketValues, setTicketValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/notifications/clientValues');
      setClientValues(clientValuesResponse.data);
      const userValuesResponse = await axios.get('/api/notifications/userValues');
      setUserValues(userValuesResponse.data);
      const ticketValuesResponse = await axios.get('/api/notifications/ticketValues');
      setTicketValues(ticketValuesResponse.data);
      const data = (await axios.get('/api/notifications/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateNotification = async (data: NotificationDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/notifications/' + currentId, data);
      navigate('/notifications', {
            state: {
              msgSuccess: t('notification.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('notification.edit.headline')}</h1>
      <div>
        <Link to="/notifications" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('notification.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateNotification)} noValidate>
      <InputRow useFormResult={useFormResult} object="notification" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="notification" field="notifType" required={true} type="select" options={{'SLA_ALERT': 'SLA_ALERT', 'STATUS_CHANGE': 'STATUS_CHANGE', 'COMMENT': 'COMMENT', 'ASSIGNMENT': 'ASSIGNMENT'}} />
      <InputRow useFormResult={useFormResult} object="notification" field="message" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="notification" field="isRead" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="notification" field="client" required={true} type="select" options={clientValues} />
      <InputRow useFormResult={useFormResult} object="notification" field="user" required={true} type="select" options={userValues} />
      <InputRow useFormResult={useFormResult} object="notification" field="ticket" required={true} type="select" options={ticketValues} />
      <input type="submit" value={t('notification.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
