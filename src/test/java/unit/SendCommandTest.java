package unit;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.entities.TooMuchSugarsException;
import com.zbar.kata.coffeemachine.ports.DrinkMakerPort;
import com.zbar.kata.coffeemachine.usecases.SendCommand;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
            new SendCommand(maker).execute(DrinkType.COFFEE);
            verify(maker, times(1)).make("C::");
        }

        @Test
        public void shouldMakeChocolate() {
            new SendCommand(maker).execute(DrinkType.CHOCOLATE);
            verify(maker, times(1)).make("H::");
        }

        @Test
        public void shouldMakeTea() {
            new SendCommand(maker).execute(DrinkType.TEA);
            verify(maker, times(1)).make("T::");
        }
    }

    @Nested
    class OneSugarWithStick {

        @Test
        public void shouldMakeCoffee() {
            new SendCommand(maker).execute(DrinkType.COFFEE, 1);
            verify(maker, times(1)).make("C:1:0");
        }

        @Test
        public void shouldMakeChocolate() {
            new SendCommand(maker).execute(DrinkType.CHOCOLATE, 1);
            verify(maker, times(1)).make("H:1:0");
        }

        @Test
        public void shouldMakeTea() {
            new SendCommand(maker).execute(DrinkType.TEA, 1);
            verify(maker, times(1)).make("T:1:0");
        }
    }

    @Nested
    class TwoSugarAndStick {

        @Test
        public void shouldMakeCoffee() {
            new SendCommand(maker).execute(DrinkType.COFFEE, 2);
            verify(maker, times(1)).make("C:2:0");
        }

        @Test
        public void shouldMakeChocolate() {
            new SendCommand(maker).execute(DrinkType.CHOCOLATE, 2);
            verify(maker, times(1)).make("H:2:0");
        }

        @Test
        public void shouldMakeTea() {
            new SendCommand(maker).execute(DrinkType.TEA, 2);
            verify(maker, times(1)).make("T:2:0");
        }
    }

    @Nested
    class MoreThan2Sugars {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.COFFEE, 3));
            verify(maker, times(0)).make("C:3:0");
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.CHOCOLATE, 3));
            verify(maker, times(0)).make("H:3:0");
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.TEA, 3));
            verify(maker, times(0)).make("T:3:0");
        }
    }
}
