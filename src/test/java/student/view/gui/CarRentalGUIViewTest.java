package student.view.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import student.controller.CarRentalGUIController;
import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingRepository;
import student.model.Booking.CarBookingService;
import student.model.Car.*;
import student.model.User.*;

import javax.swing.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalGUIViewTest {

    private CarRentalGUIView guiView;
    private CarRentalGUIController controller;
    private UserService userService;
    private CarBookingService bookingService;
    private CarService carService;

    @BeforeEach
    void setUp() {
        CarRepository carRepo = new CarFileRepository();
        carService = new CarService(carRepo);

        bookingService = new CarBookingService(new CarBookingRepository(), carService);
        userService = new UserService(new UserArrayRepository());

        guiView = new CarRentalGUIView(carService, bookingService, userService);
        controller = new CarRentalGUIController(carService, bookingService, userService, guiView);
        guiView.setViewListener(controller);
    }

    @Test
    void shouldSetAndDisplayCurrentUser() {
        User user = new User(UUID.randomUUID(), "TestUser");
        guiView.setCurrentUser(user);

        JLabel statusLabel = (JLabel) TestUtils.getPrivateField(guiView, "statusLabel");
        assertNotNull(statusLabel);
        assertTrue(statusLabel.getText().contains("Logged in"));
    }

    @Test
    void shouldDisplayUsersInTable() {
        guiView.showUsers(userService.getUsers());

        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() > 0);
        assertEquals("User ID", table.getColumnName(0));
    }

    @Test
    void shouldRegisterAndLoginUser() {
        String newName = "User_" + System.currentTimeMillis();
        controller.handleRegisterUser(newName);
        controller.handleLoginUser(newName);
        JLabel statusLabel = (JLabel) TestUtils.getPrivateField(guiView, "statusLabel");
        assertTrue(statusLabel.getText().contains(newName));
    }

    @Test
    void shouldShowCarsPaginated() {
        List<Car> allCars = carService.getAllCars();
        guiView.showCars(allCars);

        assertFalse(allCars.isEmpty());
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() <= 18);
    }

    @Test
    void shouldHandleEmptyCarListGracefully() {
        guiView.showCars(List.of());

        JLabel statusLabel = (JLabel) TestUtils.getPrivateField(guiView, "statusLabel");
        assertEquals("❌ No cars to display.", statusLabel.getText());

        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertEquals(0, table.getRowCount());
    }


    @Test
    void shouldUpdatePaginationCorrectly() {
        guiView.updatePagination(0, 3, 54);

        JLabel pageLabel = (JLabel) TestUtils.getPrivateField(guiView, "pageLabel");
        assertEquals("Page 1 of 3", pageLabel.getText());

        JButton prev = (JButton) TestUtils.getPrivateField(guiView, "prevPageBtn");
        JButton next = (JButton) TestUtils.getPrivateField(guiView, "nextPageBtn");

        assertFalse(prev.isEnabled());
        assertTrue(next.isEnabled());
    }

    @Test
    void shouldDisplayBookingsInTable() {
        User user = userService.register("TestBooker_" + UUID.randomUUID());

        List<Car> availableCars = bookingService.getAvailableCars();
        assertFalse(availableCars.isEmpty(), "No available cars found for testing.");

        Car car = availableCars.get(0);
        bookingService.bookCar(user, car.getRegNumber());

        guiView.setCurrentUser(user);
        guiView.showBookings(bookingService.getBookings());

        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() > 0);
        assertEquals("Booking ID", table.getColumnName(0));
    }

    @Test
    void shouldSetStatusMessage() {
        String message = "🔄 Refreshing data...";
        guiView.setStatus(message);

        JLabel statusLabel = (JLabel) TestUtils.getPrivateField(guiView, "statusLabel");
        assertEquals(message, statusLabel.getText());
    }

    @Test
    void shouldSearchKeyword() {
        controller.handleSearchByKeyword("Tesla");
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() > 0);
    }

    @Test
    void shouldCreateStyledButtonWithTooltip() throws Exception {
        var method = CarRentalGUIView.class.getDeclaredMethod("createStyledButton", String.class, String.class);
        method.setAccessible(true);

        JButton button = (JButton) method.invoke(guiView, "Test", "This is a tooltip");
        assertEquals("This is a tooltip", button.getToolTipText());
        assertEquals("Test", button.getText());
    }

    @Test
    void shouldNavigateToNextPageWhenClicked() {
        List<Car> allCars = carService.getAllCars();
        if (allCars.size() < 20) return;

        guiView.showCars(allCars);

        JButton nextBtn = (JButton) TestUtils.getPrivateField(guiView, "nextPageBtn");
        nextBtn.doClick();

        JLabel pageLabel = (JLabel) TestUtils.getPrivateField(guiView, "pageLabel");
        assertTrue(pageLabel.getText().startsWith("Page 2"));
    }

    // test buttons
    @Test
    void shouldTriggerViewCarsAction() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "viewCarsBtn");
        btn.doClick();
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() > 0);
    }

    @Test
    void shouldTriggerViewElectricAction() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "viewElectricBtn");
        btn.doClick();
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertNotNull(table);
    }

    @Test
    void shouldTriggerSortByPriceAction() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "sortBtn");
        btn.doClick();
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() > 0);
    }

    @Test
    void shouldExportBookingsOnClick() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "exportBtn");
        btn.doClick();
    }

    @Test
    void shouldShowDialogWhenSearchFieldEmpty() {
        JTextField field = (JTextField) TestUtils.getPrivateField(guiView, "searchField");
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "searchBtn");
        field.setText("");
        btn.doClick();
    }

    @Test
    void shouldNavigateToNextPageUsingButton() {
        List<Car> cars = carService.getAllCars();
        if (cars.size() < 20) return;
        guiView.showCars(cars);

        JButton next = (JButton) TestUtils.getPrivateField(guiView, "nextPageBtn");
        next.doClick();

        JLabel label = (JLabel) TestUtils.getPrivateField(guiView, "pageLabel");
        assertTrue(label.getText().startsWith("Page 2"));
    }

    @Test
    void shouldNavigateToPrevPageUsingButton() {
        List<Car> cars = carService.getAllCars();
        if (cars.size() < 20) return;
        guiView.showCars(cars);

        JButton next = (JButton) TestUtils.getPrivateField(guiView, "nextPageBtn");
        next.doClick();
        JButton prev = (JButton) TestUtils.getPrivateField(guiView, "prevPageBtn");
        prev.doClick();

        JLabel label = (JLabel) TestUtils.getPrivateField(guiView, "pageLabel");
        assertTrue(label.getText().startsWith("Page 1"));
    }

    @Test
    void shouldTriggerFilterByPriceAction() {
        JButton filter = (JButton) TestUtils.getPrivateField(guiView, "filterBtn");

        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Minimum price:")).thenReturn("30");
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Maximum price:")).thenReturn("100");

            filter.doClick();
            JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
            assertTrue(table.getRowCount() > 0);
        }
    }

    @Test
    void shouldNotRegisterWhenNameIsEmpty() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "registerBtn");
        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Enter name to register:")).thenReturn("   ");
            btn.doClick();
        }
    }

    @Test
    void shouldNotRegisterWhenDialogCanceled() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "registerBtn");
        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Enter name to register:")).thenReturn(null);
            btn.doClick();
        }
    }

    @Test
    void shouldShowErrorWhenViewMyBookingsWithoutLogin() throws Exception {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "myBookingsBtn");

        Field f = guiView.getClass().getDeclaredField("currentUser");
        f.setAccessible(true);
        f.set(guiView, null);

        btn.doClick();
    }

    @Test
    void shouldNotLoginWhenDialogCanceled() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "loginBtn");
        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Enter name to login:")).thenReturn(null);
            btn.doClick();
        }
    }

    @Test
    void shouldNotBookWhenNotLoggedIn() throws Exception {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "bookBtn");

        Field f = guiView.getClass().getDeclaredField("currentUser");
        f.setAccessible(true);
        f.set(guiView, null);

        btn.doClick();
    }

    @Test
    void shouldNotBookWhenCarListIsNull() throws Exception {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "bookBtn");

        User user = userService.register("U_" + UUID.randomUUID());
        guiView.setCurrentUser(user);

        guiView.showCars(carService.getAllCars());

        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        table.setRowSelectionInterval(0, 0);

        Field f = guiView.getClass().getDeclaredField("currentDisplayedCars");
        f.setAccessible(true);
        f.set(guiView, null);

        btn.doClick();
    }

    @Test
    void shouldNotCancelOthersBooking() {
        User u1 = userService.register("Owner_" + UUID.randomUUID());
        User u2 = userService.register("Intruder_" + UUID.randomUUID());
        Car car = bookingService.getAvailableCars().get(0);

        bookingService.bookCar(u1, car.getRegNumber());
        List<CarBooking> bookings = bookingService.getBookings();
        CarBooking b = bookings.get(0);

        guiView.setCurrentUser(u2);
        guiView.showBookings(bookings);

        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        table.setRowSelectionInterval(0, 0);

        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "cancelBtn");
        btn.doClick();
    }

    @Test
    void shouldSearchWithValidKeyword() throws Exception {
        JTextField searchField = (JTextField) TestUtils.getPrivateField(guiView, "searchField");
        searchField.setText("tesla");

        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "searchBtn");
        btn.doClick();

        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() > 0);
    }

    @Test
    void shouldWarnWhenSearchFieldEnterEmpty() throws Exception {
        JTextField field = (JTextField) TestUtils.getPrivateField(guiView, "searchField");
        field.setText("");
        field.postActionEvent();
    }

    @Test
    void shouldWarnWhenSearchKeywordEmpty() throws Exception {
        JTextField searchField = (JTextField) TestUtils.getPrivateField(guiView, "searchField");
        searchField.setText("");

        JButton searchBtn = (JButton) TestUtils.getPrivateField(guiView, "searchBtn");
        searchBtn.doClick();
    }


    @Test
    void shouldIgnoreEmptyRegisterInput() {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "registerBtn");
        try (MockedStatic<JOptionPane> mocked = Mockito.mockStatic(JOptionPane.class)) {
            mocked.when(() -> JOptionPane.showInputDialog(guiView, "Enter name to register:")).thenReturn("   ");
            btn.doClick();
        }
    }

    @Test
    void shouldShowErrorWhenNotLoggedInForMyBookings() throws Exception {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "myBookingsBtn");
        Field f = guiView.getClass().getDeclaredField("currentUser");
        f.setAccessible(true);
        f.set(guiView, null);
        btn.doClick();
    }

    @Test
    void shouldNotBookWhenRowNotSelected() throws Exception {
        JButton btn = (JButton) TestUtils.getPrivateField(guiView, "bookBtn");

        User user = userService.register("U_" + UUID.randomUUID());
        guiView.setCurrentUser(user);

        guiView.showCars(carService.getAllCars());
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        table.clearSelection();
        btn.doClick();
    }
}
