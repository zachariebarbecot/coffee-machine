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
                verifyNestedCommand(DrinkType.COFFEE, "C");
            }

            @Test
            public void shouldMakeChocolate() {
                sendCommand(DrinkType.CHOCOLATE, 0.5, 0, false);
                verifyNestedCommand(DrinkType.CHOCOLATE, "H");
            }

            @Test
            public void shouldMakeTea() {
                sendCommand(DrinkType.TEA, 0.4, 0, false);
                verifyNestedCommand(DrinkType.TEA, "T");
            }

            @Test
            public void shouldMakeOrangeJuice() {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, 0, false);
                verifyNestedCommand(DrinkType.ORANGE_JUICE, "O");
            }
        }

        @Nested
        class ExtraHot {
            @Test
            public void shouldMakeCoffee() {
                sendCommand(DrinkType.COFFEE, 0.6, 0, true);
                verifyNestedCommand(DrinkType.COFFEE, "Ch");
            }

            @Test
            public void shouldMakeChocolate() {
                sendCommand(DrinkType.CHOCOLATE, 0.5, 0, true);
                verifyNestedCommand(DrinkType.CHOCOLATE, "Hh");
            }

            @Test
            public void shouldMakeTea() {
                sendCommand(DrinkType.TEA, 0.4, 0, true);
                verifyNestedCommand(DrinkType.TEA, "Th");
            }

            @Test
            public void shouldMakeOrangeJuiceNoExtraHot() {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, 0, true);
                verifyNestedCommand(DrinkType.ORANGE_JUICE, "O");
            }
        }

        private void verifyNestedCommand(DrinkType type, String stringifyType) {
            verifyCommand(type, stringifyType + "::");
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
                verifyNestedCommand(DrinkType.COFFEE, "C", numberOfSugars);
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeChocolate(int numberOfSugars) {
                sendCommand(DrinkType.CHOCOLATE, 0.5, numberOfSugars, false);
                verifyNestedCommand(DrinkType.CHOCOLATE, "H", numberOfSugars);
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeTea(int numberOfSugars) {
                sendCommand(DrinkType.TEA, 0.4, numberOfSugars, false);
                verifyNestedCommand(DrinkType.TEA, "T", numberOfSugars);
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeOrangeJuice(int numberOfSugars) {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, numberOfSugars, false);
                verifyNestedCommand(DrinkType.ORANGE_JUICE, "O", numberOfSugars);
            }
        }

        @Nested
        class ExtraHot {

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeCoffee(int numberOfSugars) {
                sendCommand(DrinkType.COFFEE, 0.6, numberOfSugars, true);
                verifyNestedCommand(DrinkType.COFFEE, "Ch", numberOfSugars);
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeChocolate(int numberOfSugars) {
                sendCommand(DrinkType.CHOCOLATE, 0.5, numberOfSugars, true);
                verifyNestedCommand(DrinkType.CHOCOLATE, "Hh", numberOfSugars);
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeTea(int numberOfSugars) {
                sendCommand(DrinkType.TEA, 0.4, numberOfSugars, true);
                verifyNestedCommand(DrinkType.TEA, "Th", numberOfSugars);
            }

            @ParameterizedTest
            @ValueSource(ints = {1, 2})
            public void shouldMakeOrangeJuiceNoExtraHot(int numberOfSugars) {
                sendCommand(DrinkType.ORANGE_JUICE, 0.6, numberOfSugars, true);
                verifyNestedCommand(DrinkType.ORANGE_JUICE, "O", numberOfSugars);
            }
        }

        private void verifyNestedCommand(DrinkType type, String stringifyType, int numberOfSugars) {
            verifyCommand(type, stringifyType + ":" + numberOfSugars + ":0");
        }
    }

    @Nested
    class MoreThan2Sugars {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.COFFEE, 0.6, 3, false));
            verifyNestedCommand(DrinkType.COFFEE);
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.CHOCOLATE, 0.5, 3, false));
            verifyNestedCommand(DrinkType.CHOCOLATE);
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.TEA, 0.4, 3, false));
            verifyNestedCommand(DrinkType.TEA);
        }

        @Test
        public void shouldNotMakeOrangeJuice() {
            assertThatExceptionOfType(TooMuchSugarsException.class).isThrownBy(() ->
                    sendCommand(DrinkType.ORANGE_JUICE, 0.6, 3, false));
            verifyNestedCommand(DrinkType.ORANGE_JUICE);
        }

        private void verifyNestedCommand(DrinkType type) {
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(type);
            verify(maker, times(0)).make(anyString());
        }
    }

    @Nested
    class NotEnoughMoney {

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.COFFEE, 0.1, 0, false));
            verifyNestedCommand(DrinkType.COFFEE, "M:0,5€ missing");
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.CHOCOLATE, 0.1, 0, false));
            verifyNestedCommand(DrinkType.CHOCOLATE, "M:0,4€ missing");
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.TEA, 0.1, 0, false));
            verifyNestedCommand(DrinkType.TEA, "M:0,3€ missing");
        }

        @Test
        public void shouldNotMakeOrangeJuice() {
            assertThatExceptionOfType(NotEnoughMoneyException.class).isThrownBy(() ->
                    sendCommand(DrinkType.ORANGE_JUICE, 0.1, 0, false));
            verifyNestedCommand(DrinkType.ORANGE_JUICE, "M:0,5€ missing");
        }

        private void verifyNestedCommand(DrinkType type, String stringifyMessage) {
            verify(notifier, times(0)).notifyMissingDrink(anyString());
            verify(repository, times(0)).add(type);
            verify(maker, times(1)).make(stringifyMessage);
        }
    }

    @Nested
    class NoBeverage {

        private static final String RUNNING_OUT = "M:Running out of ";

        @BeforeEach
        public void init() {
            when(checker.isEmpty(anyString())).thenReturn(true);
        }

        @Test
        public void shouldNotMakeCoffee() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.COFFEE, 0.6, 0, false));
            verifyNestedCommand(DrinkType.COFFEE, RUNNING_OUT + DrinkType.COFFEE.getCode());
        }

        @Test
        public void shouldNotMakeChocolate() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.CHOCOLATE, 0.5, 0, false));
            verifyNestedCommand(DrinkType.CHOCOLATE, RUNNING_OUT + DrinkType.CHOCOLATE.getCode());
        }

        @Test
        public void shouldNotMakeTea() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.TEA, 0.4, 0, false));
            verifyNestedCommand(DrinkType.TEA, RUNNING_OUT + DrinkType.TEA.getCode());
        }

        @Test
        public void shouldNotMakeOrangeJuice() {
            assertThatExceptionOfType(RunningOutException.class).isThrownBy(() ->
                    sendCommand(DrinkType.ORANGE_JUICE, 0.6, 0, false));
            verifyNestedCommand(DrinkType.ORANGE_JUICE, RUNNING_OUT + DrinkType.ORANGE_JUICE.getCode());
        }

        private void verifyNestedCommand(DrinkType type, String stringifyMessage) {
            verify(notifier, times(1)).notifyMissingDrink(type.getCode());
            verify(repository, times(0)).add(type);
            verify(maker, times(1)).make(stringifyMessage);
        }
    }

    private void sendCommand(DrinkType type, double money, int numberOfSugars, boolean extraHot) {
        new SendCommand(maker, repository, checker, notifier).execute(type, money, numberOfSugars, extraHot);
    }

    private void verifyCommand(DrinkType type, String stringifyCommand) {
        verify(notifier, times(0)).notifyMissingDrink(anyString());
        verify(repository, times(1)).add(type);
        verify(maker, times(1)).make(stringifyCommand);
    }
}
