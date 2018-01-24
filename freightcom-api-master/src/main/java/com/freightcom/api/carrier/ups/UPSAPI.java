package com.freightcom.api.carrier.ups;

public class UPSAPI
{

    public static final String LIVE_URL_RATE = "https://www.ups.com/ups.app/xml/Rate";
    public static final String TEST_URL_RATE = "https://wwwcie.ups.com/ups.app/xml/Rate";
    public static final String LIVE_URL_SHIP_CONFIRM = "https://www.ups.com/ups.app/xml/ShipConfirm";
    public static final String TEST_URL_SHIP_CONFIRM = "https://wwwcie.ups.com/ups.app/xml/ShipConfirm";
    public static final String LIVE_URL_SHIP_ACCEPT = "https://www.ups.com/ups.app/xml/ShipAccept";
    public static final String TEST_URL_SHIP_ACCEPT = "https://wwwcie.ups.com/ups.app/xml/ShipAccept";
    public static final String LIVE_URL_TRACK = "https://www.ups.com/ups.app/xml/Track";
    public static final String TEST_URL_TRACK = "https://wwwcie.ups.com/ups.app/xml/Track";
    public static final String LIVE_URL_VOID = "https://www.ups.com/ups.app/xml/Void";
    public static final String TEST_URL_VOID = "https://wwwcie.ups.com/ups.app/xml/Void";
    public static final String LIVE_URL_TIMEINTRANSIT = "https://onlinetools.ups.com/ups.app/xml/TimeInTransit";
    public static final String TEST_URL_TIMEINTRANSIT = "https://wwwcie.ups.com/ups.app/xml/TimeInTransit";
    public static final String LIVE_URL_PICKUP = "https://onlinetools.ups.com/webservices/Pickup";
    public static final String TEST_URL_PICKUP = "https://wwwcie.ups.com/webservices/Pickup";
    public static final int REQUEST_TYPE_RATE = 1;
    public static final int REQUEST_TYPE_SHIP = 2;
    public static final int REQUEST_TYPE_TRACK = 3;
    public static final int REQUEST_TYPE_VOID = 4;
    public static final int REQUEST_TYPE_TIMEINTRANSIT = 5;

    public static final double INSURANCE_MINIMUM_US_CHARGE = 1.95;
    public static final double CWW_MARKUP_PERCENTAGE = 20;
    public static final double SCHEDULE_PICKUP_CHARGE = 4.10;

    public static final int DEFAULT_DAYS_FOR_DELIVERY = 0;

    public static final String SERVICE_TYPE_AIR = "Air";
    public static final String SERVICE_TYPE_GROUND = "Ground";
    public static final String ADDITIONAL_HANDLING = "Additional Handling";
    public static final String COD = "COD";
    public static final String DELIVERY_AREA_SURCHARGE_RESIDENTIAL = "Delivery Area Surcharge - Residential";
    public static final String DELIVERY_AREA_SURCHARGE = "Delivery Area Surcharge";
    public static final String DELIVERY_AREA_SURCHARGE_EXTENDED = "Delivery Area Surcharge - Extended";
    public static final String DELIVERY_AREA_SURCHARGE_REMOTE_AK = "Remote Area Surcharge - Alaska";
    public static final String DELIVERY_AREA_SURCHARGE_REMOTE_HI = "Remote Area Surcharge - Hawaii";
    public static final String DELIVERY_CONFIRMATION = "Delivery Confirmation";
    public static final String DELIVERY_CONFIRMATION_SIGN_REQ = "Delivery Confirmation - Signature Required";
    public static final String DELIVERY_CONFIRMATION_ADULT_SIGN_REQ = "Delivery Confirmation - Adult Signature Required";
    public static final String INSURANCE = "Declared Value";
    public static final String LARGE_PACKAGE_SURCHARGE = "Large Package Surcharge";
    public static final String SCHEDULE_PICKUP = "Schedule Pickup";
    public static final String RESIDENTIAL_DELIVERY = "Residential Delivery";
    public static final String RESIDENTIAL_DELIVERY_GND = "Residential Delivery - Ground";
    public static final String RESIDENTIAL_DELIVERY_AIR = "Residential Delivery - Air";
    public static final String SATURDAY_DELIVERY = "Saturday Delivery";
    public static final String EARLY_AM_SURCHARGE = "Early A.M. Surcharge";

    public static String getUPSPackageCode(int packageId)
    {

        switch (packageId) {
        case 1:
            return "01";
        case 2:
            return "04";
        case 3:
            return "02";
        case 4:
            return "30";
        }
        return "02";

    }

    public static String serviceDescription(String code)
    {
        switch (code) {
        case "07":
            return "UPS Worldwide Express";

        case "08":
            return "UPS Worldwide Expedited";

        case "54":
            return "UPS Worldwide Express Plus";

        case "65":
            return "UPS Express Saver";

        case "11":
            return "UPS Standard";

        case "12":
            return "UPS 3 Day Select";

        case "02":
            return "UPS Expedited";

        case "13":
            return "UPS Express Saver";

        case "14":
            return "UPS Express Early";

        case "01":
            return "UPS Express";
        }

        return "";
    }

}
