<?php

return [
    'oracle' => [
        'driver'         => 'oracle',
        'host'           => env('DB_HOST', 'oracle-db'),
        'port'           => env('DB_PORT', '1521'),
        'database'       => env('DB_DATABASE', 'XEPDB1'),
        'username'       => env('DB_USERNAME', 'jorju'),
        'password'       => env('DB_PASSWORD', '20022002'),
        'charset'        => env('DB_CHARSET', 'AL32UTF8'),
        'prefix'         => env('DB_PREFIX', ''),
        'prefix_schema'  => env('DB_SCHEMA_PREFIX', ''),
    ],
    
];
