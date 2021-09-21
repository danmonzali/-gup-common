package it.reply.sytel.gup.common.util;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.reply.sytel.gup.common.bean.GupRequestObject;
import it.reply.sytel.gup.common.mapper.GupObjectMapper;

@Component
public class GupControllerUtil {

	private static final Logger logger = LogManager.getLogger(GupControllerUtil.class);

	@Autowired
	private GupObjectMapper gupObjectMapper;

	public <T extends Serializable> T getRequestObject(GupRequestObject gup, Class<T> clz) {
		T requestObject = null;
		try {
			requestObject = gupObjectMapper.getMapper().readValue(gupObjectMapper.getMapper().writeValueAsString(gup),
					clz);
		} catch (Exception e1) {
			logger.error("gupHelperPagopaInviaRpt - Errore nella trasformazione della request da JSON a Classe", e1);
		}
		return requestObject;

	}
}
