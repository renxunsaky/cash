package com.surpassun.cash.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.surpassun.cash.domain.Client;
import com.surpassun.cash.domain.Product;
import com.surpassun.cash.repository.ClientRepository;
import com.surpassun.cash.repository.ProductRepository;

public class ExcelExportUtil {
	private static final Logger log = LoggerFactory.getLogger(ExcelExportUtil.class);
	private static String[] productHeader = {"ID", "Code", "Name", "Price", "Category", "Quantity", "Discount"};
	private static String[] clientHeader = {"Prenom", "Nom", "Telephone", "Email", "Adress", "Ville", "Code Postal", "Numero fidelite", "Date de naissance", "Active", "consomation totale"};	

	public static void exportProducts(ProductRepository productRepository, File file) {
		List<Product> products= productRepository.findAll();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		
		
		if (products != null) {
			HSSFRow header = sheet.createRow(0);
			setHeader(header, productHeader);
			for (int i = 1; i < products.size(); i++) {
				int cellCounter = 0;
				Product product = products.get(i);
				HSSFRow row = sheet.createRow(i);
				setCellValue(row, cellCounter++, product.getId());
				setCellValue(row, cellCounter++, product.getCode());
				setCellValue(row, cellCounter++, product.getName());
				setCellValue(row, cellCounter++, product.getPrice());
				setCellValue(row, cellCounter++, product.getCategory().getCode());
				setCellValue(row, cellCounter++, product.getQuantity());
				setCellValue(row, cellCounter++, product.getDiscount());
			}
		}
		
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(file);
			workbook.write(fop);
		} catch (IOException e) {
			log.error("Error while writing to excel file " + file.getAbsolutePath(), e);
		} finally {
			try {
				workbook.close();
			} catch (IOException e1) {
				log.error("Error closing HSSF workbook ", e1);
			}
			if (fop != null) {
				try {
					fop.close();
				} catch (IOException e) {
					log.error("Error closing File Outpout Stream ", e);
				}
			}
		}
	}

	public static void exportClients(ClientRepository clientRepository, File file) {
		List<Client> clients = clientRepository.findAll();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		
		if (clients != null) {
			HSSFRow header = sheet.createRow(0);
			setHeader(header, clientHeader);
			for (int i = 1; i < clients.size(); i++) {
				int cellCounter = 0;
				Client client= clients.get(i);
				HSSFRow row = sheet.createRow(i);
				
				setCellValue(row, cellCounter++, client.getFirstname());
				setCellValue(row, cellCounter++, client.getLastname());
				setCellValue(row, cellCounter++, client.getPhone());
				setCellValue(row, cellCounter++, client.getEmail());
				setCellValue(row, cellCounter++, client.getAddress());
				setCellValue(row, cellCounter++, client.getCity());
				setCellValue(row, cellCounter++, client.getPostcode());
				setCellValue(row, cellCounter++, client.getCode());
				setCellValue(row, cellCounter++, client.getBirthday());
				setCellValue(row, cellCounter++, client.getActivated());
				setCellValue(row, cellCounter++, client.getTotalConsumation());
			}
		}
		
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(file);
			workbook.write(fop);
		} catch (IOException e) {
			log.error("Error while writing to excel file " + file.getAbsolutePath(), e);
		} finally {
			try {
				workbook.close();
			} catch (IOException e1) {
				log.error("Error closing HSSF workbook ", e1);
			}
			if (fop != null) {
				try {
					fop.close();
				} catch (IOException e) {
					log.error("Error closing File Outpout Stream ", e);
				}
			}
		}
	}
	

	private static void setCellValue(HSSFRow row, int cellCounter, Object value) {
		if (value == null) {
			return;
		}
		if (value instanceof Integer) {
			row.createCell(cellCounter).setCellValue((Integer)value);
		} else if (value instanceof Long) {
			row.createCell(cellCounter).setCellValue((Long)value);
		} else if (value instanceof String) {
			row.createCell(cellCounter).setCellValue((String)value);
		} else if (value instanceof Float) {
			row.createCell(cellCounter).setCellValue((Float)value);
		} else if (value instanceof Double) {
			row.createCell(cellCounter).setCellValue((Double)value);
		} else if (value instanceof Boolean) {
			row.createCell(cellCounter).setCellValue((Boolean)value);
		} else if (value instanceof DateTime) {
			row.createCell(cellCounter).setCellValue(((DateTime)value).toDate());
		}
	}
	
	private static void setHeader(HSSFRow header, String[] productHeader) {
		int counter = 0;
		HSSFCellStyle style = header.getSheet().getWorkbook().createCellStyle();
		HSSFFont font = header.getSheet().getWorkbook().createFont();
		font.setBold(true);
		style.setFont(font);
		header.setRowStyle(style);
		for (String title : productHeader) {
			HSSFCell cell = header.createCell(counter++);
			cell.setCellValue(title);
		}
	}
}
