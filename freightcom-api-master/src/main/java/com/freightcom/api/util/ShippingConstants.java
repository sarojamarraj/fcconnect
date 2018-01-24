package com.freightcom.api.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freightcom.api.model.Service;
import com.freightcom.api.model.ShippingAddress;

public class ShippingConstants
{
    private static List<KeyValue> countries;
    private static Map<String, String> country_code_map = new HashMap<String, String>();
    private static Map<String, String> country_name_map = new HashMap<String, String>();
    private static List<KeyValue> canadianProvinces;
    private static List<KeyValue> USStates;
    public static Map<Long, String> service_code = new HashMap<Long, String>();
    public static Map<String,Long> service_code_reverse = new HashMap<String,Long>();

    public static Map<Long, String> service_US_code = new HashMap<Long, String>();
    public static Map<String,Long> service_US_code_reverse = new HashMap<String,Long>();
    public static Map<String, String> service_code_US_code = new HashMap<String, String>();
    public static Map<String,String> service_code_US_code_reverse = new HashMap<String,String>();
    public static Map<String, String> service_code_intl_code = new HashMap<String, String>();
    public static Map<String, String> service_code_intl_code_reverse = new HashMap<String, String>();

    public static final long CWW_MASTER_FRANCHISE_ID = 1;
    public static final long ESHIPPERUSA_FRANCHISE_ID = 2;
    public static ShippingAddress MASTER_FRANCHISE_CA_ADDRESS = null;
    public static ShippingAddress MASTER_FRANCHISE_US_ADDRESS = null;
    public static ShippingAddress FREIGHTCOM_FRANCHISE_CA_ADDRESS = new ShippingAddress();
    
    static {
        FREIGHTCOM_FRANCHISE_CA_ADDRESS.setProvince("ON"); 
        FREIGHTCOM_FRANCHISE_CA_ADDRESS.setProvinceState("ON"); 
    }

    public static ShippingAddress getFREIGHTCOM_FRANCHISE_CA_ADDRESS()
    {
        return FREIGHTCOM_FRANCHISE_CA_ADDRESS;
    }

    public static void setFREIGHTCOM_FRANCHISE_CA_ADDRESS(ShippingAddress address)
    {
        FREIGHTCOM_FRANCHISE_CA_ADDRESS = address;
    }

    public static final long REALLY_LONG_LONG = 9999999;

    public static final String US = "US";
    public static final String CANADA = "CA";

    public static final String NOT_APPLICABLE = "N/A";

    // Canadian provinces and taxes
    public static final String PROVINCE_AB = "AB";
    public static final String PROVINCE_BC = "BC";
    public static final String PROVINCE_NB = "NB";
    public static final String PROVINCE_PQ = "PQ";
    public static final String PROVINCE_NF = "NF";
    public static final String PROVINCE_ON = "ON";
    public static final String PROVINCE_PE = "PE";
    public static final String PROVINCE_NS = "NS";
    public static final float AB_TAX = 7f;
    public static final float BC_TAX = 7f;
    public static final float PE_TAX = 7f;
    public static final float ON_TAX = 7f;
    public static final float NS_TAX = 15f;
    public static final float NB_TAX = 15f;
    public static final float NF_TAX = 15f;
    public static final float PQ_TAX = 7.5f;
    public static final String TAX_HST = "HST";
    public static final String TAX_GST = "GST";
    public static final String TAX_QST = "QST";
    public static final float GST_TAX_PERC = 0.05f;
    public static final float HST_TAX_PERC = 0.13f;
    public static final float QST_TAX_PERC = 0.075f;

    public static final String FUEL_SURCHARGE = "Fuel";
    public static final float FUEL_SURCHARGE_PERC = 0.10f;

    // package types
    public static final int PACKAGE_TYPE_ENVELOPE = 1;
    public static final int PACKAGE_TYPE_PAK = 2;
    public static final int PACKAGE_TYPE_PACKAGE = 3;
    public static final int PACKAGE_TYPE_PALLET = 4;
    public static final String PACKAGE_TYPE_ENVELOPE_STRING = "Envelope";
    public static final String PACKAGE_TYPE_PAK_STRING = "Courier Pak";
    public static final String PACKAGE_TYPE_PACKAGE_STRING = "Package";
    public static final String PACKAGE_TYPE_PALLET_STRING = "Pallet";
    public static final String PACKAGE_TYPE_PALLET_LTL_STRING = "LTL";

    // cariers
    public static final int CARRIER_FEDEX = 27;
    public static final int CARRIER_PUROLATOR = 2;
    public static final int CARRIER_CWW = 3;
    public static final int CARRIER_DHL = 4;
    public static final int CARRIER_CANADAPOST = 5;
    public static final int CARRIER_UPS = 46;
    public static final int CARRIER_TST = 11;
    public static final int CARRIER_MG = 15;
    public static final int CARRIER_ESHIPPER_LTL = 19;
    public static final int INT_CARRIER_CTS = 21;
    public static final int INT_CARRIER_CTS2 = 21;
    public static final int INT_CARRIER_MFW = 23;
    public static final int INT_CARRIER_SFW = 24;
    public static final int INT_CARRIER_SPEEDY = 26;
    public static final int INT_CARRIER_TST = 28;
    public static final int INT_CARRIER_CCT = 29;
    public static final int INT_CARRIER_FRONTLINE = 32;
    public static final long TST_ROAD_SERVICE_ID = 2800;
    public static final long TST_RAIL_SERVICE_ID = 2801;
    public static final long GOJIT_SERVICE_ID = 3000;
    public static final long GOJIT_INTERMODAL_SERVICE_ID = 3001;
    public static final long FRONTLINE_SERVICE_ID = 3200;

    public static final String CARRIER_FEDERALEXPRESS_STRING = "Federal Express";
    public static final String CARRIER_FEDEX_STRING = "Fedex Freight";
    public static final String CARRIER_PUROLATOR_STRING = "Purolator";
    public static final String CARRIER_PUROLATOR_FREIGHT_STRING = "Purolator Freight";
    public static final String PUROLATOR_FREIGHT_EMAIL = "PUROLATOR_FREIGHT_EMAIL";
    public static final String CARRIER_CWW_STRING = "EShipper";
    public static final String CARRIER_DHL_STRING = "DHL";
    public static final String CARRIER_UPS_STRING = "UPS";
    public static final String GENERIC_CARRIER_STRING = "Fedex Freight";
    public static final String CARRIER_CANADAPOST_STRING = "Canada Post";
    public static final String CARRIER_TST_STRING = "TST";
    public static final String CARRIER_MG_STRING = "eShipper Trucking";
    public static final String CARRIER_ESHIPPER_LTL_STRING = "TFC";
    public static final String CARRIER_PUROLATOR_PROVIDER_NAME_STRING = "purolator";

    // user types
    public static final String USER_TYPE_ADMIN = "admin";
    public static final String USER_TYPE_CUSTOMER = "customer";
    public static final long CUSTOMER_EMERGEIT = 6787;
    // statuses
    public static final int STATUS_DISPATCHED = 1;
    public static final int STATUS_INTRANSIT = 2;
    public static final int STATUS_DELIVERED = 3;
    public static final int STATUS_CANCELLED = 4;
    public static final int STATUS_EXCEPTION = 5;
    public static final int STATUS_PRE_DISPATCHED = 6;
    public static final int STATUS_CLOSED = 7;
    public static final int STATUS_READY_TO_DISPATCHED = 8;
    public static final int STATUS_QUOTED = 10;

    // Manual Invoicing enhancements.
    public static final int BILLING_STATUS_NOT_READY_TO_INVOICE = 0;
    public static final int BILLING_STATUS_READY_TO_INVOICE = 1;
    public static final int BILLING_STATUS_INVOICED = 2;

    public static final int COLLECT_COD_TRANS = 1;
    public static final int REMIT_COD_TRANS = 2;

    public static final int CUSTOM_VIEW_SORT_BY_PRICE = 1;
    public static final int CUSTOM_VIEW_SORT_BY_CARRIER = 2;

    public static final int MAX_ADDRESS_CHARACTOR = 30;
    public static final int MAX_INSURANCE_ENV_PAK = 100;
    public static final int MAX_INSURANCE_PACKAGE_PALLET = 50000;
    public static final float CUSTOMS_SERVICE_CHARGE = 15.0f;

    public static final double CUST_CREDIT_LIMIT = 1000;

    public static String getStatusName(int statusId)
    {
        switch (statusId) {
        case STATUS_DISPATCHED:
            return "Ready For Shipping"; // name changed from "dispatched" to
                                         // "rfs"
        case STATUS_INTRANSIT:
            return "In Transit";
        case STATUS_DELIVERED:
            return "Delivered";
        case STATUS_CANCELLED:
            return "Cancelled";
        case STATUS_EXCEPTION:
            return "Exception";
        }
        return "";
    }

    public static String getBillingStatusName(int statusId)
    {
        switch (statusId) {
        case BILLING_STATUS_NOT_READY_TO_INVOICE:
            return "NOT READY TO INVOICE";
        case BILLING_STATUS_READY_TO_INVOICE:
            return "READY TO INVOICE";
        case BILLING_STATUS_INVOICED:
            return "INVOICED";
        }
        return "";
    }

    public static final String BEAN_TIME_ZONES = "timeZones";

    // dangerous goods
    public static final String DANGEROUS_GOODS_LIMITED = "Limited Quantity";
    public static final String DANGEROUS_GOODS_500KG_EXEMPTION = "500 Kg Exemption";
    public static final String DANGEROUS_GOODS_FULLY_REGULATED = "Fully Regulated";

    // currency types
    public static final int CURRENCY_CA = 1;
    public static final int CURRENCY_US = 2;
    public static final String CURRENCY_CA_STRING = "CAD";
    public static final String CURRENCY_US_STRING = "USD";

