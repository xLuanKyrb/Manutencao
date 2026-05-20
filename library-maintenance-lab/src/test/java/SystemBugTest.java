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
