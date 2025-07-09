<?php

namespace App\Jobs;

use App\Models\Order;
use App\Services\QrManagerClient;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Facades\Log;

class CheckQrInvoiceJob implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    public function __construct(public int $orderId, public string $invoiceId) {}

    public function handle(QrManagerClient $qr): void
    {
        $order = Order::find($this->orderId);
        if (! $order) {
            return;
        }

        try {
            $resp = $qr->checkStatus($this->invoiceId);
            if (($resp['status'] ?? null) === 'paid') {
                // подтверждение платежа
                $order->update(['status' => Order::STATUS_PAID]);
            } elseif (($resp['status'] ?? null) === 'expired') {
                $order->update(['status' => Order::STATUS_EXPIRED]);
            } else {
                // если ещё не оплачено – повторный запрос через delay
                self::dispatch($this->orderId, $this->invoiceId)->delay(now()->addSeconds(15));
            }
        } catch (\Throwable $e) {
            Log::error('QR status check failed', [
                'order_id'  => $this->orderId,
                'invoice_id'=> $this->invoiceId,
                'err'       => $e->getMessage(),
            ]);
            self::dispatch($this->orderId, $this->invoiceId)->delay(now()->addSeconds(30));
        }
    }
}