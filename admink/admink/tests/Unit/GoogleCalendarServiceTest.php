<?php

namespace Tests\Unit;

use Tests\TestCase;
use App\Services\GoogleCalendarService;
use GuzzleHttp\Client;
use GuzzleHttp\Psr7\Response;
use GuzzleHttp\Handler\MockHandler;
use GuzzleHttp\HandlerStack;
use Illuminate\Support\Facades\Log;

class GoogleCalendarServiceTest extends TestCase
{
    protected function getMockedClient($responses)
    {
        $mock = new MockHandler($responses);
        $handlerStack = HandlerStack::create($mock);
        return new Client(['handler' => $handlerStack]);
    }

    public function test_deve_sincronizar_evento_com_sucesso()
    {
        $agendamento = (object) [
            'data_horario_inicio' => new \DateTime('2026-06-05 10:00:00'),
            'data_horario_fim' => new \DateTime('2026-06-05 11:00:00'),
            'orcamento' => (object) ['tatuagem_nome' => 'Teste tatuagem'],
        ];

        $mockClient = $this->getMockedClient([
            new Response(200, [], json_encode(['access_token' => 'token_teste'])),
            new Response(200, [], json_encode(['status' => 'confirmed']))
        ]);

        $service = new GoogleCalendarService($mockClient);
        $result = $service->sync($agendamento);
        
        $this->assertTrue($result, 'A sincronização deve retornar true para sucesso');
    }

    public function test_deve_retornar_falso_se_api_google_falhar()
    {
        Log::shouldReceive('error')->once();

        $agendamento = (object) [
            'data_horario_inicio' => new \DateTime('2026-06-05 10:00:00'),
            'data_horario_fim' => new \DateTime('2026-06-05 11:00:00'),
            'orcamento' => (object) ['tatuagem_nome' => 'Teste tatuagem'],
        ];

        $mockClient = $this->getMockedClient([
            new Response(500, [], 'Internal Server Error')
        ]);

        $service = new GoogleCalendarService($mockClient);
        $result = $service->sync($agendamento);
        
        $this->assertFalse($result, 'A sincronização deve retornar false para falha');
    }
}