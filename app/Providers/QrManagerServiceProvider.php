<?php

namespace App\Providers;

use App\Services\QrManagerClient;
use Illuminate\Support\ServiceProvider;

class QrManagerServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        // Регистрируем клиент как singleton
        $this->app->singleton(QrManagerClient::class, function ($app) {
            return new QrManagerClient();
        });
    }
}