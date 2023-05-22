package com.springboot.print.printServiceUseRest.printService;


import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.QueuedJobCount;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;

import com.springboot.print.logservice.LoggingService;

public class PrintServiceEvent implements PrintServiceAttributeListener {

	LoggingService LOGGER = new LoggingService(PrintServiceEvent.class);
	StorageAndPrintService storageService;
	
	
	public PrintServiceEvent(StorageAndPrintService storageService) {
		super();
		this.storageService = storageService;
	}


	@Override
	public void attributeUpdate(PrintServiceAttributeEvent event) {
		LOGGER.debug("Job listener is called!!!");
		LOGGER.debug("Serving to print service!!!:"+event.getPrintService().getName());
		LOGGER.debug("Attributes values:!!!"+ event.getAttributes().toString());
		PrintServiceAttributeSet attributeSet = event.getAttributes();
		QueuedJobCount count = (QueuedJobCount) attributeSet.get(QueuedJobCount.class);
		LOGGER.debug("No. of queued jobs remaining:"+ count.getValue());
		if(count.getValue() == 0 && !storageService.isFileInUse()) {
			storageService.Clean();
		}
		
	}

}
