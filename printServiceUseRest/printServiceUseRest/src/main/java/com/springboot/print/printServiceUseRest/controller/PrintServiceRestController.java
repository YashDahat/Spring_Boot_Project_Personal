package com.springboot.print.printServiceUseRest.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.print.logservice.LoggingService;
import com.springboot.print.printServiceUseRest.printService.StorageAndPrintService;

@RestController
@RequestMapping("/printService")
public class PrintServiceRestController {
private LoggingService LOGGER = new LoggingService(PrintServiceRestController.class);
	
	@Autowired
	private StorageAndPrintService storageAndPrintService;
		
	
	public PrintServiceRestController() {
		super();
		// TODO Auto-generated constructor stub
	}


	@PostMapping("/print")
	public String PrintFile(@RequestParam("file") MultipartFile file, @RequestParam("printerName") String printerName) throws IllegalStateException, IOException {
		LOGGER.debug("Inside Print service...");
		LOGGER.debug("Printer Name:"+ printerName);
		LOGGER.debug("File Name:"+ file.getOriginalFilename());
		this.storageAndPrintService.StoringAndPrinting(file, printerName);
		return "Success";
	}
	
	@GetMapping("/av_printers")
	public List<String> AvailablePrinters(){
		LOGGER.debug("Inside available printers...");
		return this.storageAndPrintService.getListOfPrinters();
	}
	
	//Need service to cancel the print command
	//Need service to get status of the current print job on the specific printer
	@GetMapping("/printer_status")
	public String getStatusofPrinterbyName(@RequestParam("printerName") String printerName) {
		return this.storageAndPrintService.getPrinterStateByName(printerName);
	}
}
