
<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateRentalsTable extends Migration
{
    public function up()
    {
        Schema::create('rentals', function (Blueprint $table) {
            $table->id();
            $table->unsignedBigInteger('customer_id');
            $table->unsignedBigInteger('car_id');
            $table->timestamp('rental_date')->nullable();
            $table->timestamp('return_date')->nullable();
            $table->timestamp('actual_return_date')->nullable();
            $table->timestamp('extended_return_date')->nullable(); // Tanggal pengembalian setelah perpanjangan
            $table->decimal('total_price', 10, 2);
            $table->decimal('extension_fee', 10, 2)->nullable(); // Biaya perpanjangan sewa
            $table->decimal('penalty', 10, 2)->nullable();
            $table->decimal('late_fee_per_hour', 10, 2)->nullable(); // Biaya keterlambatan per jam
            $table->decimal('deposit_amount', 10, 2)->nullable(); // Jumlah deposit
            $table->enum('insurance_status', ['none', 'basic', 'premium'])->default('none'); // Status asuransi
            $table->string('pickup_location')->nullable(); // Lokasi penjemputan
            $table->string('dropoff_location')->nullable(); // Lokasi pengembalian
            $table->enum('fuel_level_at_pickup', ['empty', 'quarter', 'half', 'three_quarters', 'full'])->default('full'); // Level bahan bakar saat pickup
            $table->enum('fuel_level_at_return', ['empty', 'quarter', 'half', 'three_quarters', 'full'])->nullable(); // Level bahan bakar saat return
            $table->text('damage_report')->nullable(); // Laporan kerusakan mobil
            $table->enum('status', ['pending', 'ongoing', 'completed', 'cancelled']);
            $table->timestamps();

            $table->foreign('customer_id')->references('id')->on('customers')->onDelete('cascade');
            $table->foreign('car_id')->references('id')->on('cars')->onDelete('cascade');
        });
    }

    public function down()
    {
        Schema::dropIfExists('rentals');
    }
}
