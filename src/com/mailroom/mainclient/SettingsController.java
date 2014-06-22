package com.mailroom.mainclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.json.simple.*;
import org.json.simple.parser.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;

import com.mailroom.common.*;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import jfxtras.labs.scene.menu.CirclePopupMenu;


/**
 * Controls SettingsPageFx.fxml in com.mailroom.fxml.mainclient
 * 
 * @author James
 */
public class SettingsController implements Initializable
{
	//Tab Views//
	@FXML
	private Tab tabStopManagement;
	@FXML
	private Tab tabRouteManagement;
	@FXML
	private Tab tabCourierManagement;
	@FXML
	private Tab tabSoftwareUpdate;
	@FXML
	private Tab tabAbout;
	@FXML
	private TabPane tabpaneMainPane;

	//General Settings//
	@FXML
	private Tab tabGeneral;
	@FXML
	private ComboBox<String> cboxDatabaseType; 
	@FXML
	private TextField txtDatabaseLocation;
	@FXML
	private TextField txtDatabaseName;
	@FXML
	private TextField txtDatabaseUserName;
	@FXML
	private PasswordField pwdDatabasePassword;
	@FXML
	private ComboBox<Boolean> cboxAutoUpdate;
	@FXML
	private Button btnBrowse;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	@FXML
	private Slider sldrAutoUpdateFreq;
	
	//Account Management//
	@FXML
	private Tab tabAccountManagement;
	@FXML
	private Tab tabEditAccount;
	@FXML
	private Tab tabCreateAccount;
	@FXML
	private Tab tabAdministrative;
	
	//Edit Account//
	@FXML
	private Label lblUserId;
	@FXML
	private PasswordField pwdChangePwdOld;
	@FXML
	private PasswordField pwdChangePwdNew;
	@FXML
	private PasswordField pwdChangePwdConfirm;
	@FXML
	private Button btnChangePwd;

	//Create Account//
	@FXML
	private TextField txtCreateFirstName;
	@FXML
	private TextField txtCreateLastName;
	@FXML
	private TextField txtCreateUserName;
	@FXML
	private PasswordField pwdCreatePwd;
	@FXML
	private PasswordField pwdCreateConfirm;
	@FXML
	private CheckBox cboxCreateAdmin;
	@FXML
	private Button btnCreateAccount;
	@FXML
	private Button btnCreateCancel;

	//Administrative//
	@FXML
	private ComboBox<User> cboxAdminChange;
	@FXML
	private ComboBox<User> cboxAdminReactivate;
	@FXML
	private ComboBox<User> cboxAdminDeactivate;
	@FXML
	private CheckBox cboxAdminAdminStatus;
	@FXML
	private Button btnAdminChangeSave;
	@FXML
	private PasswordField pwdAdminRePwd;
	@FXML
	private PasswordField pwdAdminReConfirm;
	@FXML
	private Button btnAdminReactivate;
	@FXML
	private Button btnAdminDeactivate;
	
	//Stop Management//
	@FXML
	private ComboBox<Stop> cboxStopDelete;
	@FXML
	private Button btnStopDelete;
	@FXML
	private TextField txtStopName;
	@FXML
	private ComboBox<Route> cboxStopRoute;
	@FXML
	private CheckBox cboxStopCreateStudent;
	@FXML
	private Button btnStopCreate;
	@FXML
	private Button btnStopClear;
	@FXML
	private ComboBox<Stop> cboxStopUpdate;
	@FXML
	private CheckBox cboxStopUpdateStudent;
	@FXML
	private Button btnStopUpdateSave;
	
	//Route Management//
	@FXML
	private ComboBox<Route> cboxRouteSelect;
	@FXML
	private ListView<Stop> lviewRouteUnassigned;
	@FXML
	private ListView<Stop> lviewRouteOnRoute;
	@FXML
	private Button btnRouteAdd;
	@FXML
	private Button btnRouteRemove;
	@FXML
	private ComboBox<Route> cboxRouteDelete;
	@FXML
	private Button btnRouteDelete;
	@FXML
	private TextField txtRouteName;
	@FXML
	private Button btnRouteCreate;
	
