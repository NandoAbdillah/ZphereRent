<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class CarResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public $status;
    public $error;
    public $message;
    public $resource;

    public function __construct($error,$status, $message, $resource)
    {
        parent::__construct($resource);

        
        $this->error = $error;
        $this->message = $message;
        $this->status = $status;


    }

    public function toArray($request):array 
    {
        return[
            'error' => $this->error,
            'success' => $this->status,
            'message' => $this->message,
            'data' => $this->resource,
        ];
    }
}
