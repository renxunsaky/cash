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
import javafx.util.converter.IntegerStringConverter;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.surpassun.cash.config.Constants;
import com.surpassun.cash.domain.Category;
import com.surpassun.cash.domain.Product;
import com.surpassun.cash.repository.CategoryRepository;
import com.surpassun.cash.repository.ProductRepository;

@Component
public class ProductController extends SimpleController {

	private final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Inject
	private CategoryRepository categoryRepository;
	@Inject
	private ProductRepository productRepository;
	
	@FXML
	TableView<Category> categoryList;
	@FXML
	TableColumn<Category, String> categoryName;
	@FXML
	TableColumn<Category, String> categoryCode;
	@FXML
	TableColumn<Category, Float> categoryDiscount;
	
	@FXML
	TableView<Product> productList;
	@FXML
	TableColumn<Product, String> productName;
	@FXML
	TableColumn<Product, String> productCode;
	@FXML
	TableColumn<Product, Float> productPrice;
	@FXML
	TableColumn<Product, Integer> productQuantity;
	@FXML
	TableColumn<Product, Float> productDiscount;
	
	public void show(Stage stage) {
		super.show(this, stage, Constants.FXML_DESIGN_PRODUCT);
		init();
	}
	
	private void init() {
		//init category list
		log.info("Initializing category list");
		categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
		categoryName.setCellFactory(TextFieldTableCell.forTableColumn());
		categoryCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		categoryCode.setCellFactory(TextFieldTableCell.forTableColumn());
		categoryDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
		categoryDiscount.setCellFactory(TextFieldTableCell.<Category, Float>forTableColumn(new FloatStringConverter()));
		
		List<Category> categories = categoryRepository.findAll();
		if (categories != null && categories.size() > 0) {
			log.info("{} categories found", categories.size());
			categoryList.getItems().addAll(categories);
			/**
			categoryList.setRowFactory(new Callback<TableView<Category>, TableRow<Category>>() {
				@Override
				public TableRow<Category> call(TableView<Category> list) {
					return new CategoryTableRow();
				}
			});**/
			categoryList.getItems().addAll(categories);
			categoryList.getSelectionModel().select(0);
			
			//init product list
			initProductList(categories.get(0), true); 
		}
	}
	
	private void initProductList(Category category, boolean isFirstTime) {
		productList.getItems().clear();
		if (isFirstTime) {
			log.info("Initializing product list");
			productName.setCellValueFactory(new PropertyValueFactory<>("name"));
			productName.setCellFactory(TextFieldTableCell.forTableColumn());
			productCode.setCellValueFactory(new PropertyValueFactory<>("code"));
			productCode.setCellFactory(TextFieldTableCell.forTableColumn());
			productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
			productPrice.setCellFactory(TextFieldTableCell.<Product, Float>forTableColumn(new FloatStringConverter()));
			productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			productQuantity.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
			productDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
			productDiscount.setCellFactory(TextFieldTableCell.<Product, Float>forTableColumn(new FloatStringConverter()));
		}

		List<Product> products = productRepository.findByCategory(category);
		if (products != null && products.size() > 0) {
			log.info("{} products found", products.size());
			productList.getItems().addAll(products);
			/**
			productList.setRowFactory(new Callback<TableView<Product>, TableRow<Product>>() {
				@Override
				public TableRow<Product> call(TableView<Product> list) {
					return new ProductTableRow();
				}
			});**/
			productList.getItems().addAll(products);
			productList.getSelectionModel().select(0);
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void addCategory() {
		int newItemIndex = categoryList.getItems().size();
		Category emptyCategory = new Category("nom", "code", true);
		categoryList.getItems().add(newItemIndex, emptyCategory);
		categoryList.getSelectionModel().select(newItemIndex);
		categoryList.getFocusModel().focus(newItemIndex, categoryList.getColumns().get(0));
		categoryList.scrollTo(newItemIndex);
		categoryList.edit(categoryList.getFocusModel().getFocusedCell().getRow(), 
				categoryList.getFocusModel().getFocusedCell().getTableColumn());
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	public void addProduct() {
		Category currentCategory = categoryList.getSelectionModel().getSelectedItem();
		int newItemIndex = productList.getItems().size();
		Product emptyProduct = new Product("nom", "code", 0, currentCategory, true);
		productList.getItems().add(newItemIndex, emptyProduct);
		productList.getSelectionModel().select(newItemIndex);
		productList.getFocusModel().focus(newItemIndex, productList.getColumns().get(0));
		productList.scrollTo(newItemIndex);
		productList.edit(productList.getFocusModel().getFocusedCell().getRow(), 
				productList.getFocusModel().getFocusedCell().getTableColumn());
	}
	
	@FXML
	public void refreshProductList() {
		Category category = categoryList.getSelectionModel().getSelectedItem();
		if (category != null && category.getCode() != null && category.getName() != null) {
			initProductList(category, false);
		} else {
			productList.getItems().clear();
		}
	}
	
	@FXML
	public void updateCategory(CellEditEvent<Category, Object> event) {
		Object newValue = event.getNewValue();
		Category category = event.getRowValue();
		int columnIndex = event.getTablePosition().getColumn();
		if (columnIndex == 0) {
			category.setName((String)newValue);
		} else if (columnIndex == 1) {
			category.setCode((String)newValue);
		} else if (columnIndex == 2) {
			category.setDiscount((Float)newValue);
		}
		try {
			categoryRepository.save(category);
		} catch (Exception e) {
			log.warn("Error while saving category {}", category.getName());
		}
	}
	
	@FXML
	public void updateProduct(CellEditEvent<Product, Object> event) {
		Object newValue = event.getNewValue();
		Product product = event.getRowValue();
		int columnIndex = event.getTablePosition().getColumn();
		if (columnIndex == 0) {
			product.setName((String)newValue);
		} else if (columnIndex == 1) {
			product.setCode((String)newValue);
		} else if (columnIndex == 2) {
			product.setPrice((Float)newValue);
		} else if (columnIndex == 3) {
			product.setQuantity((Integer)newValue);
		} else if (columnIndex == 4) {
			product.setDiscount((Float)newValue);
		}
		try {
			productRepository.save(product);
		} catch (Exception e) {
			log.warn("Error while saving product {}", product.getName());
		}
	}
	
	@FXML
	public void deleteCategory() {
		int index = categoryList.getSelectionModel().getSelectedIndex();
		Category category = categoryList.getSelectionModel().getSelectedItem();
		categoryList.getItems().remove(index);
		categoryRepository.delete(category);
		log.info("Category {} deleted", category.getName());
	}
	
	@FXML
	public void deleteProduct() {
		int index = productList.getSelectionModel().getSelectedIndex();
		Product product = productList.getSelectionModel().getSelectedItem();
		productList.getItems().remove(index);
		productRepository.delete(product);
		log.info("Product {} deleted", product.getName());
	}
}
