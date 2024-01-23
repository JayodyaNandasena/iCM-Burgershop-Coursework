import java.util.*;

class Customer{
	private String orderId;
	private String customerID;
	private String name;
	private int quantity;
	private double orderValue;
	private int status;
	
	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerId) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
	
	Customer(String orderId, String customerID, String name, int quantity, double orderValue){
		this.orderId=orderId;
		this.customerID=customerID;
		this.name=name;
		this.quantity=quantity;
		this.orderValue=orderValue;
	}
	
	
}


class Burgershop {
	public static Customer[] customer = new Customer[0];
	public static final double BURGERPRICE = 500.00;
	public static final int PREPARING = 0;
	public static final int DELIVERED = 1;
	public static final int CANCEL = 2;
	public static Scanner input = new Scanner(System.in);

	public static void main(String args[]) {
		mainMenu();
	}

	public static void mainMenu() {
		System.out.println("--------------------------------------------------------------------------");
		System.out.printf("%-30c%-14s%30c%n", '|', "iHungry Burger", '|');
		System.out.println("--------------------------------------------------------------------------");
		String[] options = {
				"[1] Place Order",
				"[2] Search Best Customer",
				"[3] Search Order",
				"[4] Search Customer",
				"[5] View Orders",
				"[6] Update Order Details",
				"[7] Exit"
		};

		for (String i : options) {
			System.out.printf("%n\t%-30s", i);
		}

		while (true) {
			try {
				System.out.print("\n\nEnter an option to continue > ");
				int op = input.nextInt();
				switch (op) {
					case 1:
						clearConsole();
						placeOrder();
						break;
					case 2:
						clearConsole();
						searchBestCustomer();
						break;
					case 3:
						clearConsole();
						searchOrder();
						break;
					case 4:
						clearConsole();
						searchCustomer();
						break;
					case 5:
						clearConsole();
						viewOrder();
						break;
					case 6:
						clearConsole();
						updateOrderDetails();
						break;
					case 7:
						exit();
						break;
					default: {
						System.out.println("\n\tInvalid Input...");
						input.nextLine();
					}
				}
			} catch (Exception e) {
				System.out.println("\n\tInvalid Input...");
				input.nextLine();
			}
		}
	}

	public static void placeOrder() {
		L1: while (true) {
			System.out.println("-----------------------------------------------------------------------");
			System.out.printf("%-30c%-11s%30c%n", '|', "PLACE ORDER", '|');
			System.out.println("-----------------------------------------------------------------------");
			String orderID = generateId();
			System.out.println("\nORDER ID - " + orderID);
			System.out.println("==================");
			String custID = "";

			while (true) {
				System.out.print("\nEnter Customer ID (Phone No.) : ");
				custID = input.next();
				input.nextLine();

				if (custID.matches("^07[0-9]{8}$")) {
					break;
				} else {
					System.out.println("\n\tInvalid input. Please enter a 10-digit number starting with 07");
				}
			}

			String custName = "";
			boolean isExist = false;

			for (int i = 0; i < customer.length; i++) {
				if (customer[i].getCustomerID().equals(custID)) {
					custName = customer[i].getName();
					isExist = true;
				}
			}

			if (isExist) {
				System.out.println("Customer Name : " + custName);
			} else {
				for (;;) {
					System.out.print("Customer Name : ");
					custName = input.next();
					input.nextLine();

					if (!Character.isDigit(custName.charAt(0))) {
						break;
					} else {
						System.out.println("\t\nName cannot start with a number.");
					}
				}
			}

			int qty = 0;
			while (true) {
				try {
					System.out.print("Enter Burger Quantity - ");
					qty = input.nextInt();
					input.nextLine();

					if (qty > 0) {
						break;
					} else {
						System.out.println("\n\tInvalid input. Enter a value greater than 0...\n");
					}
				} catch (InputMismatchException e) {
					System.out.println("\n\tInvalid input. Enter a number...\n");
					input.nextLine();
				}
			}

			double value = BURGERPRICE * qty;

			System.out.printf("Total Value - %.2f%n", value);

			while (true) {
				System.out.print("\tAre you confirm order(Y/N) - ");
				char option = input.next().toUpperCase().charAt(0);
				input.nextLine();
				if (option == 'Y' || option == 'N') {
					if (option == 'Y') {
						extendArrays();
						customer[customer.length - 1]=new Customer(orderID,custID, custName, qty, value);
						System.out.println("\t\nYour order is enter to the system successfully");
					}
					String message = "Do you want to place another order (Y/N) -> ";
					if (checkOption(message)) {
						continue L1;
					}
				} else {
					System.out.println("\n\nInvalid input...");
				}
			}
		}
	}

