package student.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingService;
import student.model.Booking.CarBookingRepository;
import student.model.Car.*;
import student.model.User.*;
import student.view.gui.CarRentalGUIView;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import javax.swing.JOptionPane;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

class CarRentalGUIControllerTest {

    private CarRentalGUIController controller;
    private MockGUIView guiView;
    private UserService userService;
    private CarService carService;
    private CarBookingService bookingService;

    @BeforeEach
    void setUp() {
        carService = new CarService(new CarFileRepository());
        bookingService = new CarBookingService(new CarBookingRepository(), carService);
        userService = new UserService(new UserArrayRepository());

        guiView = new MockGUIView();
        controller = new CarRentalGUIController(carService, bookingService, userService, guiView);
    }

    @Test
    void shouldLoginValidUser() {
        controller.handleLoginUser("James");
        assertNotNull(guiView.lastUserSet);
        assertEquals("James", guiView.lastUserSet.getName());
    }


    @Test
    void shouldRegisterAndSetUser() {
        String name = "NewUser_" + UUID.randomUUID();
        controller.handleRegisterUser(name);
        assertNotNull(guiView.lastUserSet);
        assertEquals(name, guiView.lastUserSet.getName());
    }

    @Test
    void shouldShowUserBookingsOnly() {
        User user = userService.getUsers().get(0);
        Car car = bookingService.getAvailableCars().get(0);
        controller.handleBookCarAs(car, user);

        controller.handleViewMyBookings(user);
        assertNotNull(guiView.lastBookingsShown);
        assertFalse(guiView.lastBookingsShown.isEmpty());
    }

    @Test
    void shouldShowAllAvailableCars() {
        controller.handleViewAvailableCars();
        assertNotNull(guiView.lastCarsShown);
        assertFalse(guiView.lastCarsShown.isEmpty());
    }

    @Test
    void shouldShowAvailableElectricCars() {
        controller.handleViewElectricCars();
        assertNotNull(guiView.lastCarsShown);
    }

    @Test
    void shouldSortCarsByPrice() {
        controller.handleSortByPrice();
        assertNotNull(guiView.lastCarsShown);
        assertTrue(guiView.lastCarsShown.size() > 1);
        // 简单验证排序顺序
        for (int i = 0; i < guiView.lastCarsShown.size() - 1; i++) {
            assertTrue(guiView.lastCarsShown.get(i).getRentalPricePerDay()
                    .compareTo(guiView.lastCarsShown.get(i + 1).getRentalPricePerDay()) <= 0);
        }
    }

    @Test
    void shouldSearchByKeyword() {
        List<Car> all = carService.getAllCars();
        if (!all.isEmpty()) {
            String keyword = all.get(0).getBrand().name().substring(0, 2);
            controller.handleSearchByKeyword(keyword);
            assertNotNull(guiView.lastCarsShown);
            assertFalse(guiView.lastCarsShown.isEmpty());
        }
    }

    @Test
    void shouldSearchByKeywordWithoutDialog() {
        List<Car> all = carService.getAllCars();
        if (!all.isEmpty()) {
            String keyword = all.get(0).getBrand().name().substring(0, 2);
            controller.handleSearchByKeyword(keyword);
            assertNotNull(guiView.lastCarsShown);
            assertFalse(guiView.lastCarsShown.isEmpty());
        }
    }

    @Test
    void shouldHandleSearchWithValidKeyword() {
        List<Car> cars = carService.getAllCars();
        if (!cars.isEmpty()) {
            String keyword = cars.get(0).getModel().substring(0, 2);
            controller.handleSearchByKeyword(keyword);
            assertNotNull(guiView.lastCarsShown);
            assertFalse(guiView.lastCarsShown.isEmpty());
        }
    }

    @Test
    void shouldFilterCarsByValidPriceRange() {
        List<Car> filtered = carService.getCarsByPriceRange(
                new BigDecimal("30"), new BigDecimal("80"));
        assertNotNull(filtered);
        assertFalse(filtered.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenNoCarsInPriceRange() {
        List<Car> filtered = carService.getCarsByPriceRange(
                new BigDecimal("10000"), new BigDecimal("20000"));
        assertTrue(filtered.isEmpty());
    }

    @Test
    void shouldShowAllUsers() {
        controller.handleViewUsers();
        assertNotNull(guiView.lastUsersShown);
        assertFalse(guiView.lastUsersShown.isEmpty());
    }

    @Test
    void shouldShowAllBookings() {
        controller.handleViewBookings();
        assertNotNull(guiView.lastBookingsShown);
    }

    @Test
    void shouldExportBookingsWithoutException() {
        // just verify it runs without exceptions
        assertDoesNotThrow(() -> controller.handleExportBookings());
    }

    @Test
    void shouldCancelBookingSuccessfully() {
        User user = userService.getUsers().get(0);
        Car car = bookingService.getAvailableCars().get(0);

        UUID bookingId = bookingService.bookCar(user, car.getRegNumber());
        CarBooking booking = bookingService.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow();

        assertFalse(booking.isCanceled());

        controller.handleCancelBooking(booking);

        assertTrue(booking.isCanceled());
        assertNotNull(guiView.lastBookingsShown);
    }

    @Test
    void shouldNotCancelNullBooking() {
        assertDoesNotThrow(() -> controller.handleCancelBooking(null));
    }

    @Test
    void shouldNotCancelAlreadyCanceledBooking() {
        User user = userService.register("U_" + UUID.randomUUID());
        Car car = bookingService.getAvailableCars().get(0);
        UUID id = bookingService.bookCar(user, car.getRegNumber());

        CarBooking booking = bookingService.getBookings().stream()
                .filter(b -> b.getBookingId().equals(id))
                .findFirst().orElseThrow();

        controller.handleCancelBooking(booking); // 第一次取消
        assertTrue(booking.isCanceled());

        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showMessageDialog(any(), anyString()))
                    .thenAnswer(invocation -> {
                        String msg = (String) invocation.getArgument(1);
                        assertTrue(msg.contains("Already canceled."));
                        return null;
                    });

            controller.handleCancelBooking(booking); // 第二次触发提示
        }
    }


