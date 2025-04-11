package student.view;

import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingService;
import student.model.Car.Car;
import student.model.Car.CarService;
import student.model.User.User;
import student.model.User.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CarRentalGUI extends JFrame {

    private final CarService carService;
    private final CarBookingService bookingService;
    private final UserService userService;

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    private List<Car> pagedCars = List.of();
    private List<Car> currentDisplayedCars;
    private List<CarBooking> currentDisplayedBookings;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 5;

    public CarRentalGUI(CarService carService, CarBookingService bookingService, UserService userService) {
        this.carService = carService;
        this.bookingService = bookingService;
        this.userService = userService;

        setTitle("🚗 Car Rental System");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        showCars(bookingService.getAvailableCars());
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // === 顶部按钮区 ===
        JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewCarsBtn = new JButton("View Available Cars");
        JButton viewElectricBtn = new JButton("View Electric Cars");
        row1.add(viewCarsBtn);
        row1.add(viewElectricBtn);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton filterByPriceBtn = new JButton("Filter by Price");
        JButton sortByPriceBtn = new JButton("Sort by Price");
        JButton searchKeywordBtn = new JButton("Search by Keyword");
        row2.add(filterByPriceBtn);
        row2.add(sortByPriceBtn);
        row2.add(searchKeywordBtn);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewUsersBtn = new JButton("View Users");
        JButton viewBookingsBtn = new JButton("View Bookings");
        row3.add(viewUsersBtn);
        row3.add(viewBookingsBtn);

        topPanel.add(row1);
        topPanel.add(row2);
        topPanel.add(row3);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // === 表格展示 ===
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === 底部面板（分页 + 操作按钮 + 状态栏）===
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevPageBtn = new JButton("⬅ Prev Page");
        JButton nextPageBtn = new JButton("Next Page ➡");
        paginationPanel.add(prevPageBtn);
        paginationPanel.add(nextPageBtn);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bookBtn = new JButton("Book Selected Car");
        JButton cancelBtn = new JButton("Cancel Selected Booking");
        JButton exportBtn = new JButton("Export Bookings to CSV");
        actionPanel.add(bookBtn);
        actionPanel.add(cancelBtn);
        actionPanel.add(exportBtn);

        statusLabel = new JLabel("Welcome to the Car Rental System");

        bottomPanel.add(paginationPanel, BorderLayout.NORTH);
        bottomPanel.add(actionPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        viewCarsBtn.addActionListener(e -> showCars(bookingService.getAvailableCars()));
        viewElectricBtn.addActionListener(e -> showCars(bookingService.getAvailableElectricCars()));
        sortByPriceBtn.addActionListener(e -> showCars(carService.sortCarsByPrice()));
        filterByPriceBtn.addActionListener(e -> filterCarsByPriceRange());
        searchKeywordBtn.addActionListener(e -> searchCarsByKeyword());
        viewUsersBtn.addActionListener(e -> showUsers(userService.getUsers()));
        viewBookingsBtn.addActionListener(e -> showBookings(bookingService.getBookings()));
        bookBtn.addActionListener(e -> handleBooking());
        cancelBtn.addActionListener(e -> cancelBooking());
        exportBtn.addActionListener(e -> exportBookingsToCSV());
        prevPageBtn.addActionListener(e -> {
            if (currentPage > 0) showCarsPage(currentPage - 1);
        });
        nextPageBtn.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) pagedCars.size() / PAGE_SIZE) - 1;
            if (currentPage < maxPage) showCarsPage(currentPage + 1);
        });
    }

    private void showCars(List<Car> cars) {
        this.pagedCars = cars;
        showCarsPage(0);
    }

    private void showCarsPage(int pageIndex) {
        if (pagedCars == null || pagedCars.isEmpty()) {
            tableModel.setRowCount(0);
            statusLabel.setText("❌ No cars to display.");
            return;
        }

        currentPage = pageIndex;
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, pagedCars.size());
        List<Car> page = pagedCars.subList(start, end);

        currentDisplayedCars = page;
        currentDisplayedBookings = null;

        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"RegNumber", "Brand", "Model", "Price", "Electric"});
        for (Car car : page) {
            tableModel.addRow(new Object[]{
                    car.getRegNumber(),
                    car.getBrand(),
                    car.getModel(),
                    car.getRentalPricePerDay(),
                    car.isElectric() ? "Yes" : "No"
            });
        }

        int totalPages = (int) Math.ceil((double) pagedCars.size() / PAGE_SIZE);
        statusLabel.setText(String.format("📄 Page %d of %d | Total cars: %d", currentPage + 1, totalPages, pagedCars.size()));
    }

    private void handleBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || currentDisplayedCars == null || currentDisplayedCars.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a car to book.");
            return;
        }
        Car selectedCar = currentDisplayedCars.get(selectedRow);
        List<User> users = userService.getUsers();
        String[] userNames = users.stream().map(User::getName).toArray(String[]::new);
        String selectedUser = (String) JOptionPane.showInputDialog(this, "Select user:", "Booking", JOptionPane.PLAIN_MESSAGE, null, userNames, userNames[0]);
        if (selectedUser == null) return;

        User user = users.stream().filter(u -> u.getName().equals(selectedUser)).findFirst().orElse(null);
        try {
            UUID bookingId = bookingService.bookCar(user, selectedCar.getRegNumber());
            JOptionPane.showMessageDialog(this, "✅ Booking successful! ID: " + bookingId);
            showCars(bookingService.getAvailableCars());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Booking failed: " + e.getMessage());
        }
    }

    private void cancelBooking() {
        int row = table.getSelectedRow();
        if (row == -1 || currentDisplayedBookings == null) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
            return;
        }

        CarBooking booking = currentDisplayedBookings.get(row);
        if (booking.isCanceled()) {
            JOptionPane.showMessageDialog(this, "Already canceled.");
            return;
        }

        try {
            bookingService.cancelBooking(booking.getBookingId());
            JOptionPane.showMessageDialog(this, "✅ Booking canceled.");
            showBookings(bookingService.getBookings());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to cancel: " + e.getMessage());
        }
    }

    private void exportBookingsToCSV() {
        List<CarBooking> bookings = bookingService.getBookings();
        try (FileWriter writer = new FileWriter("bookings_export.csv")) {
            writer.write("BookingID,User,Brand,Model,CarReg,Price,Time,Canceled\n");
            for (CarBooking b : bookings) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                        b.getBookingId(),
                        b.getUser().getName(),
                        b.getCar().getBrand(),
                        b.getCar().getModel(),
                        b.getCar().getRegNumber(),
                        b.getCar().getRentalPricePerDay(),
                        b.getBookingTime(),
                        b.isCanceled()));
            }
            JOptionPane.showMessageDialog(this, "✅ Exported to bookings_export.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Export failed: " + e.getMessage());
        }
    }

    private void searchCarsByKeyword() {
        String keyword = JOptionPane.showInputDialog(this, "Enter keyword:");
        if (keyword == null || keyword.isBlank()) return;
        List<Car> result = carService.searchCars(keyword.trim().toLowerCase());
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cars matched.");
        }
        showCars(result);
    }

    private void filterCarsByPriceRange() {
        try {
            String minStr = JOptionPane.showInputDialog(this, "Minimum price:");
            String maxStr = JOptionPane.showInputDialog(this, "Maximum price:");
            if (minStr == null || maxStr == null) return;
            BigDecimal min = new BigDecimal(minStr.trim());
            BigDecimal max = new BigDecimal(maxStr.trim());
            List<Car> filtered = carService.getCarsByPriceRange(min, max);
            if (filtered.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No cars in that price range.");
            }
            showCars(filtered);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Invalid input.");
        }
    }

    private void showUsers(List<User> users) {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Name"});
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getName()});
        }
        statusLabel.setText("👤 Showing " + users.size() + " users");
    }

    private void showBookings(List<CarBooking> bookings) {
        currentDisplayedBookings = bookings;
        currentDisplayedCars = null;
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"Booking ID", "User", "Car", "Time", "Canceled"});
        for (CarBooking b : bookings) {
            tableModel.addRow(new Object[]{
                    b.getBookingId(),
                    b.getUser().getName(),
                    b.getCar().getBrand() + " " + b.getCar().getRegNumber(),
                    b.getBookingTime(),
                    b.isCanceled() ? "Yes" : "No"
            });
        }
        statusLabel.setText("📦 Showing " + bookings.size() + " bookings");
    }

    public static void launchGUI(CarService carService, CarBookingService bookingService, UserService userService) {
        SwingUtilities.invokeLater(() -> {
            CarRentalGUI gui = new CarRentalGUI(carService, bookingService, userService);
            gui.setVisible(true);
        });
    }
}







