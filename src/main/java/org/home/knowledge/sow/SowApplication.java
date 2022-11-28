package org.home.knowledge.sow;

import java.net.InetAddress;

import org.home.knowledge.sow.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class SowApplication {

	@Value("${server.servlet.context-path}")
	private String serverContextPath;

	@Value("${server.port}")
	private String serverPort;

	public static void main(String[] args) {
		SpringApplication.run(SowApplication.class, args);
	}

	/**
	 * Logs urls of interest on start up
	 * 
	 * @param event
	 */
	@EventListener
	void onApplicationEvent(ApplicationStartedEvent event) {
		// Not my favorite solution to a problem I ran into but when I started to use
		// SampleDataRunner to load sample data this scrolled off the
		// console defeating the purpose of even outputting points of interest. Until we
		// can figure out a way to listen for CommandLineRunners to complete and switch
		// up the event we're listening for I'm just triggering the loadSampleData
		// function from here
		event.getApplicationContext().getBean(AdminService.class).loadSampleData();

		try {
			var address = InetAddress.getLocalHost().getHostName();
			var addressBaseUrl = "http://" + address + ":" + serverPort + serverContextPath;

			log.info("Web server ready and waiting, good luck out there!\n"
					+ "\n"
					+ "\n  End points of interest:"
					+ "\n               home -> " + addressBaseUrl + "/server-home"
					+ "\n             health -> " + addressBaseUrl + "/actuator/health"
					+ "\n               info -> " + addressBaseUrl + "/actuator/info"
					+ "\n        environment -> " + addressBaseUrl + "/actuator/env"
					+ "\n    Spring Actuator -> " + addressBaseUrl + "/actuator"
					+ "\n"
					+ "\n"
					+ "\n    front end -> " + addressBaseUrl
					+ " (will require that an index.html or controller be created to point to front end)"
					+ "\n"
					+ "\n");
		} catch (Exception e) {
			var msg = "Exception occurred while building url to display in console on startup";
			log.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

}
