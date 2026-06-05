<?php
// Tenta ler o arquivo JSON de credenciais
$path = __DIR__ . '/../storage/credentials/google-calendar-credentials.json';

if (file_exists($path)) {
    $json = file_get_contents($path);
    $data = json_decode($json, true);
    
    if (isset($data['client_email'])) {
        echo "Sucesso! O sistema encontrou o arquivo e leu o e-mail: " . $data['client_email'];
    } else {
        echo "Erro: O arquivo existe, mas não parece ter a estrutura JSON correta.";
    }
} else {
    echo "Erro: Arquivo não encontrado em " . $path;
}