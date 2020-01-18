package unit;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.entities.NotEnoughMoneyException;
import com.zbar.kata.coffeemachine.entities.RunningOutException;
import com.zbar.kata.coffeemachine.entities.TooMuchSugarsException;
import com.zbar.kata.coffeemachine.ports.BeverageQuantityCheckerPort;
import com.zbar.kata.coffeemachine.ports.DrinkMakerPort;
import com.zbar.kata.coffeemachine.ports.EmailNotifierPort;
import com.zbar.kata.coffeemachine.ports.ReportRepository;
import com.zbar.kata.coffeemachine.usecases.SendCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendCommandTest {

    @Mock
    private DrinkMakerPort maker;
    @Mock
    private ReportRepository repository;
    @Mock
    private BeverageQuantityCheckerPort checker;
    @Mock
    private EmailNotifierPort notifier;

    @Nested
    class NoSugarWithoutStick {

        @BeforeEach
        public void init() {
            when(checker.isEmpty(anyString())).thenReturn(false);
        }

        @Nested
        class NoExtraHot {
            @Test
            public void shouldMakeCoffee() {
                sendCommand(DrinkType.COFFEE, 0.6, 0, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.COFFEE);
                verify(maker, times(1)).make("C::");
            }

            @Test
            public void shouldMakeChocolate() {
                sendCommand(DrinkType.CHOCOLATE, 0.5, 0, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.CHOCOLATE);
                verify(maker, times(1)).make("H::");
            }

            @Test
            public void shouldMakeTea() {
                sendCommand(DrinkType.TEA, 0.4, 0, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.TEA);
                verify(maker, times(1)).make("T::");
            }

            @Test
            public void shouldMakeOrangeJuice() {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, 0, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.ORANGE_JUICE);
                verify(maker, times(1)).make("O::");
            }
        }

        @Nested
        class ExtraHot {
            @Test
            public void shouldMakeCoffee() {
                sendCommand(DrinkType.COFFEE, 0.6, 0, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.COFFEE);
                verify(maker, times(1)).make("Ch::");
            }

            @Test
            public void shouldMakeChocolate() {
                sendCommand(DrinkType.CHOCOLATE, 0.5, 0, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.CHOCOLATE);
                verify(maker, times(1)).make("Hh::");
            }

            @Test
            public void shouldMakeTea() {
                sendCommand(DrinkType.TEA, 0.4, 0, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.TEA);
                verify(maker, times(1)).make("Th::");
            }

            @Test
            public void shouldMakeOrangeJuiceNoExtraHot() {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, 0, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.ORANGE_JUICE);
                verify(maker, times(1)).make("O::");
            }
        }
    }

    @Nested
    class SugarWithStick {

        @BeforeEach
        public void init() {
            when(checker.isEmpty(anyString())).thenReturn(false);
        }

        @Nested
        class NoExtraHot {

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeCoffee(int numberOfSugars) {
                sendCommand(DrinkType.COFFEE, 0.6, numberOfSugars, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.COFFEE);
                verify(maker, times(1)).make("C:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeChocolate(int numberOfSugars) {
                sendCommand(DrinkType.CHOCOLATE, 0.5, numberOfSugars, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.CHOCOLATE);
                verify(maker, times(1)).make("H:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeTea(int numberOfSugars) {
                sendCommand(DrinkType.TEA, 0.4, numberOfSugars, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.TEA);
                verify(maker, times(1)).make("T:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeOrangeJuice(int numberOfSugars) {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, numberOfSugars, false);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.ORANGE_JUICE);
                verify(maker, times(1)).make("O:" + numberOfSugars + ":0");
            }
        }

        @Nested
        class ExtraHot {

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeCoffee(int numberOfSugars) {
                sendCommand(DrinkType.COFFEE, 0.6, numberOfSugars, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.COFFEE);
                verify(maker, times(1)).make("Ch:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeChocolate(int numberOfSugars) {
                sendCommand(DrinkType.CHOCOLATE, 0.5, numberOfSugars, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.CHOCOLATE);
                verify(maker, times(1)).make("Hh:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeTea(int numberOfSugars) {
                sendCommand(DrinkType.TEA, 0.4, numberOfSugars, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.TEA);
                verify(maker, times(1)).make("Th:" + numberOfSugars + ":0");
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeOrangeJuiceNoExtraHot(int numberOfSugars) {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, numberOfSugars, true);
                verify(notifier, times(0)).notifyMissingDrink(anyString());
                verify(repository, times(1)).add(DrinkType.ORANGE_JUICE);
                verify(maker, times(1)).make("O:" + numberOfSugars + ":0");
            }
        }
    }

    @Nested
    class MoreThan2Sugars {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.COFFEE, 0.6, 3, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.COFFEE);
            verify(maker, times(0)).make(anyString());
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.CHOCOLATE, 0.5, 3, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.CHOCOLATE);
            verify(maker, times(0)).make(anyString());
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.TEA, 0.4, 3, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.TEA);
            verify(maker, times(0)).make(anyString());
        }

        @Test
        public void shouldNotMakeOrangeJuice() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.ORANGE_JUICE, 0.6, 3, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.ORANGE_JUICE);
            verify(maker, times(0)).make(anyString());
        }
    }

    @Nested
    class NotEnoughMoney {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.COFFEE, 0.1, 0, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.COFFEE);
            verify(maker, times(1)).make("M:0,5€ missing");
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.CHOCOLATE, 0.1, 0, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.CHOCOLATE);
            verify(maker, times(1)).make("M:0,4€ missing");
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.TEA, 0.1, 0, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.TEA);
            verify(maker, times(1)).make("M:0,3€ missing");
        }

        @Test
        public void shouldNotMakeOrangeJuice() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.ORANGE_JUICE, 0.1, 0, false));
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(DrinkType.ORANGE_JUICE);
            verify(maker, times(1)).make("M:0,5€ missing");
        }
    }

    @Nested
    class NoBeverage {

        @BeforeEach
        public void init() {
            when(checker.isEmpty(anyString())).thenReturn(true);
        }

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.COFFEE, 0.6, 0, false));
            verify(notifier, times(1)).notifyMissingDrink(DrinkType.COFFEE.getCode());
            verify(repository, times(0)).add(DrinkType.COFFEE);
            verify(maker, times(1)).make("M:Running out of " + DrinkType.COFFEE.getCode());
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.CHOCOLATE, 0.5, 0, false));
            verify(notifier, times(1)).notifyMissingDrink(DrinkType.CHOCOLATE.getCode());
            verify(repository, times(0)).add(DrinkType.CHOCOLATE);
            verify(maker, times(1)).make("M:Running out of " + DrinkType.CHOCOLATE.getCode());
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.TEA, 0.4, 0, false));
            verify(notifier, times(1)).notifyMissingDrink(DrinkType.TEA.getCode());
            verify(repository, times(0)).add(DrinkType.TEA);
            verify(maker, times(1)).make("M:Running out of " + DrinkType.TEA.getCode());
        }

        @Test
        public void shouldNotMakeOrangeJuice() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.ORANGE_JUICE, 0.6, 0, false));
            verify(notifier, times(1)).notifyMissingDrink(DrinkType.ORANGE_JUICE.getCode());
            verify(repository, times(0)).add(DrinkType.ORANGE_JUICE);
            verify(maker, times(1)).make("M:Running out of " + DrinkType.ORANGE_JUICE.getCode());
        }
    }

    private void sendCommand(DrinkType type, double money, int numberOfSugars, boolean extraHot) {
        new SendCommand(maker, repository, checker, notifier).execute(type, money, numberOfSugars, extraHot);
    }
}
