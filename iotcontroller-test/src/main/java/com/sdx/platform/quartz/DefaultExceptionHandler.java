package com.sdx.platform.quartz;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException exception) {

		if (exception.getMessage().equals("HTTP 404 Not Found")) {
			log.info("Response.status(404).build()" + Response.status(404).build());
			return Response.status(404).build();
			// don't change content type or set entity
		} else {
			log.error("Response.status(Response.Status.INTERNAL_SERVER_ERROR)"
					+ Response.status(Response.Status.INTERNAL_SERVER_ERROR));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage())
					.type(MediaType.APPLICATION_JSON).build();

		}

	}

}