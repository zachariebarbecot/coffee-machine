package unit;

import com.zbar.kata.coffeemachine.adapters.InMemoryReportRepository;
import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.ports.ReportRepository;
import com.zbar.kata.coffeemachine.usecases.DisplayReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisplayReportTest {

    @Mock
    PrintStream out;
    private final ReportRepository repository = spy(new InMemoryReportRepository());

    @BeforeEach
    public void init() {
        System.setOut(out);
    }

    @Test
    public void shouldDisplayEmptyReport() {
        displayReport();
        verifyReport(0, 0, 0, 0, "0");
    }

    @Test
    public void shouldDisplayOneCoffeeReport() {
        given(DrinkType.COFFEE);
        displayReport();
        verifyReport(1, 0, 0, 0, "0,6");
    }

    @Test
    public void shouldDisplayReport() {
        given(DrinkType.COFFEE, DrinkType.COFFEE, DrinkType.CHOCOLATE, DrinkType.CHOCOLATE, DrinkType.CHOCOLATE,
                DrinkType.TEA, DrinkType.ORANGE_JUICE);
        displayReport();
        verifyReport(2, 3, 1, 1, "3,7");
    }

    private void displayReport() {
        new DisplayReport(repository).execute();
    }

    private void given(DrinkType... types) {
        Arrays.stream(types)
                .forEach(repository::add);
    }

    private void verifyReport(long numberOfCoffees, long numberOfChocolates, long numberOfTeas, long numberOfOrangeJuices, String sum) {
        verify(repository, times(4)).countByCode(anyString());
        verify(repository, times(1)).sumAll();

        verify(out, times(1)).println(DrinkType.COFFEE.getCode() + ":" + numberOfCoffees);
        verify(out, times(1)).println(DrinkType.CHOCOLATE.getCode() + ":" + numberOfChocolates);
        verify(out, times(1)).println(DrinkType.TEA.getCode() + ":" + numberOfTeas);
        verify(out, times(1)).println(DrinkType.ORANGE_JUICE.getCode() + ":" + numberOfOrangeJuices);
        verify(out, times(1)).println("Total: " + sum + "€");
    }
}
