package com.mailroom.mainclient;

/**
 * Controls OpenPageFx.fxml in com.mailroom.fxml.mainclient
 * 
 * @author James
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import jfxtras.labs.scene.menu.CirclePopupMenu;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import com.panemu.tiwulfx.table.*;

/**
 * Controls OpenPageFx.fxml in com.mailroom.fxml.mainclient
 * 
 * @author James
 */
public class OpenPageController implements Initializable
{
	private DatabaseManager dbManager;
	private User cUser;
	private AutoUpdater au;
	
	@FXML
	private AnchorPane apaneAnchor;
	@FXML
	private Label lblUserLabel;
	@FXML
	private Button btnScanPackage;
	@FXML
	private Button btnPrint;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnSettings;
	@FXML
	private Button btnRefresh;
	@FXML
	private Button btnQuit;
	@FXML
	private Button btnLogout;
	@FXML
	private Label lblAutoUpdate;
	@FXML
	private TableView<Package> tblViewTable;
	@FXML
	private Label lblTickCount;
	
	//Columns
	private TickColumn<Package> clmnDelivered;
	private TextColumn<Package> clmnFirstName;
	private TextColumn<Package> clmnLastName;
	private ComboBoxColumn<Package, Stop> clmnStop;
	private TextColumn<Package> clmnTrackingNumber;
	private ComboBoxColumn<Package, Courier> clmnCourier;
	private TextColumn<Package> clmnDateReceived;
	private TextColumn<Package> clmnUserName;
	
	private TableControl<Package> tblCntrlTable;
	private OpenTableController<Package> tblController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		MainFrame.stage.setTitle("Main Page");
		
		cUser = MainFrame.cUser;
		dbManager = MainFrame.dbManager;
		
		String name = "Welcome " + cUser.getFirstName() + " " + cUser.getLastName();
		lblUserLabel.setText(name);
		
//		tblViewTable.setEditable(false);
//		
		//Create Columns
		clmnDelivered = new TickColumn<Package>();
		clmnFirstName = new TextColumn<Package>("firstName");
		clmnLastName = new TextColumn<Package>("lastName");
		clmnStop = new ComboBoxColumn<Package, Stop>("stop");
		clmnTrackingNumber = new TextColumn<Package>("trackingNumber");
		clmnCourier = new ComboBoxColumn<Package, Courier>("courier");
		clmnDateReceived = new TextColumn<Package>("receivedDate");
		clmnUserName = new TextColumn<Package>("user");
		
		for(Stop s : dbManager.getStops())
		{
			clmnStop.addItem(s.toString(), s);
		}
		
		//Set Resizable False
		clmnDelivered.setResizable(false);
		clmnFirstName.setResizable(false);
		clmnLastName.setResizable(false);
		clmnStop.setResizable(false);
		clmnTrackingNumber.setResizable(false);
		clmnCourier.setResizable(false);
		clmnDateReceived.setResizable(false);
		clmnUserName.setResizable(false);
		
		//Set Titles
		clmnFirstName.setText("First");
		clmnLastName.setText("Last");
		clmnStop.setText("Stop");
		clmnTrackingNumber.setText("Tracking");
		clmnCourier.setText("Carrier");
		clmnDateReceived.setText("Date");
		clmnUserName.setText("User");
		
		//Define Max Width
		clmnDelivered.setMaxWidth(30);
		clmnFirstName.setMaxWidth(70);
		clmnLastName.setMaxWidth(70);
		clmnStop.setMaxWidth(100); //wider because of data contained
		clmnTrackingNumber.setMaxWidth(70);
		clmnCourier.setMaxWidth(70);
		clmnDateReceived.setMaxWidth(100); //wider because of data contained
		clmnUserName.setMaxWidth(75);
//		
//		//Add Columns
//		tblViewTable.getColumns().add(clmnDelivered);
//		tblViewTable.getColumns().add(clmnFirstName);
//		tblViewTable.getColumns().add(clmnLastName);
//		tblViewTable.getColumns().add(clmnStop);
//		tblViewTable.getColumns().add(clmnTrackingNumber);
//		tblViewTable.getColumns().add(clmnCourier);
//		tblViewTable.getColumns().add(clmnDateReceived);
//		tblViewTable.getColumns().add(clmnUserName);
//		
		tblCntrlTable = new TableControl<Package>();
		tblCntrlTable.minWidth(500);
		tblCntrlTable.minHeight(625);
		tblCntrlTable.setLayoutX(170);
		tblCntrlTable.setLayoutY(14);
		tblCntrlTable.setVisible(true);
		tblCntrlTable.setRecordClass(Package.class);