	//Courier Management//
	@FXML
	private ComboBox<Courier> cboxCourierDelete;
	@FXML
	private Button btnCourierDelete;
	@FXML
	private TextField txtCourierName;
	@FXML
	private Button btnCourierCreate;
	
	//Software Update//
	@FXML
	private Label lblUpdateCurrentVersion;
	@FXML
	private Label lblUpdateAvailableVersion;
	@FXML
	private Button btnUpdate;
	
	//About Tab//
	@FXML
	private Label lblAboutVersion;
	
	private DatabaseManager dbManager;
	private Properties prefs;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		MainFrame.stage.setTitle("Settings");
		
		this.dbManager = MainFrame.dbManager;
		this.prefs = MainFrame.properties;
		
		cboxDatabaseType.getItems().clear();
		
		switch(Integer.valueOf(prefs.getProperty("DBTYPE")))
		{
			case SQLiteManager.dbId:
			{
				cboxDatabaseType.getItems().add(SQLiteManager.dbName);
				cboxDatabaseType.getItems().add(MysqlManager.dbName);
				cboxDatabaseType.getItems().add(PostgreSQLManager.dbName);
				
				cboxDatabaseType.setValue(cboxDatabaseType.getItems().get(0));
				
				break;
			}
			case MysqlManager.dbId:
			{
				cboxDatabaseType.getItems().add(MysqlManager.dbName);
				cboxDatabaseType.getItems().add(SQLiteManager.dbName);
				cboxDatabaseType.getItems().add(PostgreSQLManager.dbName);
				
				cboxDatabaseType.setValue(cboxDatabaseType.getItems().get(0));
				
				break;
			}
			case PostgreSQLManager.dbId:
			{
				cboxDatabaseType.getItems().add(PostgreSQLManager.dbName);
				cboxDatabaseType.getItems().add(SQLiteManager.dbName);
				cboxDatabaseType.getItems().add(MysqlManager.dbName);
				
				cboxDatabaseType.setValue(cboxDatabaseType.getItems().get(0));
				
				break;
			}
		}
		
		if(MainFrame.cUser.getAdmin())
		{
			tabGeneral.setDisable(false);
			tabCreateAccount.setDisable(false);
			tabAdministrative.setDisable(false);
			tabStopManagement.setDisable(false);
			tabRouteManagement.setDisable(false);
			tabCourierManagement.setDisable(false);
		}
		else
		{
			tabGeneral.setDisable(true);
			tabCreateAccount.setDisable(true);
			tabAdministrative.setDisable(true);
			tabStopManagement.setDisable(true);
			tabRouteManagement.setDisable(true);
			tabCourierManagement.setDisable(true);
			tabpaneMainPane.getSelectionModel().select(tabAccountManagement);
		}
		
		lblUserId.textProperty().set("User ID: " + String.valueOf(MainFrame.cUser.getUserId()));
		
		cboxAutoUpdate.getItems().clear();
		cboxAutoUpdate.getItems().add(true);
		cboxAutoUpdate.getItems().add(false);
		cboxAutoUpdate.setValue(Boolean.valueOf(prefs.getProperty("AUTOUPDATE")));
		sldrAutoUpdateFreq.valueProperty().set(Double.valueOf(prefs.getProperty("AUFREQ")));
		
		lblAboutVersion.setText("Version: " + prefs.getProperty("VERSION") + " Build " + prefs.getProperty("BUILD"));
		
