package com.surpassun.cash.service;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.surpassun.cash.domain.Config;
import com.surpassun.cash.repository.ConfigRepository;
import com.surpassun.cash.util.StringPool;

@Service
public class ConfigService {

	private final Logger log = LoggerFactory.getLogger(ConfigService.class);
	
	@Inject
	private ConfigRepository configRepository;
	
	public Integer findInteger(String name) {
		String result = findByName(name);
		if (result != null) {
			return Integer.valueOf(result);
		} else {
			return null;
		}
	}
	
	public Boolean findBoolean(String name) {
		String result = findByName(name);
		if (result != null) {
			return Boolean.valueOf(result);
		} else {
			return false;
		}
	}
	
	public String findByName(String name) {
		Config config = configRepository.findByName(name);
		if (config != null) {
			return config.getValue();
		} else {
			return null;
		}
	}
	
	public Float[] findFloatListByName(String name) {
		Float[] result = null;
		String[] values = findListByName(name);
		if (values != null) {
			result = new Float[values.length];
			int counter = 0;
			for (String temp : values) {
				result[counter] = Float.valueOf(temp);
				counter++;
			}
		}
		return result;
	}
	
	public String[] findListByName(String name) {
		return findListByName(name, StringPool.SEMICOLON);
	}

	public String[] findListByName(String name, String separator) {
		Config config = configRepository.findByName(name);
		String[] results = null;
		if (config != null) {
			String value = config.getValue();
			results = StringUtils.split(value, separator);
			log.debug("Config value {} found with name {}", value, name);
		} else {
			log.warn("No config found with the name {}", name);
		}
		return results;
	}
}
