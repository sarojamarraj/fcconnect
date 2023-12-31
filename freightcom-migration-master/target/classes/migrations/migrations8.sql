alter table customer_order drop foreign key customer_order_ibfk_9;
alter table customer_order drop workorder_status_id;
alter table markup drop foreign key markup_ibfk_4;
alter table pickup drop foreign key pickup_ibfk_5;


drop table if exists `A`;
drop table if exists `acc_charge`;
drop table if exists `accounts_receivable`;
drop table if exists `activity`;
drop table if exists `carrier_account`;
drop table if exists `carrier_cutofftime`;
drop table if exists `cc_transaction`;
drop table if exists `claim_docs`;
drop table if exists `cod_account`;
drop table if exists `cod_transaction`;
drop table if exists `codaccount`;
drop table if exists `commission_payable`;
drop table if exists `commission_rate`;
drop table if exists `configuration`;
drop table if exists `creator_temp`;
drop table if exists `crm`;
drop table if exists `custom_options`;
drop table if exists `customer_comment`;
drop table if exists `customer_comments_log`;
drop table if exists `customer_option`;
drop table if exists `customer_pickup`;
drop table if exists `customeradmin`;
drop table if exists `customs_invoice`;
drop table if exists `customs_invoice_product`;
drop table if exists `distribution_carrier`;
drop table if exists `employee`;
drop table if exists `gojit_interlines_quebec_ontario_10lb_rates`;
drop table if exists `gojit_zone_transitday_mapping`;
drop table if exists `instant_quote`;
drop table if exists `instant_quote_package`;
drop table if exists `invoice_comment`;
drop table if exists `invoice_status_change`;
drop table if exists `lead`;
drop table if exists `merchant_account`;
drop table if exists `msgbox`;
drop table if exists `news`;
drop table if exists `nmfc_code`;
drop table if exists `opp_stage`;
drop table if exists `opp_stage_opportunity`;
drop table if exists `opportunity`;
drop table if exists `order_accounts_payable_category`;
drop table if exists `order_accounts_payable_cheque`;
drop table if exists `order_accounts_payable_payables`;
drop table if exists `order_accounts_payable_payables_category`;
drop table if exists `order_accounts_payable_transactions`;
drop table if exists `order_accounts_receivable`;
drop table if exists `order_accounts_receivable_details`;
drop table if exists `order_agent`;
drop table if exists `order_ap_cheque_trans`;
drop table if exists `order_batch`;
drop table if exists `order_business`;
drop table if exists `order_charges`;
drop table if exists `order_comm_report`;
drop table if exists `order_comm_report_customer`;
drop table if exists `order_comm_report_workorder`;
drop table if exists `order_credit_note`;
drop table if exists `order_delivery_pre_alert`;
drop table if exists `order_docs`;
drop table if exists `order_document`;
drop table if exists `order_edi_charge`;
drop table if exists `order_edi_charge_group`;
drop table if exists `order_edi_detail_canadapost`;
drop table if exists `order_edi_detail_cts`;
drop table if exists `order_edi_detail_cts2`;
drop table if exists `order_edi_detail_dhl`;
drop table if exists `order_edi_detail_fedex`;
drop table if exists `order_edi_detail_mfw`;
drop table if exists `order_edi_detail_puro`;
drop table if exists `order_edi_detail_puro_c`;
drop table if exists `order_edi_detail_puro_d`;
drop table if exists `order_edi_detail_puro_e`;
drop table if exists `order_edi_detail_puro_f`;
drop table if exists `order_edi_detail_sfw`;
drop table if exists `order_edi_detail_tfc`;
drop table if exists `order_edi_detail_ups`;
drop table if exists `order_edi_header`;
drop table if exists `order_edi_service`;
drop table if exists `order_flight_segment`;
drop table if exists `order_invoice_comments`;
drop table if exists `order_invoice_creditnotes`;
drop table if exists `order_job_type`;
drop table if exists `order_rate`;
drop table if exists `order_status_change`;
drop table if exists `order_status_message`;
drop table if exists `order_subservice`;
drop table if exists `order_subservice_eshipper_charge`;
drop table if exists `order_tax`;
drop table if exists `order_taxes`;
drop table if exists `order_transport_segment`;
drop table if exists `order_trucking_pre_alert`;
drop table if exists `order_trucking_segment`;
drop table if exists `order_workorder_service`;
drop table if exists `order_workorder_subservice`;
drop table if exists `package_service`;
drop table if exists `package_type_services`;
drop table if exists `package_type_vehicle_types`;
drop table if exists `pins`;
drop table if exists `polaris_ca_rating_zone`;
drop table if exists `privileged_rate`;
drop table if exists `privilieged_rate`;
drop table if exists `product_list`;
drop table if exists `puro_invoicing_temp_final`;
drop table if exists `puro_invoicing_temp_final_archive`;
drop table if exists `puro_orderid`;
drop table if exists `puro_temp`;
drop table if exists `purocourier_manifest_details`;
drop table if exists `purolator_first_tariff_negotiated_beyond_and_rural`;
drop table if exists `purolator_first_tariff_surcharge_data`;
drop table if exists `purolator_negotiated_beyond_and_rural`;
drop table if exists `rate_adjustment`;
drop table if exists `rt_charge_type`;
drop table if exists `rt_city`;
drop table if exists `rt_rate`;
drop table if exists `rt_service_transit_code_rate`;
drop table if exists `rt_transit_code_vehicle_rate`;
drop table if exists `rt_vehicle_type`;
drop table if exists `rt_zone`;
drop table if exists `rt_zone_city_map`;
drop table if exists `rt_zone_postal_code`;
drop table if exists `rt_zone_transit_code`;
drop table if exists `ship_item`;
drop table if exists `shipments_by_cust_view`;
drop table if exists `shipping_labels`;
drop table if exists `shipping_labels_archive`;
drop table if exists `shipping_order_archive`;
drop table if exists `subservice`;
drop table if exists `surcharge`;
drop table if exists `surcharge_default_cost`;
drop table if exists `tab`;
drop table if exists `tax_rate`;
drop table if exists `temp_generic`;
drop table if exists `transport_segment`;
drop table if exists `weight_break`;
drop table if exists `workorder_status`;
drop table if exists `xpress_ship_work_order_shipping_order`;
