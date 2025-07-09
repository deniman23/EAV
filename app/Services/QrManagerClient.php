<?php

namespace App\Services;

use GuzzleHttp\Client;
use GuzzleHttp\Exception\GuzzleException;
use Brick\Math\BigDecimal;
use Illuminate\Support\Facades\Log;

/**
 * Минимальный HTTP-клиент для работы с сервисом QR-manager.
 * Логика: создаёт счёт под заказ и возвращает массив с данными QR.
 */
class QrManagerClient
{
    private Client $http;
    private string $apiKey;
    private string $baseUrl;

    public function __construct(?Client $client = null)
    {
        $this->http    = $client ?? new Client([
            'timeout'         => 2.5,
            'connect_timeout' => 1.0,
            'http_errors'     => false,
        ]);
        $this->apiKey  = config('services.qrmanager.key');
        $this->baseUrl = rtrim(config('services.qrmanager.base_url', 'https://api.qrmanager.ru/v1'), '/');
    }

    /**
     * Создать инвойс в QR-manager.
     *
     * @param string     $orderId  Идентификатор заказа в нашей системе
     * @param BigDecimal $amount   Сумма заказа
     * @param string     $currency Код валюты (ISO-4217)
     *
     * @return array [qr_url, invoice_id]
     */
    public function createInvoice(string $orderId, BigDecimal $amount, string $currency): array
    {
        $payload = [
            'order_id' => $orderId,
            'amount'   => $amount->toFloat(),
            'currency' => $currency,
        ];

        try {
            $response = $this->http->post($this->baseUrl . '/invoice', [
                'headers' => [
                    'X-Api-Key'     => $this->apiKey,
                    'Accept'        => 'application/json',
                    'Content-Type'  => 'application/json',
                ],
                'json'    => $payload,
            ]);

            $body = json_decode($response->getBody()->getContents(), true);

            if ($response->getStatusCode() >= 300 || ! isset($body['qr_url'], $body['invoice_id'])) {
                throw new \RuntimeException('QR Manager error: ' . $response->getStatusCode());
            }

            return $body;
        } catch (GuzzleException $e) {
            Log::channel('orders_log')->error('QR Manager HTTP error', [
                'order_id' => $orderId,
                'message'  => $e->getMessage(),
            ]);
            throw $e;
        }
    }

    public function checkStatus(string $invoiceId): array
    {
        try {
            $response = $this->http->get($this->baseUrl . '/invoice/' . urlencode($invoiceId), [
                'headers' => [
                    'X-Api-Key'    => $this->apiKey,
                    'Accept'       => 'application/json',
                ],
            ]);
            $body = json_decode($response->getBody()->getContents(), true);
            if ($response->getStatusCode() >= 300 || ! isset($body['status'])) {
                throw new \RuntimeException('QR Manager status error');
            }
            return $body;
        } catch (GuzzleException $e) {
            Log::error('QR Manager status HTTP error', [
                'invoice_id' => $invoiceId,
                'msg'        => $e->getMessage(),
            ]);
            throw $e;
        }
    }
}