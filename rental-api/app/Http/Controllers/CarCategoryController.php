<?php

namespace App\Http\Controllers;

use App\Http\Resources\CarCategoryResource;
use App\Models\CarCategory;
use App\Http\Requests\StoreCarCategoryRequest;
use App\Http\Requests\UpdateCarCategoryRequest;
use Illuminate\Http\Request;

class CarCategoryController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $categories = CarCategory::all();
        return new CarCategoryResource(true, 'Cart Category List', $categories);
    }

    /**
     * Show the form for creating a new resource.
     */
    // public function create(Request $request)
    // {

    // }

    /**
     * Store a newly created resource in storage.
     */
    public function store(StoreCarCategoryRequest $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
        ]);

        $category = CarCategory::create([
            'name' => $request->name,
        ]);

        return response()->json([
            'message' => 'Car category created successfully',
            'category' => $category,
        ], 201);
        
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $category = CarCategory::find($id);

        if (!$category) {
            return response()->json(['message' => 'Category not found'], 404);
        }

        return response()->json($category);
    }

    /**
     * Show the form for editing the specified resource.
     */
    // public function edit(CarCategory $carCategory)
    // {
    //     //
    // }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $category = CarCategory::find($id);

        if (!$category) {
            return response()->json(['message' => 'Category not found'], 404);
        }

        $request->validate([
            'name' => 'required|string|max:255',
        ]);

        $category->update([
            'name' => $request->name,
        ]);

        return response()->json([
            'message' => 'Category updated successfully',
            'category' => $category,
        ]);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        $category = CarCategory::find($id);

        if (!$category) {
            return response()->json(['message' => 'Category not found'], 404);
        }

        $category->delete();

        return response()->json(['message' => 'Category deleted successfully']);
    }
}