    // mode of transport
    public static final String MODE_TRANSPORT_AIR = "A";
    public static final String MODE_TRANSPORT_GROUND = "G";
    public static final int MODE_TRANSPORT_AIR_VALUE = 1;
    public static final int MODE_TRANSPORT_GROUND_VALUE = 2;

    public static final String CWW_ADDRESS = "Canada WorldWide Services Inc.\n2896 Slough Street, Units 1 & 2\nMississauga, Ontario\nL4T 1G3";

    public static final String TST_RAIL_ADDRESS_OLD = "FCI ( Intermodal ) \n7699 Bath Road, \nMississauga, ON L4T 3T1 \nFreight Charge Terms: 3rd Party Prepaid";
    public static final String TST_ROAD_ADDRESS_OLD = "FCI ( Standard ) \n7699 Bath Road, \nMississauga, ON L4T 3T1 \nFreight Charge Terms: 3rd Party Prepaid";
    public static final String TST_ADDRESS_OLD = "FCI \n7699 Bath Road, \nMississauga, ON L4T 3T1 \nFreight Charge Terms: 3rd Party Prepaid";
    public static final String TST_RAIL_ADDRESS = "FCI ( Intermodal ) \n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8\n Freight Charge Terms: 3rd Party Prepaid";
    public static final String TST_ROAD_ADDRESS = "FCI ( Standard ) \n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8\n Freight Charge Terms: 3rd Party Prepaid";
    public static final String TST_ADDRESS = "FCI \n \n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8\n Freight Charge Terms: 3rd Party Prepaid";

    public final static String[] pickUpLocations = { "BackDoor", "MailRoom", "Basement", "Office", "BetweenDoors",
            "OutsideDoor", "Counter", "PartsDepartment", "Desk", "Pharmacy", "FrontDesk", "ProShop", "FrontDoor",
            "Receiving", "FrontPorch", "Reception", "Garage", "Security", "GateHouse", "ServiceCounter", "Kiosk", "Lab",
            "Shipping", "LoadingDock", "SideDoor", "Lobby", "Mailbox" };
    // pickups
    public static final int PICKUP_TYPE_DAILY = 1;
    public static final int PICKUP_TYPE_ADHOC = 2;

    private static String basePath = null;

    public static final int LABEL_TYPE_PNG = 1;
    public static final String LABEL_TYPE_PNG_STRING = "PNG";
    public static final int LABEL_TYPE_PDF = 2;
    public static final String LABEL_TYPE_PDF_STRING = "PDF";

    // EDI Flat Rate Charges
    public static final String EDI_FLAT_RATE_CHARGE = "FLAT_RATE_CH";
    public static final String EDI_FLAT_RATE_CHARGE_1 = "FLAT_RATE_CH_1";
    public static final String EDI_FLAT_RATE_CHARGE_2 = "FLAT_RATE_CH_2";
    public static final String EDI_FLAT_RATE_CHARGE_3 = "FLAT_RATE_CH_3";

    // Needed by Canada Post EDI file
    public static final String SCHEDULED_PICKUP_SERVICE = "Scheduled Pick Up";

    // PROPERTIES
    public static final String APPLY_DISCOUNTS_TO_TARIFF_RATES = "APPLY_DISCOUNTS_TO_TARIFF_RATES";
    public static final String WO_BILLING_MODULE_ENABLE = "WO_BILLING_MODULE_ENABLE";
    public static final String EXECUTE_QUARTZ_JOBS_TO_WEB_SERVER = "EXECUTE_QUARTZ_JOBS_TO_WEB_SERVER";
    public static final String CURRENCY_EXCHANGE_FACTOR = "CURRENCY_EXCHANGE_FACTOR";

    public static final String REGISTER_PAGE_GENERIC = "register_generic.jsp";

    public static String getBasePath()
    {
        return basePath;
    }

    public static void setBasePath(String value)
    {
        if (basePath == null) {
            basePath = value;
        }
    }

    private static String mode = null;
    private static String instanceName = null;

    public static String getMode()
    {
        return mode;
    }

    public static void setMode(String value)
    {
        if (mode == null) {
            mode = value;
        }
    }

    public static boolean isTestMode()
    {
        if (mode.equalsIgnoreCase("test"))
            return true;
        return false;
    }

    public static String getInstanceName()
    {
        return instanceName;
    }

    public static void setInstanceName(String value)
    {
        if (instanceName == null) {
            instanceName = value;
        }
    }

    public static List<KeyValue> getCanadianProvinces()
    {
        return canadianProvinces;
    }

    public static void setCanadianProvinces(List<KeyValue> canadianProvinces)
    {
        ShippingConstants.canadianProvinces = canadianProvinces;
    }

    public static List<KeyValue> getCountries()
    {
        return countries;
    }

    public static void setCountries(List<KeyValue> countries)
    {
        ShippingConstants.countries = countries;

        for (KeyValue country : countries) {
            country_code_map.put(country.getValue(), country.getKey());
            country_name_map.put(country.getKey(), country.getValue());
        }
        country_code_map.put(CANADA, "CANADA");
        country_code_map.put(US, "UNITED STATES");
        country_name_map.put("CANADA", CANADA);
        country_name_map.put("UNITED STATES", US);
    }

    public static List<KeyValue> getUSStates()
    {
        return USStates;
    }

    public static void setUSStates(List<KeyValue> states)
    {
        USStates = states;
    }

    public static void setServices(List<Service> services)
    {
        for (Service service : services) {
            service_code.put(service.getId(), service.getCode());
            service_code_reverse.put(service.getCode(), service.getId());
            service_US_code.put(service.getId(), service.getCodeUS());
            service_US_code_reverse.put(service.getCodeUS(), service.getId());
            service_code_US_code.put(service.getCode(), service.getCodeUS());
            service_code_US_code_reverse.put(service.getCodeUS(), service.getCode());

            service_code_intl_code.put(service.getCode(), service.getCodeIntl());
            service_code_intl_code_reverse.put(service.getCodeIntl(), service.getCode());
        }
    }

    // payment types
    public static final String PAYMENT_TYPE_THIRD_PARTY = "Third Party";
    public static final String PAYMENT_TYPE_CC = "Credit Card";
    public static final int PAYMENT_TYPE_THIRD_PARTY_VAL = 1;
    public static final int PAYMENT_TYPE_CC_VAL = 2;

    // for Primary key lookups
    public static final String RANDOM_KEY_MAPPING = "randomKeyMapping";
    public static final String BEAN_PROPERTY_PK = "primaryKey";

    public static String getCountryName(String countryCode)
    {
        String name = (String) country_code_map.get(countryCode);
        if (name == null) {
            // log.error("Could not find country for code " + countryCode);
            return "";
        }
        return name;
    }

    public static String getCountryCode(String countryName)
    {
        String code = (String) country_name_map.get(countryName);
        if (code == null) {
            // log.error("Could not find code for country " + countryName);
            return "";
        }
        return code;
    }

    // work order specific
    private static List<StatusMessage> pickUpMessages;
    private static List<StatusMessage> deliveryMessages;
    private static List<StatusMessage> flightStatusMessages;
    private static List<StatusMessage> customsStatusMessages;
    private static List<StatusMessage> miscelleneousMessages;


    public final static int PICKUP_STATUS_MESSAGE = 1;
    public final static int DELIVERY_STATUS_MESSAGE = 2;
    public final static int FLIGHT_STATUS_MESSAGE = 3;
    public final static int CUSTOMS_STATUS_MESSAGE = 4;
    public final static int MISCELLENEOUS_MESSAGE = 5;

    public static List<StatusMessage> getCustomsStatusMessages()
    {
        return customsStatusMessages;
    }

    public static void setCustomsStatusMessages(List<StatusMessage> customsStatusMessages)
    {
        ShippingConstants.customsStatusMessages = customsStatusMessages;
    }

    public static List<StatusMessage> getDeliveryMessages()
    {
        return deliveryMessages;
    }

    public static void setDeliveryMessages(List<StatusMessage> deliveryMessages)
    {
        ShippingConstants.deliveryMessages = deliveryMessages;
    }

    public static List<StatusMessage> getFlightStatusMessages()
    {
        return flightStatusMessages;
    }

    public static void setFlightStatusMessages(List<StatusMessage> flightStatusMessages)
    {
        ShippingConstants.flightStatusMessages = flightStatusMessages;
    }

    public static List<StatusMessage> getMiscelleneousMessages()
    {
        return miscelleneousMessages;
    }

    public static void setMiscelleneousMessages(List<StatusMessage> miscelleneousMessages)
    {
        ShippingConstants.miscelleneousMessages = miscelleneousMessages;
    }

    public static List<StatusMessage> getPickUpMessages()
    {
        return pickUpMessages;
    }

    public static void setPickUpMessages(List<StatusMessage> pickUpMessages)
    {
        ShippingConstants.pickUpMessages = pickUpMessages;
    }

    public static List<StatusMessage> getWOStatusMessages(int type)
    {
        switch (type) {
        case 1:
            return getPickUpMessages();
        case 2:
            return getDeliveryMessages();
        case 3:
            return getFlightStatusMessages();
        case 4:
            return getCustomsStatusMessages();
        case 5:
            return getMiscelleneousMessages();
        }

        return getPickUpMessages();
    }

    public static void setCanadaFranchiseShippingAddress(ShippingAddress a)
    {
        ShippingConstants.MASTER_FRANCHISE_CA_ADDRESS = a;
    }

    public static void setUSFranchise(ShippingAddress a)
    {
        ShippingConstants.MASTER_FRANCHISE_US_ADDRESS = a;
    }