	public static void searchBestCustomer() {
		System.out.println("-------------------------------------------------------------------------");
		System.out.printf("%-30c%-13s%30c%n", '|', "BEST CUSTOMER", '|');
		System.out.println("-------------------------------------------------------------------------\n\n");
		System.out.println("-------------------------------------------");
		System.out.printf("%-13s%-20s%-6s%n", "CustomerID", "Name", "Total");
		System.out.println("-------------------------------------------");
		
		Customer[] sortCustomer = new Customer[0];

		for (int i = 0; i < customer.length; i++) {
			boolean isExist = false;
			for (int j = 0; j < sortCustomer.length; j++) {
				if (sortCustomer[j].getCustomerID().equals(customer[i].getCustomerID())) {
					if (customer[i].getStatus() != CANCEL) {
						double tot=0;
						sortCustomer[j].setOrderValue(tot+= customer[i].getOrderValue());
					}
					isExist = true;
				}
			}
			if (!isExist) {
				Customer[] tempSortCustomer = new Customer[sortCustomer.length+1];
				for (int j = 0; j < sortCustomer.length; j++) {
					tempSortCustomer[j] = sortCustomer[j];
				}
				sortCustomer = tempSortCustomer;

				sortCustomer[sortCustomer.length - 1] = customer[i];
			}
		}
		// sort
		for (int i = 1; i < sortCustomer.length; i++) {
			for (int j = 0; j < i; j++) {
				if (sortCustomer[j].getOrderValue() < sortCustomer[i].getOrderValue()) {
					Customer temp = sortCustomer[j];
					sortCustomer[j] = sortCustomer[i];
					sortCustomer[i] = temp;
				}
			}
		}
		for (int i = 0; i < sortCustomer.length; i++) {
			String line = String.format("%1s%-14s%-15s%8.2f", " ", sortCustomer[i].getCustomerID(), sortCustomer[i].getName(), sortCustomer[i].getOrderValue());
			System.out.println(line);
			System.out.println("-------------------------------------------");

		}
		L: do {
			System.out.print("\n\tDo you want to go back to main menu? (Y/N)> ");
			char exitOption = input.next().toUpperCase().charAt(0);
			if (exitOption == 'Y') {
				clearConsole();
				mainMenu();
			} else if (exitOption == 'N') {
				clearConsole();
				searchBestCustomer();
			} else {
				System.out.println("\tInvalid option..input again...");
				continue L;
			}
		} while (true);

	}

	public static void searchOrder() {
		L1: while (true) {
			System.out.println("--------------------------------------------------------------------------------");
			System.out.printf("%-30c%-20s%30c%n", '|', "SEARCH ORDER DETAILS", '|');
			System.out.println("--------------------------------------------------------------------------------\n");
			System.out.print("Enter order ID - ");
			String orderID = input.next().toUpperCase();
			input.nextLine();

			int i=0;

			boolean found = false;

			for (i = 0; i < customer.length; i++) {
				if (customer[i].getOrderId().equals(orderID)) {
					found = true;
				}
			}

			if (found) {
				System.out.println("\n---------------------------------------------------------------------");
				System.out.printf("%-10s%-13s%-10s%-10s%-12s%-13s%-1c%n", "OrderID", "CustomerID", "Name", "Quantity",
						"OrderValue", "OrderStatus", '|');
				System.out.println("---------------------------------------------------------------------");
				System.out.printf("%-10s%-13s%-10s%-10d%-12.2f%-13s%-1c%n", customer[i].getOrderId(), customer[i].getCustomerID(), customer[i].getName(), customer[i].getQuantity(), customer[i].getOrderValue(), getStatusString(customer[i].getStatus()),'|');
				System.out.println("---------------------------------------------------------------------\n");
				String message = "Do you want to search another order details? (Y/N)> ";
				if (checkOption(message)) {
					continue L1;
				}
			} else {
				System.out.println("Invalid order ID.");
				String message = "Do you want to enter again? (Y/N)> ";
				if (checkOption(message)) {
					continue L1;
				}
			}
		}
	}

