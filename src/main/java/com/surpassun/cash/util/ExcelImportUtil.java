package com.surpassun.cash.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.surpassun.cash.domain.Category;
import com.surpassun.cash.domain.Client;
import com.surpassun.cash.domain.Product;
import com.surpassun.cash.repository.CategoryRepository;

public class ExcelImportUtil {
	private static final Logger log = LoggerFactory.getLogger(ExcelImportUtil.class);
	private static final HSSFDataFormatter formatter = new HSSFDataFormatter();
	private static final Map<String, Category> categoryInfos = new HashMap<String, Category>();
	private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yy");

	public static Set<Product> workbookToProducts(HSSFSheet sheet, CategoryRepository categoryRepository, List<Integer> errorLines) {
		if (sheet != null) {
			List<Category> categories = categoryRepository.findAll();
			if (categories == null) {
				categories = new ArrayList<Category>();
			} else {
				for (Category category : categories) {
					categoryInfos.put(category.getName(), category);
				}
			}
			Iterator<Row> it = sheet.rowIterator();
			int counter = 0;
			Set<Product> products = new HashSet<Product>();
			
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				if (counter > 0) {
					try {
						Product product = extractAndConstructObject(categoryInfos, categoryRepository, row);
						if (product != null) {
							if (!products.contains(product)) {
								products.add(product);
							} else {
								log.error("Duplicate product code {} at line {}", product.getCode(), row.getRowNum());
								errorLines.add(row.getRowNum() + 1);
							}
						}
					} catch (Exception e) {
						log.error("Error while constructing object", e);
						errorLines.add(row.getRowNum() + 1);
					}
				}
				counter++;
			}

			return products;
		}
		return null;
	}

	private static Product extractAndConstructObject(Map<String, Category> categories, CategoryRepository categoryRepository, Row row) throws Exception {
		// create new instance
		Iterator<Cell> it = row.cellIterator();
		String productName = null;
		String productCode = null;
		String categoryName = null;
		String priceText = null;
		while (it.hasNext()) {
			Cell cell = it.next();
			String value = formatter.formatCellValue(cell);
			int index = cell.getColumnIndex();
			if (index == 0) {
				productName = StringUtils.lowerCase(StringUtils.trim(value));
			} else if (index == 1) {
				productCode = StringUtils.trim(value);
			} else if (index == 3) {
				//save always the category name in lower case
				categoryName = StringUtils.lowerCase(StringUtils.trim(value));
			} else if (index == 5) {
				priceText = StringUtils.trim(value);
			}
		}

		if (!categories.containsKey(categoryName)) {
			Category category = new Category();
			category.setName(categoryName);
			//String categoryCode = StringUtils.substring(productCode, 0, 2);
			String categoryCode = categoryName;
			category.setCode(categoryCode);
			category.setShortcutButtonEnabled(true);
			categoryRepository.save(category);
			categories.put(categoryName, category);
		}
		
		float price = 0F;
		if (StringUtils.isNotBlank(priceText)) {
			priceText = StringUtils.remove(priceText, StringPool.EURO);
			priceText = StringUtils.replace(priceText, StringPool.COMMA, StringPool.PERIOD);
			priceText = StringUtils.trim(priceText);
			if (NumberUtils.isNumber(priceText)) {
				price = Float.parseFloat(priceText);
			} else {
				throw new Exception("File Format is valid");
			}
		} else {
			throw new Exception("File Format is valid");
		}
		return new Product(productName, productCode, price, categories.get(categoryName), true);
	}
	
	public static Set<Client> workbookToClients(HSSFSheet sheet, List<Integer> errorLines) {
		if (sheet != null) {
			Iterator<Row> it = sheet.rowIterator();
			int counter = 0;
			Set<Client> clients = new HashSet<Client>();
			
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				if (counter > 0) {
					try {
						Client client = extractAndConstructClient(row);
						if (client != null) {
							if (!clients.contains(client)) {
								clients.add(client);
							} else {
								log.error("Duplicate client code {} at line {}", client.getCode(), row.getRowNum());
								errorLines.add(row.getRowNum() + 1);
							}
						}
					} catch (Exception e) {
						log.error("Error while constructing object", e);
						errorLines.add(row.getRowNum() + 1);
					}
				}
				counter++;
			}

			return clients;
		}
		return null;
	}

	private static Client extractAndConstructClient(Row row) {
		// create new instance
		Iterator<Cell> it = row.cellIterator();
		String firstName = null;
		String lastName = null;
		String telephone = null;
		String email = null;
		String address = null;
		String city = null;
		String postcode = null;
		String loyaltyCardNumber = null;
		String birthdayString = null;
		DateTime birthday  = null;
		while (it.hasNext()) {
			Cell cell = it.next();
			String value = formatter.formatCellValue(cell);
			int index = cell.getColumnIndex();
			if (index == 0) {
				lastName = StringUtils.trim(value);
			} else if (index == 1) {
				firstName = StringUtils.trim(value);
			} else if (index == 2) {
				telephone = StringUtils.trim(value);
			} else if (index == 3) {
				email = StringUtils.trim(value);
			} else if (index == 4) {
				address = StringUtils.trim(value);
			} else if (index == 5) {
				city = StringUtils.trim(value);
			} else if (index == 6) {
				postcode = StringUtils.trim(value);
			} else if (index == 7) {
				loyaltyCardNumber = StringUtils.trim(value);
			} else if (index == 8) {
				birthdayString = StringUtils.trim(value);
				if (StringUtils.isNoneBlank(birthdayString)) {
					birthday = dtf.parseDateTime(birthdayString);
				}
			}
		}

		return new Client(loyaltyCardNumber, firstName, lastName, address, postcode, city, telephone, email, birthday);
	}
}
