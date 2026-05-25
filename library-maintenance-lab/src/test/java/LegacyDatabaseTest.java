import org.junit.Test;

import static org.junit.Assert.*;

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

    @Test
    public void testAddUserDataShouldMaintainStatusFieldCorrectly(){
        LegacyDatabase.users.clear();
        String statusOriginal = "ACTIVE";

        int userId = LegacyDatabase.addUserData(
                "Ana", "ana@mail", "1111-1111", "student", "Maringa", "DOC-1", statusOriginal
        );

        java.util.Map<String, Object> user = LegacyDatabase.getUserById(userId);

        org.junit.Assert.assertNotNull(user);

        assertEquals("O status do usuário deveria ter sido salvo corretamente", statusOriginal, user.get("status"));
    }

    @Test
    public void testAddLoanData_ShouldMaintainUserIdFieldCorrectly (){
        LegacyDatabase.loans.clear();
        int bookId = 10;
        int userIdEsperado = 42;

        LegacyDatabase.addLoanData(bookId, userIdEsperado, "2026-05-01", "2026-05-15", null, "OPEN", 0.0, "Refatoraçao");
        java.util.Map<String, Object> loan = LegacyDatabase.getLoans().get(0);
        org.junit.Assert.assertNotNull(loan);
        assertEquals("O userId do empréstimo deveria ter sido salvo e recuperado corretamente", userIdEsperado, ((Integer) loan.get("userId")).intValue());
    }

    @Test
    public void testConstructorIsPrivate() throws Exception{
        java.lang.reflect.Constructor<?>[] constructors = LegacyDatabase.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        java.lang.reflect.Constructor<?> constructor = constructors[0];
        assertFalse("O construtor deveria ser privado", constructor.isAccessible());

    }
}