    // ******************** EDI Charge Lookup Section *******************
    public static final int EDI_CHARGE_GROUP_OTHER = 6;
    public static final String FR_EDI_CHARGE_AFTER_HOURS = "After Hours Charge";
    public static final String FR_EDI_CHARGE_SERVICE = "Service Charge";
    public static final String FR_EDI_CHARGE_VEHICLE = "Vehicle Charge";
    public static final String FR_EDI_CHARGE_EXTRA_PEICES = "Extra Peices Charge";
    public static final String FR_EDI_CHARGE_EXTRA_WEIGHT = "Extra Weight Charge";
    public static final String FR_EDI_CHARGE_FUEL = "Fuel Charge";
    public static final String FR_EDI_CHARGE_INSURANCE = "Insurance Charge";
    public static final String FR_EDI_CHARGE_VEHICLE_TRANSIT = "Vehicle Transit Charge";
    public static final String FR_EDI_CHARGE_WAITING_TIME = "Waiting Time Charge";

    // ******************** Ends here *******************

    // ******************** MG Trucking *******************
    public static final String DIM_UNITS = "in";
    public static final String WEIGHT_UNITS = "lb";
    public static final String TYPE_PICKUP = "Pickup";
    public static final String TYPE_DROP = "Drop";
    public static final String CLASS_TYPE_PALLET = "Pallet";
    public static final String CLASS_TYPE_DRUM = "Drum";
    // public static final String MG_RATE_REQ_SOAP_URL =
    // "http://rrd.mercurygate.net/MercuryGate";
    // public static final String MG_RATE_REQ_SOAP_URL =
    // "http://rrd.mercurygate.net/MercuryGate/rateSOAP";
    public static final String TEST_MG_RATE_REQ_HTTPS_SOAP_URL = "https://rrd.mercurygate.net/MercuryGate/rateSOAP";
    public static final String PROD_MG_RATE_REQ_HTTPS_SOAP_URL = "https://rrd.mercurygate.net/MercuryGate/rateSOAP";
    // public static final String MG_RATE_REQ_SOAP_URL =
    // "http://server.example.com/MercuryGate/rateSOAP";
    // public static final String MG_RATE_REQ_HTTP_POST_URL =
    // "http://rrd.mercurygate.net/MercuryGate/inquiry/remoteXMLRating.jsp";
    public static final String MG_RATE_REQ_USER_ID_PARAM = "userid";
    public static final String MG_RATE_REQ_PASSWORD_PARAM = "password";
    public static final String MG_RATE_REQ_USER_ID = "yyzremote";
    public static final String MG_RATE_REQ_PASSWORD = "2zz4zp";
    public static final String OK_RESPONSE = "Response code=\"OK\"";
    public static final String START_XML = "&lt;?xml";
    public static final String ESC_SEQ_LESS_THAN = "&lt;";
    public static final String ESC_SEQ_MORE_THAN = "&gt;";
    public static final String RATE_RESULTS_END_STRING = "</Response>";
    public static final String COLOR_BROWN = "#A52A2A";
    public static final String COLOR_PURPLE = "#800080";

    // code for MG
    public final static String LIFTGATE_DELIVERY = "LG1";
    public final static String LIFTGATE_PICKUP = "LG2";
    public final static String RESIDENTIAL_DELIVERY = "RES1";
    public final static String RESIDENTIAL_PICKUP = "RES2";
    public final static String INSIDE_DELIVERY = "ID1";
    public final static String INSIDE_PICKUP = "ID2";
    public final static String HAZARDOUS_MATERIAL = "HAZM";
    public final static String COD = "COD";
    public final static String CONVENTION_EXHIBITION_SITE_PICKUP = "CONV2";

    public static final String CODTRANSACTIONCHECKNUM = "COD Transaction";

    public static String INSURANCE_LOCAL_RELATIVE_PATH = "Insurance";
    public static String INSURANCE_MANIFESTS_LOCAL_RELATIVE_PATH = "Manifests";
    public static final int INSURANCE_TYPE_IN_HOUSE = 1;
    public static final int INSURANCE_TYPE_CARRIER = 2;
    public static String IN_HOUSE_INSURANCE_EMAIL = "IN_HOUSE_INSURANCE_EMAIL";
    public static String IN_HOUSE_INSURANCE_PERCENT_US = "IN_HOUSE_INSURANCE_PERCENT_US";
    public static String IN_HOUSE_INSURANCE_PERCENT_CA = "IN_HOUSE_INSURANCE_PERCENT_CA";
    public static final double IN_HOUSE_INSURANCE_PERCENT_MF = 0.18;
    public static final double IN_HOUSE_INSURANCE_PERCENT_F = 0.75;
    public static final double IN_HOUSE_MIN_INSURANCE = 3.0;

    public static final String PUROLATOR_WEB_SERVICE = "PUROLATOR_WEB_SERVICE";

    public static final String ESHIPPERAPI_WEB_SERVICE = "ESHIPPERAPI_WEB_SERVICE";

    public static final String CERTIFIED_CHECK = "Certified Check";
    public static final String CHECK = "Check";

    public static final String PURO_WEB_EDI_HEADER_INIT = "A";
    public static final String PURO_WEB_TRANSPORT_MODE_GROUND = "Ground";
    public static final String PURO_WEB_TRANSPORT_MODE_AIR = "Air";
    public static final String PURO_WEB_EDI_RECORD_F = "F";
    public static final String PURO_WEB_EDI_RECORD_E = "E";
    public static final String PUROLATOR_WEB_EDI_SERVICE = "PUROLATOR_WEB_EDI_SERVICE";

    public static final String TFC_MOP_3P = "3P";
    public static final String CARRIER_TFC = "TFC";
    public static final String TFC_ACCESSORIALCODE_MNC = "MNC";
    public static final String TFC_ACCESSORIALCODE_APT = "APT";
    public static final String TFC_ACCESSORIALCODE_HAZ = "HAZ";
    public static final String TFC_ACCESSORIALCODE_LGP = "LGP";
    public static final String TFC_ACCESSORIALCODE_LFT = "LFT";
    public static final String TFC_ACCESSORIALCODE_IPU = "IPU";
    public static final String TFC_ACCESSORIALCODE_IDL = "IDL";
    public static final String TFC_ACCESSORIALCODE_REP = "REP";
    public static final String TFC_ACCESSORIALCODE_RES = "RES";

    public static final String TFC_USERNAME = "NVKS6";
    public static final String TFC_PASSWORD = "NVKS6";
    public static final String ESHIPPER_LTL_SERVICE = "ESHIPPER_LTL_SERVICE";
    public static final String ESHIPPER_LTL_STATUS_SERVICE = "ESHIPPER_LTL_STATUS_SERVICE";

    public static final String CARRIER_CTS = "CTS";
    public static final String CARRIER_CTS2 = "CTS";

    public static final long MASTER_FRAN_FRIETCOM = 11;

    public static String CTS_OPERATION_EMAIL = "SROD1@shipwithcts.com";

    public static String getCTS_OPERATION_EMAIL()
    {
        return CTS_OPERATION_EMAIL;
    }

    public static void setCTS_OPERATION_EMAIL(String cts_operation_email)
    {
        CTS_OPERATION_EMAIL = cts_operation_email;
    }

    public static final String URL_3CE = "3CE_URL";
    public static final String DAYR = "DAYR";
    public static final String CNWY = "CNWY";
    public static final String SAIA = "SAIA";
    public static final String EXLA = "EXLA";
    public static final String ODFL = "ODFL";
    public static final String UPGF = "UPGF";
    public static final String FXNL = "FXNL";
    public static final String FXFE = "FXFE";
    public static final int FLAT = 1;
    public static final int PER_CWT = 2;
    public static final String CTS_SURCHARGE_MESSAGE = "Rate configured is less than the Webservice rates";
    public static final String SCAC_RATES_FILTER = "SCAC_RATES_FILTER";
    // public static final String TFC_PAYMENT_CENTER_ADDRESS = "Freightcom
    // Payment Center, C/O DTA Services Limited 505 Consumers Road, Suite 600
    // Toronto, ON M2J 4V8";
    // public static final String TFC_PAYMENT_CENTER_ADDRESS = "Freightcom Inc.
    // Logica C/O PDP group 10909 McCormick Road Hunt Valley, MD 21065 ";
    // public static final String TFC_PAYMENT_CENTER_ADDRESS = "Freightcom Inc.
    // Logica,\nC/O PDP group \n10909 McCormick Road Hunt Valley, MD 21065";
    // New SFW address applicable from January 1, 2013.
    public static final String TFC_PAYMENT_CENTER_ADDRESS_OLD = "FCI C/O SFW Payment Ctr \n PO Box 860007 \nShawnee, KS  66286";
    // New SFW address applicable from Dec 4, 2015 as per seema's instruction.
    public static final String TFC_PAYMENT_CENTER_ADDRESS = "FCI C/O AFS Payment Center \n PO Box 1208 \n Mauldin, SC 29662";
    public static final String SFW_PAYMENT_CENTER_ADDRESS_ROAD_RUNNER = "FCI C/O AFS Payment Center \n PO Box 1208 \n Mauldin, SC 29662";
    public static final String TFC_PAYMENT_CENTER_CWW_ADDRESS = "CWS PAYMENT CENTER, C/O  DTA Services Limited 505 Consumers Road, Suite 600 Toronto, ON M2J 4V8";
    // public static final String CTS_PAYMENT_CENTER_ADDRESS = "Freightcom Inc
    // c/o CTS Freight Payment, PO BOX 441326, Kennesaw, GA, 30160"; (changed on
    // june 24,2013)
    // 1/31/2016 Commenting the below as CTS is being merged into One global
    // account,and they requested to change the address
    // public static final String CTS_PAYMENT_CENTER_ADDRESS = "FCI c/o CTS
    // Freight Payment, PO BOX 441326, Kennesaw, GA 30160" ;
    public static final String CTS_PAYMENT_CENTER_ADDRESS = "Freightcom c/o CTS Freight Payment, PO BOX 441326, Kennesaw, GA  30160";
    public static final String CTS_PAYMENT_CENTER_ADDRESS_OLD = "FCI c/o CTS Freight Payment, PO BOX 441326, Kennesaw, GA  30160";
    public static final String CTS2_PAYMENT_CENTER_ADDRESS_OLD = "CWSI c/o CTS Freight Payment, PO BOX 441326, Kennesaw, GA, 30160";
    public static final String CTS2_PAYMENT_CENTER_ADDRESS = "CWSI c/o CTS Freight Payment, PO BOX 441326, Kennesaw, GA, 30160";

