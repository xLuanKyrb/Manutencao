import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LegacyDatabaseTest {

    @Test
    public void testCountOpenLoansByBook_ShouldReturnCorrectCount (){
        LegacyDatabase.books.clear();
        LegacyDatabase.loans.clear();

        int bookId = 100;

        LegacyDatabase.addLoanData(bookId, 1, "2026-05-01", "2026-05-15", null, "OPEN", 0.0, "");

        int count = LegacyDatabase.countOpenLoansByBook(bookId);

        assertEquals("Deveria contar exatamente 1 empréstimo aberto para o livro 100", 1, count);
    }
}
