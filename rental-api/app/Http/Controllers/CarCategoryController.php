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

    public function store(StoreCarCategoryRequest $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
        ]);

        $category = CarCategory::create([
            'name' => $request->name,
        ]);

        return response()->json([
            'error' => false,
            'success' => 1,
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
