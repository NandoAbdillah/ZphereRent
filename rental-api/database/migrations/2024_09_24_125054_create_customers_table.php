<?php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateCustomersTable extends Migration
{
    public function up()
    {
        Schema::create('customers', function (Blueprint $table) {
            $table->id();
            $table->string('name'); // Nama customer
            $table->string('email')->unique(); // Email customer
            $table->string('phone'); // Nomor telepon
            $table->string('address'); // Alamat lengkap
            $table->string('identity_number')->unique(); // NIK atau nomor identitas lain
            $table->date('date_of_birth'); // Tanggal lahir
            $table->string('profile')->nullable();
            $table->enum('gender', ['male', 'female']); // Jenis kelamin
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('customers');
    }
}
