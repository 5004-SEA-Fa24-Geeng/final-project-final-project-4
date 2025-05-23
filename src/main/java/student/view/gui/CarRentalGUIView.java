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

/**
 * Graphical User Interface (GUI) view for the Car Rental System.
 * <p>
 * This class builds a modern, dark-themed Swing-based interface that allows
 * users to browse, search, book, cancel, and export car bookings.
 * It represents the View layer in an MVC architecture and communicates with
 * {@link student.controller.CarRentalGUIController} for user actions.
 * </p>
 *
 * Features include:
 * <ul>
 *   <li>Car listing with pagination</li>
 *   <li>Search, sort, and filter operations</li>
 *   <li>User login and registration</li>
 *   <li>Booking and cancellation</li>
 *   <li>Exporting bookings to CSV</li>
 * </ul>
 */
public class CarRentalGUIView extends JFrame {

    /** Controller that handles all GUI event interactions. */
    private CarRentalGUIController controller;

    /** Table component for displaying cars, users, or bookings. */
    private JTable table;

    /** Backing model for the JTable. */
    private DefaultTableModel tableModel;

    /** Status label displayed at the bottom of the GUI. */
    private JLabel statusLabel;

    /** Label showing current pagination status (e.g., "Page 1 of 3"). */
    private JLabel pageLabel;

    /** Pagination buttons for navigating car pages. */
    private JButton prevPageBtn, nextPageBtn;

    /** User-related buttons: login, registration, and viewing personal bookings. */
    private JButton loginBtn, registerBtn, myBookingsBtn;

    /** Action buttons: book a car, cancel a booking, export bookings. */
    private JButton bookBtn, cancelBtn, exportBtn;

    /** Car browsing controls: show all, electric only, search, filter, sort. */
    private JButton viewCarsBtn, viewElectricBtn, searchBtn, filterBtn, sortBtn;

    /** Text field for keyword search. */
    private JTextField searchField;

    /** Currently logged-in user (null if not logged in). */
    private User currentUser;

    /** All cars available for pagination display. */
    private List<Car> pagedCars = List.of();

    /** Cars currently shown in the table (maybe a page subset). */
    private List<Car> currentDisplayedCars;

    /** Bookings currently shown in the table. */
    private List<CarBooking> currentDisplayedBookings;

    /** Index of the currently displayed car page. */
    private int currentPage = 0;

    /** Maximum number of cars displayed per page. */
    private static final int PAGE_SIZE = 18;

    /**
     * Constructs the CarRental GUI view and initializes its layout and appearance.
     * <p>
     * This sets up the window size, title, theme, and calls the internal UI builder.
     * </p>
     *
     * @param carService       the car service used for car-related actions (passed but not used directly here)
     * @param bookingService   the booking service used for reservation logic
     * @param userService      the user service for login/registration
     */
    public CarRentalGUIView(CarService carService, CarBookingService bookingService, UserService userService) {
        setTitle("🚗 Car Rental System");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUITheme();
        initUI();
    }

    /**
     * Applies a custom macOS-style light theme using Swing's UIManager settings.
     * <p>
     * This method sets fonts, colors, table styles, and control look-and-feel
     * to give the application a consistent and modern aesthetic.
     * </p>
     */
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

    /**
     * Creates a uniformly styled JButton with custom color, cursor, hover effects,
     * and a tooltip for enhanced UX.
     *
     * @param text    the button label text (can include emojis/icons)
     * @param tooltip the tooltip text shown on hover
     * @return the styled {@link JButton} instance
     */
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

    /**
     * Sets the controller that handles user actions triggered from the GUI.
     *
     * @param controller the controller to bind as listener
     */
    public void setViewListener(CarRentalGUIController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the entire GUI layout and component structure.
     * <p>
     * This method sets up:
     * <ul>
     *   <li>Main layout and color scheme</li>
     *   <li>Top control panel with car filters, search, sort, etc.</li>
     *   <li>Center table for displaying cars, users, or bookings</li>
     *   <li>Bottom control panel for pagination, user login, and booking actions</li>
     * </ul>
     *
     * It also registers all event listeners that connect UI buttons to controller logic
     * in {@link student.controller.CarRentalGUIController}.
     * </p>
     */
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create top panel with two rows: browsing and filtering
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBackground(mainPanel.getBackground());

        // First row: browsing buttons and search box
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.setBackground(mainPanel.getBackground());

        // Second row: filtering/sorting
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.setBackground(mainPanel.getBackground());

        // Create search field
        searchField = new JTextField(16);
        searchField.setPreferredSize(new Dimension(160, 28));
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setForeground(Color.BLACK);
        searchField.setCaretColor(Color.BLACK);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Styled buttons
        searchBtn = createStyledButton("🔍", "Search keyword");
        sortBtn = createStyledButton("⬆ Price", "Sort by price");
        filterBtn = createStyledButton("💰 Filter", "Filter by price range");
        viewCarsBtn = createStyledButton("🚗 All Cars", "View all cars");
        viewElectricBtn = createStyledButton("🔌 Electric", "View electric cars");

        // Add to respective rows
        row1.add(viewCarsBtn);
        row1.add(viewElectricBtn);
        row1.add(searchField);
        row1.add(searchBtn);

        row2.add(filterBtn);
        row2.add(sortBtn);

        // Add rows to top panel
        topPanel.add(row1);
        topPanel.add(row2);

        // Wrap the topPanel into a card-like panel
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setBackground(mainPanel.getBackground());
        cardWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        cardWrapper.add(topPanel, BorderLayout.CENTER);

        // Add to main panel
        mainPanel.add(cardWrapper, BorderLayout.NORTH);

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

    /**
     * Sets the currently logged-in user and updates the status bar.
     *
     * @param user the user who has logged in
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        JOptionPane.showMessageDialog(this, "👋 Welcome " + user.getName());
        statusLabel.setText("🟢 Logged in: " + user.getName() + " | Ready to book");
    }

    /**
     * Displays the given list of cars in the table with pagination.
     * Resets current page to 0.
     *
     * @param cars the list of cars to display
     */
    public void showCars(List<Car> cars) {
        this.pagedCars = cars;
        showCarsPage(0);
    }

    /**
     * Displays a specific page of the currently loaded car list.
     *
     * @param pageIndex the index of the page to show (0-based)
     */
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

        statusLabel.setText(String.format(
                "📄 Page %d of %d | Total cars: %d",
                currentPage + 1,
                totalPages,
                pagedCars.size()
        ));

    }

    /**
     * Displays a list of bookings in the table, including canceled status.
     *
     * @param bookings the list of car bookings to display
     */
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

    /**
     * Displays all users in the table.
     *
     * @param users the list of users to display
     */
    public void showUsers(List<User> users) {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Name"});
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getName()});
        }
        statusLabel.setText("👤 Showing " + users.size() + " users");
    }

    /**
     * Updates the pagination label and enables/disables navigation buttons accordingly.
     *
     * @param page       the current page index
     * @param totalPages the total number of pages
     * @param totalCars  the total number of cars available
     */
    public void updatePagination(int page, int totalPages, int totalCars) {
        currentPage = page;
        pageLabel.setText("Page " + (page + 1) + " of " + totalPages);
        prevPageBtn.setEnabled(page > 0);
        nextPageBtn.setEnabled(page + 1 < totalPages);
    }

    /**
     * Updates the status label text shown at the bottom of the interface.
     *
     * @param message the new status message
     */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}
