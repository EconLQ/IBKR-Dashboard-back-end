package com.liquidus.ibkrdasboardjee8.rest.dashboard;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/upload")
public class FileUploadResource {
    Logger logger = Logger.getLogger(FileUploadResource.class.getName());

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@MultipartForm MultipartFormDataInput data) {

        Map<String, List<InputPart>> formDataMap = data.getFormDataMap();
        InputPart filePart = formDataMap.get("file").get(0); // get original file

        try {
            InputStream fileInputStream = filePart.getBody(InputStream.class, null);
            // get name of the file from the payload
            String fileName = filePart.getHeaders().getFirst("Content-Disposition").split("filename=")[1];

            // save file to user.home and create dir /reports if not exist
            String uploadedFileLocation = saveFileToHome(fileName);

            // rewrite saved file with data from file input stream
            saveToFile(fileInputStream, uploadedFileLocation);
        } catch (IOException e) {
            logger.warning("Failed to read from the input: " + e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        // return 200
        return Response.ok().build();
    }

    /**
     * Creates reports directory in home and saves file mock file there as a placeholder
     *
     * @param fileName name of the file from the received request
     * @return location of a file
     */
    private String saveFileToHome(String fileName) {
        // skip quotes
        String fileNameWithoutQuotes = fileName.split("\"")[1];

        String directory = System.getProperty("user.home") + "/reports/";
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String uploadedFileLocation = directory + fileNameWithoutQuotes;

        // save file
        File fileObject = new File(uploadedFileLocation);
        if (fileObject.exists()) {
            fileObject.delete();
        }
        return uploadedFileLocation;
    }

    /**
     * Save data from the {@link InputStream} to a file at uploadedFileLocation
     *
     * @param uploadedInputStream  file's input stream data
     * @param uploadedFileLocation location of a written file
     */
    private void saveToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        try {
            OutputStream out;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(uploadedFileLocation);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            logger.severe("File could not been found at: " + e.getMessage());
        } catch (IOException e) {
            logger.warning("Error to process reading from file: " + e.getMessage());
        }
        logger.info("File has been saved to: " + uploadedFileLocation);
    }
}
