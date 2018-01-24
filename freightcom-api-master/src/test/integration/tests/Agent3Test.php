<?php

require_once(__DIR__ . '/CommonMacros.php');

class Agent3Test extends CommonMacros
{
  public static function requests()
  {
    $password = substr(md5(mt_rand()), 0, 18);
    $login = substr(md5(mt_rand()), 0, 18);
    $email = substr(md5(mt_rand()), 0, 18);
    $customer_name = substr(md5(mt_rand()), 0, 18);

    $uniqueUser = "george." . substr(md5(mt_rand()), 0, 8) . '.jonas';

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name) ],
        [ 'customer-id' => 'id' ],
        []
        ],

       [ 'create an agent',
         [ 'POST', '/api/user/agent', null, [
                                             'login' => $login,
                                             'email' => $email,
                                             'firstname' => 'Jacqueline',
                                             'lastname' => 'Jones',
                                             'agentName' => 'Jacqueline Jones',
                                             'customerId' => '${customer-id}',
                                             ]],
         [ 'agent-id' => 'id',
           'agent-role-id' => 'authorities.0.id' ],
         [],
         ],

       [ 'list agents',
         [ 'GET', '/api/user', [ 'login' => $login, 'role' => 'agent', 'page' => 0 ],
           ],
         [
          'agent1-size' => '_embedded.user.0.authorities.@size',
          ],
         [
          'agent1-size' => 1,
          ]
         ],


       [ 'list agents 2',
         [ 'GET', '/api/user', [ 'email' => substr($email, 4, 8), 'role' => 'agent', 'page' => 0 ],
           ],
         [
          'agent1-size' => '_embedded.user.0.authorities.@size',
          ],
         [
          'agent1-size' => 1,
          ]
         ],


       [ 'list agents 2',
         [ 'GET', '/api/user', [ 'namefields' => substr($email, 4, 8), 'role' => 'agent', 'page' => 0 ],
           ],
         [
          'agent1-size' => '_embedded.user.0.authorities.@size',
          ],
         [
          'agent1-size' => 1,
          ]
         ],


       [ 'create a user',
         [ 'POST', '/api/user', null, [ 'login' => $uniqueUser, 'firstname' => 'George', 'lastname' => 'Jonas', 'email' => self::generateEmail(),  ], null ],
        [ 'login' => 'login', 'firstname' => 'firstname', 'lastname' => 'lastname', 'new-user-id' => 'id' ],
        [ 'login' => $uniqueUser, 'firstname' => 'George', 'lastname' => 'Jonas' ],
        ],


       [ 'make user a sales agent',
         [ 'POST', '/api/user/role/agent', null, [ 'userId' => '${new-user-id}',
                                                   'parentSalesAgentId' => '${agent-role-id}',
                                                   ]],
        [  ],
        [  ],
        ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       ['Delete the first agent role', [ 'DELETE', '/api/user/role/${agent-role-id}' ] ],
       ['Delete the first agent', [ 'DELETE', '/api/user/${agent-id}' ] ],
       ['Delete the customer', [ 'DELETE', '/api/customer/${customer-id}' ] ],
       ['Delete the second user', [ 'DELETE', '/api/user/${new-user-id}' ] ],


       [ "logout", [ 'POST', '/api/logout', [], null, null ]]
       ];
  }
}
