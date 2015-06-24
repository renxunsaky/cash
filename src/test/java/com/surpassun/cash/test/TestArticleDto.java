package com.surpassun.cash.test;

import org.junit.Assert;
import org.junit.Test;

import com.surpassun.cash.fx.dto.ArticleDto;

public class TestArticleDto {

	@Test
	public void testHashCode() {
		ArticleDto dto1 = new ArticleDto();
		dto1.setCode("100001");
		dto1.setDisplayName("Name 1");
		dto1.setDiscount(0.15F);

		ArticleDto dto2 = new ArticleDto();
		dto2.setCode("100001");
		dto2.setDisplayName("Name 2");
		dto2.setDiscount(0.30F);
		
		ArticleDto dto3 = new ArticleDto();
		dto3.setCode("200001");
		dto3.setDisplayName("Name 3");
		dto3.setDiscount(0.40F);
		
		Assert.assertTrue(dto1.equals(dto2));
		Assert.assertFalse(dto1.equals(dto3));
	}
}
