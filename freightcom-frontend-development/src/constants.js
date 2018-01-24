
export const SHIPMENT_TYPES = {
  ENVELOPE: 'env',
  PAK: 'pak',
  PACKAGE: 'package',
  LTL: 'pallet',
};

export const SHIPMENT_STATUS = {
  NEW: 'ORDER_NEW',
  DRAFT: 'ORDER_DRAFT',
  CONFIRM: 'ORDER_CONFIRM',
  VIEW: 'ORDER_VIEW',
};

export const USER_ROLES = {
  FREIGHTCOM_ADMIN: 'Freightcom Admin',
  FREIGHTCOM_STAFF: 'Freightcom Staff',
  CUSTOMER_ADMIN: 'Admin',
  CUSTOMER_STAFF: 'Staff',
  AGENT: 'Agent',
}

export const orderStatuses = [
  { id: 16, label: "DRAFT", actions: [ "delete" ] },
  { id: 10, label: "QUOTED DRAFT", actions: [ "delete" ] },
  { id: 15, label: "SUBMITTED", actions: [ "schedule-pickup" ] },
  { id: 1, label: "READY FOR SHIPPING", actions: [ "cancel", "missed" ] },
  { id: 2, label: "IN TRANSIT", actions: [] },
  { id: 3, label: "DELIVERED", actions: [ "claim" ] },
  { id: 4, label: "CANCELLED", actions: [] },
  { id: 6, label: "PREDISPATCHED", actions: [ "cancel" ] },
  { id: 7, label: "READY TO INVOICE", actions: [] },
  { id: 15, label: "INVOICED", actions: [ "dispute" ] },
  { id: 8, label: "READY FOR DISPATCH", actions: [] },
  { id: 9, label: "MISSED PICKUP", actions: [ "reschedule" ] },
  { id: 999, label: "DELETED", actions: [ "reopen" ] }
];

export const ORDER_STATUS = {
  DRAFT: 16,
  QUOTED_DRAFT: 10,
}