    public static final String DAYR_PAYMENT_CENTER_ADDRESS = "FREIGHTCOM PAYMENT CENTER, C/O  DTA Services Limited 505 Consumers Road, Suite 600 Toronto, ON M2J 4V8";
    /*
     * public static final String PURO_FREIGHT_10LB_PAYMENT_CENTER_ADDRESS
     * ="Freightcom Inc. FCPC (10), 7699 Bath Rd. Mississauga, ON L4T 3T1";
     * public static final String PURO_FREIGHT_15LB_PAYMENT_CENTER_ADDRESS
     * ="FC Payment Centre FCPC (15), 7699 Bath Rd. Mississauga, ON, L4T 3T1";
     * public static final String PURO_FREIGHT_PICKUP_EMAIL_15LB_ADDRESS
     * ="FC Payment Centre,#FCPC (15),#7699 Bath Rd. #Mississauga, ON, L4T 3T1";
     * public static final String PURO_FREIGHT_PICKUP_EMAIL_10LB_ADDRESS
     * ="FC Payment Centre,#FCPC (10),#7699 Bath Rd. #Mississauga, ON, L4T 3T1";
     */
    // As per Seema's request change the third party bill address on 16 Nov
    // 2015.
    // public static final String PURO_FREIGHT_10LB_PAYMENT_CENTER_ADDRESS
    // ="Freightcom Inc. FCPC (10), 7000 Pine Valley Drive, Suite # 201 Vaughan,
    // ON L4L 4Y8";
    // public static final String PURO_FREIGHT_10LB_PAYMENT_CENTER_ADDRESS_OLD
    // ="Freightcom Inc. FCPC (10), 7699 Bath Rd. Mississauga, ON L4T 3T1";
    // public static final String PURO_FREIGHT_15LB_PAYMENT_CENTER_ADDRESS ="FC
    // Payment Centre FCPC (15), 7000 Pine Valley Drive, Suite # 201 Vaughan, ON
    // L4L 4Y8";
    // public static final String PURO_FREIGHT_15LB_PAYMENT_CENTER_ADDRESS_OLD
    // ="FC Payment Centre FCPC (15), 7699 Bath Rd. Mississauga, ON, L4T 3T1";
    public static final String PURO_FREIGHT_10LB_PAYMENT_CENTER_ADDRESS = "FCI FCPC (10), 7000 Pine Valley Drive, Suite # 201 Vaughan, ON L4L 4Y8";
    public static final String PURO_FREIGHT_10LB_PAYMENT_CENTER_ADDRESS_OLD = "FCI FCPC (10), 7699 Bath Rd. Mississauga, ON L4T 3T1";
    public static final String PURO_FREIGHT_15LB_PAYMENT_CENTER_ADDRESS = "FCI (15), 7000 Pine Valley Drive, Suite # 201 Vaughan, ON L4L 4Y8";
    public static final String PURO_FREIGHT_15LB_PAYMENT_CENTER_ADDRESS_OLD = "FCI (15), 7699 Bath Rd. Mississauga, ON, L4T 3T1";
    public static final String PURO_FREIGHT_PICKUP_EMAIL_15LB_ADDRESS = "FC Payment Centre,#FCPC (15),#7699 Bath Rd. #Mississauga, ON, L4T 3T1";
    public static final String PURO_FREIGHT_PICKUP_EMAIL_10LB_ADDRESS = "FC Payment Centre,#FCPC (10),#7699 Bath Rd. #Mississauga, ON, L4T 3T1";

    public static final String HEAVY_TRAFFIC = "Heavy Traffic";
    public static final String ACC_RESIDENTIAL_DELIVERY = "Residential Delivery";
    public static final String ACC_PICKUP_TAILGATE = "Lift Gate at Pick Up";
    public static final String ACC_RESIDENTIAL_PICKUP = "Residential pickup";
    public static final String ACC_DELIVERY_TAILGATE = "Lift Gate at Delivery";
    public static final String ACC_SATURDAY_DELIVERY = "Saturday Delivery";
    public static final String ACC_INSIDE_PICKUP = "Inside Pickup";
    public static final String ACC_INSIDE_DELIVERY = "Inside Delivery";
    public static final String ACC_CONSTRUCTION_SITE = "Construction Site";
    public static final String ACC_HAZMAT = "Hazmat";
    public static final String ACC_SATURDAY_PICKUP = "Saturday Pickup";
    public static final String ACC_SINGLE_SHIPMENT = "Single Shipment";
    public static final String ACC_NOTIFICATION = "Notification";
    public static final String ACC_HOMELAND_SECURITY = "Homeland Security";
    public static final String ACC_PIER_CHARGE = "Pier Charge";
    public static final String ACC_EXCESS_LENGTH = "Excess Length";
    public static final String ACC_EXCESS_OVER_LENGTH = "Excess Over Length";
    public static final String ACC_TEAM_CHARGE = "Team Charge";
    public static final String ACC_LIMITED_ACCESS = "Limited Access";
    public static final String ACC_BORDER_FEE = "Cross Border Fee";
    public static final String ACC_MILITARY_BASE_DELIVERY = "Military Base Delivery";
    public static final String ACC_CUSTOMS = "Customs Fee";
    public static final String ACC_SORT_SEGREGATE_CHARGE = "Sorting Segregating";
    public static final String ACC_SORT_SEGREGATE_GOJIT_CHARGE = "Sort and Segregate";
    public static final String ACC_APPOINTMENT_FEE = "Appointment";
    public static final String ACC_IN_BOND_FEE = "Inbond Freight";
    public static final String ACC_CAN_CUSTOMS = "Canadian Customs";
    public static final String ACC_LINE_HAUL_PICKUP = "High Cost PU";
    public static final String ACC_LINE_HAUL_DELIVERY = "High Cost Delivery";
    public static final String ACC_GOJIT_DC_DELIVERY = "DC Deliveries Charge";
    public static final String ACC_GOJIT_CONGESTION_CHARGE = "Congestion Surcharge";
    public static final String ACC_MILITARY_ACCESS = "Military Access";

    public static final String ACC_GUARANTEE_CHARGE = "Guarantee Charge";
    public static final String ACC_GUARANTEE_CHARGE_CTS = "Guarantee Standard";

    public static final String SUCCESS = "Success";
    public static final int CARRIER_ESHIPPER = 22;
    public static final String CARRIER_ESHIPPER_STRING = "22";
    public static final String ESHIPPER_SHIP_ORDER_ERROR_MSG = "An error occured during Ship Order";
    public static final String ESHIPPER_SCHEDULED_PICKUP_ORDER_ERROR_MSG = "An error occured during Scheduling Pickup";
    public static final String ESHIPPER_SCHEDULED_PICKUP_CANCEL_ORDER_ERROR_MSG = "An error occured during canceling the scheduled Pickup";
    public static final String XML_VERSION = "3.1.0";
    public static final Object ESHIPPER_INSURANCE_NAME = "eshipper";
    public static final String ESHIPPER_LIVE_SERVER_URL = "ESHIPPER_LIVE_SERVER_URL";
    public static final String FREIGHTCOM = "Freightcom";
    public static final String FREIGHTCOM_CONTACT_PHONE = "877-335-8740";
    public static final String FREIGHTCOM_CONTACT_EMAIL = "operations@freightcom.com";
    public static final String STATUS_CODE_ORDER_RECEIVED = "1";
    public static final String STATUS_CODE_SCHEDULED_PICKUP = "2";
    public static final String STATUS_CODE_PICKED_UP = "3";
    public static final String STATUS_CODE_IN_TRANSIT = "4";
    public static final String STATUS_CODE_DELIVERED = "5";
    public static final String STATUS_CODE_CANCELED = "7";
    public static final String STATUS_CODE_PENDING = "8";
    public static final String ORDER = "order";
    public static final String STATUS = "status";
    public static final String SMALL_PACKAGE_STATUS_SERVICE = "SMALL_PACKAGE_STATUS_SERVICE";
    public static final String NMFC_REQUEST_TO_ADDRESS = "operations@freightcom.com";
    public static final String NMFC_REQUEST_SUBJECT = "Email sent through NMFC Request on freightcom Site";
    public static final String FREIGHTCOM_INVOICE_GENERATION_SERVICE = "FREIGHTCOM_INVOICE_GENERATION_SERVICE";
    public static final String ESHIPPER_EDI_DATA_PROCESSROR = "EshipperEDIDataProcessorBase";
    public static final long CARRIER_MFW_ID = 23;
    public static final String CARRIER_MFW = "MFW";
    public static final String CARRIER_MFW_PROVIDER = "MFW";
    public static final int MFW_READ_TIME_OUT = 20000;
    public static final String MFW_STATUS_SERVICE = "MFW_STATUS_SERVICE";
    public static final String MFW_PAYMENT_CENTER_ADDRESS = "Payment Center,\n7133 W.95th Street Ste.205,\nOverland Park, KS 66212";
    public static final String MFW_PAYMENT_CENTER_ADDRESS_NEW_SERVICES1 = "LP Freight Payment Center,\n PO Box 183850 Shelby Twp.,\n MI 48318";
    public static final String MFW_PAYMENT_CENTER_FOR_ODFL_AND_SAIA = "Payment Center, 2900 E La Palma Ave, Anaheim, CA 92806 USA Phone:7146321440";
    public static final String MFW_PAYMENT_CENTER_FOR_UPGF = "Elite Ops,\n 370 E. South Temple,\n Salt Lake City, UT, 84111";
    public static final String MFW_PAYMENT_CENTER_FOR_FXNL_FXFE = "Payment Center,\n 7133 W.95th Street,\nSte.205,Overland Park, KS 66212";
    public static final String MFW_PAYMENT_CENTER_FOR_EXLA = "Payment Center Account # 5028461, 2900 E La Palma Ave, Anaheim, CA 92806 USA";
    public static final String MFW_PAYMENT_CENTER_FOR_CNWY = "Payment Center Account # 313595615, 2900 E La Palma Ave, Anaheim, CA 92806 USA";
    public static final String CARRIER_INSURANCE_NAME = "Carrier";

