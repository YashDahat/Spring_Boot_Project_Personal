package com.springboot.print.printServiceUseRest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.print.logservice.LoggingService;
import com.springboot.print.printServiceUseRest.printService.StorageAndPrintService;

@Controller
@RequestMapping("/fileSubmission")
public class FileSubmissionFormController {
	LoggingService LOGGER = new LoggingService(FileSubmissionFormController.class);

	 
	 @Autowired
	 StorageAndPrintService storageAndPrintService;
	 
	@GetMapping("/form")
	public String showForm(Model theModel) {
		theModel.addAttribute("Printers", this.storageAndPrintService.getListOfPrinters());
		return "FileSubmissionForm";
	}
	
	@PostMapping("/submitFile")
	public String submitFile(@RequestParam("file") MultipartFile file, @RequestParam("PrintersdropDownList") String printerName) throws IllegalStateException, IOException {
		System.out.println("You have successfully submitted File!!!");
		System.out.println("You have successfully submitted FileName!!!:"+file.getOriginalFilename());
		LOGGER.debug("Selected service name!!!:"+printerName);
		storageAndPrintService.StoringAndPrinting(file, printerName);
		return "FileSubmittedPage";
	}
	
	@GetMapping("/cancel")
	public String cancelPrintJob() {
		String mssg = storageAndPrintService.cancelPrintJob();
		LOGGER.debug("Canceling Job Status:"+ mssg);
		return "FileSubmittedPage";
	}
}
