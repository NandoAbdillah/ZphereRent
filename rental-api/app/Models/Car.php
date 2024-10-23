<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Car extends Model
{
    use HasFactory;
    protected $fillable = [
        'brand',
        'model',
        'year',
        'color',
        'seats',
        'category_id',
        'license_plate',
        'price_per_day',
        'available',
        'image',
        'fuel_type',
        'transmission',
        'mileage',
        'last_service_date',
        'condition_notes',
        'vin',
        'in_maintenance',
        'insured',
        'description'
    ];
}
