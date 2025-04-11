package student.view.gui;

import student.controller.CarRentalGUIController;
import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingService;
import student.model.Car.Car;
import student.model.Car.CarService;
import student.model.User.User;
import student.model.User.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CarRentalGUIView extends JFrame {

    private CarRentalGUIController controller;

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private User currentUser;

    private List<Car> pagedCars = List.of();
    private List<Car> currentDisplayedCars;
    private List<CarBooking> currentDisplayedBookings;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 5;

    public CarRentalGUIView(CarService carService, CarBookingService bookingService, UserService userService) {
        setTitle("🚗 Car Rental System");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    public void setViewListener(CarRentalGUIController controller) {
        this.controller = controller;
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

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

        JButton btnRegisterUser = new JButton("Register User");
        JButton btnLoginUser = new JButton("Login User");
        row3.add(btnRegisterUser);
        row3.add(btnLoginUser);

        JButton viewUsersBtn = new JButton("View Users");
        JButton viewBookingsBtn = new JButton("View Bookings");
        row3.add(viewUsersBtn);
        row3.add(viewBookingsBtn);

        topPanel.add(row1);
        topPanel.add(row2);
        topPanel.add(row3);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

        viewCarsBtn.addActionListener(e -> controller.handleViewAvailableCars());
        viewElectricBtn.addActionListener(e -> controller.handleViewElectricCars());
        sortByPriceBtn.addActionListener(e -> controller.handleSortByPrice());
        filterByPriceBtn.addActionListener(e -> controller.handleFilterByPrice());
        searchKeywordBtn.addActionListener(e -> controller.handleSearchByKeyword());
        viewUsersBtn.addActionListener(e -> controller.handleViewUsers());
        viewBookingsBtn.addActionListener(e -> controller.handleViewBookings());
        bookBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1 && currentDisplayedCars != null) {
                controller.handleBookCar(currentDisplayedCars.get(row));
            }
        });
        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1 && currentDisplayedBookings != null) {
                controller.handleCancelBooking(currentDisplayedBookings.get(row));
            }
        });
        exportBtn.addActionListener(e -> controller.handleExportBookings());
        prevPageBtn.addActionListener(e -> {
            if (currentPage > 0) showCarsPage(currentPage - 1);
        });
        nextPageBtn.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) pagedCars.size() / PAGE_SIZE) - 1;
            if (currentPage < maxPage) showCarsPage(currentPage + 1);
        });

        btnRegisterUser.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter name to register:");
            if (name != null && !name.isBlank()) {
                controller.handleRegisterUser(name.trim());
            }
        });

        btnLoginUser.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter name to login:");
            if (name != null && !name.isBlank()) {
                controller.handleLoginUser(name.trim());
            }
        });
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        JOptionPane.showMessageDialog(this, "👋 Welcome " + user.getName());
        statusLabel.setText("🟢 Logged in: " + user.getName());
    }


    public void showCars(List<Car> cars) {
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

    public void showUsers(List<User> users) {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Name"});
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getName()});
        }
        statusLabel.setText("👤 Showing " + users.size() + " users");
    }

    public void showBookings(List<CarBooking> bookings) {
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
}
