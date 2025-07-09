<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void
    {
        Schema::table('orders', function (Blueprint $table) {
            $table->string('qr_invoice_id')->nullable()->after('status');
            $table->string('qr_url')->nullable()->after('qr_invoice_id');
            $table->index('qr_invoice_id');
        });
    }

    public function down(): void
    {
        Schema::table('orders', function (Blueprint $table) {
            $table->dropIndex(['qr_invoice_id']);
            $table->dropColumn(['qr_invoice_id', 'qr_url']);
        });
    }
};