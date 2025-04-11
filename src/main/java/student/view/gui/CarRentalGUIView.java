// Dark-themed, macOS-style GUI for CarRental System
package student.view.gui;

import student.controller.CarRentalGUIController;
import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingService;
import student.model.Car.Car;
import student.model.Car.CarService;
import student.model.User.User;
import student.model.User.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CarRentalGUIView extends JFrame {
    private CarRentalGUIController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel pageLabel;
    private JButton prevPageBtn, nextPageBtn;
    private JButton loginBtn, registerBtn, myBookingsBtn;
    private JButton bookBtn, cancelBtn, exportBtn;
    private JButton viewCarsBtn, viewElectricBtn, searchBtn, filterBtn, sortBtn;
    private JTextField searchField;
    private User currentUser;
    private List<Car> pagedCars = List.of();
    private List<Car> currentDisplayedCars;
    private List<CarBooking> currentDisplayedBookings;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 10;

    public CarRentalGUIView(CarService carService, CarBookingService bookingService, UserService userService) {
        setTitle("🚗 Car Rental System");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUITheme();
        initUI();
    }

    private void setUITheme() {
        UIManager.put("control", Color.WHITE);
        UIManager.put("info", Color.LIGHT_GRAY);
        UIManager.put("nimbusBase", new Color(230, 230, 230));
        UIManager.put("nimbusBlueGrey", new Color(200, 200, 200));
        UIManager.put("nimbusLightBackground", Color.WHITE);
        UIManager.put("text", Color.BLACK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("Table.selectionBackground", new Color(180, 200, 255));
        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("Table.gridColor", new Color(210, 210, 210));
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(230, 230, 230));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(200, 200, 200));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(230, 230, 230));
            }
        });
        return button;
    }

    public void setViewListener(CarRentalGUIController controller) {
        this.controller = controller;
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(mainPanel.getBackground());

        searchField = new JTextField(15);
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setForeground(Color.BLACK);
        searchField.setCaretColor(Color.BLACK);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        searchBtn = createStyledButton("🔍", "Search keyword");
        sortBtn = createStyledButton("⬆ Price", "Sort by price");
        filterBtn = createStyledButton("💰 Filter", "Filter by price range");
        viewCarsBtn = createStyledButton("🚗 All Cars", "View all cars");
        viewElectricBtn = createStyledButton("🔌 Electric", "View electric cars");

        searchField = new JTextField(16);
        searchField.setPreferredSize(new Dimension(160, 28));
        topPanel.add(searchField);

        topPanel.add(viewCarsBtn);
        topPanel.add(viewElectricBtn);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(filterBtn);
        topPanel.add(sortBtn);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom control area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(mainPanel.getBackground());

        // Left: Login/User Buttons
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftControls.setBackground(mainPanel.getBackground());
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");
        myBookingsBtn = new JButton("My Bookings");
        leftControls.add(loginBtn);
        leftControls.add(registerBtn);
        leftControls.add(myBookingsBtn);

        // Center: Page nav
        JPanel centerControls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerControls.setBackground(mainPanel.getBackground());
        prevPageBtn = new JButton("<");
        pageLabel = new JLabel("Page 1 of 1");
        pageLabel.setForeground(Color.LIGHT_GRAY);
        nextPageBtn = new JButton(">");
        centerControls.add(prevPageBtn);
        centerControls.add(pageLabel);
        centerControls.add(nextPageBtn);

        // Right: Book/Cancel/Export
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightControls.setBackground(mainPanel.getBackground());
        bookBtn = new JButton("Book");
        cancelBtn = new JButton("Cancel");
        exportBtn = new JButton("Export");
        rightControls.add(bookBtn);
        rightControls.add(cancelBtn);
        rightControls.add(exportBtn);

        // Status Bar
        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(new Color(0, 200, 100));
        statusLabel.setBorder(new EmptyBorder(5, 5, 0, 0));

        bottomPanel.add(leftControls, BorderLayout.WEST);
        bottomPanel.add(centerControls, BorderLayout.CENTER);
        bottomPanel.add(rightControls, BorderLayout.EAST);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        // --- ACTION LISTENERS ---
        viewCarsBtn.addActionListener(e -> controller.handleViewAvailableCars());
        viewElectricBtn.addActionListener(e -> controller.handleViewElectricCars());
        sortBtn.addActionListener(e -> controller.handleSortByPrice());
        filterBtn.addActionListener(e -> controller.handleFilterByPrice());
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                controller.handleSearchByKeyword(keyword);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a keyword to search.");
            }
        });
        searchField.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                controller.handleSearchByKeyword(keyword);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a keyword to search.");
            }
        });
        exportBtn.addActionListener(e -> controller.handleExportBookings());
        registerBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter name to register:");
            if (name != null && !name.isBlank()) controller.handleRegisterUser(name.trim());
        });
        loginBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter name to login:");
            if (name != null && !name.isBlank()) controller.handleLoginUser(name.trim());
        });
        myBookingsBtn.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "❌ Please login to view your bookings.");
                return;
            }
            controller.handleViewMyBookings(currentUser);
        });
        bookBtn.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "❌ Please login before booking.");
                return;
            }
            int row = table.getSelectedRow();
            if (row != -1 && currentDisplayedCars != null) {
                controller.handleBookCarAs(currentDisplayedCars.get(row), currentUser);
            }
        });
        cancelBtn.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "❌ Please login to cancel bookings.");
                return;
            }
            int row = table.getSelectedRow();
            if (row == -1 || currentDisplayedBookings == null) {
                JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
                return;
            }
            CarBooking booking = currentDisplayedBookings.get(row);
            if (!booking.getUser().getId().equals(currentUser.getId())) {
                JOptionPane.showMessageDialog(this, "❌ You can only cancel your own bookings.");
                return;
            }
            controller.handleCancelBooking(booking);
        });
        prevPageBtn.addActionListener(e -> {
            if (currentPage > 0) showCarsPage(currentPage - 1);
        });
        nextPageBtn.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) pagedCars.size() / PAGE_SIZE) - 1;
            if (currentPage < maxPage) showCarsPage(currentPage + 1);
        });
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        JOptionPane.showMessageDialog(this, "👋 Welcome " + user.getName());
        statusLabel.setText("🟢 Logged in: " + user.getName() + " | Ready to book");
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
        updatePagination(currentPage, totalPages, pagedCars.size());
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

    public void showUsers(List<User> users) {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Name"});
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getName()});
        }
        statusLabel.setText("👤 Showing " + users.size() + " users");
    }

    public void updatePagination(int page, int totalPages, int totalCars) {
        currentPage = page;
        pageLabel.setText("Page " + (page + 1) + " of " + totalPages);
        prevPageBtn.setEnabled(page > 0);
        nextPageBtn.setEnabled(page + 1 < totalPages);
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}
