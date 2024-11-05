<?php

namespace App\Http\Controllers;

use App\Models\CarFeature;
use App\Http\Requests\StoreCarFeatureRequest;
use App\Http\Requests\UpdateCarFeatureRequest;
use Illuminate\Http\Request;

class CarFeatureController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $features = CarFeature::all();
        return response()->json($features);
    }

    public function store(Request $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'description' => 'nullable|string',
        ]);

        $feature = CarFeature::create([
            'name' => $request->name,
            'description' => $request->description,
        ]);

        return response()->json([
            'error' => false,
            'success' => 1,
            'message' => 'Car feature created successfully',
            'feature' => $feature,
        ], 201);
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $feature = CarFeature::find($id);

        if (!$feature) {
            return response()->json(['message' => 'Feature not found'], 404);
        }

        return response()->json($feature);
    }

    /**
     * Show the form for editing the specified resource.
     */
    // public function edit(CarFeature $carFeature)
    // {
    //     //
    // }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $feature = CarFeature::find($id);

        if (!$feature) {
            return response()->json(['message' => 'Feature not found'], 404);
        }

        $request->validate([
            'name' => 'required|string|max:255',
            'description' => 'nullable|string',
        ]);

        $feature->update([
            'name' => $request->name,
            'description' => $request->description,
        ]);

        return response()->json([
            'message' => 'Feature updated successfully',
            'feature' => $feature,
        ]);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        $feature = CarFeature::find($id);

        if (!$feature) {
            return response()->json(['message' => 'Feature not found'], 404);
        }

        $feature->delete();

        return response()->json(['message' => 'Feature deleted successfully']);
    }
}