		tblCntrlTable.addColumn(clmnDelivered);
		tblCntrlTable.addColumn(clmnFirstName);
		tblCntrlTable.addColumn(clmnLastName);
		tblCntrlTable.addColumn(clmnStop);
		tblCntrlTable.addColumn(clmnTrackingNumber);
		tblCntrlTable.addColumn(clmnCourier);
		tblCntrlTable.addColumn(clmnDateReceived);
		tblCntrlTable.addColumn(clmnUserName);
		
		apaneAnchor.getChildren().add(tblCntrlTable);
		
		tblViewTable.setVisible(false);
//		
//		lblTickCount.setText(clmnDelivered.getTickedRecords().size() + " Selected");
		
		dbManager.loadAllPackages();
		
//		if(dbManager.getPackages().size() == 0)
//		{
//			Package p = new Package(-1, "", "", "", "", "", "", null, null, null, false, false, "", false);
//			tblViewTable.getItems().add(p);
//		}
//		else
//		{
//			tblViewTable.getItems().addAll(dbManager.getPackages());
//		}
		
		tblController = new OpenTableController<Package>();
		
		if(dbManager.getPackages().size() == 0)
		{
			Package p = new Package(-1, "", "", "", "", "", "", null, null, null, false, false, "", false);
			tblViewTable.getItems().add(p);
		}
		else
		{
			tblController.insert(dbManager.getPackages());
		}
		
		if(Boolean.valueOf(MainFrame.properties.getProperty("AUTOUPDATE")))
		{
			lblAutoUpdate.setText("Auto Update Enabled");
			au = new AutoUpdater(btnRefresh);
		}
		else
		{
			lblAutoUpdate.setText("Auto Update Disabled");
			au = null;
		}
		
