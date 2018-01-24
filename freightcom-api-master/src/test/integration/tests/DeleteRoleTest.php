<?php

require_once(__DIR__ . '/CommonMacros.php');

class DeleteRoleTest extends CommonMacros
{
  public static function requests()
  {
    $customer_name = 'customer_name' . substr(md5(mt_rand()), 0, 8);
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $agent_login = 'agent_user_name' . substr(md5(mt_rand()), 0, 8);
    $agent2_login = 'agent2_user_name' . substr(md5(mt_rand()), 0, 8);
    $agent3_login = 'agent3_user_name' . substr(md5(mt_rand()), 0, 8);

    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Create an agent',
        [ 'POST', '/api/user/agent', null, ['login' => $agent_login,
                                            'firstname' => 'Franz',
                                            'lastname' => 'Liszt',
                                            'email' => self::generateEmail(),
                                            ]],
        [ 'agent-user-id' => 'id',
          'agent-login' => 'login',
          'agent-role-id' => 'authorities.0.id',
          ],
        [ 'agent-login' => $agent_login,
          'agent-role-id' => '@notnull' ]
        ],

       [
        'Create a seccond  agent',
        [ 'POST', '/api/user/agent', null, ['login' => $agent2_login,
                                            'firstname' => 'Frederika',
                                            'lastname' => 'Von Stade',
                                            'email' => self::generateEmail(), ]],
        [ 'agent2-user-id' => 'id',
          'agent2-login' => 'login',
          'agent2-role-id' => 'authorities.0.id',
          ],
        [ 'agent2-login' => $agent2_login,
          'agent2-role-id' => '@notnull' ]
        ],

       [
        'Create a third  agent',
        [ 'POST', '/api/user/agent', null, ['login' => $agent3_login,
                                            'firstname' => 'Fred',
                                            'lastname' => 'Mumbly',
                                            'email' => self::generateEmail(),]],
        [ 'agent3-user-id' => 'id',
          'agent3-login' => 'login',
          'agent3-role-id' => 'authorities.0.id',
          ],
        [ 'agent3-login' => $agent3_login,
          'agent3-role-id' => '@notnull' ]
        ],

       [
        'Create a customer',
        [ 'POST', '/api/customer', null,
          self::sampleCustomer($customer_name,
                               [
                                'salesAgent' => [
                                                 'id' => '${agent-role-id}'
                                                 ]]) ],
        [ 'customer-id' => 'id',
          'customer-sales-agent' => 'salesAgent.id' ],
        [ 'customer-sales-agent' => '${agent-role-id}' ]
        ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [ 'login' => $customer_user_name,
                                                                     'email' => self::generateEmail(),
                                                                     'firstname' => 'Thaddius',
                                                                     'lastname' => 'Arnold'
                                                                     ]],
        [ 'customer-user-login' => 'login',
          'customer-user-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [
        'Delete the customer staff role from the user',
        [ 'DELETE', '/api/user/role/${customer-user-id}/customer_staff', [ 'customerId' => '${customer-id}' ]],
        [ 'result' => '0.id' ],
        [ 'result' => '@notnull' ]
        ],

       [
        'Check the user\'s authorities',
        [ 'GET', '/api/user/${customer-user-id}' ],
        [ 'new-authorities' => 'authorities.@size' ],
        [ 'new-authorities' => '0'  ]
        ],

       // Get some alerts until desired alert seen
       [ 'get alerts',
         [ 'GET', '/api/alert', [ 'userId' => '${agent-user-id}', ]],
         [ 'count' => '_embedded.alerts.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>0' ], 'FOUND_ALERTS' ],

       [ 'sleep', [ 'SLEEP', 3 ]],

       // Get some alerts until desired alert seen
       [ 'get alerts',
         [ 'GET', '/api/alert', [ 'userId' => '${agent-user-id}', ]],
         [ 'count' => '_embedded.alerts.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>0' ], 'FOUND_ALERTS' ],

       [ 'sleep', [ 'SLEEP', 3 ]],

       // Get some alerts until desired alert seen
       [ 'get alerts',
         [ 'GET', '/api/alert', [ 'userId' => '${agent-user-id}', ]],
         [ 'count' => '_embedded.alerts.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>0' ], 'FOUND_ALERTS' ],

       // Get some alerts until desired alert seen
       [ 'get alerts',
         [ 'GET', '/api/alert', [ 'userId' => '${agent-user-id}', ]],
         [ 'count' => '_embedded.alerts.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>0' ], 'FOUND_ALERTS' ],

       [ 'sleep', [ 'SLEEP', 3 ]],

       // Get some alerts until desired alert seen
       [ 'get alerts',
         [ 'GET', '/api/alert', [ 'userId' => '${agent-user-id}', ]],
         [ 'count' => '_embedded.alerts.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>0' ], 'FOUND_ALERTS' ],

       // Get some alerts until desired alert seen
       [ 'get alerts',
         [ 'GET', '/api/alert', [ 'userId' => '${agent-user-id}', ]],
         [ 'count' => '_embedded.alerts.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>0' ], 'FOUND_ALERTS' ],



       [ 'LABEL', 'FOUND_ALERTS' ],

       [ 'get alerts',
         [ 'GET', '/api/alert', [ 'userId' => '${agent-user-id}', ]],
         [ 'count' => '_embedded.alerts.@size' ],
         [ 'count' => '>0' ]
         ],

       // Clean up

       [
        'Delete the customer staff role if not deleted',
        [ 'DELETE', '/api/user/role/${role-id}' ],
        [],
        [],
        "No such role"
        ],

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer-user-id}' ],
        [],
        []
        ],

       [
        'Delete the agent role',
        [ 'DELETE', '/api/user/role/${agent-role-id}', [ 'reassignment' => '${agent2-role-id}'] ],
        [],
        []
        ],

       [
        'Check the customer for reassignment',
        [ 'GET', '/api/customer/${customer-id}' ],
        [
         'customer-sales-agent' => 'salesAgent.id'
          ],
        [
         'customer-sales-agent' => '${agent2-role-id}']
        ],

       [
        'Check the agent',
        [ 'GET', '/api/user/${agent-user-id}', ],
        [ 'deleted-roles' => 'authorities.@size' ],
        [ 'deleted-roles' => '0' ]
        ],

       [
        'Delete the agent',
        [ 'DELETE', '/api/user/${agent-user-id}' ],
        [],
        []
        ],

       [
        'Delete the agent2 role',
        [ 'DELETE', '/api/user/role/${agent2-role-id}', [ 'reassignment' => '${agent3-role-id}' ]],
        [],
        []
        ],

       [
        'Delete the second agent',
        [ 'DELETE', '/api/user/${agent2-user-id}' ],
        [],
        []
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer-id}' ],
        [],
        []
        ],

       [
        'Delete the third agent',
        [ 'DELETE', '/api/user/${agent3-user-id}' ],
        [],
        []
        ],



       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}