<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CarController;
use App\Http\Controllers\Auth\AuthController;
use App\Http\Controllers\CarFeatureController;
use App\Http\Controllers\CarCategoryController;

Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);


Route::post('forgot-password', [AuthController::class, 'forgotPassword']);
Route::post('verify-token', [AuthController::class, 'verifyToken']);
Route::post('reset-password', [AuthController::class, 'resetPassword'])->name('password.reset');


// auth 

Route::apiResource('car-categories', CarCategoryController::class);
Route::apiResource('car-features', CarFeatureController::class);
Route::apiResource('/cars', CarController::class);


Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [AuthController::class, 'logout']);
});