		//Popup Menu//
		MenuItem mainMenu = new MenuItem("Main Menu", new ImageView(new Image(this.getClass().getResourceAsStream("/com/mailroom/resources/icon_main.png"))));
		mainMenu.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						try
						{
							Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/OpenPageFx.fxml"));
							Scene scene = new Scene(root);
							MainFrame.stage.setScene(scene);
						}
						catch(IOException e)
						{
							Logger.log(e);
						}
					}
				}
			);
		MenuItem scanMenu = new MenuItem("Scan Package", new ImageView(new Image(this.getClass().getResourceAsStream("/com/mailroom/resources/icon_scan.png"))));
		scanMenu.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						try
						{
							Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/ScanPageFx.fxml"));
							Scene scene = new Scene(root);
							MainFrame.stage.setScene(scene);
						}
						catch(IOException e)
						{
							Logger.log(e);
						}
					}
				}
			);
		MenuItem searchMenu = new MenuItem("Search", new ImageView(new Image(this.getClass().getResourceAsStream("/com/mailroom/resources/icon_search.png"))));
		searchMenu.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						try
						{
							Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/common/AdvSearchFx.fxml"));
							Scene scene = new Scene(root);
							MainFrame.stage.setScene(scene);
						}
						catch(IOException e)
						{
							Logger.log(e);
						}
					}
				}
			);
		MenuItem logoutMenu = new MenuItem("Logout", new ImageView(new Image(this.getClass().getResourceAsStream("/com/mailroom/resources/icon_logout.png"))));
		logoutMenu.setOnAction(new EventHandler<ActionEvent>()
				{
					public void handle(ActionEvent ae)
					{
						try
						{
							Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/LoginFx.fxml"));
							Scene scene = new Scene(root);
							MainFrame.stage.setScene(scene);
						}
						catch(IOException e)
						{
							Logger.log(e);
						}
					}
				}
			);
		
		CirclePopupMenu popup = new CirclePopupMenu(apaneAnchor, MouseButton.SECONDARY);
		popup.getItems().add(mainMenu);
		popup.getItems().add(scanMenu);
		popup.getItems().add(searchMenu);
		popup.getItems().add(logoutMenu);
	}
	
	public void btnScanPackageAction(ActionEvent ae)
	{
		try
		{
			if(au != null)
			{
				au.stop();
			}
			Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/ScanPageFx.fxml"));
			Scene scene = new Scene(root);
			MainFrame.stage.setScene(scene);
		}
		catch(IOException e)
		{
			Logger.log(e);
			e.printStackTrace();
		}
	}
	
	public void btnPrintAction(ActionEvent ae)
	{
		try
		{
			if(au != null)
			{
				au.stop();
			}
			Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/PrintPageFx.fxml"));
			Scene scene = new Scene(root);
			MainFrame.stage.setScene(scene);
		}
		catch(IOException e)
		{
			Logger.log(e);
		}
	}
	
	public void btnSearchAction(ActionEvent ae)
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/common/AdvSearchFx.fxml"));
			Scene scene = new Scene(root);
			MainFrame.stage.setScene(scene);
		}
		catch(IOException e)
		{
			Logger.log(e);
		}
	}
	
	public void btnRefreshAction(ActionEvent ae)
	{
		//refresh current scene
		//update packages
		//reload packages
		
		ObservableList<Package> delivered = (ObservableList<Package>)clmnDelivered.getTickedRecords();
		
		for(Package p : delivered)
		{
			dbManager.updatePackage(p.getPackageId(), true, true);
		}
		
		dbManager.loadAllPackages();
		
		tblViewTable.getItems().clear();
		
		if(dbManager.getPackages().size() == 0)
		{
			Package p = new Package(-1, "", "", "", "", "", "", null, null, null, false, false, "", false);
			tblViewTable.getItems().add(p);
		}
		else
		{
			tblViewTable.getItems().addAll(dbManager.getPackages());
		}
	}
	
	public void btnSettingsAction(ActionEvent ae)
	{
		try
		{
			if(au != null)
			{
				au.stop();
			}
			Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/SettingsPageFx.fxml"));
			Scene scene = new Scene(root);
			MainFrame.stage.setScene(scene);
		}
		catch(IOException e)
		{
			Logger.log(e);
			e.printStackTrace();
		}
	}
	
	public void btnLogoutAction(ActionEvent ae)
	{		
		MessageDialog.Answer a = MessageDialogBuilder.confirmation().message("Confirm Logout?").title("Logout").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(a == MessageDialog.Answer.YES_OK)
		{
			try
			{
				if(au != null)
				{
					au.stop();
				}
				Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/LoginFx.fxml"));
				Scene scene = new Scene(root);
				MainFrame.stage.setScene(scene);
			}
			catch(IOException e)
			{
				Logger.log(e);
			}
		}
	}
	
	public void btnQuitAction(ActionEvent ae)
	{		
		MessageDialog.Answer a = MessageDialogBuilder.confirmation().message("Confirm Quit?").title("Quit").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(a == MessageDialog.Answer.YES_OK)
		{
			MainFrame.saveProperties();
			
			if(au != null)
			{
				au.stop();
			}
			
			dbManager.dispose();
			
			System.exit(0);
		}
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.Q)
		{
			if(!ke.getSource().equals(tblViewTable))
			{
				btnLogout.fire();
			}
		}
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			btnQuit.fire();
		}
		if(ke.getCode() == KeyCode.R)
		{
			btnRefresh.fire();
		}
	}
	
	private class AutoUpdater implements Runnable
	{
		Button btn = null;
		private boolean running;
		
		public AutoUpdater(Button btn)
		{
			this.btn = btn;
			new Thread(this).start();
			running = true;
		}
		
		public void run()
		{
			while(running)
			{
				try
				{
					Thread.sleep((long) (Double.valueOf(MainFrame.properties.getProperty("AUFREQ")) * 1000));
					btn.fire();
				}
				catch (NumberFormatException e)
				{
					Logger.log(e);
				}
				catch (InterruptedException e)
				{
					Logger.log(e);
				}
			}
		}
		
		public void stop()
		{
			running = false;
		}
	}
}
