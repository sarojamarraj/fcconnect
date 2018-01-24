<?php

require_once(__DIR__ . '/CommonMacros.php');


class SavePickupAndCustoms extends CommonMacros
{
  public static function requests()
  {
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'update pickup and customs',
         [ 'PUT', '/api/order/save-pickup-and-customs/507644', null,
           [
            'scheduledPickup' => [
                                  'deliveryCloseTime' => "17:00",
                                  'pickupCloseTime' => "17:00",
                                  'pickupDate' => "2017-10-31",
                                  'pickupReadyTime' => "09:00"
                                  ] ]
           ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}