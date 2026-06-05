<?php

namespace App\Services;

use GuzzleHttp\Client;
use Illuminate\Support\Facades\Log;

class GoogleCalendarService
{
    protected $client;

    public function __construct(Client $client = null)
    {
        $this->client = $client ?: new Client();
    }

    private function base64UrlEncode($data)
    {
        return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
    }

    public function sync($agendamento)
    {
        try {
            $path = env('GOOGLE_CREDENTIALS_PATH', 'credentials/google-calendar-credentials.json');
            $jsonPath = storage_path($path);
            
            if (!file_exists($jsonPath)) {
                throw new \Exception("Arquivo de credenciais não encontrado.");
            }

            $keyFile = json_decode(file_get_contents($jsonPath), true);
            
            // 1. Gera o Token JWT nativamente (Agora com o relógio correto!)
            $agora = time(); 
            
            $header = json_encode(['alg' => 'RS256', 'typ' => 'JWT']);
            $payload = json_encode([
                'iss' => $keyFile['client_email'],
                'scope' => 'https://www.googleapis.com/auth/calendar',
                'aud' => 'https://oauth2.googleapis.com/token',
                'exp' => $agora + 3600,
                'iat' => $agora
            ]);

            $base64UrlHeader = $this->base64UrlEncode($header);
            $base64UrlPayload = $this->base64UrlEncode($payload);

            $signature = '';
            openssl_sign($base64UrlHeader . "." . $base64UrlPayload, $signature, $keyFile['private_key'], OPENSSL_ALGO_SHA256);
            $jwt = $base64UrlHeader . "." . $base64UrlPayload . "." . $this->base64UrlEncode($signature);

            // 2. Troca JWT por Token de Acesso usando o Guzzle
            $authResponse = $this->client->post('https://oauth2.googleapis.com/token', [
                'form_params' => [
                    'grant_type' => 'urn:ietf:params:oauth:grant-type:jwt-bearer',
                    'assertion' => $jwt
                ]
            ]);
            
            $accessToken = json_decode($authResponse->getBody(), true)['access_token'];

            // 3. Monta o evento e envia para a sua agenda
            $titulo = 'Sessão: ' . ($agendamento->orcamento->tatuagem_nome ?? 'Tatuagem');
            $calendarId = env('GOOGLE_CALENDAR_ID');

            $this->client->post("https://www.googleapis.com/calendar/v3/calendars/{$calendarId}/events", [
                'headers' => ['Authorization' => 'Bearer ' . $accessToken],
                'json' => [
                    'summary' => $titulo,
                    'start' => [
                        'dateTime' => $agendamento->data_horario_inicio->format('Y-m-d\TH:i:s'),
                        'timeZone' => 'America/Sao_Paulo' // <-- Fuso horário adicionado!
                    ],
                    'end'   => [
                        'dateTime' => $agendamento->data_horario_fim->format('Y-m-d\TH:i:s'),
                        'timeZone' => 'America/Sao_Paulo' // <-- Fuso horário adicionado!
                    ]
                ]
            ]);
            
            return true;

        } catch (\Exception $e) {
            Log::error('Erro ao sincronizar com Google Calendar: ' . $e->getMessage());
            return false;
        }
    }
}