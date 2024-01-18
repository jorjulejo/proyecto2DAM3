<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Http\Response;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/

Route::get('/', function () {
    try {
        $conn = oci_connect(env('DB_USERNAME'), env('DB_PASSWORD'), env('DB_HOST') . ':' . env('DB_PORT') . '/' . env('DB_DATABASE'));
        if (!$conn) {
            throw new Exception('Error de conexión con la base de datos Oracle.');
        }
        oci_close($conn);
        return new Response('Conexión exitosa con la base de datos Oracle.', 200);
    } catch (Exception $e) {
        return new Response('Error al conectar con la base de datos Oracle: ' . $e->getMessage(), 500);
    }
});
