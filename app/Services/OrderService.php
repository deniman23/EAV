<?php

namespace App\Services;

use App\Jobs\LogRequestJob;
use App\Models\Order;
use App\Services\FinanceService;
use App\Services\CallsService;
use App\Services\CascadeService;
use App\Services\IntegrationService;
use App\Services\RiskManagementService;
use App\Services\QrManagerClient;
use Brick\Math\BigDecimal;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Str;

class OrderService
{
    // Кэш запроса
    private static array $requestCache = [];

    public function __construct(
        protected FinanceService         $financeService,
        protected CallsService           $callsService,
        protected CascadeService         $cascadeService,
        protected IntegrationService     $integrationService,
        protected RiskManagementService  $riskService,
        protected QrManagerClient        $qrClient,
    ) {}

    /**
     * Создание заказа с полной логикой
     */
    public function create(CreateOrderRequest $request): JsonResponse
    {
        $startTime = microtime(true);
        $traceId   = $request->header('X-TraceId') ?? Str::uuid();

        // ... данные из запроса (merchant, currency, etc.) ...
        $sum      = $request->input('sum');
        $orderSum = BigDecimal::of($sum);

        // ... риск-менеджмент, проверка существующего заказа и сама транзакция ...
        try {
            // Здесь предполагается, что $orderData содержит будущий ответ API,
            // а $orderModel — сохранённую модель Order.
            // После успешного сохранения заказа:
            // ----------------------------------------------------

            // QR-код через QR-manager (fail-soft)
            try {
                $qr = $this->qrClient->createInvoice(
                    (string) $orderModel->id,
                    $orderSum,
                    $currency->code,
                );

                $orderModel->update([
                    'qr_invoice_id' => $qr['invoice_id'],
                    'qr_url'        => $qr['qr_url'],
                ]);

                $orderData['qr_url'] = $qr['qr_url'];
            } catch (\Throwable $e) {
                Log::channel('orders_log')->warning("[{$traceId}] -> QR Manager unavailable", [
                    'order_id' => $orderModel->id ?? null,
                    'message'  => $e->getMessage(),
                ]);
            }

            // ... фоновые задачи, логирование запроса ...

            return response()->json($orderData, 201);
        } catch (\Throwable $e) {
            // ... обработка ошибок ...
            return response()->json(['error' => 'Order creation error'], 500);
        }
    }
}