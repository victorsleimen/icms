import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { TicketDTO } from 'app/ticket/ticket-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    code: yup.string().emptyToNull().max(12).required(),
    name: yup.string().emptyToNull().max(255).required(),
    description: yup.string().emptyToNull().max(255).required(),
    attachment: yup.string().emptyToNull(),
    openDate: yup.string().emptyToNull().max(255),
    ticketType: yup.string().emptyToNull().max(80).required(),
    owner: yup.string().emptyToNull().max(75).required(),
    status: yup.string().emptyToNull(),
    urgency: yup.number().integer().emptyToNull().required(),
    impact: yup.number().integer().emptyToNull().required(),
    priority: yup.string().emptyToNull(),
    client: yup.number().integer().emptyToNull().required(),
    sla: yup.number().integer().emptyToNull().required(),
    assignee: yup.number().integer().emptyToNull().required()
  });
}

export default function TicketAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('ticket.add.headline'));

  const navigate = useNavigate();
  const [clientValues, setClientValues] = useState<Map<number,string>>(new Map());
  const [slaValues, setSlaValues] = useState<Map<number,string>>(new Map());
  const [assigneeValues, setAssigneeValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const clientValuesResponse = await axios.get('/api/tickets/clientValues');
      setClientValues(clientValuesResponse.data);
      const slaValuesResponse = await axios.get('/api/tickets/slaValues');
      setSlaValues(slaValuesResponse.data);
      const assigneeValuesResponse = await axios.get('/api/tickets/assigneeValues');
      setAssigneeValues(assigneeValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createTicket = async (data: TicketDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/tickets', data);
      navigate('/tickets', {
            state: {
              msgSuccess: t('ticket.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('ticket.add.headline')}</h1>
      <div>
        <Link to="/tickets" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('ticket.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createTicket)} noValidate>
      <InputRow useFormResult={useFormResult} object="ticket" field="code" required={true} />
      <InputRow useFormResult={useFormResult} object="ticket" field="name" required={true} />
      <InputRow useFormResult={useFormResult} object="ticket" field="description" required={true} />
      <InputRow useFormResult={useFormResult} object="ticket" field="attachment" type="textarea" />
      <InputRow useFormResult={useFormResult} object="ticket" field="openDate" />
      <InputRow useFormResult={useFormResult} object="ticket" field="ticketType" required={true} />
      <InputRow useFormResult={useFormResult} object="ticket" field="owner" required={true} />
      <InputRow useFormResult={useFormResult} object="ticket" field="status" required={true} type="select" options={{'PENDING': 'PENDING', 'ORDERED': 'ORDERED', 'RECEIVED': 'RECEIVED', 'CANCELED': 'CANCELED'}} />
      <InputRow useFormResult={useFormResult} object="ticket" field="urgency" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="ticket" field="impact" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="ticket" field="priority" required={true} type="select" options={{'LOW': 'LOW', 'MEDIUM': 'MEDIUM', 'HIGH': 'HIGH', 'CRITICAL': 'CRITICAL'}} />
      <InputRow useFormResult={useFormResult} object="ticket" field="client" required={true} type="select" options={clientValues} />
      <InputRow useFormResult={useFormResult} object="ticket" field="sla" required={true} type="select" options={slaValues} />
      <InputRow useFormResult={useFormResult} object="ticket" field="assignee" required={true} type="select" options={assigneeValues} />
      <input type="submit" value={t('ticket.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
