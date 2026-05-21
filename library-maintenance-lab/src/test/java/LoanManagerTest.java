import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LoanManagerTest {

    @Before
    public void resetLegacyDatabase() {
        LegacyDatabase.getBooks().clear();
        LegacyDatabase.getUsers().clear();
        LegacyDatabase.getLoans().clear();
        LegacyDatabase.getLogs().clear();
        LegacyDatabase.BOOK_SEQ = 1;
        LegacyDatabase.USER_SEQ = 1;
        LegacyDatabase.LOAN_SEQ = 1;
        LegacyDatabase.seedInitialData();
    }

    @Test
    public void deveCalcularMultaPadraoQuandoHouverAtraso() {
        LoanManager loanManager = new LoanManager();

        double fine = loanManager.calculateFineLegacy("2026-05-01", "2026-05-02", 0, "teste", "helper", 1, 2);

        assertEquals(2.0, fine, 0.0001);
    }

    @Test
    public void deveRetornarZeroQuandoNaoHouverAtraso() {
        LoanManager loanManager = new LoanManager();

        double fine = loanManager.calculateFineLegacy("2026-05-10", "2026-05-10", 0, "teste", "helper", 1, 2);

        assertEquals(0.0, fine, 0.0001);
    }

    @Test
    public void deveCriarApenasUmEmprestimoQuandoCanalForSms() {
        LoanManager loanManager = new LoanManager();

        loanManager.borrowBook(1, 1, "2026-05-01", "2026-05-10", "sms", 9, "teste", 0);

        assertEquals(1, LegacyDatabase.getLoans().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveFalharRapidoAoDevolverEmprestimoInexistente() {
        LoanManager loanManager = new LoanManager();

        loanManager.returnBook(999, "2026-05-10", "email", 0, "teste", "handler");
    }

    @Test
    public void deveAumentarDividaDoUsuarioQuandoDevolucaoTemMulta() {
        LoanManager loanManager = new LoanManager();
        int loanId = loanManager.borrowBook(1, 1, "2026-05-01", "2026-05-02", "email", 1, "teste", 0);

        loanManager.returnBook(loanId, "2026-05-03", "email", 0, "teste", "handler");

        Map<String, Object> user = LegacyDatabase.getUserById(1);
        assertEquals(2.0, ((Double) user.get("debt")).doubleValue(), 0.0001);
    }
}
