<?php
// Tenta carregar o autoloader do Laravel
require 'vendor/autoload.php';

try {
    // Verifica se a classe do Google Client existe
    if (class_exists('Google\Client')) {
        echo "Sucesso! A biblioteca do Google foi encontrada e carregada.";
    } else {
        echo "Erro: A classe Google\Client não foi encontrada.";
    }
} catch (Exception $e) {
    echo "Erro inesperado: " . $e->getMessage();
}