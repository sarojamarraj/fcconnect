<?php

class CommonMacros
{
  /**
   *
   */
  public static function admin_login_request()
  {
    return [
            'Admin Login',
            [ 'POST', '/api/login', [], null, ['username' => 'admin', 'password' => TestRunner::$admin_password]],
            [ 'user-id' => function($text)  {
                if (preg_match('/value="([^""]+)"/ui', $text, $matches)) {
                  return $matches[1];
                } else {
                  return null;
                }
              }],
            [ 'user-id' => '@notnull' ]
            ];
  }

  /**
   *
   */
  public static function customer_login_request($user_key = null)
  {
    if (empty($user_key)) {
      $username = getenv('FREIGHTCOM_CUSTOMER_ADMIN_LOGIN');

      if (empty($username)) {
        $username = 'sdaudlin';
      }

      $password = getenv('FREIGHTCOM_CUSTOMER_ADMIN_PASSWORD');

      if (empty($password)) {
        $password = '';
      }
    } else {
      $username = $user_key;
      $password = getenv("FREIGHTCOM_{$user_key}");
    }

    return ['Customer Admin Login',
            [ 'POST', '/api/login', [], null, ['username' => $username, 'password' => $password ]],
            [ 'user-id' => function($text)  {
                if (preg_match('/value="([^""]+)"/ui', $text, $matches)) {
                  return $matches[1];
                } else {
                  return null;
                }
              }],
            [ 'user-id' => '@notnull' ]
            ];
  }

  /**
   *
   */
  public static function customer_staff_login_request($userName, $password)
  {
    return ['Customer Staff Login',
            [ 'POST', '/api/login', [], null, ['username' => $userName, 'password' => $password ]],
            [ 'user-id' => function($text)  {
                if (preg_match('/value="([^""]+)"/ui', $text, $matches)) {
                  return $matches[1];
                } else {
                  return null;
                }
              }],
            [ 'user-id' => '@notnull' ]
            ];
  }

  /**
   *
   */
  public static function user_login_request($userName, $password)
  {
    return ['User login ' . $userName,
            [ 'POST', '/api/login', [], null, ['username' => $userName, 'password' => $password ]],
            [ 'user-id' => function($text)  {
                if (preg_match('/value="([^""]+)"/ui', $text, $matches)) {
                  return $matches[1];
                } else {
                  return null;
                }
              }],
            [ 'user-id' => '@notnull' ]
            ];
  }

  /**
   * Send a request for roles, set login-role to the id of the role of the selected
   * type
   */
  public static function login_lookup_request($roleName, array $others = []) {
    $role_requests = [
                      'login-role' => function($object) use ($roleName) {
                        if (isset($object['authorities'])) {
                          foreach ($object['authorities'] as $authority) {
                            if ($authority['roleName'] === $roleName) {
                              return $authority['id'];
                            }
                          }
                        }

                        return null;
                      }
                      ];

    $role_tests = [ 'login-role' => '@notNull' ];

    foreach ($others as $role_variable => $role_name) {
      $role_requests[$role_variable] = self::getRoleExtract($role_name);
      $role_tests[$role_variable] = '@notNull';
    }

    return ['get the user to extract roles',
            [ 'GET', '/api/user/${user-id}' ],
            $role_requests,
            $role_tests,
            ];
  }

  private static function getRoleExtract($roleName)
  {
    return function($object) use ($roleName) {
      if (isset($object['authorities'])) {
        foreach ($object['authorities'] as $authority) {
          if ($authority['roleName'] === $roleName) {
            return $authority['id'];
          }
        }
      }
    };
  }

  public static function login_set_role_request($role_variable_name) {
    return [[ 'POST', '/api/set-session-role/${' . $role_variable_name . '}' ]];
  }

  public static $login_set_role_request
    = [[ 'POST', '/api/set-session-role/${login-role}' ]];

  /**
   *
   */
  public static function generateEmail()
  {
    return substr(md5(mt_rand()), 0, 6)
      . '.'
      . substr(md5(mt_rand()), 0, 6)
      . '@palominosys.com';
  }


