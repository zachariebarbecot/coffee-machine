package unit;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.entities.NotEnoughMoneyException;
import com.zbar.kata.coffeemachine.entities.TooMuchSugarsException;
import com.zbar.kata.coffeemachine.ports.DrinkMakerPort;
import com.zbar.kata.coffeemachine.usecases.SendCommand;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SendCommandTest {

    @Mock
    private DrinkMakerPort maker;

    @Nested
    class NoSugarWithoutStick {

        @Test
        public void shouldMakeCoffee() {
            new SendCommand(maker).execute(DrinkType.COFFEE, 0.6);
            verify(maker, times(1)).make("C::");
        }

        @Test
        public void shouldMakeChocolate() {
            new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5);
            verify(maker, times(1)).make("H::");
        }

        @Test
        public void shouldMakeTea() {
            new SendCommand(maker).execute(DrinkType.TEA, 0.4);
            verify(maker, times(1)).make("T::");
        }
    }

    @Nested
    class OneSugarWithStick {

        @Test
        public void shouldMakeCoffee() {
            new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, 1);
            verify(maker, times(1)).make("C:1:0");
        }

        @Test
        public void shouldMakeChocolate() {
            new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, 1);
            verify(maker, times(1)).make("H:1:0");
        }

        @Test
        public void shouldMakeTea() {
            new SendCommand(maker).execute(DrinkType.TEA, 0.4, 1);
            verify(maker, times(1)).make("T:1:0");
        }
    }

    @Nested
    class TwoSugarAndStick {

        @Test
        public void shouldMakeCoffee() {
            new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, 2);
            verify(maker, times(1)).make("C:2:0");
        }

        @Test
        public void shouldMakeChocolate() {
            new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, 2);
            verify(maker, times(1)).make("H:2:0");
        }

        @Test
        public void shouldMakeTea() {
            new SendCommand(maker).execute(DrinkType.TEA, 0.4, 2);
            verify(maker, times(1)).make("T:2:0");
        }
    }

    @Nested
    class MoreThan2Sugars {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, 3));
            verify(maker, times(0)).make(anyString());
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, 3));
            verify(maker, times(0)).make(anyString());
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.TEA, 0.4, 3));
            verify(maker, times(0)).make(anyString());
        }
    }

    @Nested
    class NotEnoughMoney {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.COFFEE, 0.1));
            verify(maker, times(1)).make("M:0,5€ missing");
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.1));
            verify(maker, times(1)).make("M:0,4€ missing");
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.TEA, 0.1));
            verify(maker, times(1)).make("M:0,3€ missing");
        }
    }
}