	public static void searchCustomer() {
		L1: while (true) {
			System.out.println("-----------------------------------------------------------------------------------");
			System.out.printf("%-30c%-23s%30c%n", '|', "SEARCH CUSTOMER DETAILS", '|');
			System.out.println("-----------------------------------------------------------------------------------\n");
			System.out.print("Enter customer ID - ");
			String custID = input.next();
			input.nextLine();

			String orderID = "";
			String name = "";
			int qty = 0;
			double value = 0;
			boolean found = false;

			for (int i = 0; i < customer.length; i++) {
				if (customer[i].getCustomerID().equals(custID)) {
					name = customer[i].getName();
					found = true;
				}
			}
			if (found) {
				System.out.printf("%n%-12s%-2c%-10s%n", "Customer ID", '-', custID);
				System.out.printf("%-12s%-2c%-10s%n%n", "Name", '-', name);
				System.out.println("Customer Order details");
				System.out.println("======================\n");
				System.out.println("--------------------------------------------------");
				System.out.printf("%-13s%-18s%-14s%n", "Order_ID", "Order_Quantity", "Total_Value");
				System.out.println("--------------------------------------------------");
				for (int i = 0; i < customer.length; i++) {
					if (customer[i].getCustomerID().equals(custID)) {
						System.out.printf("%-13s%-18d%-14.2f%n", customer[i].getOrderId(), customer[i].getQuantity(), customer[i].getOrderValue());
					}
				}
				System.out.println("--------------------------------------------------");

				String message = "Do you want to search another customer details? (Y/N)> ";
				if (checkOption(message)) {
					continue L1;
				}
			} else {
				System.out.println("\n\tThis customer ID is not added yet...");
				String message = "Do you want to search another customer details? (Y/N)> ";
				if (checkOption(message)) {
					continue L1;
				}
			}
		}
	}