    public static final int TFC_DAY_AND_ROSS = 1913;
    public static final int TFC_DAY_AND_ROSS_R_AND_L = 1914;
    public static final int PUROLATOR_FREIGHT_SERVICE_ID = 200;

    // wo_charge chargeId
    public static final int WO_FUEL_CHARGE = 10;
    public static final int WO_FREIGHT_CHARGE = 50;

    public static final int EDI_FUEL_CHARGE = 5;
    public static final int EDI_FREIGHT_CHARGE = 4;

    // PUROLATOR FREIGHT
    public static final String PUROLATOR_PAYMENT_CENTER_CONTACT_NUMBER = "905-612-7205";

    // SFW
    public static final int CARRIER_SFW_ID = 24;
    public static final String CARRIER_SFW = "SFW";
    public static final long SFW_SERVICE_NEMF_ID = 2414;
    // public static final String SFW_PAYMENT_CENTER_ADDRESS = "FREIGHTCOM
    // PAYMENT CENTER,\nC/O DTA Services Limited \n505 Consumers Road,\nSuite
    // 600 Toronto, ON M2J 4V8";
    public static final String SFW_PAYMENT_CENTER_ADDRESS = "Freightcom Inc. Logica,\nC/O PDP group \n10909 McCormick Road Hunt Valley, MD 21065";
    public static final String SFW_PAYMENT_CENTER_CONTACT_NUMBER = "905-612-7205";
    public static final String SFW_STATUS_SERVICE = "SFW_STATUS_SERVICE";
    public static final String SFW_POLARIS_PAYMENT_CENTER_ADDRESS_OLD = "FCI Payment Center (CLASS),\n 7699 Bath Road,\n Mississauga, ON L4T 3T1";
    public static final String SFW_POLARIS_PAYMENT_CENTER_ADDRESS = "FCI Payment Center (CLASS),\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";
    // POLARIS
    public static final int SFW_POLARIS_SERVICE_ID = 2413;
    public static final int SFW_ROAD_RUNNER_SERVICE_ID = 2403;
    public static final int CARRIER_POLARIS_ID = 25;
    public static final String POLARIS_PAYMENT_CENTER_ADDRESS_OLD = "FCI Payment Center (PALLET),\n 7699 Bath Road,\n Mississauga, ON L4T 3T1";
    public static final String POLARIS_PAYMENT_CENTER_ADDRESS = "Freightcom PALLET,\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";

    public static final String POLARIS_PAYMENT_CENTER_CONTACT_NUMBER = "905-612-7205";
    public static final String POLARIS_EMAIL = "POLARIS_EMAIL";
    public static final int POLARIS_SERVICE_ID = 2500;

    // CCT
    public static final int CARRIER_CCT_ID = 29;
    public static final String CCT_PAYMENT_CENTER_ADDRESS_OLD = "FCI Payment Center ,\n7699 Bath Road,\nMississauga, ON L4T 3T1";
    public static final String CCT_PAYMENT_CENTER_ADDRESS = "FCI Payment Center,\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";
    public static final String CCT_PAYMENT_CENTER_CONTACT_NUMBER = "905-612-7205";
    public static final String CCT_EMAIL_CANCEL = "CCT_EMAIL_CANCEL";
    public static final String CCT_EMAIL_PICKUP = "CCT_EMAIL_PICKUP";
    public static final int CCT_SERVICE_ID = 2900;
    public static final String CCT_FREIGHT_PICKUP_EMAIL_ADDRESS = "FCI Payment Center,\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";
    public static final String CARRIER_CCT_STRING = "CCT";

    // Max Length for CTS,CTS2 and MFW Rating
    // public static final int MAX_DIM_LENGTH_RATING = 143;
    public static final int MAX_DIM_LENGTH_RATING = 144;
    public static final String EXCEEDED_MAX_LENGTH_STRING = "Exceeded Length";
    public static final String EXCEEDED_MAX_WIDTH_STRING = "Exceeded width";
    public static final String EXCEEDED_MAX_HEIGHT_STRING = "Exceeded height";
    public static final String EXCEEDED_MAX_CUBIC_STRING = "Exceeded cubic";
    public static final String EXCEEDED_MAX_WEIGHT_STRING = "Exceeded weight";
    public static final String EXCEEDED_MAX_TAILGATE_STRING = "Exceeded tailgate";
    public static final String EXCEEDED_MAX_WIDTH_TAILGATE_STRING = "Exceeded tailgate width";
    public static final String EXCEEDED_MAX_HEIGHT_TAILGATE_STRING = "Exceeded tailgate height";
    public static final String EXCEEDED_MAX_WEIGHT_TAILGATE_STRING = "Exceeded tailgate weight";

    // Max Length for SFW Rating
    // public static final int MAX_DIM_LENGTH_RATING_SFW = 120;
    public static final int MAX_DIM_LENGTH_RATING_SFW = 143;
    public static final int MAX_DIM_LENGTH_RATING_SFW_POLARIS = 167;
    public static final int MAX_DIM_LENGTH_RATING_DR_INT = 179;
    /*
     * public static final int MAX_DIM_LENGTH_RATING_DR = 239; As per the
     * business requirement dated 01/11/2016, changing the SFW(Day & Ross)
     * Linear feet rule from 20 to 18
     */
    public static final int MAX_DIM_LENGTH_RATING_DR = 215;
    public static final int MAX_DIM_LENGTH_RATING_SINGLE_PALLET_DR = 96;
    public static final double MAX_DIM_CUBED_FEET_SFW = 750.00;
    public static final double MAX_DIM_CUBED_FEET_DR_INT = 960.00;
    public static final double MAX_DIM_CUBED_FEET_DR = 1280.00;
    public static final int MAX_DIM_HEIGHT_RATING_SFW = 96; // originally was 96
    public static final int MAX_DIM_WIDTH_RATING_SFW = 90; // Originally was 96
    public static final int MAX_DIM_LENGTH_SFW = 120;
    public static final double MAX_DIM_CUBED_FEET_GROUP_A = 640.00;
    public static final double MAX_DIM_CUBED_FEET_GROUP_B = 768.00;
    public static final double MAX_DIM_CUBED_FEET_GROUP_F = 640.00;
    public static final int MAX_DIM_LENGTH_RATING_CONWAY = 167;
    public static final double MAX_DIM_CUBED_FEET_CONWAY = 896.00;
    public static final double MAX_DIM_CUBED_FEET_CZARLITE = 512.00;
    public static final int MAX_DIM_LENGTH_RATING_CZARLITE = 95;
    public static final int MAX_DIM_HEIGHT_RATING_CZARLITE = 96;
    public static final int MAX_DIM_WIDTH_RATING_CZARLITE = 90; // Originally
                                                                // was 95

    // MIN_TAILGATE_CONSTANT & MAX_TAILGATE_CONSTANT
    public static final double MIN_TAILGATE_CONSTANT = 45.00;
    public static final double MAX_TAILGATE_CONSTANT = 125.00;
    public static final double HUNDRED_CONSTANT = 100.00;
    public static final double TAILGATE_MULTIPLY_CONSTANT = 3.75;

    // Central Arizona Under Service C
    public static final int MAX_DIM_LENGTH_RATING_CAZF = 143;
    public static final int MAX_DIM_HEIGHT_RATING_CAZF = 84;
    public static final int MAX_DIM_WIDTH_RATING_CAZF = 84;

    // Cubic Logic services
    // Group B
    public static final int CTS_AAA_COOPER_B = 2000;
    public static final int CTS_AAA_COOPER_GUARANTEED_B = 2049;
    public static final int CTS_Wilson_B = 2025;

    public static final int CTS2_AAA_COOPER_TRANSPORTATION_B = 2131;
    public static final int CTS2_AAA_COOPER_GUARANTEED_B = 2157;

    // Group C
    public static final int CTS_STREAM_LINKS_EXPRESS_B = 2037;
    public static final int CTS_NEBRASKA_TRANSPORT_B = 2014;
    public static final int CTS_ROADRUNNER_B = 2040;
    public static final int CTS_ROADRUNNER_GUARANTEED_B = 2048;

    public static final int CTS2_STREAM_LINKS_EXPRESS_B = 2150;
    public static final int CTS2_NEBRASKA_TRANSPORT_B = 2143;
    public static final int CTS2_ROADRUNNER_B = 2100;

    // Group I
    public static final int CTS_DRUG_TRANSPORT_B = 2006;
    public static final int CTS_GRILEY_AIR_FREIGHT_B = 2033;
    public static final int CTS_WARD_B = 2024;
    public static final int CTS_WARD_GUARANTEED_B = 2057;

    public static final int CTS2_DRUG_TRANSPORT_B = 2136;
    public static final int CTS2_GRILEY_AIR_FREIGHT_B = 2139;
    public static final int CTS2_WARD_TRUCKING_B = 2153;
    public static final int CTS2_WARD_TRUCKING_GUARANTEED_B = 2162;

