package bams;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		AccountDAO accountdao = new AccountDAO();
		TransactionDAO transactiondao = new TransactionDAO();
		
		System.out.println("🟢⭐Welcome to BAMS⭐🟢");
		
		while(true) {
			System.out.println("\n==========MENU===============");
			System.out.println("1. Create account");
			System.out.println("2. See account detials");
			System.out.println("3. Deposit");
			System.out.println("4. Withdraw");
			System.out.println("5. Check account balance");
			System.out.println("6. Transfer");
			System.out.println("7. View transaction history");
			System.out.println("8. exit");
			System.out.println("Enter an option");
			
			switch(sc.nextInt()) {
				case 1:{
					sc.nextLine();
					System.out.println("Enter account holder name");
					String name = sc.nextLine();
					System.out.println("Enter initial deposit amount");
					double amount;
					try {
						amount = Double.parseDouble(sc.nextLine());
					}catch(NumberFormatException e) {
						System.out.println("Invalid amount");
						break;
					}
					if(amount < 0 || name.isBlank()) {
						System.out.println("Invalid input, Name cannot be blank and amount must not be negative");
						break;
					}
					accountdao.createAccount(name, amount);
					break;
				}
				
				case 2:{
					System.out.println("Enter Account Id to check account detials");
					int id = sc.nextInt();
					accountdao.getAccountDetials(id);
					break;
				}
				
				case 3:{
					System.out.println("Enter Account ID:");
					int id= sc.nextInt();
					System.out.println("Enter amount:");
					double amount = sc.nextDouble();
					if(amount < 0) {
						System.out.println("Invalid amount ");
						break;
					}
					accountdao.deposit(id, amount);
					break;
				}
				
				case 4:{
					System.out.println("Enter Account Id:");
					int id = sc.nextInt();
					System.out.println("Enter amount to withdraw");
					double amount = sc.nextDouble();
					if(amount<0) {
						System.out.println("Invalid amount");
						break;
					}
					accountdao.withdraw(id, amount);
					break;
				}
				
				case 5:{
					System.out.println("Enter Acount Id:");
					int id = sc.nextInt();
					accountdao.checkAccountBalance(id);
					break;
				}
				case 6:{
					System.out.println("Enter account id to transfer from");
					int fromId = sc.nextInt();
					System.out.println("Enter account id to transfer to ");
					int toId = sc.nextInt();
					System.out.println("Enter amount to transer");
					double amount = sc.nextDouble();
					transactiondao.transferMoney(fromId, toId, amount);
					break;
				}
				
				case 7:{
					System.out.println("Enter account Id");
					int id = sc.nextInt();
					transactiondao.getTransactionHistory(id);
					break;
				}
				
				case 8:{
					System.out.println("Exiting ... Thank you for using BAMS");
					sc.close();
					System.exit(0);
				}
				
				default:{
					System.out.println("Invalid option, please try again ");
				}
			}
		}
	}
}
