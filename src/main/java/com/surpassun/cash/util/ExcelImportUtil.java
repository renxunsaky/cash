package com.surpassun.cash.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.surpassun.cash.domain.Category;
import com.surpassun.cash.domain.Product;
import com.surpassun.cash.repository.CategoryRepository;

public class ExcelImportUtil {
	private static final Logger log = LoggerFactory.getLogger(ExcelImportUtil.class);
	private static final HSSFDataFormatter formatter = new HSSFDataFormatter();
	private static final Map<String, Category> categoryInfos = new HashMap<String, Category>();

	public static List<Product> workbookToProducts(HSSFSheet sheet, CategoryRepository categoryRepository, List<Integer> errorLines) {
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
			List<Product> products = new ArrayList<Product>();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				if (counter > 0) {
					try {
						Product product = extractAndConstructObject(categoryInfos, categoryRepository, row);
						if (product != null) {
							products.add(product);
						}
					} catch (Exception e) {
						log.error("Error while constructing object", e);
						errorLines.add(row.getRowNum());
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
				productName = StringUtils.trim(value);
			} else if (index == 1) {
				productCode = StringUtils.trim(value);
			} else if (index == 3) {
				categoryName = StringUtils.trim(value);
			} else if (index == 5) {
				priceText = StringUtils.trim(value);
			}
		}

		if (!categories.containsKey(categoryName)) {
			Category category = new Category();
			category.setName(categoryName);
			String categoryCode = StringUtils.substring(productCode, 0, 2);
			category.setCode(categoryCode);
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
		return new Product(productName, productCode, price, categories.get(categoryName));
	}
}