    // Group J
    public static final int CTS_NEW_CENTURY_TRANSPORTATION_B = 2047;
    public static final int CTS_RHODY_TRANSPORTATION_B = 2020;
    public static final int CTS_RIST_TRANSPORTATION_B = 2046;

    public static final int CTS2_PARAGON_SERVICES_B = 2156;

    // For Extreme length surcharge amount for SFW
    public static final double EXCEED_LENGTH_SURCHARGE_AMT = 50;

    // Max Length for Purolator Rating
    public static final int MAX_DIM_LENGTH_RATING_PURO = 96;

    // For Calculating Cubed weight
    public final static Double CTS_LB_DIVIDER_CONSTANT = 1728.00;

    // SPEEDY CARRIER
    public static final int CARRIER_SPEEDY_ID = 26;
    public static final int SPEEDY_SERVICE_ID = 2600;
    // public static final String SPEEDY_PAYMENT_CENTER_10LB_ADDRESS =
    // "Freightcom Payment Center,FCPC (10),\n7699 Bath Road,\nMississauga, ON
    // L4T 3T1";
    // public static final String SPEEDY_PAYMENT_CENTER_15LB_ADDRESS =
    // "Freightcom Payment Center,FCPC (15),\n7699 Bath Road,\nMississauga, ON
    // L4T 3T1";
    // public static final String SPEEDY_PAYMENT_CENTER_10LB_ADDRESS = "FCI
    // Payment Center (10),\n7699 BATH ROAD,\nMISSISSAUGA, ON L4T 3T1";
    // public static final String SPEEDY_PAYMENT_CENTER_15LB_ADDRESS = "FCI
    // Payment Center (15),\n7699 BATH ROAD,\nMISSISSAUGA, ON L4T 3T1" ;
    public static final String SPEEDY_PAYMENT_CENTER_10LB_ADDRESS = "FCI Payment Center (10),\n7000 Pine Valley Drive, Suite # 201\n Vaughan, ON L4L 4Y8";
    public static final String SPEEDY_PAYMENT_CENTER_15LB_ADDRESS = "FCI Payment Center (15),\n7000 Pine Valley Drive, Suite # 201\n Vaughan, ON L4L 4Y8";
    public static final String SPEEDY_PAYMENT_CENTER_10LB_ADDRESS_OLD = "FCI Payment Center (10),\n7699 BATH ROAD,\nMISSISSAUGA, ON L4T 3T1";
    public static final String SPEEDY_PAYMENT_CENTER_15LB_ADDRESS_OLD = "FCI Payment Center (15),\n7699 BATH ROAD,\nMISSISSAUGA, ON L4T 3T1";

    public static final String SPEEDY_PAYMENT_CENTER_CONTACT_NUMBER = "905-612-7205";
    public static final String SPEEDY_EMAIL = "SPEEDY_EMAIL";
    public static final String TST_EMAIL = "TST_EMAIL";
    public static final String GOJIT_EMAIL = "GOJIT_EMAIL";
    public static final String SHIFT_EMAIL = "SHIFT_EMAIL";
    public static final String FRONTLINE_EMAIL = "FRONTLINE_EMAIL";

    public static final String CARRIER_SPEEDY_STRING = "Speedy Transport";
    public static final String CARRIER_SPEEDY = "SPEEDY";
    public static final int MAX_DIM_LENGTH_RATING_SPEEDY = 180;
    // For Speedy Accessorial we added this constants
    public static final String ACC_APPOINTMENT_PICKUP = "Appointment Pickup";
    public static final String ACC_APPOINTMENT_DELIVERY = "Appointment Delivery";
    public static final String ACC_SORT_AND_SEGREGATE = "Sort and Segregate";
    public static final String ACC_CONGESTION_COST_FROM = "From Metro Congestion";
    public static final String ACC_CONGESTION_COST_TO = "To Metro Congestion";

    // Acc Charge and Acc Cost for Purolator Freight, Polaris, Speedy Carrier
    public static final String ACC_COST_VALUE = "Acc_Cost_Value";
    public static final String ACC_CHARGE_VALUE = "Charge_Value";

    public static int CUSTOMS_SERVICE_CHARGE_VALUE = 90;

    // For Purolator Tracking purpose
    public static final String PUROLATOR_TRACKING_URL = "http://www.purolatorfreight.com/scripts/cgiip.exe/protrace.htm?wpronumb=";

    // For Speedy Tracking purpose
    // public static final String SPEEDY_TRACKING_URL =
    // "http://www.speedy.ca/status.asp?Trace=";
    public static final String SPEEDY_TRACKING_URL = "http://www.speedy.ca/en/findpod.asp?Trace=";
    public static final String CCT_TRACKING_URL = "http://www.cctcanada.com/cgi-bin/wspd_cgi.sh/facts?startpage=protrace&pronum=";

    public static final String SPEEDY_STATUS_SERVICE = "SPEEDY_STATUS_SERVICE";
    public static final String GOJIT_STATUS_SERVICE = "GOJIT_STATUS_SERVICE";

    // 12 New MFW servics
    public static final String USRD = "USRD";
    public static final String AMAP = "AMAP";
    public static final String CPPV = "CPPV";
    public static final String DPHE = "DPHE";
    public static final String MIDW = "MIDW";
    public static final String RDFS = "RDFS";
    public static final String SMTL = "SMTL";
    public static final String WEBE = "WEBE";
    public static final String WTVA = "WTVA";
    public static final String ESTS = "ESTS";
    public static final String CHAP = "CHAP";
    public static final String NYCI = "NYCI";

    public static final String SFW_EMAIL = "SFW_EMAIL";
    public static final String SFW_CONVEY_EMAIL = "SFW_CONVEY_EMAIL";
    public static final String SFW_TST_EMAIL = "SFW_TST_EMAIL";
    public static final String SFW_FRONTLINE_EMAIL = "SFW_FRONTLINE_EMAIL";
    public static final String SFW_CLEARLANE_EMAIL = "SFW_CLEARLANE_EMAIL";
    public static final String SFW_NEMF_EMAIL = "SFW_NEMF_EMAIL";
    public static final String NEMF_EMAIL_PROSART = "NEMF_EMAIL_PROSART";
    public static final String SFW_MTS_EMAIL = "SFW_MTS_EMAIL";
    public static final String SFW_CAZF_EMAIL = "CENTRAL_ARIZONA_EMAIL";

    // FEDEX FREIGHT CARRIER
    public static final int CARRIER_FEDEX_FREIGHT = 27;
    public static final int FEDEX_PRIORITY_FREIGHT = 2700;
    public static final String CARRIER_FEDEX_FREIGHT_STRING = "Fedex Freight";
    public static final String FEDEX_FREIGHT_SERVICE = "FEDEX_FREIGHT_SERVICE";
    public static final String FEDEX_FREIGHT_STATUS_SERVICE = "FEDEX_FREIGHT_STATUS_SERVICE";
    public static final String TST_PRO_NUMBER_UPDATE_SERVICE = "TST_PRO_NUMBER_UPDATE_SERVICE";
    public static final String SHIFT_STATUS_UPDATE_SERVICE = "SHIFT_STATUS_UPDATE_SERVICE";
    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_ADDRESS_OLD = "FCI,\n7699 Bath Road,\nMississauga, ON L4T 3T1";
    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_ADDRESS_NORTHBOUND_OLD = "FCI (NB),\n7699 Bath Road,\nMississauga, ON L4T 3T1";
    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_ADDRESS_SOUTHBOUND_OLD = "FCI (SB),\n7699 Bath Road,\nMississauga, ON L4T 3T1";

    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_ADDRESS = "FCI,\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";
    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_ADDRESS_NORTHBOUND = "FCI (NB),\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";
    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_ADDRESS_US_DOMESTIC = "FCI,\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";
    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_ADDRESS_SOUTHBOUND = "FCI (SB),\n7000 Pine Valley Drive,\nSuite # 201 Vaughan, ON L4L 4Y8";
    public static final String CARRIER_FEDEX_EDI_NAME = "FEDEX";
    public static final String SHIFT_FREIGHT_SERVICE = "SHIFT_FREIGHT_SERVICE";

    // HST logic for ON,BC,NB,NS,NL for Manual Workorder.

    public static final int HST_GROUP_ID = 2;
    public static final int HST_ON_GROUP_ID = 10;
    public static final int HST_BC_GROUP_ID = 11;
    public static final int HST_NB_GROUP_ID = 12;
    public static final int HST_NS_GROUP_ID = 13;
    public static final int HST_NL_GROUP_ID = 14;

    public static final String FEDEX_FREIGHT_PAYMENT_CENTER_NUMBER = "905-612-7205";
    public static final String FEDEX_FREIGHT_EMAIL = "FEDEX_FREIGHT_EMAIL";

    public static final String FREIGHTCOM_PAYMENT_CENTER_NUMBER = "877-335-8740 x175";
    public static final String CERASIS_PAYMENT_CENTER_NUMBER = "800-734-5351";
    // Aramex Service Ids
    public static final int ARAMEX_PRIORITY_LETTER_EXPRESS_SERVICE_ID = 2258;
    public static final int ARAMEX_PRIORITY_PARCEL_EXPRESS_SERVICE_ID = 2259;
    public static final String CARRIER_ESHIPPER_ARAMEX_STRING = "Aramex";
    public static final String CARRIER_ESHIPPER_ARAMEX_TRACKING_URL = "http://www.aramex.com/express/track_results_multiple.aspx?ShipmentNumber=";

    // Order Status History -Type
    public static final String TYPE_ALL = "all";
    public static final String TYPE_PUBLIC = "public";

