w<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateCarsTable extends Migration
{
    public function up()
    {
        Schema::create('cars', function (Blueprint $table) {
            $table->id();
            $table->string('brand'); // Merek mobil (contoh: Toyota, Honda)
            $table->string('model'); // Model mobil (contoh: Avanza, Civic)
            $table->integer('year'); // Tahun produksi
            $table->string('color'); // Warna mobil
            $table->integer('seats'); // Jumlah tempat duduk
            $table->unsignedBigInteger('category_id'); // Kategori mobil
            $table->string('license_plate')->unique(); // Nomor plat
            $table->decimal('price_per_day', 8, 2); // Harga sewa per hari
            $table->boolean('available')->default(true); // Ketersediaan mobil
            $table->string('image')->nullable(); // URL gambar mobil
            $table->enum('fuel_type', ['petrol', 'diesel', 'electric', 'hybrid'])->default('petrol'); // Jenis bahan bakar
            $table->enum('transmission', ['manual', 'automatic'])->default('manual'); // Tipe transmisi
            $table->integer('mileage')->default(0); // Kilometer atau jarak tempuh
            $table->date('last_service_date')->nullable(); // Tanggal servis terakhir
            $table->text('condition_notes')->nullable(); // Catatan kondisi mobil
            $table->string('vin')->nullable(); // Nomor rangka (VIN)
            $table->boolean('in_maintenance')->default(false); // Status pemeliharaan
            $table->boolean('insured')->default(true); // Status asuransi mobil
            $table->text('description')->nullable(); // Deskripsi singkat mobil
            $table->timestamps();

            // Foreign key untuk kategori mobil
            $table->foreign('category_id')->references('id')->on('car_categories')->onDelete('cascade');
        });
    }

    public function down()
    {
        Schema::dropIfExists('cars');
    }
}
