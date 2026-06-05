<?php

Route::get('/', function () {
    return view('welcome');
});

// Grupo protegido por autenticação
Route::group(['middleware' => 'auth', 'middleware' => 'check.estudio'], function () {

    Route::prefix('admin')->name('admin.')->namespace('Admin')->group(function () {
        Route::get('/home', 'HomeController@index')->name('home');
        Route::get('/home/estudio/{id_estudio}', 'HomeController@estudio')->name('home.estudio');

        Route::resource('/clientes', 'ClienteController');
        Route::resource('/artistas', 'ArtistaController');
        Route::resource('/estacoes', 'EstacaoController')->parameters(['estacoes' => 'estacao']);
        Route::resource('/orcamentos', 'OrcamentoController');
        
        Route::get('/orcamentos/{orcamento}/cancelar/', 'OrcamentoController@cancelar')->name('orcamentos.cancelar');
        Route::get('/orcamentos/{orcamento}/recuperar/', 'OrcamentoController@recuperar')->name('orcamentos.recuperar');
        
        Route::resource('/agendamentos', 'AgendamentoController');

        Route::get('/agendamentos/{agendamento}/finalizar/', 'AgendamentoController@finalizar')->name('agendamentos.finalizar');
        Route::get('/agendamentos/{agendamento}/cancelar/', 'AgendamentoController@cancelar')->name('agendamentos.cancelar');

        Route::get('/downloadPDF/{id}','AgendamentoController@downloadPDF')->name('downloadPDF');
    });
});

Route::get('/teste-api', function () {
    try {
        $service = new \App\Services\GoogleCalendarService();
        $service->createEvent(env('GOOGLE_CALENDAR_ID'), [
            'title' => 'Teste de Agendamento via API',
            'start' => date('Y-m-d\TH:i:s', strtotime('+1 hour')),
            'end' => date('Y-m-d\TH:i:s', strtotime('+2 hour')),
        ]);
        return "Evento criado com sucesso no Google Calendar!";
    } catch (\Exception $e) {
        return "Erro: " . $e->getMessage();
    }
});

Auth::routes(['register' => false, 'reset' => false]);