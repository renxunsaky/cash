package com.surpassun.cash.fx.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.domain.Client;
import com.surpassun.cash.repository.ClientRepository;
import com.surpassun.cash.util.CashUtil;
import com.surpassun.cash.util.LanguageUtil;

@Component
public class ClientController extends SimpleController {
	
	private final Logger log = LoggerFactory.getLogger(ClientController.class);
	
	@Inject
	private ClientRepository clientRepository;
	
	@FXML
	TableView<Client> clientList;
	@FXML
	TableColumn<Client, String> clientFirstName;
	@FXML
	TableColumn<Client, String> clientLastName;
	@FXML
	TableColumn<Client, String> clientEmail;
	@FXML
	TableColumn<Client, String> clientPhone;
	@FXML
	TableColumn<Client, String> clientAddress;
	@FXML
	TableColumn<Client, String> clientPostCode;
	@FXML
	TableColumn<Client, String> clientCity;
	@FXML
	TableColumn<Client, String> clientCode;
	@FXML
	TableColumn<Client, Float> clientTotalConsumation;

	public void show(Stage stage) {
		super.show(this, stage, Constants.FXML_DESIGN_CLIENT);
		init();
	}

	private void init() {
		log.info("Initializing client list");
		clientFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
		clientFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
		clientLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
		clientLastName.setCellFactory(TextFieldTableCell.forTableColumn());
		clientEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		clientEmail.setCellFactory(TextFieldTableCell.forTableColumn());
		clientPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		clientPhone.setCellFactory(TextFieldTableCell.forTableColumn());
		clientAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		clientAddress.setCellFactory(TextFieldTableCell.forTableColumn());
		clientPostCode.setCellValueFactory(new PropertyValueFactory<>("postcode"));
		clientPostCode.setCellFactory(TextFieldTableCell.forTableColumn());
		clientCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		clientCity.setCellFactory(TextFieldTableCell.forTableColumn());
		clientCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		clientCode.setCellFactory(TextFieldTableCell.forTableColumn());
		clientTotalConsumation.setCellValueFactory(new PropertyValueFactory<>("totalConsumation"));
		clientTotalConsumation.setCellFactory(TextFieldTableCell.<Client, Float>forTableColumn(new FloatStringConverter()));
		
		List<Client> clients = clientRepository.findAll();
		if (clients != null && clients.size() > 0) {
			log.info("{} clients found", clients.size());
			clientList.getItems().addAll(clients);
			clientList.getSelectionModel().select(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	public void addClient() {
		int newItemIndex = clientList.getItems().size();
		Client emptyClient = new Client("Toto", "Toto", "00000000");
		clientList.getItems().add(newItemIndex, emptyClient);
		clientList.getSelectionModel().select(newItemIndex);
		clientList.requestFocus();
		clientList.getFocusModel().focus(newItemIndex, clientList.getColumns().get(0));
		clientList.scrollTo(newItemIndex);
		clientList.edit(clientList.getFocusModel().getFocusedCell().getRow(), 
				clientList.getFocusModel().getFocusedCell().getTableColumn());
	}
	
	@FXML
	public void updateClient(CellEditEvent<Client, Object> event) {
		Object newValue = event.getNewValue();
		Client client = event.getRowValue();
		int columnIndex = event.getTablePosition().getColumn();
		if (columnIndex == 0) {
			client.setFirstname((String)newValue);
		} else if (columnIndex == 1) {
			client.setLastname((String)newValue);
		} else if (columnIndex == 2) {
			client.setEmail((String)newValue);
		} else if (columnIndex == 3) {
			client.setPhone((String)newValue);
		} else if (columnIndex == 4) {
			client.setAddress((String)newValue);
		} else if (columnIndex == 5) {
			client.setPostcode((String)newValue);
		} else if (columnIndex == 6) {
			client.setCity((String)newValue);
		} else if (columnIndex == 7) {
			client.setTotalConsumation((Float)newValue);
		} else if (columnIndex == 8) {
			client.setCode((String)newValue);
		}
		clientList.getItems().set(event.getTablePosition().getRow(), client);
		try {
			if (StringUtils.isBlank(client.getCode()) || client.getCode().equals("00000000")) {
				CashUtil.createWarningPopup(LanguageUtil.getMessage("ui.title.common.warning"), LanguageUtil.getMessage("ui.popup.header.client.code.empty"),
						LanguageUtil.getMessage("ui.popup.content.client.code.empty"));
				return;
			}
			if (StringUtils.isBlank(client.getFirstname()) || client.getFirstname().equals("Toto")) {
				CashUtil.createWarningPopup(LanguageUtil.getMessage("ui.title.common.warning"), LanguageUtil.getMessage("ui.popup.header.client.firstname.empty"),
						LanguageUtil.getMessage("ui.popup.content.client.firstname.empty"));
				return;
			}
			if (StringUtils.isBlank(client.getLastname()) || client.getLastname().equals("Toto")) {
				CashUtil.createWarningPopup(LanguageUtil.getMessage("ui.title.common.warning"), LanguageUtil.getMessage("ui.popup.header.client.lastname.empty"),
						LanguageUtil.getMessage("ui.popup.content.client.lastname.empty"));
				return;
			}
			
			client.setCreatedBy("1");
			clientRepository.save(client);
		} catch (Exception e) {
			log.warn("Error while saving client {}", client.getLastname());
		}
	}
	
	@FXML
	public void deleteClient() {
		int index = clientList.getSelectionModel().getSelectedIndex();
		Client client = clientList.getSelectionModel().getSelectedItem();
		clientList.getItems().remove(index);
		clientRepository.delete(client);
		log.info("Client {} deleted", client.getLastname());
	}

}