    public static final int MAX_DIM_LENGTH_GROUP_A = 119;
    public static final int MAX_DIM_LENGTH_GROUP_B = 144;
    public static final int MAX_DIM_LENGTH_GROUP_F = 119;
    public static final int MAX_DIM_HEIGHT_GROUP = 96; // originally was 96
    public static final int MAX_DIM_HEIGHT_GROUP_F = 95;
    public static final int MAX_DIM_HEIGHT_GROUP_A = 96;

    public static final int MAX_DIM_LENGTH_GROUP_SINGLE_F = 95;
    public static final int MAX_DIM_WIDTH_GROUP = 90; // originally was 96
    public static final int MAX_DIM_WIDTH_GROUP_F = 53;
    public static final int MAX_DIM_WIDTH_COMBINATION = 48;
    public static final int MAX_DIM_HEIGHT_COMBINATION = 48;

    public static final int MAX_DIM_LENGTH_TAILGATE_GROUP = 96;
    public static final int MAX_DIM_LENGTH_TAILGATE_GROUP_F = 71;
    public static final int MAX_DIM_LENGTH_TAILGATE_GROUP_B = 48;
    public static final int MAX_DIM_LENGTH_TAILGATE_GROUP_H = 60;
    public static final int MAX_DIM_WIDTH_TAILGATE_GROUP = 60;
    public static final int MAX_DIM_WIDTH_TAILGATE_GROUP_F = 53;
    public static final int MAX_DIM_WIDTH_TAILGATE_GROUP_B = 72;
    public static final int MAX_DIM_WIDTH_TAILGATE_GROUP_H = 48;
    public static final int MAX_DIM_WEIGHT_TAILGATE_GROUP = 2000;
    public static final int MAX_DIM_WEIGHT_TAILGATE_GROUP_EXTENDED = 3000;
    public static final int MAX_DIM_WEIGHT_TAILGATE_GROUP_WILSON_MTS = 2500;
    public static final int MAX_DIM_WEIGHT_TAILGATE_GROUP_F = 2999;
    public static final int MAX_DIM_WEIGHT_TAILGATE_GROUP_B = 1500;
    public static final int MAX_DIM_WEIGHT_TAILGATE_GROUP_H = 1000;

    public static final int MAX_DIM_WEIGHT_GROUP = 3000;
    public static final int MAX_DIM_WEIGHT_GROUP_F = 4499;
    public static final int MAX_DIM_WEIGHT_GROUP_B = 1500;
    public static final int MAX_DIM_WEIGHT_GROUP_H = 1500;

    // Group I and J belongs to CTS
    public static final int MAX_DIM_LENGTH_GROUP_I = 215;
    public static final int MAX_DIM_HEIGHT_GROUP_I = 96;
    public static final int MAX_DIM_WIDTH_GROUP_I = 90;
    public static final double MAX_DIM_CUBED_FEET_I = 1152.00;

    public static final int MAX_DIM_LENGTH_GROUP_J = 287;
    public static final int MAX_DIM_HEIGHT_GROUP_J = 96;
    public static final int MAX_DIM_WIDTH_GROUP_J = 90;
    public static final double MAX_DIM_CUBED_FEET_J = 1536.00;

    // Central Arizona Tailgate Limits
    public static final int MAX_DIM_LENGTH_TAILGATE_CAZF = 60;
    public static final int MAX_DIM_WIDTH_TAILGATE_CAZF = 60;
    public static final int MAX_DIM_HEIGHT_TAILGATE_CAZF = 70;
    public static final int MAX_DIM_WEIGHT_TAILGATE_CAZF = 2000;

    public static final int SHIPMENT_DENSITY_15LB = 15;
    public static final String PUROLATOR_BILLTOADDRESS_15LB_FORAPI = "FC Payment Centre FCPC (15)";
    public static final String SPEEDY_BILLTOADDRESS_15LB_FORAPI = "FCI Payment Center (15)";
    public static final String EMERGEIT_OPERATOR_EMAIL = "EMERGEIT_OPERATOR_EMAIL";

    public static final String BILL_TO_SHIPPER = "shipper";
    public static final String BILL_TO_THIRD_PARTY = "consignee";

    public static final double FEDEX_SCHEDULE_PICKUP_CHARGE = 4.10;
    public static final double UPS_SCHEDULE_PICKUP_CHARGE = 6.00;

    // colors
    public static final String COLOR_RED = "#FA5B46";
    public static final String COLOR_YELLOW = "#FDF906";
    public static final String COLOR_ORANGE = "#FE9B07";
    public static final String COLOR_GREEN = "#AAFA4D";
    public static final String COLOR_PURPLE_INTL = "#BC94DC";
    public static final String COLOR_TRANSPARENT = "TRANSPARENT";

    public static final String GUEST_CONTACT_EMAIL = "GUEST_CONTACT_EMAIL";
    public static final String FREIGHTCOM_SALES_EMAIL = "FREIGHTCOM_SALES_EMAIL";
    public static final String FREIGHTCOM_CUSTOMER_CARE_EMAIL = "FREIGHTCOM_CUSTOMER_CARE_EMAIL";
    public static final String FREIGHTCOM_ACCOUNTING_EMAIL = "FREIGHTCOM_ACCOUNTING_EMAIL";
    public static final String FREIGHTCOM_QUOTE_EMAIL = "FREIGHTCOM_QUOTE_EMAIL";
    public static final String GUEST_ACCOUNT_NAME = "instant789";

    // Batch Shipment
    public static final String SERVICE_TYPE_BY_CARRIER = "byCarrier";
    public static final String SERVICE_TYPE_BY_BEST_RATE = "byBestRate";
    public static final String SERVICE_TYPE_BY_BEST_AIR = "byBestAir";
    public static final String SERVICE_TYPE_BY_BEST_GROUND = "byBestGround";

    public static final String STATUS_BATCH_UPLOADED = "Uploaded";
    public static final String STATUS_BATCH_RATING = "Rating";
    public static final String STATUS_BATCH_RATED = "Rated";
    public static final String STATUS_BATCH_SHIPPING = "Shipping";
    public static final String STATUS_BATCH_SHIPPED = "Shipped";
    public static final String STATUS_BATCH_CANCELLING = "Cancelling";
    public static final String STATUS_BATCH_CANCELLED = "Cancelled";

    public static String BATCHUPLOAD_FOLDER = "BatchUpload";
    public static String BATCHUPLOAD_IN_FOLDER = "inprocess";
    public static String BATCHUPLOAD_OUT_FOLDER = "backup";

    public static final String PACKAGE_TYPE_ENVELOPE_STRING_REPORT = "Env";
    public static final String PACKAGE_TYPE_PAK_STRING_REPORT = "Pak";

    public static final long CARRIER_TST_FREIGHT = 28;
    public static final String CARRIER_TST_FREIGHT_STRING = "TST";

    public static final long CARRIER_SHIFT_FREIGHT = 31;
    public static final String CARRIER_SHIFT_FREIGHT_STRING = "SHIFT";

    public static final int MAX_NINTY_SIX_INCH_LENGTH = 96;// 12*8=96
    public static final int MIN_TWELVE_INCH_LENGTH = 12;
    public static final int MAX_FIFTY_SIX_INCH_LENGTH = 56;
    public static final int MAX_FOURTY_EIGHT_INCH_LENGTH = 48;
    public static final int MAX_FOURTY_NINE_INCH_LENGTH = 49;
    public static final int DIVIDE_BY_FOUR_CONSTANAT = 4;
    public static final int DIVIDE_BY_TWO_CONSTANAT = 2;

    public static final String CARRIER_GOJIT_STRING = "Dicom Freight";
    public static final String CARRIER_FRONTLINE_STRING = "Frontline";
    public static final long CARRIER_GOJIT_ID = 30;
    public static final String GOJIT_TRACKING_URL = "GOJIT_TRACKING_URL";
    public static final int GOJIT_CARRIER_ID = 30;
    public static final int SHIFT_CARRIER_ID = 31;
    public static final int FRONTLINE_CARRIER_ID = 32;

    public static final String ACC_NFLD = "NFLD Surcharge";
    public static final int MARK_UP_VALUE = 0;
    public static final int MARK_DOWN_VALUE = 1;

    // Polaris Standard carrierId-24
    public static final long POLARIES_STD_SERVICE_ID = 2413;
    // Polaris Standard carrierId-24
    public static final long POLARIES_EXP_SERVICE_ID = 2500;
    public static final long[] FC_CARRIER_DHL_SERVICE_IDS = { 2218, 2219, 2220, 2221, 2222, 2223, 2224 };
    public static final long[] FC_CARRIER_FEDEX_SERVICE_IDS = { 2200, 2201, 2202, 2247, 2248, 2249, 2250, 2251, 2252,
            2253, 2260 };
    public static final long[] FC_CARRIER_PUROLATOR_SERVICE_IDS = { 2203, 2204, 2205, 2206, 2207, 2208, 2209, 2210,
            2211, 2212, 2217, 2225, 2226 };
    public static final long[] FC_CARRIER_UPS_SERVICE_IDS = { 2239, 2240, 2241, 2242, 2243, 2244, 2245, 2246, 2254,
            2255, 2256, 2257 };
    public static final String MODE_TRANSPORT_AIR_VALUE_A = "A";
    public static final String MODE_TRANSPORT_GROUND_VALUE_G = "G";

    public static final float SCHEDULE_PICKUP_CHARGE = 6;
    public static final int FREIGHTCOM_OTHER_CHARGE_ID = 20;

    public static int BROKER_YES = 1;
    public static int BROKER_NO = 2;

    public static final int ORDER_QUOTE_STATUS_INITIAL = 0;
    public static final int ORDER_QUOTE_STATUS_QUOTED = 1;
    public static final int ORDER_QUOTE_STATUS_SHIPPED = 2;
    public static final int ORDER_QUOTE_STATUS_EXPIRED = 3;

