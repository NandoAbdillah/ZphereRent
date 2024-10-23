<?php

namespace App\Http\Controllers;

use App\Models\Car;
use Illuminate\Http\Request;
use App\Http\Resources\CarResource;
use App\Http\Requests\StoreCarRequest;
use App\Http\Requests\UpdateCarRequest;
use Illuminate\Support\Facades\Validator;

class CarController extends Controller
{
    // Create Car
    public function store(Request $request)
    {
        // Validasi input
        $validator = Validator::make($request->all(), [
            'brand' => 'required|string|max:255',
            'model' => 'required|string|max:255',
            'year' => 'required|integer|min:1900|max:' . date('Y'),
            'color' => 'required|string|max:255',
            'seats' => 'required|integer|min:1',
            'category_id' => 'required|exists:car_categories,id',
            'license_plate' => 'required|string|unique:cars',
            'price_per_day' => 'required|numeric|min:0',
            'available' => 'boolean',
            'image' => 'nullable|string',
            'fuel_type' => 'required|in:petrol,diesel,electric,hybrid',
            'transmission' => 'required|in:manual,automatic',
            'mileage' => 'integer|min:0',
            'last_service_date' => 'nullable|date',
            'condition_notes' => 'nullable|string',
            'vin' => 'nullable|string|unique:cars',
            'in_maintenance' => 'boolean',
            'insured' => 'boolean',
            'description' => 'nullable|string',
        ]);

        // Jika validasi gagal, kirim respon error
        if ($validator->fails()) {
            return response()->json([
                'error' => true,
                'success' => 0,
                'message' => $validator->errors()->first(),
            ], 400);
        }

        // Buat data baru
        $car = Car::create($request->all());

        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Car created successfully',
            'car' => $car,
        ], 201);
    }

    // Get all cars
    public function index()
    {
        $cars = Car::all();
        return response()->json($cars, 200);
    }

    // Get single car by ID
    public function show($id)
    {
        $car = Car::find($id);
        if (!$car) {
            return response()->json(['message' => 'Car not found'], 404);
        }
        return response()->json($car, 200);
    }

    // Update car by ID
    public function update(Request $request, $id)
    {
        $car = Car::find($id);
        if (!$car) {
            return response()->json(['message' => 'Car not found'], 404);
        }

        // Validasi input
        $validator = Validator::make($request->all(), [
            'brand' => 'sometimes|string|max:255',
            'model' => 'sometimes|string|max:255',
            'year' => 'sometimes|integer|min:1900|max:' . date('Y'),
            'color' => 'sometimes|string|max:255',
            'seats' => 'sometimes|integer|min:1',
            'category_id' => 'sometimes|exists:car_categories,id',
            'license_plate' => 'sometimes|string|unique:cars,license_plate,' . $car->id,
            'price_per_day' => 'sometimes|numeric|min:0',
            'available' => 'boolean',
            'image' => 'nullable|string',
            'fuel_type' => 'sometimes|in:petrol,diesel,electric,hybrid',
            'transmission' => 'sometimes|in:manual,automatic',
            'mileage' => 'integer|min:0',
            'last_service_date' => 'nullable|date',
            'condition_notes' => 'nullable|string',
            'vin' => 'nullable|string|unique:cars,vin,' . $car->id,
            'in_maintenance' => 'boolean',
            'insured' => 'boolean',
            'description' => 'nullable|string',
        ]);

        // Jika validasi gagal, kirim respon error
        if ($validator->fails()) {
            return response()->json([
                'error' => true,
                'success' => 0,
                'message' => $validator->errors()->first(),
            ], 400);
        }

        // Update data mobil
        $car->update($request->all());

        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Car updated successfully',
            'car' => $car,
        ], 200);
    }

    // Delete car by ID
    public function destroy($id)
    {
        $car = Car::find($id);
        if (!$car) {
            return response()->json(['message' => 'Car not found'], 404);
        }

        $car->delete();
        return response()->json(['message' => 'Car deleted successfully'], 200);
    }
}
