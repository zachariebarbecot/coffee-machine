package unit;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.entities.NotEnoughMoneyException;
import com.zbar.kata.coffeemachine.entities.TooMuchSugarsException;
import com.zbar.kata.coffeemachine.ports.DrinkMakerPort;
import com.zbar.kata.coffeemachine.usecases.SendCommand;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

        @Nested
        class NoExtraHot {
            @Test
            public void shouldMakeCoffee() {
                new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, 0, false);
                verify(maker, times(1)).make("C::");
            }

            @Test
            public void shouldMakeChocolate() {
                new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, 0, false);
                verify(maker, times(1)).make("H::");
            }

            @Test
            public void shouldMakeTea() {
                new SendCommand(maker).execute(DrinkType.TEA, 0.4, 0, false);
                verify(maker, times(1)).make("T::");
            }

            @Test
            public void shouldMakeOrangeJuice() {
                new SendCommand(maker).execute(DrinkType.ORANGE_JUICE, 0.6, 0, false);
                verify(maker, times(1)).make("O::");
            }
        }

        @Nested
        class ExtraHot {
            @Test
            public void shouldMakeCoffee() {
                new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, 0, true);
                verify(maker, times(1)).make("Ch::");
            }

            @Test
            public void shouldMakeChocolate() {
                new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, 0, true);
                verify(maker, times(1)).make("Hh::");
            }

            @Test
            public void shouldMakeTea() {
                new SendCommand(maker).execute(DrinkType.TEA, 0.4, 0, true);
                verify(maker, times(1)).make("Th::");
            }

            @Test
            public void shouldMakeOrangeJuiceNoExtraHot() {
                new SendCommand(maker).execute(DrinkType.ORANGE_JUICE, 0.6, 0, true);
                verify(maker, times(1)).make("O::");
            }
        }
    }

    @Nested
    class SugarWithStick {

        @Nested
        class NoExtraHot {

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeCoffee(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, numberOfSugars, false);
                verify(maker, times(1)).make("C:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeChocolate(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, numberOfSugars, false);
                verify(maker, times(1)).make("H:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeTea(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.TEA, 0.4, numberOfSugars, false);
                verify(maker, times(1)).make("T:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeOrangeJuice(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.ORANGE_JUICE, 0.6, numberOfSugars, false);
                verify(maker, times(1)).make("O:" + numberOfSugars + ":0");
            }
        }

        @Nested
        class ExtraHot {

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeCoffee(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, numberOfSugars, true);
                verify(maker, times(1)).make("Ch:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeChocolate(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, numberOfSugars, true);
                verify(maker, times(1)).make("Hh:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeTea(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.TEA, 0.4, numberOfSugars, true);
                verify(maker, times(1)).make("Th:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeOrangeJuiceNoExtraHot(int numberOfSugars) {
                new SendCommand(maker).execute(DrinkType.ORANGE_JUICE, 0.6, numberOfSugars, true);
                verify(maker, times(1)).make("O:" + numberOfSugars + ":0");
            }
        }
    }

    @Nested
    class MoreThan2Sugars {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.COFFEE, 0.6, 3, false));
            verify(maker, times(0)).make(anyString());
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.5, 3, false));
            verify(maker, times(0)).make(anyString());
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.TEA, 0.4, 3, false));
            verify(maker, times(0)).make(anyString());
        }
    }

    @Nested
    class NotEnoughMoney {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.COFFEE, 0.1, 0, false));
            verify(maker, times(1)).make("M:0,5€ missing");
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.CHOCOLATE, 0.1, 0, false));
            verify(maker, times(1)).make("M:0,4€ missing");
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    new SendCommand(maker).execute(DrinkType.TEA, 0.1, 0, false));
            verify(maker, times(1)).make("M:0,3€ missing");
        }
    }
}