    public static final int ESHIPPER_USER_FREIGHTCOMAPI = 1;
    public static final int ESHIPPER_USER_FREIGHTCOMAPI_USA = 2;
    public static final int FREIGHTCOM_FRANCHISE_ID = 11;

    // Cerasis Constant
    public static final long CERASIS_CARRIER_ID = 33;
    public static final int CARRIER_CERARIS_INT = 33;
    public static final String CERARIS_LTL_SERVICE = "CERARIS_LTL_SERVICE";
    public static final String CARRIER_CERASIS_FREIGHT_STRING = "Cerasis";
    public static final String CERASIS_SHIPPER_ID = "1768";
    public static final String CERASIS_USER_NAME = "xmluser";
    public static final String CERASIS_USER_PASSWORD = "xmluser";
    public static final String CERASIS_XML_ACCESS_KEY = "b152a8e0-fd9c-4a9c-a5c7-a041f922063f";
    public static final String CERASIS_PAYMENT_CENTER_ADDRESS = "Cerasis, Inc \n PO Box 21248 \n Eagan, MN. 55121";
    public static final String CERASIS_EXECUTE_QUARTZ_JOBS_TO_WEB_SERVER = "CERASIS_EXECUTE_QUARTZ_JOBS_TO_WEB_SERVER";

    public static final long CUSTOMER_IDEAL_WAREHOUSE = 7163;

    public static final String CERASIS_EDI_DATA_PROCESSROR = "CerasisEDIDataProcessorBase";

    public static final String CERASIS_EMAIL = "CERASIS_EMAIL";
    public static final String FC_COST = "FC_COST";
    public static final String WEB_HOUSE_REGISTRATION_EMAIL = "WEB_HOUSE_REGISTRATION_EMAIL";
    public static final String GUEST_WEB_QUOTE_EMAIL = "GUEST_WEB_QUOTE_EMAIL";
    public static final String CTS_LTL_SERVICE_CTS_1 = "CTS_LTL_SERVICE_CTS_1";

    // Canpar Constant
    public static final long CANPAR_CARRIER_ID = 34;
    public static final String CARRIER_CANPAR_STRING = "Canpar";
    public static final String CANPAR_SMALL_PACKAGE_SERVICE = "CANPAR_SMALL_PACKAGE_SERVICE";
    public static final double CANPAR_SCHEDULE_PICKUP_CHARGE = 6.00;

    public static final String CANPAR_MANIFEST_NOTIFICATION_EMAIL = "CANPAR_MANIFEST_NOTIFICATION_EMAIL";
    public static final String FCCOST_CANPAR_SMALL_PACKAGE_SERVICE = "FCCOST_CANPAR_SMALL_PACKAGE_SERVICE";
    public static final String CANPAR_SCHEDULED_PICKUP_ORDER_ERROR_MSG = "An error occured during Scheduling Pickup";

    // Apex Constants

    public static final long APEX_CARRIER_ID = 35;
    public static final long APEX_SERVICE_ID = 3500;
    public static final String APEX_LTL_SERVICE = "APEX_LTL_SERVICE";
    public static final String CARRIER_APEX_FREIGHT_STRING = "Apex";
    public static final String APEX_PAYMENT_CENTER_ADDRESS = "FCI Payment Center,\n7000 Pine Valley Drive, Suite # 201\nVaughan, ON L4L 4Y8";
    public static final String APEX_EXECUTE_QUARTZ_JOBS_TO_WEB_SERVER = "APEX_EXECUTE_QUARTZ_JOBS_TO_WEB_SERVER";
    public static final String APEX_EMAIL = "APEX_EMAIL";

    public static final String APEX_EDI_DATA_PROCESSROR = "ApexEDIDataProcessorBase";

    public static final int FC_COST_CUSTOMER_ID = 6396;
    public static final String POLARIS_EMAIL_CANCEL = "POLARIS_EMAIL_CANCEL";
    public static final String FREIGHTCOM_OFFICE_CONTACT = "8773358740";
    public static final String FREIGHTCOM_OERATION_EMAIL = "operations@freightcom.com";
    public static final String FREIGHTCOM_TEST_COMPANY = "TEST";
    public static final String FREIGHTCOM_TEST_ADDRESS = "Test ShippingAddress";
    public static final String FREIGHTCOM_TEST_ATTENTION = "Test Attention";

    public static final long FREIGHT_CHARGE_ID = 30;
    public static final long FUEL_CHARGE_ID = 31;
    public static final String CSV_INVOICE_HEADERS = "Invoice #,Date Created,Work Order #,Customer Name,Sales Agent,Reference,Carrier,# of Packages,ShipFrom Company,ShipFrom ShippingAddress,ShipFrom City,ShipFrom PostalCode/Zip,ShipFrom Province,ShipFrom Country,ShipFrom Residential,ShipTo Company,ShipTo ShippingAddress,ShipTo City,ShipTo Province,ShipTo PostalCode/Zip,ShipTo Country,ShipTo Residential,Actual Weight(lbs),Dim Weight(lbs),Tracking #,BOL #,COD Value,Insurance Amount,Payment Type,POD: Date of Delivery,Freightcom Order#,Base Charge,Fuel Charge,Surcharge1 Name,Surcharge1 Charge,Surcharge2 Name,Surcharge2 Charge,Surcharge3 Name,Surcharge3 Charge,Surcharge4 Name,Surcharge4 Charge,Surcharge5 Name,Surcharge5 Charge,Surcharge6 Name,Surcharge6 Charge,Surcharge7 Name,Surcharge7 Charge,Tax1 Name,Tax1 Charge ,Tax2 Name,Tax2 Charge,Tax3 Name,Tax3 Charge,Tax4 Name,Tax4 Charge,Other Tax Name,Other Tax Charge,Weight1,Class1,Weight2,Class2,Weight3,Class3,Weight4,Class4,Weight5,Class5,Weight6,Class6,Weight7,Class7,Weight8,Class8,Weight9,Class9,Weight10,Class10,Weight11,Class11,Weight12,Class12,Total Charge";
    public static final double WEBHOUSE_CUSTOMER_INITIAL_CREDIT_LIMIT = 1000.00;

    public static final String DICOM_SMALL_PACKAGE_SERVICE = "DICOM_SMALL_PACKAGE_SERVICE";
    public static final String FCCOST_DICOM_SMALL_PACKAGE_SERVICE = "FCCOST_DICOM_SMALL_PACKAGE_SERVICE";

    public static final double FREIGHTCOM_MINIMUM_CHARGE_FOR_CC = 0.05;
    public static final int WO_DIMENSION_TABLE_DEFAULT_LENGTH = 1;

    public static final double IN_HOUSE_DEFAULT_INSURANCE_PERCENT_CHARGE = 0.75;
    public static final double IN_HOUSE_DEFAULT_INSURANCE_PERCENT_COST = 0.18;
    public static final double IN_HOUSE_DEFAULT_INSURANCE_MIN_CHARGE = 3.0;
    public static final double IN_HOUSE_DEFAULT_INSURANCE_MIN_COST = 3.0;

    public static final String IN_HOUSE_INSURANCE_COST_PERCENT_US = "IN_HOUSE_INSURANCE_COST_PERCENT_US";
    public static final String IN_HOUSE_INSURANCE_COST_PERCENT_CA = "IN_HOUSE_INSURANCE_COST_PERCENT_CA";
    public static final String IN_HOUSE_INSURANCE_CHARGE_MIN_US = "IN_HOUSE_INSURANCE_CHARGE_MIN_US";
    public static final String IN_HOUSE_INSURANCE_COST_MIN_US = "IN_HOUSE_INSURANCE_COST_MIN_US";
    public static final String IN_HOUSE_INSURANCE_CHARGE_MIN_CA = "IN_HOUSE_INSURANCE_CHARGE_MIN_CA";
    public static final String IN_HOUSE_INSURANCE_COST_MIN_CA = "IN_HOUSE_INSURANCE_COST_MIN_CA";

    public static final String INSURANCE_SURCHARGE_NAME = "Insurance";

    public static final String FREIGHTCOM_ADMIN_EMAILID = "admin@freightcom.com";
    public static final String FREIGHTCOM_SHIPMENTS_REPORT_EMAIL_ID = "tkermally@freightcom.com";
    public static final long DEFAULT_ESHIPPER_ORDER_ID = 0;

    public static final float WEBHOUSE_SIGNUP_MARKUP = 30;

    public static final String SIGNUP_MARKUP_EXCLUDED_CARRIERID = "SIGNUP_MARKUP_EXCLUDED_CARRIERID";
    public static final String COMMISSION_START_DATE = "2014-09-15 23:59";

    public static final int FC_INSURANCE = 1;

    public static final String DAILY_SHIPMENT_COUNT_NOTIFICATION_EMAIL = "DAILY_SHIPMENT_COUNT_NOTIFICATION_EMAIL";

    public static final int CTS_EXCESS_LENGTH_LIMIT = 120;

    public static final String DEFAULT_LIABILITY_NOTE = "NOTE: Liability limitation for loss or damage in this shipment may be applicable. See 49 USC 14706(c)(1)(A) and (B).";
    public static final String OVERLAND_LIABILITY_NOTE = "DECLARED VALUATION Maximum liability of $2.00 per pound ($4.41 per kilogram) computed on the total weight of the shipment unless declared otherwise.";

    public static final String INVOICE_GEN_EMAIL_LIST = "INVOICE_GEN_EMAIL_LIST";
    public static final String PRO_AVALABILITY_REPORT_EMAIL_LIST = "PRO_AVALABILITY_REPORT_EMAIL_LIST";
    public static final String EMAIL_FAILURE_NOTIFY_LIST = "EMAIL_FAILURE_NOTIFY_LIST";

}