	public static void viewOrder() {
		L1: while (true) {
			System.out.println("---------------------------------------------------------------------------");
			System.out.printf("%-30c%-15s%30c%n", '|', "VIEW ORDER LIST", '|');
			System.out.println("---------------------------------------------------------------------------\n");
			System.out.println("[1] Delivered Order");
			System.out.println("[2] Preparing Order");
			System.out.println("[3] Cancelled Order");

			boolean isValid = false;
			while (!isValid) {
				try {
					System.out.print("\nEnter an option to continue > ");
					int option = input.nextInt();
					input.nextLine();

					switch (option) {
						case 1: {
							System.out.println(
									"\n-------------------------------------------------------------------------");
							System.out.printf("%-30c%-15s%30c%n", '|', "DELIVERED ORDER", '|');
							System.out.println(
									"-------------------------------------------------------------------------\n");
							System.out.println(
									"-------------------------------------------------------------------------");
							System.out.printf("%-10s%-13s%-10s%-10s%-12s%n", "OrderID", "CustomerID", "Name",
									"Quantity", "OrderValue");
							System.out.println(
									"-------------------------------------------------------------------------");
							for (int i = 0; i < customer.length; i++) {
								if (1 == customer[i].getStatus()) {
									System.out.printf("%-10s%-13s%-10s%-10d%.2f%n", customer[i].getOrderId(),
											customer[i].getCustomerID(),
											customer[i].getName(), customer[i].getQuantity(), customer[i].getOrderValue());
									System.out.println(
											"-------------------------------------------------------------------------");
								}
							}
							break;
						}
						case 2: {
							System.out.println(
									"\n--------------------------------------------------------------------------");
							System.out.printf("%-30c%-15s%30c%n", '|', "PREPARING ORDER", '|');
							System.out.println(
									"--------------------------------------------------------------------------\n");
							System.out.println(
									"--------------------------------------------------------------------------");
							System.out.printf("%-10s%-13s%-10s%-10s%-12s%n", "OrderID", "CustomerID", "Name",
									"Quantity", "OrderValue");
							System.out.println(
									"--------------------------------------------------------------------------");
							for (int i = 0; i < customer.length; i++) {
								if (0 == customer[i].getStatus()) {
									System.out.printf("%-10s%-13s%-10s%-10d%.2f%n", customer[i].getOrderId(),
											customer[i].getCustomerID(),
											customer[i].getName(), customer[i].getQuantity(), customer[i].getOrderValue());
									System.out.println(
											"--------------------------------------------------------------------------");
								}
							}
							break;
						}
						case 3: {
							System.out.println(
									"\n-------------------------------------------------------------------------");
							System.out.printf("%-30c%-15s%30c%n", '|', "CANCELLED ORDER", '|');
							System.out.println(
									"-------------------------------------------------------------------------\n");
							System.out.println(
									"-------------------------------------------------------------------------");
							System.out.printf("%-10s%-13s%-10s%-10s%-12s%n", "OrderID", "CustomerID", "Name",
									"Quantity", "OrderValue");
							System.out.println(
									"-------------------------------------------------------------------------");
							for (int i = 0; i < customer.length; i++) {
								if (2 == customer[i].getStatus()) {
									System.out.printf("%-10s%-13s%-10s%-10d%.2f%n", customer[i].getOrderId(),
											customer[i].getCustomerID(),
											customer[i].getName(), customer[i].getQuantity(), customer[i].getOrderValue());
									System.out.println(
											"--------------------------------------------------------------------------");
								}
							}
							break;
						}
						default:
							System.out.println("\n\tInvalid Input...\n");
					}
					isValid = true;
				} catch (Exception e) {
					System.out.println("\n\tInvalid input...");
					input.nextLine();
				}
			}
			while (true) {
				System.out.print("\nDo you want to go to the home page? (Y/N)> ");
				char op = input.next().toUpperCase().charAt(0);
				input.nextLine();
				if (op == 'N') {
					clearConsole();
					continue L1;
				} else if (op == 'Y') {
					clearConsole();
					mainMenu();
				} else {
					System.out.println("Invalid Input.\n");
				}
			}
		}
	}

