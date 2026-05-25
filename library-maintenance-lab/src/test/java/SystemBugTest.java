import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SystemBugTest {

    // Passava com o titulo em branco
    @Test
    public void testRegisterBookComTituloEmBranco_DeveLancarExcecao(){
        BookManager bookManager = new BookManager();
        assertThrows(IllegalArgumentException.class, () -> {
            bookManager.registerBook("", "Autor", 2026, "IT", 5, 5, "A1", "12345");
        });
    }

    //Ele retornava sem avisar se nao existisse o emprestimo
    @Test
    public void testReturnBookInexistente_DeveLancarExcecao(){
        LoanManager loanManager = new LoanManager();
        assertThrows(IllegalArgumentException.class, () -> {
            loanManager.returnBook(-999, "2026-05-20", "email", 0, "test", "handler");
        });
    }

    //Emprestimo passava com o usuario nulo ou vazio
    @Test
    public void testBorrowBookComUsuarioInexistente_DeveLancarExcecao(){
        LoanManager loanManager = new LoanManager();
        assertThrows(IllegalArgumentException.class, () ->{
            loanManager.borrowBook(-1, 1, "2026-05-01", "2026-05-15", "email", 14, "cli", 0);
        });
    }
}

/// 1. Introdução
/// Olá a todos, meu nome é Alexs, e este vídeo é a apresentação da Atividade 3 da disciplina de Manutenção de Software.
/// O objetivo deste atividade foi refatorar um sistema legado de uma biblioteca, identificando falhas de validação,
/// aplicando o conceito de Fail Fast e adicionando observabilidade com o Log4j."
///
/// 2. O Fluxo de Trabalho e a Gestão da Issue
///
/// Todo o trabalho foi guiado por uma Issue no GitHub. Antes de alterar o código da aplicação, o primeiro
/// passo foi isolar os três bugs principais utilizando testes unitários com JUnit. A ideia foi provar que o sistema estava falhando antes de aplicar a correção.
///
/// 3. A Prova do Crime
///
/// Como podemos ver na suíte de testes, mapeamos três situações críticas:
/// Primeiro, o BookManager permitia o cadastro de livros com o título em branco.
/// Segundo, o LoanManager tinha uma falha silenciosa, retornando sem avisar caso não encontrasse um empréstimo.
/// Terceiro, o sistema permitia iniciar um empréstimo com um usuário ou livro nulo.
/// Rodando os testes contra o código original, recebemos a barra vermelha, provando que o sistema não estava
/// blindado contra essas entradas inválidas."
///
/// 4. A Solução: Fail Fast e Log4j
///
/// "Para resolver isso, refatoramos os métodos implementando a Programação por Contrato. No início dos métodos afetados,
/// adicionamos a regra de Fail Fast. Agora, se um parâmetro for nulo ou inválido, o sistema interrompe o fluxo imediatamente
/// lançando uma IllegalArgumentException.
/// Além disso, integramos o Log4j para garantir a observabilidade. Sucessos são registrados como INFO,
/// e quebras de contrato disparam um log de ERROR, facilitando a rastreabilidade do problema."
///
/// 5. A Validação
///
/// Após aplicar essas proteções, rodamos os testes novamente e, como esperado, obtivemos a barra verde.
/// O sistema agora lança as exceções corretas.
/// Aqui no terminal, podemos confirmar a observabilidade em ação: o Log4j capturou e formatou perfeitamente
/// os nossos erros de validação e as stack traces, provando que o sistema agora não falha mais de forma silenciosa.
///
/// 6. Conclusão
/// Por fim, o código foi versionado utilizando o padrão Conventional Commits,
/// e o push fechou automaticamente a nossa Issue no quadro Kanban. Com isso,
/// garantimos um código mais seguro, rastreável e fácil de manter. Obrigado!"