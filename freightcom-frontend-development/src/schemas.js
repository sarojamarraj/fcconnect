import { Schema } from 'normalizr';

export const DraftOrderSchema = new Schema('draftOrder', {
  idAttribute: order => order.id,
});
export const OrderSchema = new Schema('order', {
  idAttribute: order => order.id,
});
export const JobBoardSchema = new Schema('jobBoard', {
  idAttribute: order => order.id,
});
export const DisputeListSchema = new Schema('disputeList', {
  idAttribute: order => order.id,
});
export const ClaimListSchema = new Schema('claimList', {
  idAttribute: order => order.id,
});
export const OrderCustomerSchema = new Schema('customer', {
  idAttribute: customer => customer.id,
});
export const SalesAgentSchema = new Schema('salesAgents');
export const ClaimSchema = new Schema('claim');
export const ShippingAddressSchema = new Schema('shippingAddress');
export const OrderStatusSchema = new Schema('orderStatus', {
  idAttribute: item => item.id,
});
export const CarrierSchema = new Schema('carrier');
export const CarrierInvoiceSchema = new Schema('carrierInvoice');
export const PackageTypeSchema = new Schema('packageType', {
  idAttribute: item => item.name,
});
export const OrderLogSchema = new Schema('orderLog', {
  idAttribute: item => item.id,
});

export const PalletTemplateSchema = new Schema('palletTemplate');
export const PackageTemplateSchema = new Schema('packageTemplate');

export const RatesSchema = new Schema('rates');
export const OrderRateSchema = new Schema('orderRate');
export const AccesorialServicesSchema = new Schema('accessorialServices');

export const AddressBookSchema = new Schema('addressBook', {
  idAttribute: addressBook => addressBook.id,
});

export const InvoiceSchema = new Schema('invoice', {
  idAttribute: item => item.id,
});

export const CreditSchema = new Schema('credit', {
  idAttribute: credit => credit.id,
});

export const CustomerSchema = new Schema('customer');
export const UserSchema = new Schema('user');
export const AgentSchema = new Schema('agent');
export const SubAgentSchema = new Schema('subAgent');
export const SystemPropertySchema = new Schema(
  'systemProperties',
  {
    //idAttribute: item => item.name,
  },
);
export const CurrencySchema = new Schema('currency');
export const ServiceSchema = new Schema('service');

export const PayableStatementSchema = new Schema('payableStatement');
export const CommissionStatementSchema = new Schema('commissionStatement');

ServiceSchema.define({ carrier: CarrierSchema });

OrderCustomerSchema.define({ salesAgent: SalesAgentSchema });

OrderSchema.define({
  customer: OrderCustomerSchema,
  shipTo: ShippingAddressSchema,
  shipFrom: ShippingAddressSchema,
  //orderStatus: OrderStatusSchema,
  carrier: CarrierSchema,
});

DraftOrderSchema.define({
  customer: OrderCustomerSchema,
  shipTo: ShippingAddressSchema,
  shipFrom: ShippingAddressSchema,
});

export default {
  DraftOrderSchema,
  OrderSchema,
  OrderCustomerSchema,
  SalesAgentSchema,
  ShippingAddressSchema,
  CustomerSchema,
  OrderLogSchema,
  InvoiceSchema,
  PayableStatementSchema,
  CommissionStatementSchema,
  SubAgentSchema,
  ClaimSchema,
};