	public static void updateOrderDetails() {
		L1: while (true) {
			System.out.println("--------------------------------------------------------------------------------");
			System.out.printf("%-30c%-20s%30c%n", '|', "UPDATE ORDER DETAILS", '|');
			System.out.println("--------------------------------------------------------------------------------\n");
			System.out.print("\nEnter order ID - ");
			String orderID = input.next();
			input.nextLine();

			boolean isFound = false;
			int i = 0;
			String stts = "";

			for (i = 0; i < customer.length; i++) {
				if (customer[i].getOrderId().equals(orderID)) {
					stts = getStatusString(customer[i].getStatus());
					isFound = true;
					break;
				}
			}

			if (isFound) {
				if (stts.equals("DELIVERED")) {
					System.out.println("\nThis order is already delivered... You cannot update this order...");
				} else if (stts.equals("CANCEL")) {
					System.out.println("\nThis order is already cancelled... You cannot update this order...");
				} else {
					System.out.printf("%n%-12s%-2c%-23s%n", "OrderID", '-', customer[i].getOrderId());
					System.out.printf("%-12s%-2c%-23s%n", "CustomerID", '-', customer[i].getCustomerID());
					System.out.printf("%-12s%-2c%-23s%n", "Name", '-', customer[i].getName());
					System.out.printf("%-12s%-2c%-5d%n", "Quantity", '-', customer[i].getQuantity());
					System.out.printf("%-12s%-2c%-10.2f%n", "OrderValue", '-', customer[i].getOrderValue());
					System.out.printf("%-12s%-2c%-10s%n", "OrderStatus", '-', stts);

					System.out.println("\nWhat do you want to update?");
					System.out.println("\t(01) Quantity");
					System.out.println("\t(02) Status");

					boolean validOption = false;
					while (!validOption) {
						try {
							System.out.print("\nEnter your option - ");
							int option = input.nextInt();
							input.nextLine();

							if (option == 1) {
								System.out.println("\nQuantity Update");
								System.out.println("===============");
								System.out.printf("%n%-12s%-2c%-23s%n", "OrderID", '-', customer[i].getOrderId());
								System.out.printf("%-12s%-2c%-23s%n", "CustomerID", '-', customer[i].getCustomerID());
								System.out.printf("%-12s%-2c%-23s%n", "Name", '-', customer[i].getName());
								int qty = customer[i].getQuantity();

								L2: while (true) {
									try {
										System.out.print("Enter your quantity update value - ");
										qty = input.nextInt();
										input.nextLine();

										if (qty > 0) {
											customer[i].setQuantity(qty);
											customer[i].setOrderValue(qty * BURGERPRICE);
											break L2;
										} else {
											System.out.println("\n\tInvalid input. Enter a value greater than 0...");
										}

									} catch (InputMismatchException e) {
										System.out.println("\n\tInvalid input. Enter a number...");
										input.nextLine();
									}
								}

								System.out.println("\n\tUpdated order quantity successfully...");
								System.out.println("\nNew order quantity - " + customer[i].getQuantity());
								System.out.printf("New order value - %10.2f%n", customer[i].getOrderValue());

								validOption = true;

							} else if (option == 2) {
								System.out.println("\nStatus Update");
								System.out.println("===============");
								System.out.printf("%n%-12s%-2c%-23s%n", "OrderID", '-', customer[i].getOrderId());
								System.out.printf("%-12s%-2c%-23s%n", "CustomerID", '-', customer[i].getCustomerID());
								System.out.printf("%-12s%-2c%-23s%n", "Name", '-', customer[i].getName());
								System.out.println("\n\t(0)Cancel");
								System.out.println("\t(1)Preparing");
								System.out.println("\t(2)Delivered");

								L3: while (true) {
									try {
										System.out.print("\nEnter new order status - ");
										int newStatus = input.nextInt();
										input.nextLine();

										switch (newStatus) {
											case 0:
												customer[i].setStatus(2);
												break L3;
											case 1:
												customer[i].setStatus(0);
												break L3;
											case 2:
												customer[i].setStatus(1);
												break L3;
											default: {
												System.out.println("\n\tInvalid Input...\n");
												input.nextLine();
											}
										}
									} catch (InputMismatchException e) {
										System.out.println("\n\tInvalid Input...");
										input.nextLine();
									}
								}
								System.out.println("\n\tUpdated order status successfully...");
								System.out.println("\nNew order status - " + getStatusString(customer[i].getStatus()));

								validOption = true;
							} else {
								System.out.println("\n\tInvalid Input...\n");
								input.nextLine();
							}
						} catch (InputMismatchException e) {
							System.out.println("\n\tInvalid Input.\n");
							input.nextLine();
						}
					}
				}
			} else {
				System.out.println("This order ID is not added yet...");
			}

			String message = "Do you want to update another order details? (Y/N)> ";
			if (checkOption(message)) {
				continue L1;
			}
		}
	}

	public static void exit() {
		clearConsole();
		System.out.println("\n\t\tYou Left the program...\n\t\tThank you for using iHungry Burger...\n");
		System.exit(0);
	}

	public static String generateId() {
		if (customer.length > 0) {
			String lastId = customer[customer.length - 1].getOrderId();
			String[] ar = lastId.split("[B]");
			int num = Integer.parseInt(ar[1]);
			num++;
			return String.format("B%03d", num);
		}
		return "B001";
	}

	public static void extendArrays() {
		Customer[] tempCustomer = new Customer[customer.length + 1];

		for (int i = 0; i < customer.length; i++) {
			tempCustomer[i] = customer[i];
		}

		customer = tempCustomer;
	}

	public static String getStatusString(int code) {
		switch (code) {
			case PREPARING:
				return "PREPARING";
			case DELIVERED:
				return "DELIVERED";
			case CANCEL:
				return "CANCEL";
			default:
				return "INVALID";
		}
	}

	public static boolean checkOption(String message) {
		while (true) {
			System.out.print("\n\t" + message);
			char option = input.next().toUpperCase().charAt(0);
			input.nextLine();

			if (option == 'Y') {
				clearConsole();
				return true;
			} else if (option == 'N') {
				clearConsole();
				mainMenu();
				return false;
			} else {
				System.out.println("\n\tInvalid input...");
			}
		}
	}

	public final static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
