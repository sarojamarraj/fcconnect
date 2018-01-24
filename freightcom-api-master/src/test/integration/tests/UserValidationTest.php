<?php

require_once(__DIR__ . '/CommonMacros.php');

class UserValidationTest extends CommonMacros
{
  public static function requests()
  {
    self::clearCaches();

    return
      [
       [ 'GROUP', self::adminLogin() ],

       [ "Create a User",
         [ 'POST', '/api/user', null, [
                                       'login' => self::randomUser("user1"),
                                       'firstname' => 'George',
                                       'lastname' => 'Jonas',
                                       'email' => self::randomEmail("user1")
                                       ], null ],
        [
         'login' => 'login',
         'firstname' => 'firstname',
         'lastname' => 'lastname',
         'first-user-id' => 'id',
         'email' => 'email'
         ],
        [
         'login' => self::randomUser("user1"),
         'firstname' => 'George',
         'lastname' => 'Jonas',
         'email' => self::randomEmail("user1")
         ],
        ],

       [ "Duplicate login",
         [ 'POST', '/api/user', null, [
                                       'login' => self::randomUser("user1"),
                                       'firstname' => 'George',
                                       'lastname' => 'Jonas',
                                       'email' => self::randomEmail("user2")
                                       ], null ],
         [],
         [],
         [
          'key' => 'login',
          'message' => 'Login must be unique'
          ]
         ],

       [ "Duplicate email",
         [ 'POST', '/api/user', null, [
                                       'login' => self::randomUser("user2"),
                                       'firstname' => 'George',
                                       'lastname' => 'Jonas',
                                       'email' => self::randomEmail("user1")
                                       ], null ],
         [],
         [],
         [
          'key' => 'email',
          'message' => 'Email must be unique'
          ]
         ],

       [ "Missing login",
         [ 'POST', '/api/user', null, [
                                       'firstname' => 'Who',
                                       'lastname' => 'Jonas',
                                       'email' => self::randomEmail("user3.1")
                                       ], null ],
         [],
         [],
         [
          'key' => 'login',
          'message' => 'Login required'
          ]
         ],

       [ "Missing first name",
         [ 'POST', '/api/user', null, [
                                       'login' => self::randomUser("user3"),
                                       'lastname' => 'Jonas',
                                       'email' => self::randomEmail("user3")
                                       ], null ],
         [],
         [],
         [
          'key' => 'firstname',
          'message' => 'Firstname required'
          ]
         ],

       [ "Empty first name",
         [ 'POST', '/api/user', null, [
                                       'login' => self::randomUser("user3"),
                                       'firstname' => " ",
                                       'lastname' => 'Jonas',
                                       'email' => self::randomEmail("user3")
                                       ], null ],
         [],
         [],
         [
          'key' => 'firstname',
          'message' => 'Firstname required'
          ]
         ],

       [ "Empty last name",
         [ 'POST', '/api/user', null, [
                                       'login' => self::randomUser("user4"),
                                       'firstname' => "Greg",
                                       'lastname' => '     ',
                                       'email' => self::randomEmail("user4")
                                       ], null ],
         [],
         [],
         [
          'key' => 'lastname',
          'message' => 'Lastname required'
          ]
         ],

       [ "Empty last name",
         [ 'POST', '/api/user', null, [
                                       'login' => self::randomUser("user4"),
                                       'firstname' => "Greg",
                                       'lastname' => '',
                                       'email' => self::randomEmail("user4")
                                       ], null ],
         [],
         [],
         [
          'key' => 'lastname',
          'message' => 'Lastname required'
          ]
         ],

       [ "Update with empty last name",
         [ 'PUT', '/api/user/${first-user-id}', null, [
                                                       'firstname' => "Greg",
                                                       'lastname' => ''
                                                       ], null ],
         [],
         [],
         [
          'key' => 'lastname',
          'message' => 'Lastname required'
          ]
         ],

       [ "Update same fields",
         [ 'PUT', '/api/user/${first-user-id}', null,
           [
            'login' => self::randomUser("user1"),
            'firstname' => 'George',
            'lastname' => 'Jonas',
            'email' => self::randomEmail("user1") ]
           ],
         [],
         []
         ],

       [ "Update duplicate login",
         [ 'PUT', '/api/user/${first-user-id}', null,
           [
            'login' => 'admin',
            'firstname' => 'George',
            'lastname' => 'Jonas'
            ]
           ],
         [],
         [],
         [
          'key' => 'login',
          'message' => 'Login must be unique'
          ]
         ],


       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'Delete the user', [ 'DELETE', '/api/user/${first-user-id}' ]],

       [ "logout", [ 'POST', '/api/logout', [], null, null ]]
       ];
  }
}
