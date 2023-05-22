package com.springboot.print.printServiceUseRest.printService;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.JobState;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterState;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.print.logservice.LoggingService;

@Service
public class StorageAndPrintService {

	String fileName = "";
	String uploadDir = "C:\\StoreUploadedFile";
	LoggingService LOGGER = new LoggingService(StorageAndPrintService.class);
	PrinterJob job = null;
	public StorageAndPrintService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void StoringAndPrinting(MultipartFile file, String printerName) throws IllegalStateException, IOException {
		//Convert Multipart file to file
		//File doc = multipartToFile(file, file.getName());
		//Add function for Storing
		String mssg = Store(convertMultipartFileToFile(file)); 
		LOGGER.debug("STORING STATUS:"+mssg);
		//Add function for Printing
		mssg = Print(printerName); 
		LOGGER.debug("PRINTING STATUS:"+mssg);
		//Add function o clean the memory
		
		//Clean();
		
	}
//	public String Store(MultipartFile file) {
//		 try {	
//			 	Path uploadPath = Paths.get(uploadDir);
//			 	if (!Files.exists(uploadPath)) {
//			 	    Files.createDirectories(uploadPath);
//			 	}
//			 	String filename = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
//			 	fileName = filename;
//			 	Path filePath = uploadPath.resolve(filename);
//			 	OutputStream os = Files.newOutputStream(filePath);
//			 	os.write(file.getBytes());
//			 	os.close();
//	            return "success";
//	        } catch (Exception e) {
//	            LOGGER.error("ERRROR:"+e.getMessage());
//	        	return "error";
//	        }
//	}
	public String Store(File file) {
	    File destinationFile = new File(uploadDir);
	    if (!destinationFile.exists()) {
	        destinationFile.mkdirs();
	    }
	    String filename = UUID.randomUUID().toString() + "." + file.getName();
	 	fileName = filename;
	    File outputFile = new File(destinationFile.getAbsolutePath() + File.separator + fileName);
	    try (FileInputStream inputStream = new FileInputStream(file);
	         FileOutputStream outputStream = new FileOutputStream(outputFile)) {
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = inputStream.read(buffer)) > 0) {
	            outputStream.write(buffer, 0, length);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "error";
	    }
	    return "success";
	}
	public String Print(String printerName) {
		try {
			Path path = Paths.get(uploadDir, fileName);
			LOGGER.debug("Path"+ path.toString());
			
			PrintService selectedService = getPrintServiceByName(printerName);

			selectedService.addPrintServiceAttributeListener(new PrintServiceEvent(this));
			
			InputStream inputStream = new FileInputStream(path.toString());
			
			PDDocument pdf = Loader.loadPDF(inputStream);
			
			job = PrinterJob.getPrinterJob();
			
			PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
			
			job.setPrintService(selectedService);
						
			job.setPageable(new PDFPageable(pdf));
					
			job.print(attributes);
			
			pdf.close();
			
		}catch(Exception e) {
			LOGGER.error("Error in printing:"+ e.getMessage());
			return "error";
		}
		return "success";
	}
	public void Clean() {
		try {
			Path path = Paths.get(uploadDir, fileName);
			File file = new File(path.toString());
			if(Files.exists(path)) {
				LOGGER.debug("File exists, now deleting the file!!!");
				Files.delete(path);
				return;
			}
			LOGGER.debug("File doesn't exist!!!");
			return;
		}catch(Exception e) {
			LOGGER.error("Got error while cleaning the file!!!:"+e.getMessage());
		}
	}
	public String cancelPrintJob() {
		try {
			PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
			attributes.add(JobState.CANCELED);
			job.print(attributes);
			if(job.isCancelled()) {
				LOGGER.debug("JOB WAS CANCELLED SUCCESSFULLLY!!!");
			}else {
				LOGGER.debug("JOB WAS NOT CANCELLED!!!");
			}
		}catch(Exception e) {
			LOGGER.error("Error in canecling the print job!!!: "+e.getMessage());
			return "error";
		}
		return "success";	
	}
	public List<String> getListOfPrinters(){
		List<String> printers = new ArrayList<String>();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for(PrintService service : services) {
			printers.add(service.getName());
		}
		return printers;
	}
	
	public PrintService getPrintServiceByName(String name){
		List<String> printers = new ArrayList<String>();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for(PrintService service : services) {
			if(name.equals(service.getName())) {
				return service;
			}
		}
		return null;
	}
	
	public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
	    byte[] bytes = multipartFile.getBytes();
	    File file = new File(multipartFile.getOriginalFilename());
	    try (FileOutputStream outputStream = new FileOutputStream(file)) {
	        outputStream.write(bytes);
	    }
	    return file;
	}
	
	public boolean isFileInUse() {
		Path path = Paths.get(uploadDir, fileName);
		if(Files.isReadable(path) || Files.isWritable(path)) {
			return true;
		}
		return false;
	}
	
	public String getPrinterStateByName(String PrinterName) {
		List<String> printers = new ArrayList<String>();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for(PrintService service : services) {
			if(PrinterName.equals(service.getName())) {
				PrintServiceAttributeSet attributes = service.getAttributes();
				Attribute stateAttribute = attributes.get(PrinterIsAcceptingJobs.class);
				PrinterIsAcceptingJobs state = (PrinterIsAcceptingJobs) stateAttribute;
				if(state.getValue() == 1) {
					return "ACCEPTING";
				}
				return "NOT ACCEPTING";
					
			}
		}
		return null;
	}
}