		if(!prefs.getProperty("VERSION").endsWith("InDev") && MainFrame.cUser.getAdmin())
		{
			try
			{
				URL url = new URL("http://minecraft.math.adams.edu/ptracker/version.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Accept",  "txt/plain");
				con.connect();
				
				int statusCode = con.getResponseCode();
				
				if(statusCode == HttpURLConnection.HTTP_OK)
				{
					InputStreamReader isr = new InputStreamReader(con.getInputStream());
					BufferedReader br = new BufferedReader(isr);
					String json = br.readLine();
					
					JSONParser parser = new JSONParser();
					
					Object obj = parser.parse(json);
					JSONObject version = (JSONObject) obj;
					
					String availVersion = version.get("major") + "." + version.get("minor") + "." + version.get("revision");
					String curVersion = prefs.getProperty("VERSION");
					String availBuild = version.get("build").toString();
					String curBuild = prefs.getProperty("BUILD");
					
					if(!availVersion.equals(prefs.getProperty("VERSION")) || !curBuild.equals(availBuild))
					{
						lblUpdateCurrentVersion.setText("Current Version:  " + curVersion + " Build " + curBuild);
						lblUpdateAvailableVersion.setText("Available Version: " + availVersion + " Build " + availBuild);
					}
					else
					{
						tabpaneMainPane.getTabs().remove(tabSoftwareUpdate);
					}
				}
				else
				{
					MessageDialogBuilder.error().message("Error Reaching Update Server").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				}			
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			tabpaneMainPane.getTabs().remove(tabSoftwareUpdate);
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
		
		CirclePopupMenu popup = new CirclePopupMenu(tabpaneMainPane, MouseButton.SECONDARY);
		popup.getItems().add(mainMenu);
		popup.getItems().add(scanMenu);
		popup.getItems().add(searchMenu);
		popup.getItems().add(logoutMenu);
		
		lviewRouteOnRoute.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lviewRouteUnassigned.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		loadAdminComboBoxes();
		loadRouteComboBoxes();
		loadStopComboBoxes();
		loadCourierComboBoxes();
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			btnCancel.fire();
		}
	}
	
	//General Settings//
	public void cboxAutoUpdateAction(ActionEvent ae)
	{
		if(cboxAutoUpdate.getValue())
		{
			sldrAutoUpdateFreq.disableProperty().set(false);
		}
		else
		{
			sldrAutoUpdateFreq.disableProperty().set(true);
		}
		prefs.setProperty("AUTOUPDATE", cboxAutoUpdate.getValue().toString());
		MainFrame.properties.setProperty("AUTOUPDATE", cboxAutoUpdate.getValue().toString());
	}
	
	public void cboxDatabaseTypeAction(ActionEvent ae)
	{
		switch(Integer.valueOf(cboxDatabaseType.getValue().charAt(0)))
		{
			case SQLiteManager.dbId:
			{
				btnBrowse.setDisable(false);
				txtDatabaseLocation.setText("");
				txtDatabaseName.setText("");
				txtDatabaseUserName.setText("");
				pwdDatabasePassword.setText("");
				
				txtDatabaseName.setDisable(true);
				txtDatabaseUserName.setDisable(true);
				pwdDatabasePassword.setDisable(true);
				
				break;
			}
			case MysqlManager.dbId:
			{
				btnBrowse.setDisable(true);
				txtDatabaseLocation.setText("");
				txtDatabaseName.setText("");
				txtDatabaseUserName.setText("");
				pwdDatabasePassword.setText("");
				
				txtDatabaseName.setDisable(false);
				txtDatabaseUserName.setDisable(false);
				pwdDatabasePassword.setDisable(false);
				
				break;
			}
			case PostgreSQLManager.dbId:
			{
				btnBrowse.setDisable(true);
				txtDatabaseLocation.setText("");
				txtDatabaseName.setText("");
				txtDatabaseUserName.setText("");
				pwdDatabasePassword.setText("");
				
				txtDatabaseName.setDisable(false);
				txtDatabaseUserName.setDisable(false);
				pwdDatabasePassword.setDisable(false);
				break;
			}
			default:
			{
				MessageDialogBuilder.error().message("Unsupported Database Selected").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				break;
			}
		}
	}
	
