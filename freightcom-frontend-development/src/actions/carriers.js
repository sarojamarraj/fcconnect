import { getAll, getOne, update } from './api';
import { CarrierSchema, CarrierInvoiceSchema } from '../schemas';

export function loadCarriers(
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) {
  const endpoint = 'carrier';
  const entityMap = 'carrier';

  return getAll(
    'carrier',
    CarrierSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    endpoint,
    entityMap,
  );
}

export function loadCarrierById(carrierId) {
  return getOne('carrier', carrierId);
}

export function updateCarrier(carrierData) {
  return update('carrier', carrierData.id, carrierData);
}

export function loadCarrierInvoices(
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) {
  const endpoint = 'carrier/invoice';
  const entityMap = 'carrierInvoices';

  return getAll(
    'carrierInvoice',
    CarrierInvoiceSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    endpoint,
    entityMap,
  );
}

export function uploadEDIFile({ carrierId, fileInput, callback = () => {} }) {
  return dispatch => {
    const formData = new window.FormData();
    formData.append('file', fileInput.files[0]);

    dispatch({ type: 'EDI_UPLOAD_SUBMIT' });

    fetch(`/api/carrier/${carrierId}/upload-invoice`, {
      method: 'POST',
      credentials: 'include',
      body: formData,
    })
      .then(response => response.json())
      .then(json => {
        dispatch({ type: 'EDI_UPLOAD_SUCCESS', payload: { ...json } });
        callback(json);
      })
      .catch(error => {
        dispatch({ type: 'EDI_UPLOAD_ERROR', error: 'Something wrong' });
        console.error(error);
      });
  };
}