  public static function createCustomerStaff($customer_user_name, $password)
  {
    $invoiceTestEmail = getenv("INVOICE_TEST_EMAIL");

    if (empty($invoiceTestEmail)) {
      $invoiceTestEmail = self::generateEmail($customer_user_name);
    }

    $customer_name = 'customer_name' . substr(md5(mt_rand()), 0, 8);

    return
      [
       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name) ],
        [ 'customer-id' => 'id' ],
        []
        ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [
                                                                    'login' => $customer_user_name,
                                                                    'password' => $password,
                                                                    'email' => $invoiceTestEmail,
                                                                    'firstname' => 'Hello',
                                                                    'lastname' => 'Smith'
                                                                    ]],
        [ 'customer-user-login' => 'login',
          'customer-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],
       ];
  }


  public static function createCustomerFull($customer_user_name, $password)
  {
    $invoiceTestEmail = getenv("INVOICE_TEST_EMAIL");

    if (empty($invoiceTestEmail)) {
      $invoiceTestEmail = self::generateEmail($customer_user_name);
    }

    $customer_name = 'customer_name' . substr(md5(mt_rand()), 0, 8);

    return
      [
       [
        'Create a customer',
        [ 'POST',
          '/api/customer-and-staff', null, [
                                            'name' => $customer_name,
                                            'email' => 'foo.bar.@palominosys.com',
                                            'firstname' => 'Zachary',
                                            'lastname' => 'Smith',
                                            "country" => "Canada",
                                            "address" => "1 North St. South",
                                            "city" => "Cityville",
                                            "invoiceTerm" => "30",
                                            "invoiceEmail" => "foo.bar.@palominosys.com",
                                            "invoiceTermWarning" => "40",
                                            "login" => self::randomUser("customerTest"),
                                            "invoiceCurrency" => "CAD",
                                            "shippingNMFCRequired" => false,
                                            "autoCharge" => "NEVER",
                                            "password" => self::randomPassword('customerTest'),
                                            "autoInvoice" => "MONTHLY",
                                            "province" => "ON",
                                            "shippingPODRequired" => true,
                                            "passwordConfirm" => self::randomPassword('customerTest'),
                                            "phone" => "416-452-5267",
                                            "contact" => "Zachary Smith",
                                            "pastDueAction" => "DISALLOW",
                                            "applicableTaxes" => [ [ "taxDefinition" => [ 'id' => 1 ] ] ]
                                            ]],
        [ 'customer-id' => 'id' ],
        []
        ],

       [ 'find the admin',
         [ 'GET', '/api/user', [ 'customerId' => '${customer-id}' ]],
         [ 'customer-admin-user-id' => '_embedded.user.0.id' ],
         [ 'customer-admin-user-id' => '@notnull' ]
         ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [
                                                                    'login' => $customer_user_name,
                                                                    'password' => $password,
                                                                    'email' => $invoiceTestEmail,
                                                                    'firstname' => 'Last',
                                                                    'lastname' => 'Samson'
                                                                    ]],
        [ 'customer-user-login' => 'login',
          'customer-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],
       ];
  }


  public static function cleanupCustomerStaff()
  {
    return
      [

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer-staff-id}' ],
        [],
        []
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer-id}' ],
        [],
        []
        ],

       ];
  }

  static $generatedUsers = [];

  public static function randomUser($key = null)
  {
    if ($key !== null && isset(self::$generatedUsers[$key])) {
      return self::$generatedUsers[$key];
    }

    $user = $key . '_' . substr(md5(mt_rand()), 0, 18);

    if ($key !== null) {
      self::$generatedUsers[$key] = $user;
    }

    return $user;
  }

  static $generatedPhones = [];

  public static function randomPhone($key = null)
  {
    if ($key !== null && isset(self::$generatedPhones[$key])) {
      return self::$generatedPhones[$key];
    }

    $phone = rand(111, 999) . '-555-' . sprintf('%04d', rand(0, 9999));

    if ($key !== null) {
      self::$generatedPhones[$key] = $phone;
    }

    return $phone;
  }

  static $generatedEmails = [];

  public static function randomEmail($key = null)
  {
    if ($key !== null && isset(self::$generatedEmails[$key])) {
      return self::$generatedEmails[$key];
    }

    $email = "{$key}." . substr(md5(mt_rand()), 0, 18) . "@palominosys.com";

    if ($key !== null) {
      self::$generatedEmails[$key] = $email;
    }

    return $email;
  }

  static $generatedPasswords = [];

  public static function randomPassword($key = null)
  {
    if ($key !== null && isset(self::$generatedPasswords[$key])) {
      return self::$generatedPasswords[$key];
    }

    $password = substr(md5(mt_rand()), 0, 18);

    if ($key !== null) {
      self::$generatedPasswords[$key] = $password;
    }

    return $password;
  }

  public static function clearCaches()
  {
    self::$generatedPasswords = [];
    self::$generatedEmails = [];
    self::$generatedPhones = [];
    self::$generatedUsers = [];
  }

  public static function date($interval = "+1 day")
  {
    $date = new DateTime();
    $date->setTimeZone(new DateTimeZone('UTC'));
    $date->modify($interval);

    return $date->format('Y-m-d');
  }


  public static function adminLogin()
  {
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,
       ];
  }

  static function samplePayload()
  {
    return [
            'shipTo' => [ 'city' => 'Toronto' ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}',
            "signatureRequired" => "Yes",
            'shipDate' => self::date('+4 weeks'),
            "accessorialServices" => [
                                      "Saturday Pickup" => true,
                                      "Residential Delivery" => true,
                                      "Saturday Delivery" => true,
                                      "Hold For Pickup" => true,
                                      "Residential Pickup" => true
                                      ],
            "shipFrom" =>
            [
             "contactName" => "Bear MacDonald",
             "consigneeName" => "Company Name",
             "address1" => "Sample Address1",
             "address2" => "Sample Address2",
             "city" => "City",
             "province" => "AB",
             "phone" => "3333",
             "email" => "george@palominosys.com",
             "emailNotification" => true,
             "company" => "Company Name",
             "companyName" => "Company Name",
             "country" => 'CA'
             ],
            "shipTo" => [
                         "company" => "BBBBB",
                         "companyName" => "BBBBB",
                         "country" => 'CA'
                         ]
            ];
  }

  static function sampleCustomer($customer_name, array $overrides = [])
  {
    return
      array_merge(
                  [
                   'name' => $customer_name,
                   'country' => 'Canada',
                   'address' => '1 First st.',
                   'city' => 'District 99',
                   'invoiceTerm' => '30',
                   'invoiceEmail' => 'foo.bar.@palominosys.com',
                   'invoiceTermWarning' => '40',
                   'invoiceCurrency' => 'CAD',
                   'shippingNMFCRequired' => false,
                   'autoCharge' => 'IMMEDIATELY',
                   'autoInvoice' => 'MONTHLY',
                   'province' => 'ON',
                   'suspended' => false,
                   'postalCode' => 'M3A 5A3',
                   'shippingPODRequired' => true,
                   'pastDueAction' => 'DISALLOW',
                   "applicableTaxes" => [ [ "taxDefinition" => [ 'id' => 1 ] ] ]
                   ],
                  $overrides);
  }

  public static function shipDate()
  {
    $date = new DateTime();
    $date->setTimeZone(new DateTimeZone('UTC'));
    $date->modify('+1 day');

    return $date->format('Y-m-d');
  }
}