	public void btnBrowseAction(ActionEvent ae)
	{
		FileChooser fChooser = new FileChooser();
		fChooser.setTitle("Select Database File");
		
		File dbFile = fChooser.showOpenDialog(MainFrame.stage);
		
		if(dbFile != null)
		{
			txtDatabaseLocation.setText(dbFile.getAbsolutePath());
		}
		else
		{
			MessageDialogBuilder.error().message("No File Selected").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
	}
	
	public void btnSaveAction(ActionEvent ae)
	{		
		switch(Integer.valueOf(cboxDatabaseType.getValue().charAt(0)))
		{
			case SQLiteManager.dbId:
			{
				MainFrame.properties.setProperty("DBTYPE", String.valueOf(0));
				MainFrame.properties.setProperty("DATABASE", txtDatabaseLocation.getText());
				MainFrame.properties.setProperty("USERNAME", "");
				MainFrame.properties.setProperty("PASSWORD", "");
				MainFrame.properties.setProperty("DBNAME", "");
				MainFrame.properties.setProperty("AUTOUPDATE", Boolean.toString(cboxAutoUpdate.getValue()));
				MainFrame.properties.setProperty("AUFREQ", sldrAutoUpdateFreq.valueProperty().getValue().toString());
				
				break;
			}
			case MysqlManager.dbId:
			{
				MainFrame.properties.setProperty("DBTYPE", String.valueOf(1));
				MainFrame.properties.setProperty("DATABASE", txtDatabaseLocation.getText());
				MainFrame.properties.setProperty("USERNAME", txtDatabaseUserName.getText());
				MainFrame.properties.setProperty("PASSWORD", pwdDatabasePassword.getText());
				MainFrame.properties.setProperty("DBNAME", txtDatabaseName.getText());
				MainFrame.properties.setProperty("AUTOUPDATE", Boolean.toString(cboxAutoUpdate.getValue()));
				MainFrame.properties.setProperty("AUFREQ", sldrAutoUpdateFreq.valueProperty().getValue().toString());
				break;
			}
			case PostgreSQLManager.dbId:
			{
				MainFrame.properties.setProperty("DBTYPE", String.valueOf(2));
				MainFrame.properties.setProperty("DATABASE", txtDatabaseLocation.getText());
				MainFrame.properties.setProperty("USERNAME", txtDatabaseUserName.getText());
				MainFrame.properties.setProperty("PASSWORD", pwdDatabasePassword.getText());
				MainFrame.properties.setProperty("DBNAME", txtDatabaseName.getText());
				MainFrame.properties.setProperty("AUTOUPDATE", Boolean.toString(cboxAutoUpdate.getValue()));
				MainFrame.properties.setProperty("AUFREQ", sldrAutoUpdateFreq.valueProperty().getValue().toString());
				break;
			}
			default:
			{
				MessageDialogBuilder.error().message("Unsupported Database Selected").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				break;
			}
		}
		
		MainFrame.saveProperties();
		
		MessageDialog.Answer restart = MessageDialogBuilder.info().message("Program Must Be Restart!\nRestart Now?").title("Save Complete").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(restart == MessageDialog.Answer.YES_OK)
		{
	        StringBuilder cmd = new StringBuilder();
	        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
	        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) 
	        {
	            cmd.append(jvmArg + " ");
	        }
	        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
	        cmd.append(MainFrame.class.getName()).append(" ");
	        for (String arg : MainFrame.pubArgs)
	        {
	            cmd.append(arg).append(" ");
	        }
	        try
			{
				Runtime.getRuntime().exec(cmd.toString());
			}
			catch (IOException e)
			{
				Logger.log(e);
			}
	        dbManager.dispose();
	        System.exit(0);
		}
		else
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
				e.printStackTrace();
			}
		}
	}
	
	public void btnCancelAction(ActionEvent ae)
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
	
	//Account Management//
	public void btnChangePwdAction(ActionEvent ae)
	{
		int oldPassword = 0;
		int newPassword = 0;
		String username = MainFrame.cUser.getUserName();
		String oldPwd = pwdChangePwdOld.getText();
		String newPwd = pwdChangePwdNew.getText();
		String pwdConfirm = pwdChangePwdConfirm.getText();
		boolean verified = true;
		
		if(pwdChangePwdOld.getText().equals("") || pwdChangePwdNew.getText().equals("") || pwdChangePwdConfirm.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Fields").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			if(oldPwd.equals(newPwd))
			{
				MessageDialogBuilder.error().message("New Password cannot be same as Old Password").title("Password Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				verified = false;
				pwdChangePwdOld.textProperty().set("");
				pwdChangePwdNew.textProperty().set("");
				pwdChangePwdConfirm.textProperty().set("");
			}
			if(!newPwd.equals(pwdConfirm))
			{
				MessageDialogBuilder.error().message("New Password and Confirm Password are not the same").title("Password Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				verified = false;
				pwdChangePwdNew.textProperty().set("");
				pwdChangePwdConfirm.textProperty().set("");
			}
			
			if(verified)
			{
				oldPassword = (username + oldPwd).hashCode();
				newPassword = (username + newPwd).hashCode();
				
				if(dbManager.changePassword(MainFrame.cUser, oldPassword, newPassword))
				{
					MessageDialogBuilder.info().message("Password Changed Successfully\nYou will now be logged out").title("Success").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
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
				else
				{
					MessageDialogBuilder.error().message("Password Change Unsuccessful").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				}
			}
		}
	}
	
	public void btnCreateAccountAction(ActionEvent ae)
	{
		int password;
		String pwd = pwdCreatePwd.getText();
		String pwdConfirm = pwdCreateConfirm.getText();
		if(txtCreateFirstName.getText().equals("") || txtCreateLastName.getText().equals("") || txtCreateUserName.getText().equals("") || pwdCreatePwd.getText().equals("") || pwdCreateConfirm.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Fields").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			if(pwd.equals(pwdConfirm))
			{
				password = txtCreateUserName.getText().hashCode() + pwd.hashCode();
				if(dbManager.addUser(new User(-1,txtCreateUserName.getText(),txtCreateFirstName.getText(),txtCreateLastName.getText(),cboxCreateAdmin.selectedProperty().get()), password))
				{
					MessageDialogBuilder.info().message("User " + txtCreateUserName.getText() + " Added").title("Success").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
					btnCreateCancel.fire();
					loadAdminComboBoxes();
				}
				else
				{
					MessageDialogBuilder.error().message("Error Adding User " + txtCreateUserName.getText()).title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				}
			}
			else
			{
				MessageDialogBuilder.error().message("Passwords Do Not Match").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				pwdCreatePwd.textProperty().set("");
				pwdCreateConfirm.textProperty().set("");
			}
		}
	}
	
	public void btnCreateCancelAction(ActionEvent ae)
	{
		txtCreateFirstName.textProperty().set("");
		txtCreateLastName.textProperty().set("");
		txtCreateUserName.textProperty().set("");
		pwdCreatePwd.textProperty().set("");
		pwdCreateConfirm.textProperty().set("");
		cboxCreateAdmin.selectedProperty().set(false);
	}
	
	public void btnAdminChangeSaveAction(ActionEvent ae)
	{
		dbManager.setUserAdmin(cboxAdminChange.getValue(), cboxAdminAdminStatus.isSelected());
		loadAdminComboBoxes();
	}
	
	public void btnAdminReactivateAction(ActionEvent ae)
	{
		int password = 0;
		String pwd = pwdAdminRePwd.getText();
		String pwdConfirm = pwdAdminReConfirm.getText();
		
		if(pwd.equals(pwdConfirm))
		{
			password = cboxAdminReactivate.getValue().getUserName().hashCode() + pwd.hashCode();
			if(dbManager.reactivateUser(cboxAdminReactivate.getValue(), password))
			{
				MessageDialogBuilder.info().message("User " + cboxAdminReactivate.getValue() + " reactivated").title("Success").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
			}
			else
			{
				MessageDialogBuilder.error().message("Error Reactivating User: " + cboxAdminReactivate.getValue()).title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
			}
			pwdAdminRePwd.setText("");
			pwdAdminReConfirm.setText("");
			loadAdminComboBoxes();
		}
		else
		{
			MessageDialogBuilder.error().message("Password and Confirm Password do not match").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
			pwdAdminRePwd.setText("");
			pwdAdminReConfirm.setText("");
		}
	}
	
	public void btnAdminDeactivateAction(ActionEvent ae)
	{
		if(!MainFrame.cUser.getUserName().equals(cboxAdminDeactivate.getValue()))
		{
			MessageDialog.Answer del = MessageDialogBuilder.warning().message("Delete " + cboxAdminDeactivate.getValue() + "?").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO_CANCEL).yesOkButtonText("Yes").noButtonText("No").cancelButtonText("Cancel").show(MainFrame.stage.getScene().getWindow());
		
			if(del == MessageDialog.Answer.YES_OK)
			{
				dbManager.deleteUser(cboxAdminDeactivate.getValue());
				loadAdminComboBoxes();
			}
		}
		else
		{
			MessageDialogBuilder.warning().message("Cannot Deactivate Yourself").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
	}
	
	private void loadAdminComboBoxes()
	{
		cboxAdminChange.itemsProperty().get().clear();
		cboxAdminReactivate.itemsProperty().get().clear();
		cboxAdminDeactivate.itemsProperty().get().clear();
		
		for(User u : dbManager.getActiveUsers())
		{
			cboxAdminChange.itemsProperty().get().add(u);
			cboxAdminDeactivate.itemsProperty().get().add(u);
		}
		for(User u : dbManager.getDeactivatedUsers())
		{
			cboxAdminReactivate.itemsProperty().get().add(u);
		}
		
		if(cboxAdminChange.getItems().size() > 0)
		{
			cboxAdminChange.setValue(cboxAdminChange.itemsProperty().get().get(0));
		}
		if(cboxAdminDeactivate.getItems().size() > 0)
		{
			cboxAdminDeactivate.setValue(cboxAdminDeactivate.itemsProperty().get().get(0));
		}
		if(cboxAdminReactivate.getItems().size() > 0)
		{
			cboxAdminReactivate.setValue(cboxAdminReactivate.itemsProperty().get().get(0));
		}
	}

	//Stop Management//
	public void btnStopDeleteAction(ActionEvent ae)
	{
		MessageDialog.Answer del = MessageDialogBuilder.confirmation().message("Delete " + cboxStopDelete.getValue() + " Stop").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(del == MessageDialog.Answer.YES_OK)
		{
			dbManager.deleteStop(cboxStopDelete.getValue());
		}
		
		loadStopComboBoxes();
		loadRouteComboBoxes();
	}
	
	public void btnStopCreateAction(ActionEvent ae)
	{
		if(txtStopName.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Stop Name").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			dbManager.addStop(new Stop(-1,txtStopName.getText(),cboxStopRoute.getValue().getRouteName(),1,cboxStopCreateStudent.isSelected()));
			btnStopClear.fire();
			loadStopComboBoxes();
			loadRouteComboBoxes();
		}
	}
	
	public void btnStopClearAction(ActionEvent ae)
	{
		cboxStopCreateStudent.setSelected(false);
		txtStopName.setText("");
	}
	
	public void btnStopUpdateSaveAction(ActionEvent ae)
	{
		dbManager.updateStop(cboxStopUpdate.getValue());
		loadRouteComboBoxes();
	}
	
	private void loadStopComboBoxes()
	{
		cboxStopDelete.getItems().clear();
		cboxStopUpdate.getItems().clear();
		
		dbManager.loadStops();
		
		for(Stop s : dbManager.getStops())
		{
			if(s.getStopId() != 1)
			{
				cboxStopDelete.getItems().add(s);
				cboxStopUpdate.getItems().add(s);
			}
		}
		
		if(cboxStopDelete.getItems().size() > 0)
		{
			cboxStopDelete.setValue(cboxStopDelete.itemsProperty().get().get(0));
		}
		if(cboxStopUpdate.getItems().size() > 0)
		{
			cboxStopUpdate.setValue(cboxStopUpdate.itemsProperty().get().get(0));
		}
	}
	
	//Route Management//
	public void cboxRouteSelectAction(ActionEvent ae)
	{		
		loadRouteListViews();
	}
	
	public void btnRouteDeleteAction(ActionEvent ae)
	{
		MessageDialog.Answer del = MessageDialogBuilder.confirmation().message("Delete " + cboxRouteDelete.getValue() + " Route").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(del == MessageDialog.Answer.YES_OK)
		{
			for(Route r : dbManager.getRoutes())
			{
				if(r.getRouteName().equals(cboxRouteDelete.getValue()))
				{
					dbManager.deleteRoute(r);
					break;
				}
			}
		}
		
		loadRouteComboBoxes();
	}
	
	public void btnRouteCreateAction(ActionEvent ae)
	{
		if(txtRouteName.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Route Name").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			if(dbManager.addRoute(txtRouteName.getText()))
			{
				loadRouteComboBoxes();
				txtRouteName.setText("");
			}
		}
	}
	
	public void btnRouteAddAction(ActionEvent ae)
	{
		List<Stop> selected = lviewRouteUnassigned.selectionModelProperty().getValue().getSelectedItems(); 
		
		for(Stop s : selected)
		{
			dbManager.addStopToRoute(s, cboxRouteSelect.getValue());
		}
		
		loadRouteListViews();
	}
	
	public void btnRouteRemoveAction(ActionEvent ae)
	{
		List<Stop> selected = lviewRouteOnRoute.selectionModelProperty().get().getSelectedItems();
		
		for(Stop s : selected)
		{
			dbManager.addStopToRoute(s, new Route(1,"unassigned"));
		}
		
		loadRouteListViews();
	}
	
	private void loadRouteListViews()
	{
		dbManager.loadStops();
		
		lviewRouteUnassigned.getItems().clear();
		lviewRouteOnRoute.getItems().clear();
		
		for(Stop s : dbManager.getUnassignedStops())
		{
			if(s.getStopId() != 1)
			{
				lviewRouteUnassigned.getItems().add(s);
			}
		}
		
		if(cboxRouteSelect.getValue() != null)
		{
			List<Stop> stops = dbManager.getStopsOnRoute(cboxRouteSelect.getValue());
		
			for(Stop s1 : stops)
			{
				lviewRouteOnRoute.getItems().add(s1);
			}	
		}
		else
		{
			if(cboxRouteSelect.getItems().size() > 0)
			{
				cboxRouteSelect.setValue(cboxRouteSelect.itemsProperty().get().get(0));
			}
		}
	}
	
	private void loadRouteComboBoxes()
	{
		dbManager.loadRoutes();
		
		cboxRouteSelect.getItems().clear();
		cboxRouteDelete.getItems().clear();
		cboxStopRoute.getItems().clear();
		
		for(Route r : dbManager.getRoutes())
		{
			cboxStopRoute.getItems().add(r);
			if(r.getRouteId() != 1)
			{
				cboxRouteSelect.getItems().add(r);
				cboxRouteDelete.getItems().add(r);
			}
		}
		
		if(cboxRouteSelect.getItems().size() > 0)
		{
			cboxRouteSelect.setValue(cboxRouteSelect.itemsProperty().get().get(0));
		}
		if(cboxRouteDelete.getItems().size() > 0)
		{
			cboxRouteDelete.setValue(cboxRouteDelete.itemsProperty().get().get(0));
		}
		if(cboxStopRoute.getItems().size() > 0)
		{
			cboxStopRoute.setValue(cboxStopRoute.itemsProperty().get().get(0));
		}
	}
	
	//Courier Management//
	public void btnCourierDeleteAction(ActionEvent ae)
	{
		MessageDialog.Answer del = MessageDialogBuilder.confirmation().message("Delete " + cboxCourierDelete.getValue() + " Courier").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(del == MessageDialog.Answer.YES_OK)
		{
			for(Courier c : dbManager.getCouriers())
			{
				if(c.getCourierName().equals(cboxCourierDelete.getValue()))
				{
					dbManager.deleteCourier(c);
					break;
				}
			}
		}
		
		loadCourierComboBoxes();
	}
	
	public void btnCourierCreateAction(ActionEvent ae)
	{
		if(txtCourierName.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Courier Name").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			dbManager.addCourier(txtCourierName.getText());
			loadCourierComboBoxes();
			txtCourierName.setText("");
		}
	}
	
	//Software Update//
	public void btnUpdateAction(ActionEvent ae)
	{
		//throw message dialog, 
	}
	
	private void loadCourierComboBoxes()
	{
		dbManager.loadCouriers();
		cboxCourierDelete.getItems().clear();
		
		for(Courier c : dbManager.getCouriers())
		{
			cboxCourierDelete.getItems().add(c);
		}
		
		if(cboxCourierDelete.getItems().size() > 0)
		{
			cboxCourierDelete.setValue(cboxCourierDelete.itemsProperty().get().get(0));
		}
	}
}