    // test branches
    @Test
    void shouldShowErrorWhenUserNotFoundDuringBooking() {
        Car car = bookingService.getAvailableCars().get(0);
        List<User> users = userService.getUsers();
        String[] userNames = users.stream().map(User::getName).toArray(String[]::new);

        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            // 模拟输入不存在的用户
            mocked.when(() -> JOptionPane.showInputDialog(
                    any(), eq("Select user:"), eq("Booking"),
                    eq(JOptionPane.PLAIN_MESSAGE), isNull(), eq(userNames), eq(userNames[0])
            )).thenReturn("NonExistingUser");

            // 模拟弹窗
            mocked.when(() -> JOptionPane.showMessageDialog(any(), anyString()))
                    .thenAnswer(invocation -> {
                        String msg = (String) invocation.getArgument(1);
                        assertTrue(msg.contains("❌ User not found."));
                        return null;
                    });

            controller.handleBookCar(car);
        }
    }



    @Test
    void shouldNotBookIfSelectedCarIsNull() {
        controller.handleBookCar(null);
    }

    @Test
    void shouldHandleExportBookingsExceptionGracefully() {
        System.setProperty("user.dir", "/invalid-path");
        assertDoesNotThrow(() -> controller.handleExportBookings());
    }

    @Test
    void shouldShowErrorWhenUserNotFound() {
        controller.handleLoginUser("NonExistentUser");
        assertNull(guiView.lastUserSet);
    }

    @Test
    void shouldNotBookNullCar() {
        User user = userService.getUsers().get(0);
        int before = bookingService.getBookings().size();

        controller.handleBookCarAs(null, user);

        int after = bookingService.getBookings().size();
        assertEquals(before, after);
    }

    @Test
    void shouldNotBookIfUserCancelsDialog() {
        Car car = bookingService.getAvailableCars().get(0);
        int bookingsBefore = bookingService.getBookings().size(); // ✅ 记录之前的预约数

        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(
                            eq(guiView), eq("Select user:"), eq("Booking"),
                            Mockito.anyInt(), isNull(), any(), any()))
                    .thenReturn(null);

            controller.handleBookCar(car);
        }

        int bookingsAfter = bookingService.getBookings().size();
        assertEquals(bookingsBefore, bookingsAfter);
    }


    @Test
    void shouldBookCarSuccessfullyViaDialog() {
        Car car = bookingService.getAvailableCars().get(0);
        String validUserName = userService.getUsers().get(0).getName();

        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(
                            eq(guiView), eq("Select user:"), eq("Booking"),
                            Mockito.anyInt(), isNull(), any(), any()))
                    .thenReturn(validUserName);

            controller.handleBookCar(car);

            assertFalse(bookingService.getBookings().isEmpty());
            assertNotNull(guiView.lastCarsShown);
        }
    }

    @Test
    void shouldHandleBookingFailureGracefully() {
        Car car = bookingService.getAvailableCars().get(0);
        User user = userService.register("U_" + UUID.randomUUID());

        // 使用 mock 模拟 bookingService 抛异常
        CarBookingService mockBookingService = mock(CarBookingService.class);
        try {
            when(mockBookingService.bookCar(eq(user), eq(car.getRegNumber())))
                    .thenThrow(new IllegalStateException("System error"));

            CarRentalGUIController faultyController = new CarRentalGUIController(
                    carService, mockBookingService, userService, guiView);

            try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
                mocked.when(() -> JOptionPane.showMessageDialog(any(), anyString()))
                        .thenAnswer(invocation -> {
                            String msg = (String) invocation.getArgument(1);
                            assertTrue(msg.contains("❌ Booking failed: System error"));
                            return null;
                        });

                faultyController.handleBookCarAs(car, user);
            }

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }


    @Test
    void shouldHandleNoBookingsForUser() {
        User newUser = userService.register("NoBookingUser");
        controller.handleViewMyBookings(newUser);

        assertNull(guiView.lastBookingsShown);
    }

    @Test
    void shouldHandleFilterByPriceViaDialog() {
        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Minimum price:")).thenReturn("20");
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Maximum price:")).thenReturn("100");

            controller.handleFilterByPrice();

            assertNotNull(guiView.lastCarsShown);
            assertFalse(guiView.lastCarsShown.isEmpty());
        }
    }

    @Test
    void shouldHandleSearchByKeywordViaDialog() {
        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Enter keyword:")).thenReturn("tesla");

            controller.handleSearchByKeyword();

            assertNotNull(guiView.lastCarsShown);
        }
    }

    @Test
    void shouldIgnoreEmptyFilterInput() {
        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Minimum price:")).thenReturn(null);
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Maximum price:")).thenReturn("100");

            controller.handleFilterByPrice();
            assertNull(guiView.lastCarsShown);
        }
    }
}
